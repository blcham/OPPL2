package org.coode.oppl.variabletypes;

import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asSet;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.coode.oppl.VariableScopes.Direction;
import org.coode.oppl.function.OPPLFunction;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author Luigi Iannone */
public class ANNOTATIONPROPERTYVariableType extends
        AbstractVariableType<OWLAnnotationProperty> implements
        VariableType<OWLAnnotationProperty> {

    /**
     * @param name
     *        name
     */
    public ANNOTATIONPROPERTYVariableType(VariableTypeName name) {
        super(name, EnumSet.noneOf(Direction.class));
    }

    @Override
    public Set<OWLAnnotationProperty> getReferencedOWLObjects(
            Collection<? extends OWLOntology> ontologies) {
        return asSet(ontologies.stream().flatMap(
                o -> o.annotationPropertiesInSignature()));
    }

    @Override
    public boolean isCompatibleWith(OWLObject o) {
        return o.accept(new OWLObjectVisitorEx<Boolean>() {

            @Nonnull
            @Override
            public Boolean doDefault(@Nonnull Object object) {
                return false;
            }

            @Override
            public Boolean visit(OWLAnnotationProperty property) {
                return true;
            }
        });
    }

    @Override
    public RegexpGeneratedVariable<? extends OWLAnnotationProperty>
            getRegexpGeneratedVariable(String name,
                    OPPLFunction<Pattern> patternGeneratingOPPLFunction) {
        return VariableFactory.getRegexpGeneratedVariable(name, this,
                patternGeneratingOPPLFunction);
    }

    @Override
    public void accept(VariableTypeVisitor visitor) {
        visitor.visitANNOTATIONPROPERTYVariableType(this);
    }

    @Override
    public <P> P accept(VariableTypeVisitorEx<P> visitor) {
        return visitor.visitANNOTATIONPROPERTYVariableType(this);
    }
}
