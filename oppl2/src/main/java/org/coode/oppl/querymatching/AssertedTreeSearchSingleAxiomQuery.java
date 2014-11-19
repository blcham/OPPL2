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

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.search.OPPLOWLAxiomSearchNode;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author Luigi Iannone */
public class AssertedTreeSearchSingleAxiomQuery extends AbstractAxiomQuery {

    private final Set<OWLOntology> ontologies = new HashSet<>();

    /**
     * @param ontologies
     *        ontologies
     * @param constraintSystem
     *        constraintSystem
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     */
    public AssertedTreeSearchSingleAxiomQuery(Set<OWLOntology> ontologies,
            ConstraintSystem constraintSystem,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        super(runtimeExceptionHandler, constraintSystem);
        this.ontologies.addAll(checkNotNull(ontologies, "ontologies"));
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
        AxiomType<?> type = start.getAxiom().getAxiomType();
        ontologies.stream()
                .flatMap(o -> filterAxioms(start.getAxiom(), o.axioms()))
                .filter(ax -> type.equals(ax.getAxiomType()))
                .forEach(ax -> solutions.addAll(matchTargetAxiom(start, ax)));
        return solutions;
    }
}
