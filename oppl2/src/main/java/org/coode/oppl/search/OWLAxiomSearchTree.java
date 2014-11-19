package org.coode.oppl.search;

import static org.coode.oppl.StreamUtils.*;
import static org.coode.oppl.utils.ArgCheck.checkNotNull;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.add;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.coode.oppl.PartialOWLObjectInstantiator;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableVisitorEx;
import org.coode.oppl.bindingtree.Assignment;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.function.ValueComputationParameters;
import org.coode.oppl.generated.GeneratedVariable;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.oppl.utils.VariableExtractor;
import org.coode.oppl.variabletypes.ANNOTATIONPROPERTYVariableType;
import org.coode.oppl.variabletypes.CLASSVariableType;
import org.coode.oppl.variabletypes.CONSTANTVariableType;
import org.coode.oppl.variabletypes.DATAPROPERTYVariableType;
import org.coode.oppl.variabletypes.INDIVIDUALVariableType;
import org.coode.oppl.variabletypes.InputVariable;
import org.coode.oppl.variabletypes.OBJECTPROPERTYVariableType;
import org.coode.oppl.variabletypes.VariableTypeVisitorEx;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

/** @author Luigi Iannone */
public class OWLAxiomSearchTree extends SearchTree<OWLAxiom> {

    private final ValueComputationParameters parameters;

    /**
     * @param parameters
     *        parameters
     */
    public OWLAxiomSearchTree(ValueComputationParameters parameters) {
        this.parameters = checkNotNull(parameters, "parameters");
    }

    /** @return the parameters */
    public ValueComputationParameters getParameters() {
        return parameters;
    }

    @Override
    protected List<OWLAxiom> getChildren(OWLAxiom node) {
        Set<BindingNode> leaves = getParameters().getConstraintSystem()
                .getLeaves();
        List<OWLAxiom> toReturn = new ArrayList<>();
        VariableExtractor variableExtractor = new VariableExtractor(
                getParameters().getConstraintSystem(), false);
        Set<Variable<?>> variables = variableExtractor.extractVariables(node);
        if (!variables.isEmpty()) {
            Variable<?> variable = variables.iterator().next();
            Collection<OWLObject> values = new HashSet<>();
            if (leaves == null) {
                add(values, getAssignableValues(variable));
            } else {
                for (BindingNode bindingNode : leaves) {
                    SimpleValueComputationParameters pars = new SimpleValueComputationParameters(
                            getParameters().getConstraintSystem(), bindingNode,
                            getParameters().getRuntimeExceptionHandler());
                    if (bindingNode.containsAssignedVariable(variable)) {
                        values.add(getParameters().getBindingNode()
                                .getAssignmentValue(variable, pars));
                    } else {
                        add(values, getAssignableValues(variable));
                    }
                }
            }
            for (OWLObject value : values) {
                Assignment assignment = new Assignment(variable, value);
                BindingNode bindingNode = new BindingNode(
                        Collections.singleton(assignment), variables);
                SimpleValueComputationParameters par = new SimpleValueComputationParameters(
                        getParameters().getConstraintSystem(), bindingNode,
                        getParameters().getRuntimeExceptionHandler());
                PartialOWLObjectInstantiator instantiator = new PartialOWLObjectInstantiator(
                        par);
                toReturn.add((OWLAxiom) node.accept(instantiator));
            }
        }
        return toReturn;
    }

    @Override
    protected boolean goalReached(OWLAxiom start) {
        boolean found = false;
        Iterator<OWLOntology> iterator = getParameters().getConstraintSystem()
                .getOntologyManager().ontologies().iterator();
        while (!found && iterator.hasNext()) {
            OWLOntology ontology = iterator.next();
            found = ontology.containsAxiom(start);
        }
        return found;
    }

    protected Stream<OWLOntology> ontologies() {
        return getParameters().getConstraintSystem().getOntologyManager()
                .ontologies();
    }

    protected final VariableTypeVisitorEx<Stream<? extends OWLObject>> assignableValuesVisitor = new VariableTypeVisitorEx<Stream<? extends OWLObject>>() {

        @Override
        public Stream<? extends OWLObject> visitCLASSVariableType(
                CLASSVariableType classVariableType) {
            return getAllClasses(ontologies());
        }

        @Override
        public Stream<? extends OWLObject> visitOBJECTPROPERTYVariableType(
                OBJECTPROPERTYVariableType objectpropertyVariableType) {
            return getAllObjectProperties(ontologies());
        }

        @Override
        public Stream<? extends OWLObject> visitDATAPROPERTYVariableType(
                DATAPROPERTYVariableType datapropertyVariableType) {
            return getAllDataProperties(ontologies());
        }

        @Override
        public Stream<? extends OWLObject> visitANNOTATIONPROPERTYVariableType(
                ANNOTATIONPROPERTYVariableType annotationpropertyVariableType) {
            return getAllAnnotationProperties(ontologies());
        }

        @Override
        public Stream<? extends OWLObject> visitINDIVIDUALVariableType(
                INDIVIDUALVariableType individualVariableType) {
            return getAllIndividuals(ontologies());
        }

        @Override
        public Stream<? extends OWLObject> visitCONSTANTVariableType(
                CONSTANTVariableType constantVariableType) {
            return getAllConstants(ontologies());
        }
    };

    private Stream<? extends OWLObject>
            getAssignableValues(Variable<?> variable) {
        VariableVisitorEx<Stream<? extends OWLObject>> visitor = new VariableVisitorEx<Stream<? extends OWLObject>>() {

            @Override
            public <O extends OWLObject> Stream<? extends OWLObject> visit(
                    InputVariable<O> v) {
                return v.getType().accept(assignableValuesVisitor);
            }

            @Override
            public <O extends OWLObject> Stream<? extends OWLObject> visit(
                    RegexpGeneratedVariable<O> v) {
                Stream<? extends OWLObject> result = v.getType().accept(
                        assignableValuesVisitor);
                Iterator<? extends OWLObject> iterator = result.iterator();
                while (iterator.hasNext()) {
                    OWLObject owlObject = iterator.next();
                    ManchesterSyntaxRenderer renderer = OWLAxiomSearchTree.this
                            .getParameters()
                            .getConstraintSystem()
                            .getOPPLFactory()
                            .getManchesterSyntaxRenderer(
                                    OWLAxiomSearchTree.this.getParameters()
                                            .getConstraintSystem());
                    owlObject.accept(renderer);
                    if (!v.getPatternGeneratingOPPLFunction()
                            .compute(OWLAxiomSearchTree.this.getParameters())
                            .matcher(renderer.toString()).matches()) {
                        iterator.remove();
                    }
                }
                return result;
            }

            @Override
            public <O extends OWLObject> Stream<? extends OWLObject> visit(
                    GeneratedVariable<O> v) {
                return Stream.empty();
            }
        };
        return variable.accept(visitor);
    }

    /** @return the assignableValuesVisitor */
    public VariableTypeVisitorEx<Stream<? extends OWLObject>>
            getAssignableValuesVisitor() {
        return assignableValuesVisitor;
    }
}
