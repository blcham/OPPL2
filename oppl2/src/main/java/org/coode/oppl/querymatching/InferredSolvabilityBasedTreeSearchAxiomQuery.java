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
package org.coode.oppl.querymatching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Variable;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.search.OPPLInferredOWLAxiomSearchTree;
import org.coode.oppl.search.OPPLOWLAxiomSearchNode;
import org.coode.oppl.search.solvability.InferredSolvabilitySearchTree;
import org.coode.oppl.search.solvability.SolvabilitySearchNode;
import org.coode.oppl.utils.PositionBasedVariableComparator;
import org.coode.oppl.utils.VariableExtractor;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

/** @author Luigi Iannone */
public class InferredSolvabilityBasedTreeSearchAxiomQuery extends
        AbstractAxiomQuery {

    /**
     * @param constraintSystem
     *        constraintSystem
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     */
    public InferredSolvabilityBasedTreeSearchAxiomQuery(
            ConstraintSystem constraintSystem,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        super(runtimeExceptionHandler, constraintSystem);
    }

    @Override
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

    @Override
    protected List<List<OPPLOWLAxiomSearchNode>> doMatch(
            OPPLOWLAxiomSearchNode start) {
        List<List<OPPLOWLAxiomSearchNode>> solutions = new ArrayList<>();
        OWLAxiom axiom = start.getAxiom();
        // Solvability based search is not worth applying if the axiom is not of
        // a specific kind.
        if (axiom.getAxiomType() == AxiomType.SUBCLASS_OF
                || axiom.getAxiomType() == AxiomType.OBJECT_PROPERTY_ASSERTION) {
            for (List<SolvabilitySearchNode> l : solvabilityBasedMatching(
                    start.getAxiom(), start.getBinding())) {
                solutions.add(new ArrayList<OPPLOWLAxiomSearchNode>(l));
            }
        } else {
            solutions.addAll(nonSolvabilityBasedMatch(start));
        }
        return solutions;
    }

    private List<List<OPPLOWLAxiomSearchNode>> nonSolvabilityBasedMatch(
            OPPLOWLAxiomSearchNode start) {
        List<List<OPPLOWLAxiomSearchNode>> solutions = new ArrayList<>();
        OPPLInferredOWLAxiomSearchTree searchTree = new OPPLInferredOWLAxiomSearchTree(
                getConstraintSystem(), getRuntimeExceptionHandler());
        searchTree.exhaustiveSearchTree(start, solutions);
        return solutions;
    }

    private List<List<SolvabilitySearchNode>> solvabilityBasedMatching(
            OWLAxiom axiom, BindingNode bindingNode) {
        InferredSolvabilitySearchTree searchTree = new InferredSolvabilitySearchTree(
                getConstraintSystem(), getRuntimeExceptionHandler());
        List<List<SolvabilitySearchNode>> solutions = new ArrayList<>();
        SolvabilitySearchNode start = searchTree.buildSolvabilitySearchNode(
                axiom, bindingNode);
        searchTree.exhaustiveSearchTree(start, solutions);
        return solutions;
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
}
