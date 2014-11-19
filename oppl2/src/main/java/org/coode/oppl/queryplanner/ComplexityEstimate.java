package org.coode.oppl.queryplanner;

import static org.coode.oppl.StreamUtils.*;
import static org.coode.oppl.utils.ArgCheck.checkNotNull;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.add;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableScope;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.function.ValueComputationParameters;
import org.coode.oppl.log.Logging;
import org.coode.oppl.search.AssignableValueExtractor;
import org.coode.oppl.utils.AbstractVariableVisitorExAdapter;
import org.coode.oppl.utils.VariableExtractor;
import org.coode.oppl.variabletypes.ANNOTATIONPROPERTYVariableType;
import org.coode.oppl.variabletypes.CLASSVariableType;
import org.coode.oppl.variabletypes.CONSTANTVariableType;
import org.coode.oppl.variabletypes.DATAPROPERTYVariableType;
import org.coode.oppl.variabletypes.INDIVIDUALVariableType;
import org.coode.oppl.variabletypes.InputVariable;
import org.coode.oppl.variabletypes.OBJECTPROPERTYVariableType;
import org.coode.oppl.variabletypes.VariableTypeVisitorEx;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLRuntimeException;

/** @author Luigi Iannone */
public class ComplexityEstimate implements QueryPlannerVisitorEx<Float> {

    private final VariableExtractor variableExtractor;
    private final RuntimeExceptionHandler runtimeExceptionHandler;
    private final ConstraintSystem constraintSystem;
    private final VariableTypeVisitorEx<Set<? extends OWLObject>> assignableValuesVisitor = new VariableTypeVisitorEx<Set<? extends OWLObject>>() {

        @Override
        public Set<? extends OWLObject> visitCLASSVariableType(
                CLASSVariableType classVariableType) {
            return allClasses;
        }

        @Override
        public Set<? extends OWLObject> visitOBJECTPROPERTYVariableType(
                OBJECTPROPERTYVariableType objectpropertyVariableType) {
            return allObjectProperties;
        }

        @Override
        public Set<? extends OWLObject> visitDATAPROPERTYVariableType(
                DATAPROPERTYVariableType datapropertyVariableType) {
            return allDataProperties;
        }

        @Override
        public Set<? extends OWLObject> visitINDIVIDUALVariableType(
                INDIVIDUALVariableType individualVariableType) {
            return allIndividuals;
        }

        @Override
        public Set<? extends OWLObject> visitCONSTANTVariableType(
                CONSTANTVariableType constantVariableType) {
            return allConstants;
        }

        @Override
        public Set<? extends OWLObject> visitANNOTATIONPROPERTYVariableType(
                ANNOTATIONPROPERTYVariableType annotationpropertyVariableType) {
            return allAnnotationProperties;
        }
    };
    protected final Set<OWLAnnotationProperty> allAnnotationProperties = new HashSet<>();
    protected final Set<OWLClass> allClasses = new HashSet<>();
    protected final Set<OWLLiteral> allConstants = new HashSet<>();
    protected final Set<OWLDataProperty> allDataProperties = new HashSet<>();
    protected final Set<OWLIndividual> allIndividuals = new HashSet<>();
    protected final Set<OWLObjectProperty> allObjectProperties = new HashSet<>();

    /**
     * @param contraintSystem
     *        contraintSystem
     * @param runtimeExceptionHandler
     *        runtimeExceptionHandler
     */
    public ComplexityEstimate(ConstraintSystem contraintSystem,
            RuntimeExceptionHandler runtimeExceptionHandler) {
        constraintSystem = checkNotNull(contraintSystem, "contraintSystem");
        this.runtimeExceptionHandler = checkNotNull(runtimeExceptionHandler,
                "runtimeExceptionHandler");
        variableExtractor = new VariableExtractor(contraintSystem, false);
        initAssignableValues();
    }

    @Override
    public Float visitConstraintQueryPlannerItem(
            ConstraintQueryPlannerItem constraintQueryPlannerItem) {
        // Constraints do not require computing oll the possible permutations of
        // values as variables are always resolved before getting to constraint
        // evaluation
        return 0f;
    }

    @Override
    public Float visitAssertedAxiomPlannerItem(
            AssertedAxiomPlannerItem assertedAxiomPlannerItem) {
        return computeAxiomComplexity(assertedAxiomPlannerItem.getAxiom());
    }

    private float computeAxiomComplexity(OWLAxiom axiom) {
        Set<Variable<?>> variables = variableExtractor.extractVariables(axiom);
        long toReturn = 1;
        ValueComputationParameters parameters = new SimpleValueComputationParameters(
                getConstraintSystem(), BindingNode.createNewEmptyBindingNode(),
                getRuntimeExceptionHandler());
        for (Variable<?> variable : variables) {
            toReturn *= getAssignableValues(variable, parameters).size();
        }
        return toReturn;
    }

    private Set<OWLObject> getAssignableValues(Variable<?> variable,
            ValueComputationParameters parameters) {
        Set<OWLObject> toReturn = new HashSet<>();
        toReturn.addAll(variable.accept(new AssignableValueExtractor(
                assignableValuesVisitor, parameters)));
        Iterator<OWLObject> iterator = toReturn.iterator();
        while (iterator.hasNext()) {
            final OWLObject owlObject = iterator.next();
            boolean inScope = variable
                    .accept(new AbstractVariableVisitorExAdapter<Boolean>(true) {

                        @Override
                        public <P extends OWLObject> Boolean visit(
                                InputVariable<P> v) {
                            VariableScope<?> variableScope = v
                                    .getVariableScope();
                            try {
                                return variableScope == null
                                        || variableScope.check(owlObject);
                            } catch (OWLRuntimeException e) {
                                ComplexityEstimate.this
                                        .getRuntimeExceptionHandler()
                                        .handleOWLRuntimeException(e);
                                return false;
                            }
                        }
                    });
            if (!inScope) {
                iterator.remove();
            }
        }
        return toReturn;
    }

    @Override
    public Float visitInferredAxiomQueryPlannerItem(
            InferredAxiomQueryPlannerItem inferredAxiomQueryPlannerItem) {
        return computeAxiomComplexity(inferredAxiomQueryPlannerItem.getAxiom());
    }

    /** @return the variableExtractor */
    protected VariableExtractor getVariableExtractor() {
        return variableExtractor;
    }

    /** @return the runtimeExceptionHandler */
    public RuntimeExceptionHandler getRuntimeExceptionHandler() {
        return runtimeExceptionHandler;
    }

    private void initAssignableValues() {
        OWLOntologyManager m = getConstraintSystem()
                .getOntologyManager();
        add(allClasses, getAllClasses(m.ontologies()));
        Logging.getQueryLogger().fine("Possible class values ",
                allClasses.size());
        add(allDataProperties, getAllDataProperties(m.ontologies()));
        Logging.getQueryLogger().fine("Possible data property values ",
                allDataProperties.size());
        add(allObjectProperties, getAllObjectProperties(m.ontologies()));
        Logging.getQueryLogger().fine("Possible object property values ",
                allObjectProperties.size());
        add(allIndividuals, getAllIndividuals(m.ontologies()));
        Logging.getQueryLogger().fine("Possible individual  values ",
                allIndividuals.size());
        add(allConstants, getAllConstants(m.ontologies()));
        Logging.getQueryLogger().fine("Possible constant  values ",
                allConstants.size());
        add(allAnnotationProperties,
                getAllAnnotationProperties(m.ontologies()));
        Logging.getQueryLogger().fine("Possible annotation properties values ",
                allAnnotationProperties.size());
    }

    /** @return the constraintSystem */
    public ConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }
}
