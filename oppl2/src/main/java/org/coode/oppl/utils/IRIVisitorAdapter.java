package org.coode.oppl.utils;

import org.coode.parsers.oppl.IRIVisitor;
import org.coode.parsers.oppl.VariableIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectVisitor;

/** @author Luigi Iannone */
public class IRIVisitorAdapter implements IRIVisitor, OWLObjectVisitor {
    @Override
    public void visitIRI(IRI iri) {}

    @Override
    public void visitVariableIRI(VariableIRI iri) {}
}
