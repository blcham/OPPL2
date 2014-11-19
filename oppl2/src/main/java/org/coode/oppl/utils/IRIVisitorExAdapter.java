package org.coode.oppl.utils;

import javax.annotation.Nonnull;

import org.coode.parsers.oppl.IRIVisitorEx;
import org.coode.parsers.oppl.VariableIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;

/**
 * @author Luigi Iannone
 * @param <O>
 *        type
 */
public class IRIVisitorExAdapter<O> implements IRIVisitorEx<O>,
        OWLObjectVisitorEx<O> {

    O variable;

    @Nonnull
    @Override
    public O doDefault(@Nonnull Object object) {
        return variable;
    }

    /**
     * @param defaultReturnValue
     *        defaultReturnValue
     */
    public IRIVisitorExAdapter(O defaultReturnValue) {
        variable = defaultReturnValue;
    }

    @Override
    public O visit(IRI iri) {
        return this.visitIRI(iri);
    }

    @Override
    public O visitIRI(IRI iri) {
        return doDefault(iri);
    }

    @Override
    public O visitVariableIRI(VariableIRI iri) {
        return doDefault(iri);
    }
}
