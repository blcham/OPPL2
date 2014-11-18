package org.coode.oppl.utils;

import static org.semanticweb.owlapi.util.CollectionFactory.list;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;

/** @author Luigi Iannone */
public final class OWLObjectDecomposer implements
        OWLObjectVisitorEx<List<Object>> {

    @Override
    public List<Object> visit(OWLSubClassOfAxiom axiom) {
        return list(axiom.getSubClass(), axiom.getSuperClass());
    }

    @Override
    public List<Object> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return list(axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public List<Object> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLDisjointClassesAxiom axiom) {
        return asList(axiom.classExpressions(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDataPropertyDomainAxiom axiom) {
        return list(axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public List<Object> visit(OWLObjectPropertyDomainAxiom axiom) {
        return list(axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public List<Object> visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return asList(axiom.properties(), Object.class);
    }

    @Override
    public List<Object> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        return list(axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public List<Object> visit(OWLDifferentIndividualsAxiom axiom) {
        return asList(axiom.individuals(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDisjointDataPropertiesAxiom axiom) {
        return asList(axiom.properties(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDisjointObjectPropertiesAxiom axiom) {
        return asList(axiom.properties(), Object.class);
    }

    @Override
    public List<Object> visit(OWLObjectPropertyRangeAxiom axiom) {
        return list(axiom.getProperty(), axiom.getRange());
    }

    @Override
    public List<Object> visit(OWLObjectPropertyAssertionAxiom axiom) {
        return list(axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public List<Object> visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLSubObjectPropertyOfAxiom axiom) {
        return list(axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public List<Object> visit(OWLDisjointUnionAxiom axiom) {
        return asList(axiom.classExpressions(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDeclarationAxiom axiom) {
        return list(axiom.getEntity());
    }

    @Override
    public List<Object> visit(OWLAnnotationAssertionAxiom axiom) {
        return list(axiom.getSubject(), axiom.getAnnotation());
    }

    @Override
    public List<Object> visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLDataPropertyRangeAxiom axiom) {
        return list(axiom.getProperty(), axiom.getRange());
    }

    @Override
    public List<Object> visit(OWLFunctionalDataPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLEquivalentDataPropertiesAxiom axiom) {
        return asList(axiom.properties(), Object.class);
    }

    @Override
    public List<Object> visit(OWLClassAssertionAxiom axiom) {
        return list(axiom.getClassExpression(), axiom.getIndividual());
    }

    @Override
    public List<Object> visit(OWLEquivalentClassesAxiom axiom) {
        return asList(axiom.classExpressions(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDataPropertyAssertionAxiom axiom) {
        return list(axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public List<Object> visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLSubDataPropertyOfAxiom axiom) {
        return list(axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public List<Object> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return list(axiom.getProperty());
    }

    @Override
    public List<Object> visit(OWLSameIndividualAxiom axiom) {
        return asList(axiom.individuals(), Object.class);
    }

    @Override
    public List<Object> visit(OWLSubPropertyChainOfAxiom axiom) {
        List<Object> toReturn = new ArrayList<>(axiom.getPropertyChain());
        toReturn.add(axiom.getSuperProperty());
        return toReturn;
    }

    @Override
    public List<Object> visit(OWLInverseObjectPropertiesAxiom axiom) {
        return list(axiom.getFirstProperty(), axiom.getSecondProperty());
    }

    @Override
    public List<Object> visit(OWLHasKeyAxiom axiom) {
        List<Object> toReturn = list(axiom.getClassExpression());
        axiom.propertyExpressions().forEach(a -> toReturn.add(a));
        return toReturn;
    }

    @Override
    public List<Object> visit(OWLDatatypeDefinitionAxiom axiom) {
        return list(axiom.getDatatype(), axiom.getDataRange());
    }

    @Override
    public List<Object> visit(SWRLRule rule) {
        List<Object> toReturn = asList(rule.head(), Object.class);
        rule.body().forEach(a -> toReturn.add(a));
        return toReturn;
    }

    @Override
    public List<Object> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        return list(axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public List<Object> visit(OWLAnnotationPropertyDomainAxiom axiom) {
        return list(axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public List<Object> visit(OWLAnnotationPropertyRangeAxiom axiom) {
        return list(axiom.getProperty(), axiom.getRange());
    }

    @Override
    public List<Object> doDefault(Object ce) {
        return Collections.emptyList();
    }

    @Override
    public List<Object> visit(OWLObjectIntersectionOf ce) {
        return asList(ce.operands(), Object.class);
    }

    @Override
    public List<Object> visit(OWLObjectUnionOf ce) {
        return asList(ce.operands(), Object.class);
    }

    @Override
    public List<Object> visit(OWLObjectComplementOf ce) {
        return list(ce.getOperand());
    }

    @Override
    public List<Object> visit(OWLObjectSomeValuesFrom ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectAllValuesFrom ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectHasValue ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectMinCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectExactCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectMaxCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLObjectHasSelf ce) {
        return list(ce.getProperty());
    }

    @Override
    public List<Object> visit(OWLObjectOneOf ce) {
        return asList(ce.individuals(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDataSomeValuesFrom ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataAllValuesFrom ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataHasValue ce) {
        return list(ce.getProperty(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataMinCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataExactCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataMaxCardinality ce) {
        return list(ce.getProperty(), ce.getCardinality(), ce.getFiller());
    }

    @Override
    public List<Object> visit(OWLDataComplementOf node) {
        return list(node.getDataRange());
    }

    @Override
    public List<Object> visit(OWLDataOneOf node) {
        return asList(node.values(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDataIntersectionOf node) {
        return asList(node.operands(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDataUnionOf node) {
        return asList(node.operands(), Object.class);
    }

    @Override
    public List<Object> visit(OWLDatatypeRestriction node) {
        return asList(node.facetRestrictions(), Object.class);
    }

    @Override
    public List<Object> visit(OWLFacetRestriction node) {
        return list(node.getFacet(), node.getFacetValue());
    }

    @Override
    public List<Object> visit(OWLObjectInverseOf property) {
        return list(property.getInverse());
    }

    @Override
    public List<Object> visit(OWLAnnotation node) {
        return list(node.getProperty(), node.getValue());
    }

    @Override
    public List<Object> visit(SWRLClassAtom node) {
        return list(node.getPredicate(), node.getArgument());
    }

    @Override
    public List<Object> visit(SWRLDataRangeAtom node) {
        return list(node.getPredicate(), node.getArgument());
    }

    @Override
    public List<Object> visit(SWRLObjectPropertyAtom node) {
        return list(node.getPredicate(), node.getFirstArgument(),
                node.getSecondArgument());
    }

    @Override
    public List<Object> visit(SWRLDataPropertyAtom node) {
        return list(node.getPredicate(), node.getFirstArgument(),
                node.getSecondArgument());
    }

    @Override
    public List<Object> visit(SWRLBuiltInAtom node) {
        return list(node.getPredicate(), node.getArguments());
    }

    @Override
    public List<Object> visit(SWRLVariable node) {
        return list(node.getIRI());
    }

    @Override
    public List<Object> visit(SWRLIndividualArgument node) {
        return list(node.getIndividual());
    }

    @Override
    public List<Object> visit(SWRLLiteralArgument node) {
        return list(node.getLiteral());
    }

    @Override
    public List<Object> visit(SWRLSameIndividualAtom node) {
        return list(node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public List<Object> visit(SWRLDifferentIndividualsAtom node) {
        return list(node.getFirstArgument(), node.getSecondArgument());
    }
}
