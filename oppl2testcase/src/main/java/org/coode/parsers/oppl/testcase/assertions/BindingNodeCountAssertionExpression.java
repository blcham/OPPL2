/**
 * 
 */
package org.coode.parsers.oppl.testcase.assertions;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.bindingtree.Assignment;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.parsers.oppl.testcase.AbstractOPPLTestCaseFactory;

/** Represents the count of the occurrences a particular set of assignments in
 * the results of the query.
 * 
 * @author Luigi Iannone */
public class BindingNodeCountAssertionExpression implements AssertionExpression<Integer> {
    private final BindingNode bindingNode;
    private final AbstractOPPLTestCaseFactory testCaseFactory;
    private final ConstraintSystem constraintSystem;

    /** @param bindingNode */
    public BindingNodeCountAssertionExpression(BindingNode bindingNode,
            ConstraintSystem constraintSystem, AbstractOPPLTestCaseFactory testCaseFactory) {
        if (bindingNode == null) {
            throw new NullPointerException("The binding node cannot be null");
        }
        if (constraintSystem == null) {
            throw new NullPointerException("The constraint system cannot be null");
        }
        if (testCaseFactory == null) {
            throw new NullPointerException("The test case factory cannot be null");
        }
        this.bindingNode = bindingNode;
        this.testCaseFactory = testCaseFactory;
        this.constraintSystem = constraintSystem;
    }

    @Override
    public void accept(AssertionExpressionVisitor assertionExpressionVisitor) {
        assertionExpressionVisitor.visitBindingNodeCountAssertionExpression(this);
    }

    @Override
    public <O> O accept(AssertionExpressionVisitorEx<O> assertionExpressionVisitor) {
        return assertionExpressionVisitor.visitBindingNodeCountAssertionExpression(this);
    }

    @Override
    public Integer resolve(Set<? extends BindingNode> bindings,
            ConstraintSystem constraintSystem) {
        int count = 0;
        for (BindingNode bindingNode : bindings) {
            if (bindingNode.getAssignments().containsAll(
                    getBindingNode().getAssignments())) {
                count++;
            }
        }
        return count;
    }

    /** @return the bindingNode */
    public BindingNode getBindingNode() {
        return bindingNode;
    }

    /** @return the testCaseFactory */
    public AbstractOPPLTestCaseFactory getTestCaseFactory() {
        return testCaseFactory;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("count(");
        boolean first = true;
        TreeSet<Assignment> sortedAssignment = new TreeSet<Assignment>(
                new Comparator<Assignment>() {
                    @Override
                    public int compare(Assignment o1, Assignment o2) {
                        return o1.getAssignedVariable().getName()
                                .compareTo(o2.getAssignedVariable().getName());
                    }
                });
        sortedAssignment.addAll(getBindingNode().getAssignments());
        for (Assignment a : sortedAssignment) {
            String comma = first ? "" : ", ";
            ManchesterSyntaxRenderer renderer = getTestCaseFactory().getOPPLFactory()
                    .getManchesterSyntaxRenderer(getConstraintSystem());
            a.getAssignment().accept(renderer);
            b.append(String.format("%s%s = %s", comma, a.getAssignedVariable().getName(),
                    renderer));
            first = false;
        }
        b.append(")");
        return b.toString();
    }

    /** @return the constraintSystem */
    public ConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bindingNode == null ? 0 : bindingNode.hashCode());
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
        BindingNodeCountAssertionExpression other = (BindingNodeCountAssertionExpression) obj;
        if (bindingNode == null) {
            if (other.bindingNode != null) {
                return false;
            }
        } else if (!bindingNode.equals(other.bindingNode)) {
            return false;
        }
        return true;
    }
}