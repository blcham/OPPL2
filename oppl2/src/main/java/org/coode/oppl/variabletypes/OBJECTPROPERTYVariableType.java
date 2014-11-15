package org.coode.oppl.variabletypes;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.coode.oppl.VariableScopes.Direction;
import org.coode.oppl.function.OPPLFunction;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/** @author Luigi Iannone */
public class OBJECTPROPERTYVariableType extends
        AbstractVariableType<OWLObjectPropertyExpression> implements
        VariableType<OWLObjectPropertyExpression> {
    /** @param name
     *            name */
    public OBJECTPROPERTYVariableType(VariableTypeName name) {
        super(name, EnumSet.of(Direction.SUBPROPERTYOF, Direction.SUPERPROPERTYOF));
    }

    @Override
    public void accept(VariableTypeVisitor visitor) {
        visitor.visitOBJECTPROPERTYVariableType(this);
    }

    @Override
    public <P> P accept(VariableTypeVisitorEx<P> visitor) {
        return visitor.visitOBJECTPROPERTYVariableType(this);
    }

    @Override
    public RegexpGeneratedVariable<? extends OWLObjectPropertyExpression>
            getRegexpGeneratedVariable(String name,
                    OPPLFunction<Pattern> patternGeneratingOPPLFunction) {
        return new RegexpGeneratedVariable<OWLObjectPropertyExpression>(name,
                VariableTypeFactory.getOBJECTPROPERTYTypeVariableType(),
                patternGeneratingOPPLFunction);
    }

    @Override
    public Set<OWLObjectPropertyExpression> getReferencedOWLObjects(
            Collection<? extends OWLOntology> ontologies) {
        Set<OWLObjectPropertyExpression> toReturn = new HashSet<OWLObjectPropertyExpression>();
        for (OWLOntology ontology : ontologies) {
            toReturn.addAll(ontology.getObjectPropertiesInSignature());
        }
        return toReturn;
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
            public Boolean visit(OWLObjectProperty property) {
                return true;
            }

            @Override
            public Boolean visit(OWLObjectInverseOf property) {
                return true;
            }
        });
    }
}
