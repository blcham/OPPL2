/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.coode.oppl;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;
import static org.coode.oppl.variabletypes.VariableFactory.getGeneratedVariable;
import static org.coode.oppl.variabletypes.VariableTypeFactory.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.coode.oppl.bindingtree.Assignment;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.InvalidVariableNameException;
import org.coode.oppl.exceptions.OPPLException;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.function.Aggregandum;
import org.coode.oppl.function.Aggregation;
import org.coode.oppl.function.Create;
import org.coode.oppl.function.Expression;
import org.coode.oppl.function.OPPLFunction;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.generated.GeneratedVariable;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.coode.oppl.utils.VariableDetector;
import org.coode.oppl.utils.VariableExtractor;
import org.coode.oppl.utils.VariableRecogniser;
import org.coode.oppl.variabletypes.ANNOTATIONPROPERTYVariableType;
import org.coode.oppl.variabletypes.CLASSVariableType;
import org.coode.oppl.variabletypes.CONSTANTVariableType;
import org.coode.oppl.variabletypes.DATAPROPERTYVariableType;
import org.coode.oppl.variabletypes.INDIVIDUALVariableType;
import org.coode.oppl.variabletypes.InputVariable;
import org.coode.oppl.variabletypes.OBJECTPROPERTYVariableType;
import org.coode.oppl.variabletypes.VariableType;
import org.coode.oppl.variabletypes.VariableTypeVisitorEx;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * @author Luigi Iannone
 */
public class ConstraintSystem {

    private static final class RightKindVisitor implements
            VariableVisitorEx<Boolean> {

        public RightKindVisitor() {}

        @Override
        public <P extends OWLObject> Boolean visit(InputVariable<P> v) {
            return true;
        }

        @Override
        public <P extends OWLObject> Boolean visit(GeneratedVariable<P> v) {
            return false;
        }

        @Override
        public <P extends OWLObject> Boolean visit(
                RegexpGeneratedVariable<P> regExpGenerated) {
            return false;
        }
    }

    private static final RightKindVisitor rightKindVisitor = new RightKindVisitor();

    private final static class VariableSet {

        private final Map<String, Variable<?>> map = new HashMap<>();
        private final Map<IRI, Variable<?>> irisMap = new HashMap<>();

        public VariableSet() {}

        public Variable<?> get(String name) {
            return map.get(name);
        }

        public Variable<?> get(IRI variableIRI) {
            return irisMap.get(variableIRI);
        }

        public void store(Variable<?> variable) {
            map.put(variable.getName(), variable);
            irisMap.put(variable.getIRI(), variable);
        }

        public boolean containsVariableIRI(IRI variableURI) {
            return irisMap.containsKey(variableURI);
        }

        public Set<InputVariable<?>> getInputVariables() {
            Set<InputVariable<?>> toReturn = new HashSet<>(map
                    .values().size());
            for (Variable<?> v : map.values()) {
                if (VariableRecogniser.INPUT_VARIABLE_RECOGNISER.recognise(v)) {
                    toReturn.add((InputVariable<?>) v);
                }
            }
            return toReturn;
        }

        public Set<Variable<?>> getAllVariables() {
            return new HashSet<>(map.values());
        }

        public Set<GeneratedVariable<?>> getGeneratedVariables() {
            Set<GeneratedVariable<?>> toReturn = new HashSet<>(
                    map.values().size());
            for (Variable<?> v : map.values()) {
                if (VariableRecogniser.GENERATED_VARIABLE_RECOGNISER
                        .recognise(v)) {
                    toReturn.add((GeneratedVariable<?>) v);
                }
            }
            return toReturn;
        }

        public void remove(String name) {
            Variable<?> removed = map.remove(name);
            if (removed != null) {
                irisMap.remove(removed.getIRI());
            }
        }

        public void clear() {
            map.clear();
            irisMap.clear();
        }

        @Override
        public String toString() {
            Set<GeneratedVariable<?>> generatedVariables = getGeneratedVariables();
            StringBuilder b = new StringBuilder();
            for (Variable<?> variable : map.values()) {
                b.append(String.format("Variable name: %s generated: %s \n",
                        variable.getName(),
                        generatedVariables.contains(variable)));
            }
            return b.toString();
        }
    }

    /**
     *
     */
    public static final String VARIABLE_NAME_INVALID_CHARACTERS_REGEXP = "[[^\\?]&&[^\\p{Alnum}]&&[^-_]]";
    protected final VariableSet variables = new VariableSet();
    private final OWLOntology ontology;
    private Set<BindingNode> leaves = null;
    private OWLReasoner reasoner = null;
    private final OWLOntologyManager ontologyManager;
    private final OPPLAbstractFactory opplFactory;
    private final VariableDetector variableDetector = new VariableDetector(this);

    /**
     * @param ontology
     *        ontology
     * @param ontologyManager
     *        ontologyManager
     * @param opplFactory
     *        opplFactory
     */
    public ConstraintSystem(OWLOntology ontology,
            OWLOntologyManager ontologyManager, OPPLAbstractFactory opplFactory) {
        this.opplFactory = checkNotNull(opplFactory, "ontology");
        this.ontology = checkNotNull(ontology, "ontology manager");
        this.ontologyManager = checkNotNull(ontologyManager, "OPPL factory");
    }

    /**
     * @param ontology
     *        ontology
     * @param ontologyManager
     *        ontologyManager
     * @param reasoner
     *        reasoner
     * @param opplAbstractFactory
     *        opplAbstractFactory
     */
    public ConstraintSystem(OWLOntology ontology,
            OWLOntologyManager ontologyManager, OWLReasoner reasoner,
            OPPLAbstractFactory opplAbstractFactory) {
        this(ontology, ontologyManager, opplAbstractFactory);
        this.reasoner = reasoner;
    }

    /**
     * @param name
     *        name
     * @return variable for name
     */
    public Variable<?> getVariable(String name) {
        return variables.get(name);
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param variableScope
     *        variableScope
     * @param <O>
     *        variable type
     * @return input variable
     * @throws OPPLException
     *         OPPLException
     */
    public <O extends OWLObject> InputVariable<O> createVariable(
            final String name, final VariableType<O> type,
            final VariableScope<?> variableScope) throws OPPLException {
        if (name.matches("\\?([\\p{Alnum}[-_]])+")) {
            return createVariableWithVerifiedName(name, type, variableScope);
        } else {
            throw new InvalidVariableNameException("Invalid name: " + name);
        }
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param variableScope
     *        variableScope
     * @param <O>
     *        variable type
     * @return input variable
     * @throws OPPLException
     *         OPPLException
     */
    public <O extends OWLObject> InputVariable<O>
            createVariableWithVerifiedName(final String name,
                    final VariableType<O> type,
                    final VariableScope<?> variableScope) throws OPPLException {
        Variable<?> newVariable = variables.get(name);
        if (newVariable == null) {
            newVariable = type.getInputVariable(name, variableScope);
            variables.store(newVariable);
        } else {
            VariableVisitorEx<Variable<?>> visitor = new VariableVisitorEx<Variable<?>>() {

                @Override
                public <P extends OWLObject> Variable<?> visit(
                        InputVariable<P> v) {
                    Variable<?> toReturn = v;
                    if (v.getVariableScope() != variableScope) {
                        toReturn = type.getInputVariable(name, variableScope);
                        variables.store(toReturn);
                    }
                    return toReturn;
                }

                @Override
                public <P extends OWLObject> Variable<?> visit(
                        GeneratedVariable<P> v) {
                    return v;
                }

                @Override
                public <P extends OWLObject> Variable<?> visit(
                        RegexpGeneratedVariable<P> regExpGenerated) {
                    return regExpGenerated;
                }
            };
            boolean rightKind = newVariable.accept(rightKindVisitor);
            if (!rightKind) {
                throw new OPPLException(
                        String.format(
                                "A generated or regexp variable named %s already exists",
                                name));
            } else if (type != newVariable.getType()) {
                throw new OPPLException(
                        String.format(
                                "An input variable named %s already exists but is of a different type %s from the input one %s",
                                name, newVariable, type));
            } else {
                newVariable = newVariable.accept(visitor);
            }
        }
        return (InputVariable<O>) newVariable;
    }

    /**
     * @param axiom
     *        axiom
     * @return variables for axiom
     */
    public Set<Variable<?>> getAxiomVariables(OWLAxiom axiom) {
        VariableExtractor axiomVariableExtractor = new VariableExtractor(this,
                true);
        Set<Variable<?>> axiomVariables = axiomVariableExtractor
                .extractVariables(axiom);
        return new HashSet<>(axiomVariables);
    }

    /**
     * @param iri
     *        iri
     * @return true if uri is a variable
     */
    public boolean isVariableIRI(IRI iri) {
        return variables.containsVariableIRI(iri);
    }

    /**
     * @param iri
     *        iri
     * @return variable for iri
     */
    public Variable<?> getVariable(IRI iri) {
        return variables.get(iri);
    }

    /**
     * @param e
     *        e
     * @return true if desc is a variable
     */
    public boolean isVariable(OWLObject e) {
        return e.accept(variableDetector);
    }

    /**
     * @param bindingNode
     *        bindingNode
     */
    public void addLeaf(BindingNode bindingNode) {
        if (leaves == null) {
            leaves = new HashSet<>();
        }
        leaves.add(bindingNode);
    }

    /**
     * @param v
     *        v
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     * @return variable bindings
     */
    public Set<OWLObject> getVariableBindings(Variable<?> v,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        Set<OWLObject> toReturn = new HashSet<>();
        SimpleValueComputationParameters parameters = new SimpleValueComputationParameters(
                this, BindingNode.getEmptyBindingNode(),
                runtimeExceptionHandler);
        if (leaves != null) {
            for (BindingNode bindingNode : leaves) {
                OWLObject assignmentValue = bindingNode.getAssignmentValue(v,
                        parameters);
                if (assignmentValue != null) {
                    toReturn.add(assignmentValue);
                }
            }
        }
        return toReturn;
    }

    /**
     * @param variable
     *        variable
     * @param object
     *        object
     * @return true if leaf is added
     */
    public boolean addLeaf(Variable<?> variable, OWLObject object) {
        boolean toReturn = variable.getType().isCompatibleWith(object);
        if (toReturn) {
            if (leaves == null) {
                leaves = new HashSet<>();
            }
            Assignment assignment = new Assignment(variable, object);
            if (!leaves.isEmpty()) {
                for (BindingNode node : leaves) {
                    node.addAssignment(assignment);
                }
            } else {
                leaves.add(new BindingNode(assignment));
            }
        }
        return toReturn;
    }

    /**
     * @return the leaves
     */
    public Set<BindingNode> getLeaves() {
        return leaves == null ? leaves : new HashSet<>(leaves);
    }

    /**
     * @return oppl factory
     */
    public OPPLAbstractFactory getOPPLFactory() {
        return opplFactory;
    }

    /**
     * @return input variables
     */
    public Set<InputVariable<?>> getInputVariables() {
        return variables.getInputVariables();
    }

    /**
     * @param binding
     *        binding
     */
    public void removeBinding(BindingNode binding) {
        leaves.remove(binding);
    }

    /**
     * @param node
     *        node
     * @return true if node is variable
     */
    public boolean isVariable(OWLLiteral node) {
        return node.getLiteral().startsWith("?");
    }

    /**
     * @param newLeaves
     *        newLeaves
     */
    public void setLeaves(Collection<? extends BindingNode> newLeaves) {
        if (newLeaves == null) {
            leaves = null;
        } else {
            leaves = new HashSet<>(newLeaves);
        }
    }

    /**
     * @param reasoner
     *        reasoner
     */
    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    /**
     * @return all variables
     */
    public Set<Variable<?>> getVariables() {
        return variables.getAllVariables();
    }

    /**
     * @return the reasoner
     * @throws OWLRuntimeException
     *         OWLRuntimeException
     */
    public OWLReasoner getReasoner() {
        return reasoner;
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param value
     *        value
     * @param <O>
     *        variable type
     * @return generated variables
     */
    @SuppressWarnings("unchecked")
    public <O extends OWLObject> GeneratedVariable<O>
            createStringGeneratedVariable(final String name,
                    VariableType<O> type, final OPPLFunction<String> value) {
        GeneratedVariable<O> generatedVariable = type
                .accept(new VariableTypeVisitorEx<GeneratedVariable<O>>() {

                    @Override
                    public GeneratedVariable<O> visitCLASSVariableType(
                            CLASSVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t, Create.createOWLClass(value));
                    }

                    @Override
                    public GeneratedVariable<O>
                            visitOBJECTPROPERTYVariableType(
                                    OBJECTPROPERTYVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t, Create.createOWLObjectProperty(value));
                    }

                    @Override
                    public GeneratedVariable<O> visitDATAPROPERTYVariableType(
                            DATAPROPERTYVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t, Create.createOWLDataProperty(value));
                    }

                    @Override
                    public GeneratedVariable<O> visitINDIVIDUALVariableType(
                            INDIVIDUALVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t, Create.createOWLNamedIndividual(value));
                    }

                    @Override
                    public GeneratedVariable<O> visitCONSTANTVariableType(
                            CONSTANTVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t, Create.createOWLLiteral(value));
                    }

                    @Override
                    public GeneratedVariable<O>
                            visitANNOTATIONPROPERTYVariableType(
                                    ANNOTATIONPROPERTYVariableType t) {
                        return (GeneratedVariable<O>) getGeneratedVariable(
                                name, t,
                                Create.createOWLAnnotationProperty(value));
                    }
                });
        variables.store(generatedVariable);
        return generatedVariable;
    }

    /**
     * @return the ontology
     */
    public OWLOntology getOntology() {
        return ontology;
    }

    /**
     * @return set of generated variables
     */
    public Set<GeneratedVariable<?>> getGeneratedVariables() {
        return variables.getGeneratedVariables();
    }

    /**
     * @param v
     *        v
     * @return true if generated
     */
    public boolean isGenerated(Variable<?> v) {
        return variables.getGeneratedVariables().contains(v);
    }

    /**
     * clear the leaves
     */
    public void reset() {
        leaves = null;
    }

    /**
     * @param variable
     *        variable
     */
    public void removeVariable(Variable<?> variable) {
        variables.remove(variable.getName());
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param operands
     *        operands
     * @return generated variable
     */
    public
            GeneratedVariable<OWLClassExpression>
            createIntersectionGeneratedVariable(
                    final String name,
                    VariableType<?> type,
                    Collection<? extends Aggregandum<Collection<? extends OWLClassExpression>>> operands) {
        OWLDataFactory df = getOntologyManager().getOWLDataFactory();
        final Aggregation<OWLClassExpression, Collection<? extends OWLClassExpression>> and = Aggregation
                .buildClassExpressionIntersection(operands, df);
        GeneratedVariable<OWLClassExpression> toReturn = type
                .accept(new VariableTypeVisitorEx<GeneratedVariable<OWLClassExpression>>() {

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitCLASSVariableType(CLASSVariableType t) {
                        return getCLASSVariableType().getGeneratedVariable(
                                name, and);
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitANNOTATIONPROPERTYVariableType(
                                    ANNOTATIONPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitOBJECTPROPERTYVariableType(
                                    OBJECTPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitDATAPROPERTYVariableType(
                                    DATAPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public
                            GeneratedVariable<OWLClassExpression>
                            visitINDIVIDUALVariableType(INDIVIDUALVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitCONSTANTVariableType(CONSTANTVariableType t) {
                        return null;
                    }
                });
        variables.store(toReturn);
        return toReturn;
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param operands
     *        operands
     * @return generated variable
     */
    public
            GeneratedVariable<OWLClassExpression>
            createUnionGeneratedVariable(
                    final String name,
                    VariableType<?> type,
                    Collection<? extends Aggregandum<Collection<? extends OWLClassExpression>>> operands) {
        final Aggregation<OWLClassExpression, Collection<? extends OWLClassExpression>> function = Aggregation
                .buildClassExpressionUnion(operands, getOntologyManager()
                        .getOWLDataFactory());
        GeneratedVariable<OWLClassExpression> toReturn = type
                .accept(new VariableTypeVisitorEx<GeneratedVariable<OWLClassExpression>>() {

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitCLASSVariableType(CLASSVariableType t) {
                        return getCLASSVariableType().getGeneratedVariable(
                                name, function);
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitANNOTATIONPROPERTYVariableType(
                                    ANNOTATIONPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitOBJECTPROPERTYVariableType(
                                    OBJECTPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitDATAPROPERTYVariableType(
                                    DATAPROPERTYVariableType t) {
                        return null;
                    }

                    @Override
                    public
                            GeneratedVariable<OWLClassExpression>
                            visitINDIVIDUALVariableType(INDIVIDUALVariableType t) {
                        return null;
                    }

                    @Override
                    public GeneratedVariable<OWLClassExpression>
                            visitCONSTANTVariableType(CONSTANTVariableType t) {
                        return null;
                    }
                });
        variables.store(toReturn);
        return toReturn;
    }

    /**
     * @param name
     *        name
     * @param type
     *        type
     * @param patternGeneratingOPPLFunction
     *        patternGeneratingOPPLFunction
     * @param <O>
     *        expression type
     * @return regexp variable
     */
    public <O extends OWLObject> RegexpGeneratedVariable<? extends O>
            createRegexpGeneratedVariable(String name, VariableType<O> type,
                    OPPLFunction<Pattern> patternGeneratingOPPLFunction) {
        RegexpGeneratedVariable<? extends O> toReturn = type
                .getRegexpGeneratedVariable(name, patternGeneratingOPPLFunction);
        variables.store(toReturn);
        return toReturn;
    }

    /**
     * @param name
     *        name
     * @param expression
     *        expression
     * @param <O>
     *        expression type
     * @return expression generated variable
     */
    public <O extends OWLObject> GeneratedVariable<O>
            createExpressionGeneratedVariable(final String name, O expression) {
        GeneratedVariable<O> toReturn = expression
                .accept(new OWLObjectVisitorEx<GeneratedVariable<O>>() {

                    @Nonnull
                    @Override
                    public GeneratedVariable<O> doDefault(@Nonnull Object ce) {
                        return getCLASSGeneratedVariable(name,
                                (OWLClassExpression) ce);
                    }

                    @Override
                    public GeneratedVariable<O>
                            visit(OWLObjectProperty property) {
                        return getOBJECTPROPERTYGeneratedVariable(name,
                                property);
                    }

                    @Override
                    public GeneratedVariable<O> visit(
                            OWLObjectInverseOf property) {
                        return getOBJECTPROPERTYGeneratedVariable(name,
                                property);
                    }

                    @SuppressWarnings("unchecked")
                    protected GeneratedVariable<O> getCLASSGeneratedVariable(
                            final String varName, OWLClassExpression desc) {
                        return (GeneratedVariable<O>) getCLASSVariableType()
                                .getGeneratedVariable(
                                        varName,
                                        new Expression<>(desc));
                    }

                    @SuppressWarnings("unchecked")
                    protected GeneratedVariable<O>
                            getOBJECTPROPERTYGeneratedVariable(
                                    final String varName,
                                    OWLObjectPropertyExpression p) {
                        return (GeneratedVariable<O>) getOBJECTPROPERTYTypeVariableType()
                                .getGeneratedVariable(
                                        varName,
                                        new Expression<>(
                                                p));
                    }
                });
        variables.store(toReturn);
        return toReturn;
    }

    /**
     * @param variable
     *        variable
     * @return render
     */
    public String render(Variable<?> variable) {
        return variable.getName();
    }

    /**
     * @param v
     *        v
     */
    public void importVariable(Variable<?> v) {
        variables.store(v);
    }

    /**
     * clear variables
     */
    public void clearVariables() {
        variables.clear();
    }

    /**
     * @return the ontologyManager
     */
    public OWLOntologyManager getOntologyManager() {
        return ontologyManager;
    }
}
