package org.coode.parsers.oppl.testcase.assertions;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Variable;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.function.ValueComputationParameters;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.parsers.oppl.testcase.AbstractOPPLTestCaseFactory;
import org.semanticweb.owlapi.model.OWLObject;

/** Asserts that the bindings of a Variable contain a specified value.
 * 
 * @author Luigi Iannone */
public class AssertContains implements Assertion {
    private final Variable<?> variable;
    private final ConstraintSystem constraintSystem;
    private final Set<OWLObject> values = new HashSet<>();
    private final AbstractOPPLTestCaseFactory testCaseFactory;
    private final RuntimeExceptionHandler handler;

    /** @param variable
     *            variable
     * @param values
     *            values
     * @param constraintSystem
     *            constraintSystem
     * @param testCaseFactory
     *            testCaseFactory
     * @param handler
     *            handler */
    public AssertContains(Variable<?> variable, Collection<? extends OWLObject> values,
            ConstraintSystem constraintSystem,
            AbstractOPPLTestCaseFactory testCaseFactory, RuntimeExceptionHandler handler) {
        this.variable = checkNotNull(variable, "variable");
        this.values.addAll(checkNotNull(values, "values"));
        if (values.isEmpty()) {
            throw new IllegalArgumentException("The values cannot be empty");
        }
        this.constraintSystem = checkNotNull(constraintSystem, "constraintSystem");
        this.testCaseFactory = checkNotNull(testCaseFactory, "testCaseFactory");
        this.handler = checkNotNull(handler, "handler");
    }

    /** @return the variable */
    public Variable<?> getVariable() {
        return variable;
    }

    @Override
    public <O> O accept(AssertionVisitorEx<O> visitor) {
        return visitor.visitAssertContains(this);
    }

    @Override
    public void accept(AssertionVisitor visitor) {
        visitor.visitAssertContains(this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(String.format("%s CONTAINS ", getVariable().getName()));
        boolean first = true;
        for (OWLObject v : getValues()) {
            String comma = first ? "" : ", ";
            first = false;
            ManchesterSyntaxRenderer renderer = getTestCaseFactory().getOPPLFactory()
                    .getManchesterSyntaxRenderer(getConstraintSystem());
            v.accept(renderer);
            b.append(String.format("%s%s", comma, renderer));
        }
        return b.toString();
    }

    /** @return the constraintSystem */
    public ConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }

    /** @return the testCaseFactory */
    public AbstractOPPLTestCaseFactory getTestCaseFactory() {
        return testCaseFactory;
    }

    @Override
    public boolean holds(Set<? extends BindingNode> bindings, ConstraintSystem cs) {
        Set<OWLObject> containerValues = new HashSet<>(bindings.size());
        for (BindingNode bindingNode : bindings) {
            ValueComputationParameters parameters = new SimpleValueComputationParameters(
                    cs, bindingNode, getHandler());
            OWLObject value = bindingNode.getAssignmentValue(getVariable(), parameters);
            if (value != null) {
                containerValues.add(value);
            }
        }
        return containerValues.containsAll(getValues());
    }

    /** @return the values */
    public Set<OWLObject> getValues() {
        return new HashSet<>(values);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (values == null ? 0 : values.hashCode());
        result = prime * result + (variable == null ? 0 : variable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        AssertContains other = (AssertContains) obj;
        if (values == null) {
            if (other.values != null) {
                return false;
            }
        } else if (!values.equals(other.values)) {
            return false;
        }
        if (variable == null) {
            if (other.variable != null) {
                return false;
            }
        } else if (!variable.equals(other.variable)) {
            return false;
        }
        return true;
    }

    /** @return the handler */
    public RuntimeExceptionHandler getHandler() {
        return handler;
    }
}
