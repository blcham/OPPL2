package org.coode.oppl.variabletypes;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
//import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

/** @author Luigi Iannone */
public class VariableTypeFactory implements OWLObjectVisitorEx{
    private static final EnumMap<VariableTypeName, VariableType<?>> typesCache = new EnumMap<>(
            VariableTypeName.class);
    static {
        typesCache.put(VariableTypeName.CLASS, new CLASSVariableType(
                VariableTypeName.CLASS));
        typesCache.put(VariableTypeName.OBJECTPROPERTY, new OBJECTPROPERTYVariableType(
                VariableTypeName.OBJECTPROPERTY));
        typesCache.put(VariableTypeName.DATAPROPERTY, new DATAPROPERTYVariableType(
                VariableTypeName.DATAPROPERTY));
        typesCache.put(VariableTypeName.ANNOTATIONPROPERTY,
                new ANNOTATIONPROPERTYVariableType(VariableTypeName.ANNOTATIONPROPERTY));
        typesCache.put(VariableTypeName.INDIVIDUAL, new INDIVIDUALVariableType(
                VariableTypeName.INDIVIDUAL));
        typesCache.put(VariableTypeName.CONSTANT, new CONSTANTVariableType(
                VariableTypeName.CONSTANT));
    }

    /** @param owlObject
     *            owlObject
     * @return variable type */
    public static VariableType<?> getVariableType(OWLObject owlObject) {
        return owlObject.accept(new OWLObjectVisitorEx<VariableType<?>>() {
            @Nonnull
            @Override
            public VariableType<?> doDefault(@Nonnull Object object) {
                return VariableTypeFactory.getCLASSVariableType();
            }

            @Override
            public VariableType<OWLDataPropertyExpression>
                    visit(OWLDataProperty property) {
                return VariableTypeFactory.getDATAPROPERTYVariableType();
            }

            @Override
            public VariableType<OWLObjectPropertyExpression> visit(
                    OWLObjectProperty property) {
                return VariableTypeFactory.getOBJECTPROPERTYTypeVariableType();
            }

            @Override
            public VariableType<OWLObjectPropertyExpression> visit(
                    OWLObjectInverseOf property) {
                return VariableTypeFactory.getOBJECTPROPERTYTypeVariableType();
            }

            @Override
            public VariableType<OWLIndividual> visit(OWLAnonymousIndividual individual) {
                return VariableTypeFactory.getINDIVIDUALVariableType();
            }

            @Override
            public VariableType<OWLIndividual> visit(OWLNamedIndividual individual) {
                return VariableTypeFactory.getINDIVIDUALVariableType();
            }

            @Override
            public VariableType<OWLLiteral> visit(OWLLiteral literal) {
                return VariableTypeFactory.getCONSTANTVariableType();
            }

            @Override
            public VariableType<?> visit(OWLAnnotationProperty property) {
                return VariableTypeFactory.getANNOTATIONPROPERTYVariableType();
            }
        });
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLClassExpression> getCLASSVariableType() {
        return (VariableType<OWLClassExpression>) getVariableType(VariableTypeName.CLASS);
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLObjectPropertyExpression>
            getOBJECTPROPERTYTypeVariableType() {
        return (VariableType<OWLObjectPropertyExpression>) getVariableType(VariableTypeName.OBJECTPROPERTY);
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLDataPropertyExpression> getDATAPROPERTYVariableType() {
        return (VariableType<OWLDataPropertyExpression>) getVariableType(VariableTypeName.DATAPROPERTY);
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLAnnotationProperty> getANNOTATIONPROPERTYVariableType() {
        return (VariableType<OWLAnnotationProperty>) getVariableType(VariableTypeName.ANNOTATIONPROPERTY);
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLIndividual> getINDIVIDUALVariableType() {
        return (VariableType<OWLIndividual>) getVariableType(VariableTypeName.INDIVIDUAL);
    }

    /** @return variable type */
    @SuppressWarnings("unchecked")
    public static VariableType<OWLLiteral> getCONSTANTVariableType() {
        return (VariableType<OWLLiteral>) getVariableType(VariableTypeName.CONSTANT);
    }

    /** @param variableTypeName
     *            variableTypeName
     * @return variable type */
    public static VariableType<?> getVariableType(VariableTypeName variableTypeName) {
        return variableTypeName == null ? null : typesCache.get(variableTypeName);
    }

    /** @param variableTypeName
     *            variableTypeName
     * @return variable type */
    public static VariableType<?> getVariableType(String variableTypeName) {
        return getVariableType(VariableTypeName.getVariableTypeName(variableTypeName));
    }

    /** @return set of variable type */
    public static Set<VariableType<?>> getAllVariableTypes() {
        Set<VariableType<?>> toReturn = new HashSet<>();
        toReturn.add(getANNOTATIONPROPERTYVariableType());
        toReturn.add(getCONSTANTVariableType());
        toReturn.add(getDATAPROPERTYVariableType());
        toReturn.add(getOBJECTPROPERTYTypeVariableType());
        toReturn.add(getCLASSVariableType());
        toReturn.add(getINDIVIDUALVariableType());
        return toReturn;
    }
}
