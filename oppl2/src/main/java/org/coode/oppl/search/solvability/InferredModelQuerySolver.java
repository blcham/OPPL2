package org.coode.oppl.search.solvability;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.*;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/** @author Luigi Iannone */
public final class InferredModelQuerySolver implements QuerySolver {

    private final OWLReasoner reasoner;

    /**
     * @param reasoner
     *        reasoner
     */
    public InferredModelQuerySolver(OWLReasoner reasoner) {
        this.reasoner = checkNotNull(reasoner, "reasoner");
    }

    @Override
    public Set<OWLClass> getSubClasses(OWLClassExpression superClass) {
        Set<OWLClass> toReturn = asSet(reasoner
                .getSubClasses(superClass, false).entities());
        add(toReturn, reasoner.getEquivalentClasses(superClass).entities());
        if (!superClass.isAnonymous()) {
            toReturn.add(superClass.asOWLClass());
        }
        return toReturn;
    }

    @Override
    public Set<OWLClass> getSuperClasses(OWLClassExpression subClass) {
        Set<OWLClass> toReturn = asSet(reasoner
                .getSuperClasses(subClass, false).entities());
        add(toReturn, reasoner.getEquivalentClasses(subClass).entities());
        if (!subClass.isAnonymous()) {
            toReturn.add(subClass.asOWLClass());
        }
        return toReturn;
    }

    @Override
    public boolean hasNoSubClass(OWLClassExpression superClass) {
        Set<OWLClass> subClasses = asSet(reasoner.getSubClasses(superClass,
                false).entities());
        add(subClasses, reasoner.getEquivalentClasses(superClass).entities());
        subClasses.remove(superClass);
        reasoner.getBottomClassNode().entities()
                .forEach(c -> subClasses.remove(c));
        return subClasses.isEmpty();
    }

    @Override
    public boolean hasNoSuperClass(OWLClassExpression subClass) {
        Set<OWLClass> superClasses = asSet(reasoner.getSubClasses(subClass,
                false).entities());
        add(superClasses, reasoner.getEquivalentClasses(subClass).entities());
        superClasses.remove(subClass);
        reasoner.getTopClassNode().entities()
                .forEach(c -> superClasses.remove(c));
        return superClasses.isEmpty();
    }

    @Override
    public Set<OWLNamedIndividual> getNamedFillers(OWLNamedIndividual subject,
            OWLObjectPropertyExpression objectProperty) {
        NodeSet<OWLNamedIndividual> fillers = reasoner.getObjectPropertyValues(
                subject, objectProperty);
        return asSet(fillers.entities());
    }
}
