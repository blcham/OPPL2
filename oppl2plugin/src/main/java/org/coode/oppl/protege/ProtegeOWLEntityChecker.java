package org.coode.oppl.protege;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.find.OWLEntityFinder;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/** @author Luigi Iannone */
public final class ProtegeOWLEntityChecker implements OWLEntityChecker {
    private final OWLEditorKit owlEditorKit;
    private final OWLEntityFinder delegate;

    /** @param owlEditorKit
     *            owlEditorKit */
    public ProtegeOWLEntityChecker(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = checkNotNull(owlEditorKit, "owlEditorKit");
        delegate = getOWLEditorKit().getOWLModelManager().getOWLEntityFinder();
    }

    /** @return the owlEditorKit */
    public OWLEditorKit getOWLEditorKit() {
        return owlEditorKit;
    }

    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(String rendering) {
        return delegate.getOWLAnnotationProperty(rendering);
    }

    @Override
    public OWLClass getOWLClass(String rendering) {
        return delegate.getOWLClass(rendering);
    }

    @Override
    public OWLDataProperty getOWLDataProperty(String rendering) {
        return delegate.getOWLDataProperty(rendering);
    }

    @Override
    public OWLDatatype getOWLDatatype(String rendering) {
        return delegate.getOWLDatatype(rendering);
    }

    /** @param rendering
     *            rendering
     * @return entity */
    public OWLEntity getOWLEntity(String rendering) {
        return delegate.getOWLEntity(rendering);
    }

    /** @return renderings */
    public Set<String> getOWLEntityRenderings() {
        return delegate.getOWLEntityRenderings();
    }

    @Override
    public OWLNamedIndividual getOWLIndividual(String rendering) {
        return delegate.getOWLIndividual(rendering);
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(String rendering) {
        return delegate.getOWLObjectProperty(rendering);
    }
}
