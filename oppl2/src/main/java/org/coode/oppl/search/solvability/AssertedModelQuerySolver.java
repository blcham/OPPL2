package org.coode.oppl.search.solvability;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asSet;

import java.util.Set;
import java.util.stream.Stream;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/** @author Luigi Iannone */
public final class AssertedModelQuerySolver implements QuerySolver {

    private final OWLOntologyManager ontologyManager;

    /**
     * @param ontologyManager
     *        ontologyManager
     */
    public AssertedModelQuerySolver(OWLOntologyManager ontologyManager) {
        this.ontologyManager = checkNotNull(ontologyManager, "ontologyManager");
    }

    protected <T extends OWLAxiom> Stream<T> axioms(AxiomType<T> t) {
        return ontologyManager.ontologies().flatMap(o -> o.axioms(t));
    }

    @Override
    public Set<OWLClass> getSubClasses(OWLClassExpression superClass) {
        return asSet(axioms(AxiomType.SUBCLASS_OF).filter(
                ax -> ax.getSuperClass().equals(superClass)
                        && !ax.getSubClass().isAnonymous()).map(
                ax -> ax.getSubClass().asOWLClass()));
    }

    @Override
    public Set<OWLClass> getSuperClasses(OWLClassExpression subClass) {
        return asSet(axioms(AxiomType.SUBCLASS_OF).filter(
                ax -> ax.getSubClass().equals(subClass)
                        && !ax.getSuperClass().isAnonymous()).map(
                ax -> ax.getSuperClass().asOWLClass()));
    }

    @Override
    public Set<OWLNamedIndividual> getNamedFillers(OWLNamedIndividual subject,
            OWLObjectPropertyExpression objectProperty) {
        return asSet(axioms(AxiomType.OBJECT_PROPERTY_ASSERTION).filter(
                ax -> ax.getSubject().equals(subject)
                        && ax.getProperty().equals(objectProperty)
                        && !ax.getObject().isAnonymous()).map(
                ax -> ax.getObject().asOWLNamedIndividual()));
    }

    @Override
    public boolean hasNoSubClass(OWLClassExpression superClass) {
        return !axioms(AxiomType.SUBCLASS_OF).anyMatch(
                ax -> ax.getSuperClass().equals(superClass));
    }

    @Override
    public boolean hasNoSuperClass(OWLClassExpression subClass) {
        return !axioms(AxiomType.SUBCLASS_OF).anyMatch(
                ax -> ax.getSubClass().equals(subClass));
    }
}
