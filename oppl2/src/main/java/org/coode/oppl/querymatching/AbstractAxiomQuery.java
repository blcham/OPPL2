package org.coode.oppl.querymatching;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Variable;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.search.OPPLAssertedSingleOWLAxiomSearchTree;
import org.coode.oppl.search.OPPLOWLAxiomSearchNode;
import org.coode.oppl.search.SearchTree;
import org.coode.oppl.utils.OWLObjectExtractor;
import org.coode.oppl.utils.PositionBasedVariableComparator;
import org.coode.oppl.utils.VariableExtractor;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

abstract class AbstractAxiomQuery implements AxiomQuery {

    private final RuntimeExceptionHandler runtimeExceptionHandler;
    protected final Map<BindingNode, Set<OWLAxiom>> instantiations = new HashMap<>();
    private final ConstraintSystem constraintSystem;
    private final Set<BindingNode> leaves = new HashSet<>();
    private final Map<OWLAxiom, SearchTree<OPPLOWLAxiomSearchNode>> searchTrees = new HashMap<>();
    private final Map<OWLAxiom, Collection<? extends OWLObject>> cache = new HashMap<>();

    /**
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     */
    AbstractAxiomQuery(RuntimeExceptionHandler runtimeExceptionHandler,
            ConstraintSystem cs) {
        this.runtimeExceptionHandler = checkNotNull(runtimeExceptionHandler,
                "runtimeExceptionHandler");
        constraintSystem = checkNotNull(cs, "constraintSystem");
    }

    protected Set<BindingNode> match(OWLAxiom axiom) {
        clearInstantions();
        List<List<? extends OPPLOWLAxiomSearchNode>> solutions = new ArrayList<>();
        VariableExtractor variableExtractor = new VariableExtractor(
                getConstraintSystem(), false);
        Set<Variable<?>> extractedVariables = variableExtractor
                .extractVariables(axiom);
        SortedSet<Variable<?>> sortedVariables = new TreeSet<>(
                new PositionBasedVariableComparator(axiom,
                        getConstraintSystem().getOntologyManager()
                                .getOWLDataFactory()));
        sortedVariables.addAll(extractedVariables);
        OPPLOWLAxiomSearchNode start = new OPPLOWLAxiomSearchNode(axiom,
                new BindingNode(sortedVariables));
        solutions.addAll(doMatch(start));
        return extractLeaves(solutions);
    }

    private static Set<BindingNode> extractLeaves(
            List<List<? extends OPPLOWLAxiomSearchNode>> solutions) {
        Set<BindingNode> toReturn = new HashSet<>();
        for (List<? extends OPPLOWLAxiomSearchNode> path : solutions) {
            OPPLOWLAxiomSearchNode searchLeaf = path.get(path.size() - 1);
            BindingNode leaf = searchLeaf.getBinding();
            toReturn.add(leaf);
        }
        return toReturn;
    }

    /**
     * @param targetAxiom
     *        targetAxiom
     * @param start
     *        start
     * @return search nodes
     */
    protected List<List<OPPLOWLAxiomSearchNode>> matchTargetAxiom(
            OPPLOWLAxiomSearchNode start, OWLAxiom targetAxiom) {
        SearchTree<OPPLOWLAxiomSearchNode> searchTree = getSearchTree(targetAxiom);
        List<List<OPPLOWLAxiomSearchNode>> solutions = new ArrayList<>();
        searchTree.exhaustiveSearchTree(start, solutions);
        return solutions;
    }

    /**
     * @param targetAxiom
     *        targetAxiom
     * @return search nodes
     */
    protected SearchTree<OPPLOWLAxiomSearchNode> getSearchTree(
            OWLAxiom targetAxiom) {
        SearchTree<OPPLOWLAxiomSearchNode> toReturn = searchTrees
                .get(targetAxiom);
        if (toReturn == null) {
            toReturn = new OPPLAssertedSingleOWLAxiomSearchTree(targetAxiom,
                    getConstraintSystem(), getRuntimeExceptionHandler());
            searchTrees.put(targetAxiom, toReturn);
        }
        return toReturn;
    }

    protected abstract List<List<OPPLOWLAxiomSearchNode>> doMatch(
            OPPLOWLAxiomSearchNode start);

    protected final void clearInstantions() {
        instantiations.clear();
    }

    /** @return instantiations */
    public Map<BindingNode, Set<OWLAxiom>> getInstantiations() {
        return new HashMap<>(instantiations);
    }

    /** @return the constraintSystem */
    public ConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }

    protected Stream<? extends OWLAxiom> filterAxioms(OWLAxiom toMatchAxiom,
            Stream<? extends OWLAxiom> axioms) {
        Set<OWLAxiom> toReturn = new HashSet<>();
        VariableExtractor variableExtractor = new VariableExtractor(
                getConstraintSystem(), true);
        Set<Variable<?>> variables = variableExtractor
                .extractVariables(toMatchAxiom);
        Collection<? extends OWLObject> toMatchAllOWLObjects = extractOWLObjects(toMatchAxiom);
        axioms.forEach(candidate -> {
            Collection<? extends OWLObject> candidateAllOWLObjects = extractOWLObjects(candidate);
            if (candidate.getAxiomType().equals(toMatchAxiom.getAxiomType())
                    && toMatchAllOWLObjects.containsAll(candidateAllOWLObjects)) {
                toReturn.add(candidate);
            } else {
                Set<OWLObject> difference = new HashSet<>(
                        candidateAllOWLObjects);
                difference.removeAll(toMatchAllOWLObjects);
                Iterator<OWLObject> iterator = difference.iterator();
                boolean found = false;
                while (!found && iterator.hasNext()) {
                    OWLObject leftOutOWLObject = iterator.next();
                    Iterator<? extends Variable<?>> variableIterator = variables
                            .iterator();
                    boolean compatible = false;
                    while (!compatible && variableIterator.hasNext()) {
                        Variable<?> variable = variableIterator.next();
                        compatible = variable.getType().isCompatibleWith(
                                leftOutOWLObject);
                    }
                    found = !compatible;
                }
                if (!found) {
                    toReturn.add(candidate);
                }
            }
        });
        return toReturn.stream();
    }

    /**
     * @param axiom
     *        axiom
     * @return owl objects
     */
    private Collection<? extends OWLObject> extractOWLObjects(OWLAxiom axiom) {
        Collection<? extends OWLObject> toReturn = cache.get(axiom);
        if (toReturn == null) {
            toReturn = OWLObjectExtractor.getAllOWLPrimitives(axiom);
            cache.put(axiom, toReturn);
        }
        return toReturn;
    }

    @Override
    public void visit(OWLDeclarationAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDisjointClassesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDataPropertyDomainAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDifferentIndividualsAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDisjointUnionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDataPropertyRangeAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSubDataPropertyOfAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSameIndividualAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSubPropertyChainOfAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLHasKeyAxiom axiom) {}

    @Override
    public void visit(OWLDatatypeDefinitionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(SWRLRule rule) {}

    @Override
    public void visit(OWLAnnotationAssertionAxiom axiom) {
        leaves.addAll(match(axiom));
    }

    @Override
    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {}

    @Override
    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {}

    @Override
    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {}

    @Override
    public Set<BindingNode> getLeaves() {
        return new HashSet<>(leaves);
    }

    /** @return the runtimeExceptionHandler */
    public RuntimeExceptionHandler getRuntimeExceptionHandler() {
        return runtimeExceptionHandler;
    }
}
