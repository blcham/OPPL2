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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author Luigi Iannone */
public class CLASSVariableType extends AbstractVariableType<OWLClassExpression>
        implements VariableType<OWLClassExpression> {

    /**
     * @param name
     *        name
     */
    public CLASSVariableType(VariableTypeName name) {
        super(name, EnumSet.of(Direction.SUBCLASSOF, Direction.SUPERCLASSOF));
    }

    @Override
    public void accept(VariableTypeVisitor visitor) {
        visitor.visitCLASSVariableType(this);
    }

    @Override
    public <P> P accept(VariableTypeVisitorEx<P> visitor) {
        return visitor.visitCLASSVariableType(this);
    }

    @Override
    public RegexpGeneratedVariable<OWLClassExpression>
            getRegexpGeneratedVariable(String name,
                    OPPLFunction<Pattern> patternGeneratingOPPLFunction) {
        return new RegexpGeneratedVariable<>(name,
                VariableTypeFactory.getCLASSVariableType(),
                patternGeneratingOPPLFunction);
    }

    @Override
    public Set<OWLClassExpression> getReferencedOWLObjects(
            Collection<? extends OWLOntology> ontologies) {
        return asSet(ontologies.stream().flatMap(o -> o.classesInSignature()));
    }

    @Override
    public boolean isCompatibleWith(OWLObject o) {
        return o.accept(new OWLObjectVisitorEx<Boolean>() {

            @Nonnull
            @Override
            public Boolean doDefault(@Nonnull Object object) {
                return object instanceof OWLClass
                        || object instanceof OWLClassExpression;
            }
        });
    }
}
