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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.ActionType;
import org.coode.oppl.ChangeExtractor;
import org.coode.oppl.OPPLScript;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/** @author Luigi Iannone Jul 3, 2008 */
public class ClassPatternExecutor extends ChangeExtractor {
    private final InstantiatedPatternModel instantiatedPatternModel;
    private final OWLClass owlClass;
    private final IRI annotationIRI;
    private final OWLOntology ontology;
    private final OWLOntologyManager ontologyManager;

    /** @param thisClass
     *            thisClass
     * @param instantiatedPatternModel
     *            instantiatedPatternModel
     * @param ontology
     *            ontology
     * @param ontologyManager
     *            ontologyManager
     * @param annotationIRI
     *            annotationIRI
     * @param runtimeExceptionHandler
     *            runtimeExceptionHandler */
    public ClassPatternExecutor(OWLClass thisClass,
            InstantiatedPatternModel instantiatedPatternModel, OWLOntology ontology,
            OWLOntologyManager ontologyManager, IRI annotationIRI,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        super(runtimeExceptionHandler, true);
        owlClass = thisClass;
        this.instantiatedPatternModel = instantiatedPatternModel;
        this.annotationIRI = annotationIRI;
        this.ontology = ontology;
        this.ontologyManager = ontologyManager;
    }

    @Override
    public List<OWLAxiomChange> visit(OPPLScript script) {
        List<OWLAxiomChange> changes = script.getActions();
        Set<OWLAxiomChange> p = new HashSet<>(changes.size());
        for (OWLAxiomChange axiomChange : changes) {
            ActionType actionType = axiomChange.isAddAxiom() ? ActionType.ADD
                    : ActionType.REMOVE;
            OWLAxiom axiom = axiomChange.getAxiom();
            Collection<? extends OWLAxiomChange> createdChanges = PatternActionFactory
                    .createChange(owlClass, actionType, axiom, instantiatedPatternModel,
                            ontologyManager.getOWLDataFactory(), annotationIRI, ontology,
                            getRuntimeExceptionHandler());
            p.addAll(createdChanges);
        }
        return new ArrayList<>(p);
    }
}
