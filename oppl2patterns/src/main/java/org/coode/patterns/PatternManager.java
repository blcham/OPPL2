/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.coode.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.coode.oppl.OPPLScript;
import org.coode.oppl.exceptions.QuickFailRuntimeExceptionHandler;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.common.SystemErrorEcho;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;

/** @author Luigi Iannone Jul 3, 2008 */
public class PatternManager implements OWLOntologyChangeListener {

    /**
     * 
     */
    public static final RuntimeExceptionHandler HANDLER = new QuickFailRuntimeExceptionHandler();

    static class AdditionManager implements OWLAxiomVisitor {

        protected final OWLOntologyManager ontologyManager;
        protected final OWLOntology ontology;
        private final AbstractPatternModelFactory factory;

        public AdditionManager(OWLOntology ontology,
                OWLOntologyManager ontologyManager,
                AbstractPatternModelFactory f) {
            this.ontologyManager = ontologyManager;
            this.ontology = ontology;
            factory = f;
        }

        @Override
        public void visit(OWLAnnotationAssertionAxiom axiom) {
            PatternExtractor patternExtractor = factory
                    .getPatternExtractor(getDefaultErrorListener());
            final OWLAnnotation annotation = axiom.getAnnotation();
            final OPPLScript patternModel = annotation.accept(patternExtractor);
            OWLAnnotationSubject subject = axiom.getSubject();
            if (patternModel != null && subject instanceof OWLClass
                    && patternModel instanceof InstantiatedPatternModel) {
                final PatternOPPLScript opplStatement = ((InstantiatedPatternModel) patternModel)
                        .getInstantiatedPattern();
                subject.accept(new OWLObjectVisitor() {

                    @Override
                    public void visit(OWLClass desc) {
                        ClassPatternExecutor patternExecutor = new ClassPatternExecutor(
                                desc, (InstantiatedPatternModel) patternModel,
                                ontology, ontologyManager, annotation
                                        .getProperty().getIRI(), HANDLER);
                        List<OWLAxiomChange> changes = patternExecutor
                                .visit(opplStatement);
                        try {
                            ontologyManager.applyChanges(changes);
                        } catch (OWLOntologyChangeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    }

    static class DeletionManager implements OWLAxiomVisitor {

        private final OWLOntologyManager ontologyManager;
        private final AbstractPatternModelFactory factory;

        /**
         * @param ontologyManager
         *        ontologyManager
         * @param f
         *        f
         */
        public DeletionManager(OWLOntologyManager ontologyManager,
                AbstractPatternModelFactory f) {
            this.ontologyManager = ontologyManager;
            factory = f;
        }

        @Override
        public void visit(OWLAnnotationAssertionAxiom axiom) {
            PatternExtractor patternExtractor = factory
                    .getPatternExtractor(getDefaultErrorListener());
            OWLAnnotation annotation = axiom.getAnnotation();
            PatternOPPLScript patternModel = annotation
                    .accept(patternExtractor);
            OWLAnnotationSubject subject = axiom.getSubject();
            List<OWLAxiomChange> changes = new ArrayList<>();
            if (patternModel != null && subject instanceof OWLClass
                    && patternModel instanceof InstantiatedPatternModel) {
                ontologyManager.ontologies().forEach(
                        o -> {
                            o.axioms().forEach(
                                    ax -> {
                                        ax.annotations().forEach(
                                                a -> removeAxiom(patternModel,
                                                        changes, o, ax, a));
                                    });
                        });
                try {
                    ontologyManager.applyChanges(changes);
                } catch (OWLOntologyChangeException e) {
                    throw new RuntimeException(
                            "Could not store the pattern inside the ontology",
                            e);
                }
            }
        }

        protected void removeAxiom(PatternOPPLScript patternModel,
                List<OWLAxiomChange> changes, OWLOntology o, OWLAxiom ax,
                OWLAnnotation a) {
            String value = a.getValue().accept(
                    new OWLObjectVisitorEx<String>() {

                        @Override
                        public String visit(OWLLiteral literal) {
                            return literal.getLiteral();
                        }
                    });
            if (value != null) {
                IRI annotationIRI = a.getProperty().getIRI();
                if (value.equals(patternModel.getIRI().toString())
                        && annotationIRI.equals(IRI.create(
                                PatternModel.NAMESPACE,
                                PatternActionFactory.CREATED_BY))) {
                    changes.add(new RemoveAxiom(o, ax));
                }
            }
        }
    }

    private static Map<OWLOntologyManager, PatternManager> instances = new HashMap<>();
    private final OWLOntologyManager ontologyManager;
    private final AbstractPatternModelFactory factory;

    PatternManager(OWLOntologyManager ontologyManager,
            AbstractPatternModelFactory f) {
        this.ontologyManager = ontologyManager;
        factory = f;
    }

    /** @return factory */
    public AbstractPatternModelFactory getFactory() {
        return factory;
    }

    @Override
    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) {
        for (OWLOntologyChange ontologyChange : changes) {
            if (ontologyChange.isAxiomChange()) {
                OWLOntology ontology = ontologyChange.getOntology();
                OWLAxiomChange axiomChange = (OWLAxiomChange) ontologyChange;
                OWLAxiom axiom = axiomChange.getAxiom();
                OWLAxiomVisitor visitor;
                if (axiomChange.isAddAxiom()) {
                    visitor = new AdditionManager(ontology, ontologyManager,
                            factory);
                    axiom.accept(visitor);
                } else {
                    visitor = new DeletionManager(ontologyManager, factory);
                    axiom.accept(visitor);
                }
            }
        }
    }

    /**
     * @param ontologyManager
     *        ontologyManager
     * @param f
     *        f
     * @return the instance
     */
    public static synchronized PatternManager getInstance(
            OWLOntologyManager ontologyManager, AbstractPatternModelFactory f) {
        PatternManager patternManager = instances.get(ontologyManager);
        if (patternManager == null) {
            patternManager = new PatternManager(ontologyManager, f);
            instances.put(ontologyManager, patternManager);
        }
        return patternManager;
    }

    /** @return default error listener */
    public static ErrorListener getDefaultErrorListener() {
        return new SystemErrorEcho();
    }
}
