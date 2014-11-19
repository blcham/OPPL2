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
import java.util.List;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.search.OPPLOWLAxiomSearchNode;
import org.coode.oppl.search.solvability.AssertedSolvabilitySearchTree;
import org.coode.oppl.search.solvability.SolvabilitySearchNode;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/** @author Luigi Iannone */
public class AssertedSolvabilityBasedAxiomQuery extends AbstractAxiomQuery {

    private final OWLOntologyManager manager;

    /**
     * @param m
     *        m
     * @param constraintSystem
     *        constraintSystem
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     */
    public AssertedSolvabilityBasedAxiomQuery(OWLOntologyManager m,
            ConstraintSystem constraintSystem,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        super(runtimeExceptionHandler, constraintSystem);
        manager = m;
    }

    /**
     * @param start
     *        start
     * @return search nodes
     */
    @Override
    protected List<List<OPPLOWLAxiomSearchNode>> doMatch(
            OPPLOWLAxiomSearchNode start) {
        List<List<OPPLOWLAxiomSearchNode>> solutions = new ArrayList<>();
        // Solvability based search is not worth applying if the axiom is not of
        // a specific kind.
        AxiomType<?> type = start.getAxiom().getAxiomType();
        if (type == AxiomType.SUBCLASS_OF
                || type == AxiomType.OBJECT_PROPERTY_ASSERTION) {
            for (List<SolvabilitySearchNode> l : solvabilityBasedMatching(
                    start.getAxiom(), start.getBinding())) {
                solutions.add(new ArrayList<OPPLOWLAxiomSearchNode>(l));
            }
            return solutions;
        }
        manager.ontologies()
                .flatMap(o -> filterAxioms(start.getAxiom(), o.axioms()))
                .filter(ax -> type.equals(ax.getAxiomType()))
                .forEach(ax -> solutions.addAll(matchTargetAxiom(start, ax)));
        return solutions;
    }

    private List<List<SolvabilitySearchNode>> solvabilityBasedMatching(
            OWLAxiom axiom, BindingNode bindingNode) {
        AssertedSolvabilitySearchTree searchTree = new AssertedSolvabilitySearchTree(
                getConstraintSystem(), getConstraintSystem()
                        .getOntologyManager(), getRuntimeExceptionHandler());
        List<List<SolvabilitySearchNode>> solutions = new ArrayList<>();
        SolvabilitySearchNode start = searchTree.buildSolvabilitySearchNode(
                axiom, bindingNode);
        searchTree.exhaustiveSearchTree(start, solutions);
        return solutions;
    }
}
