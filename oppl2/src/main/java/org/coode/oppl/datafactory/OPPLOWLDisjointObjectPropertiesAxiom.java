package org.coode.oppl.datafactory;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.coode.oppl.function.inline.InlineSet;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLPairwiseVisitor;

/** @author Luigi Iannone */
public class OPPLOWLDisjointObjectPropertiesAxiom extends
        AbstractInlineSetAxiom<OWLObjectPropertyExpression> implements
        OWLDisjointObjectPropertiesAxiom {

    private static final long serialVersionUID = 20100L;
    private final OWLDisjointObjectPropertiesAxiom delegate;
    private final boolean shouldExpandAsPairWise;

    /**
     * @param dataFactory
     *        dataFactory
     * @param propertyExpressions
     *        propertyExpressions
     * @param annotations
     *        annotations
     * @param shouldExpandAsPairWise
     *        shouldExpandAsPairWise
     */
    public OPPLOWLDisjointObjectPropertiesAxiom(OPPLOWLDataFactory dataFactory,
            InlineSet<OWLObjectPropertyExpression> propertyExpressions,
            Set<OWLAnnotation> annotations, boolean shouldExpandAsPairWise) {
        super(propertyExpressions);
        checkNotNull(dataFactory, "dataFactory");
        checkNotNull(propertyExpressions, "propertyExpressions");
        checkNotNull(annotations, "annotations");
        this.shouldExpandAsPairWise = shouldExpandAsPairWise;
        delegate = dataFactory.getDelegate()
                .getOWLDisjointObjectPropertiesAxiom(propertyExpressions,
                        annotations);
    }

    /** @return the shouldExpandAsPairwise */
    public boolean shouldExpandAsPairWise() {
        return shouldExpandAsPairWise;
    }

    // Delegate methods
    @Override
    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return delegate.getAnonymousIndividuals();
    }

    @Override
    public Stream<OWLObjectPropertyExpression> properties() {
        return delegate.properties();
    }

    @Override
    public Set<OWLObjectPropertyExpression> getPropertiesMinus(
            OWLObjectPropertyExpression property) {
        return delegate.getPropertiesMinus(property);
    }

    @Override
    public OWLDisjointObjectPropertiesAxiom getAxiomWithoutAnnotations() {
        return delegate.getAxiomWithoutAnnotations();
    }

    @Nonnull
    @Override
    public OWLAxiom getAnnotatedAxiom(
            @Nonnull Collection<OWLAnnotation> annotations) {
        return delegate.getAnnotatedAxiom(annotations);
    }

    @Nonnull
    @Override
    public Set<OWLDisjointObjectPropertiesAxiom> asPairwiseAxioms() {
        return delegate.asPairwiseAxioms();
    }

    @Nonnull
    @Override
    public <T> Collection<T> walkPairwise(
            OWLPairwiseVisitor<T, OWLObjectPropertyExpression> visitor) {
        return delegate.walkPairwise(visitor);
    }

    @Nonnull
    @Override
    public Set<OWLDisjointObjectPropertiesAxiom> splitToAnnotatedPairs() {
        return delegate.splitToAnnotatedPairs();
    }

    @Override
    public void accept(OWLAxiomVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLAxiomVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotation> annotations() {
        return null;
    }

    @Override
    public OWLAxiom getAnnotatedAxiom(Stream<OWLAnnotation> annotations) {
        return delegate.getAnnotatedAxiom(annotations);
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int compareTo(OWLObject o) {
        return delegate.compareTo(o);
    }

    @Override
    public boolean equalsIgnoreAnnotations(OWLAxiom axiom) {
        return delegate.equalsIgnoreAnnotations(axiom);
    }

    @Override
    public boolean isTopEntity() {
        return delegate.isTopEntity();
    }

    @Override
    public boolean isLogicalAxiom() {
        return delegate.isLogicalAxiom();
    }

    @Override
    public boolean isBottomEntity() {
        return delegate.isBottomEntity();
    }

    @Override
    public boolean isAnnotated() {
        return delegate.isAnnotated();
    }

    @Override
    public AxiomType<?> getAxiomType() {
        return delegate.getAxiomType();
    }

    @Override
    public boolean isOfType(AxiomType<?>... axiomTypes) {
        return delegate.isOfType(axiomTypes);
    }

    @Override
    public boolean isOfType(Set<AxiomType<?>> types) {
        return delegate.isOfType(types);
    }

    @Override
    public OWLAxiom getNNF() {
        return delegate.getNNF();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public boolean isAnnotationAxiom() {
        return delegate.isAnnotationAxiom();
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity) {
        return false;
    }
}
