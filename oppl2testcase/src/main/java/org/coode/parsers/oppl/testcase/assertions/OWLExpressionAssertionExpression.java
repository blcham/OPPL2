package org.coode.parsers.oppl.testcase.assertions;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.HashSet;
import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.PartialOWLObjectInstantiator;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.function.ValueComputationParameters;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.parsers.oppl.testcase.AbstractOPPLTestCaseFactory;
import org.semanticweb.owlapi.model.OWLObject;

/** Represents an OWL Object in an assertion.
 * 
 * @author Luigi Iannone */
public class OWLExpressionAssertionExpression implements
        AssertionExpression<Set<OWLObject>> {
    private final OWLObject owlObject;
    private final AbstractOPPLTestCaseFactory testCaseFactory;
    private final ConstraintSystem constraintSystem;
    private final RuntimeExceptionHandler handler;

    /** @param owlObject
     *            owlObject
     * @param constraintSystem
     *            constraintSystem
     * @param testCaseFactory
     *            testCaseFactory
     * @param handler
     *            handler */
    public OWLExpressionAssertionExpression(OWLObject owlObject,
            ConstraintSystem constraintSystem,
            AbstractOPPLTestCaseFactory testCaseFactory, RuntimeExceptionHandler handler) {
        this.owlObject = checkNotNull(owlObject, "owlObject");
        this.testCaseFactory = checkNotNull(testCaseFactory, "testCaseFactory");
        this.constraintSystem = checkNotNull(constraintSystem, "constraintSystem");
        this.handler = checkNotNull(handler, "handler");
    }

    @Override
    public void accept(AssertionExpressionVisitor assertionExpressionVisitor) {
        assertionExpressionVisitor.visitOWLExpressionAssertionExpression(this);
    }

    @Override
    public <O> O accept(AssertionExpressionVisitorEx<O> assertionExpressionVisitor) {
        return assertionExpressionVisitor.visitOWLExpressionAssertionExpression(this);
    }

    /** @return the owlObject */
    public OWLObject getOWLObject() {
        return owlObject;
    }

    /** @return the testCaseFactory */
    public AbstractOPPLTestCaseFactory getTestCaseFactory() {
        return testCaseFactory;
    }

    @Override
    public String toString() {
        ManchesterSyntaxRenderer renderer = getTestCaseFactory().getOPPLFactory()
                .getManchesterSyntaxRenderer(getConstraintSystem());
        owlObject.accept(renderer);
        return renderer.toString();
    }

    /** @return the constraintSystem */
    public ConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }

    @Override
    public Set<OWLObject>
            resolve(Set<? extends BindingNode> bindings, ConstraintSystem cs) {
        Set<OWLObject> toReturn = new HashSet<>();
        for (BindingNode bindingNode : bindings) {
            ValueComputationParameters parameters = new SimpleValueComputationParameters(
                    cs, bindingNode, handler);
            PartialOWLObjectInstantiator instantiator = new PartialOWLObjectInstantiator(
                    parameters);
            toReturn.add(owlObject.accept(instantiator));
        }
        return toReturn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (owlObject == null ? 0 : owlObject.hashCode());
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
        OWLExpressionAssertionExpression other = (OWLExpressionAssertionExpression) obj;
        if (owlObject == null) {
            if (other.owlObject != null) {
                return false;
            }
        } else if (!owlObject.equals(other.owlObject)) {
            return false;
        }
        return true;
    }

    /** @return the handler */
    public RuntimeExceptionHandler getHandler() {
        return handler;
    }
}
