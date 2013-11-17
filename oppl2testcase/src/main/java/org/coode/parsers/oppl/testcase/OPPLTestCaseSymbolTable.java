/**
 * 
 */
package org.coode.parsers.oppl.testcase;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Variable;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.parsers.Scope;
import org.coode.parsers.oppl.OPPLSymbolTable;
import org.coode.parsers.oppl.OPPLSyntaxTree;
import org.coode.parsers.oppl.testcase.assertions.AssertContains;
import org.coode.parsers.oppl.testcase.assertions.AssertEqual;
import org.coode.parsers.oppl.testcase.assertions.AssertNotEqual;
import org.coode.parsers.oppl.testcase.assertions.Assertion;
import org.coode.parsers.oppl.testcase.assertions.AssertionComplement;
import org.coode.parsers.oppl.testcase.assertions.AssertionExpression;
import org.coode.parsers.oppl.testcase.assertions.AssertionExpressionVisitorEx;
import org.coode.parsers.oppl.testcase.assertions.BindingNodeCountAssertionExpression;
import org.coode.parsers.oppl.testcase.assertions.CountAssertionExpression;
import org.coode.parsers.oppl.testcase.assertions.CountStarAssertionExpression;
import org.coode.parsers.oppl.testcase.assertions.DefaultAssertionExpressionVisitorExAdapter;
import org.coode.parsers.oppl.testcase.assertions.GreatThanAssertion;
import org.coode.parsers.oppl.testcase.assertions.GreaterThanEqualToAssertion;
import org.coode.parsers.oppl.testcase.assertions.IntegerAssertionExpression;
import org.coode.parsers.oppl.testcase.assertions.LessThanAssertion;
import org.coode.parsers.oppl.testcase.assertions.LessThanEqualToAssertion;
import org.coode.parsers.oppl.testcase.assertions.OWLExpressionAssertionExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/** @author Luigi Iannone */
public class OPPLTestCaseSymbolTable extends OPPLSymbolTable {
    private static final DefaultAssertionExpressionVisitorExAdapter<Boolean> INTEGER_EXPRESSION_DETECTOR = new DefaultAssertionExpressionVisitorExAdapter<Boolean>(
            false) {
        @Override
        public Boolean visitIntegerAssertionExpressionVisitor(
                IntegerAssertionExpression integerAssertionExpression) {
            return true;
        }

        @Override
        public Boolean visitBindingNodeCountAssertionExpression(
                BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
            return true;
        }

        @Override
        public Boolean visitCountAssertionExpression(
                CountAssertionExpression countAssertionExpression) {
            return true;
        }

        @Override
        public Boolean visitCountStarAssertionExpression(
                CountStarAssertionExpression countStarAssertionExpression) {
            return true;
        }
    };

    /** @param globalScope
     * @param dataFactory */
    public OPPLTestCaseSymbolTable(Scope globalScope, OWLDataFactory dataFactory) {
        super(globalScope, dataFactory);
    }

    public AssertEqual getAssertEqual(AssertionExpression<?> left,
            OPPLSyntaxTree leftNode, AssertionExpression<?> right,
            OPPLSyntaxTree rightNode, OPPLSyntaxTree parentExpression) {
        AssertEqual toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!areCompatible(left, right)) {
            getErrorListener().incompatibleSymbols(parentExpression, leftNode, rightNode);
        } else {
            toReturn = new AssertEqual(left, right);
        }
        return toReturn;
    }

    public AssertNotEqual getAssertNotEqual(AssertionExpression<?> left,
            OPPLSyntaxTree leftNode, AssertionExpression<?> right,
            OPPLSyntaxTree rightNode, OPPLSyntaxTree parentExpression) {
        AssertNotEqual toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!areCompatible(left, right)) {
            getErrorListener().incompatibleSymbols(parentExpression, leftNode, rightNode);
        } else {
            toReturn = new AssertNotEqual(left, right);
        }
        return toReturn;
    }

    public AssertContains getAssertContains(OPPLSyntaxTree variableNode,
            Collection<? extends OPPLSyntaxTree> expressionNodes,
            ConstraintSystem constraintSystem,
            AbstractOPPLTestCaseFactory testCaseFactory, OPPLSyntaxTree parentExpression,
            RuntimeExceptionHandler handler) {
        if (constraintSystem == null) {
            throw new NullPointerException("The constraint system cannot be null");
        }
        if (testCaseFactory == null) {
            throw new NullPointerException("The test case factory cannot be null");
        }
        if (handler == null) {
            throw new NullPointerException("The run-time exception cannot be null");
        }
        Variable<?> variable = constraintSystem.getVariable(variableNode.getText());
        AssertContains toReturn = null;
        Set<OWLObject> expressions = new HashSet<OWLObject>();
        boolean allFine = true;
        for (OPPLSyntaxTree expressionNode : expressionNodes) {
            OWLObject owlObject = expressionNode.getOWLObject();
            if (owlObject != null) {
                if (variable.getType().isCompatibleWith(owlObject)) {
                    expressions.add(owlObject);
                } else {
                    allFine = false;
                    getErrorListener().incompatibleSymbols(parentExpression,
                            variableNode, expressionNode);
                }
            } else {
                getErrorListener().illegalToken(expressionNode, "Null OWL object");
            }
        }
        if (expressions.isEmpty()) {
            allFine = false;
            reportIllegalToken(parentExpression, "Empty set of contained expressions");
        }
        if (variable != null) {
            if (allFine) {
                toReturn = new AssertContains(variable, expressions, constraintSystem,
                        testCaseFactory, handler);
            }
        } else {
            getErrorListener().illegalToken(variableNode, "Undefined variable");
        }
        return toReturn;
    }

    public CountAssertionExpression getCountAssertionExpression(
            OPPLSyntaxTree variableNode, ConstraintSystem constraintSystem,
            RuntimeExceptionHandler handler) {
        if (handler == null) {
            throw new NullPointerException("The run-time exception cannot be null");
        }
        Variable<?> variable = constraintSystem.getVariable(variableNode.getText());
        CountAssertionExpression toReturn = null;
        if (variable != null) {
            toReturn = new CountAssertionExpression(variable, handler);
        } else {
            getErrorListener().illegalToken(variableNode, "Undefined variable");
        }
        return toReturn;
    }

    public IntegerAssertionExpression getIntegerAssertionExpression(
            OPPLSyntaxTree intValueNode) {
        IntegerAssertionExpression toReturn = null;
        try {
            int intValue = Integer.parseInt(intValueNode.getText());
            toReturn = new IntegerAssertionExpression(intValue);
        } catch (RuntimeException e) {
            getErrorListener()
                    .reportThrowable(e, intValueNode.getToken().getLine(),
                            intValueNode.getCharPositionInLine(),
                            intValueNode.getText().length());
        }
        return toReturn;
    }

    public OWLExpressionAssertionExpression getOWLExpressionAssertionExpression(
            OPPLSyntaxTree owlObjectNode, ConstraintSystem constraintSystem,
            AbstractOPPLTestCaseFactory testCaseFactory, RuntimeExceptionHandler handler) {
        if (testCaseFactory == null) {
            throw new NullPointerException("The test case factory cannot be null");
        }
        if (constraintSystem == null) {
            throw new NullPointerException("The constraint system cannot be null");
        }
        if (handler == null) {
            throw new NullPointerException("The runtime exception handler cannot be null");
        }
        OWLExpressionAssertionExpression toReturn = null;
        OWLObject owlObject = owlObjectNode.getOWLObject();
        if (owlObject != null) {
            toReturn = new OWLExpressionAssertionExpression(owlObject, constraintSystem,
                    testCaseFactory, handler);
        } else {
            getErrorListener().illegalToken(owlObjectNode, "Null OWL Object");
        }
        return toReturn;
    }

    private boolean areCompatible(AssertionExpression<?> assertionExpression,
            AssertionExpression<?> anotherAssertionExpression) {
        return anotherAssertionExpression
                .accept(getCompatibilityChecker(assertionExpression));
    }

    private AssertionExpressionVisitorEx<Boolean> getCompatibilityChecker(
            AssertionExpression<?> assertionExpression) {
        return assertionExpression
                .accept(new AssertionExpressionVisitorEx<AssertionExpressionVisitorEx<Boolean>>() {
                    @Override
                    public AssertionExpressionVisitorEx<Boolean>
                            visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                        return new AssertionExpressionVisitorEx<Boolean>() {
                            @Override
                            public Boolean visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitBindingNodeCountAssertionExpression(
                                            BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitIntegerAssertionExpressionVisitor(
                                            IntegerAssertionExpression integerAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitCountStarAssertionExpression(
                                            CountStarAssertionExpression countStarAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitOWLExpressionAssertionExpression(
                                            OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                                return false;
                            }
                        };
                    }

                    @Override
                    public
                            AssertionExpressionVisitorEx<Boolean>
                            visitCountStarAssertionExpression(
                                    CountStarAssertionExpression countStarAssertionExpression) {
                        return new AssertionExpressionVisitorEx<Boolean>() {
                            @Override
                            public Boolean visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitBindingNodeCountAssertionExpression(
                                            BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitIntegerAssertionExpressionVisitor(
                                            IntegerAssertionExpression integerAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitCountStarAssertionExpression(
                                            CountStarAssertionExpression countStarAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitOWLExpressionAssertionExpression(
                                            OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                                return false;
                            }
                        };
                    }

                    @Override
                    public
                            AssertionExpressionVisitorEx<Boolean>
                            visitIntegerAssertionExpressionVisitor(
                                    IntegerAssertionExpression integerAssertionExpression) {
                        return new AssertionExpressionVisitorEx<Boolean>() {
                            @Override
                            public Boolean visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitBindingNodeCountAssertionExpression(
                                            BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitCountStarAssertionExpression(
                                            CountStarAssertionExpression countStarAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitIntegerAssertionExpressionVisitor(
                                            IntegerAssertionExpression integerAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitOWLExpressionAssertionExpression(
                                            OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                                return false;
                            }
                        };
                    }

                    @Override
                    public
                            AssertionExpressionVisitorEx<Boolean>
                            visitBindingNodeCountAssertionExpression(
                                    BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                        return new AssertionExpressionVisitorEx<Boolean>() {
                            @Override
                            public Boolean visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitBindingNodeCountAssertionExpression(
                                            BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitIntegerAssertionExpressionVisitor(
                                            IntegerAssertionExpression integerAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitCountStarAssertionExpression(
                                            CountStarAssertionExpression countStarAssertionExpression) {
                                return true;
                            }

                            @Override
                            public
                                    Boolean
                                    visitOWLExpressionAssertionExpression(
                                            OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                                return false;
                            }
                        };
                    }

                    @Override
                    public
                            AssertionExpressionVisitorEx<Boolean>
                            visitOWLExpressionAssertionExpression(
                                    OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                        return new AssertionExpressionVisitorEx<Boolean>() {
                            @Override
                            public Boolean visitCountAssertionExpression(
                                    CountAssertionExpression countAssertionExpression) {
                                return false;
                            }

                            @Override
                            public
                                    Boolean
                                    visitBindingNodeCountAssertionExpression(
                                            BindingNodeCountAssertionExpression bindingNodeCountAssertionExpression) {
                                return false;
                            }

                            @Override
                            public
                                    Boolean
                                    visitIntegerAssertionExpressionVisitor(
                                            IntegerAssertionExpression integerAssertionExpression) {
                                return false;
                            }

                            @Override
                            public
                                    Boolean
                                    visitCountStarAssertionExpression(
                                            CountStarAssertionExpression countStarAssertionExpression) {
                                return false;
                            }

                            @Override
                            public
                                    Boolean
                                    visitOWLExpressionAssertionExpression(
                                            OWLExpressionAssertionExpression owlExpressionAssertionExpression) {
                                return true;
                            }
                        };
                    }
                });
    }

    public AssertionComplement getAssertionComplement(Assertion a) {
        return new AssertionComplement(a);
    }

    public CountStarAssertionExpression getCountStarAssertionExpression() {
        return CountStarAssertionExpression.getInstance();
    }

    @SuppressWarnings("unchecked")
    public LessThanAssertion getAssertLessThan(AssertionExpression<?> left,
            OPPLSyntaxTree leftNode, AssertionExpression<?> right,
            OPPLSyntaxTree rightNode, OPPLSyntaxTree parentExpression) {
        LessThanAssertion toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!left.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(leftNode,
                    "Only integer values expressions can be used for comparisons");
        } else if (!right.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(rightNode,
                    "Only integer values expressions can be used for comparisons");
        } else {
            toReturn = new LessThanAssertion((AssertionExpression<Integer>) left,
                    (AssertionExpression<Integer>) right);
        }
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public LessThanEqualToAssertion getAssertLessThanEqualtTo(
            AssertionExpression<?> left, OPPLSyntaxTree leftNode,
            AssertionExpression<?> right, OPPLSyntaxTree rightNode,
            OPPLSyntaxTree parentExpression) {
        LessThanEqualToAssertion toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!left.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(leftNode,
                    "Only integer values expressions can be used for comparisons");
        } else if (!right.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(rightNode,
                    "Only integer values expressions can be used for comparisons");
        } else {
            toReturn = new LessThanEqualToAssertion((AssertionExpression<Integer>) left,
                    (AssertionExpression<Integer>) right);
        }
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public GreatThanAssertion getAssertGreaterThan(AssertionExpression<?> left,
            OPPLSyntaxTree leftNode, AssertionExpression<?> right,
            OPPLSyntaxTree rightNode, OPPLSyntaxTree parentExpression) {
        GreatThanAssertion toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!left.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(leftNode,
                    "Only integer values expressions can be used for comparisons");
        } else if (!right.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(rightNode,
                    "Only integer values expressions can be used for comparisons");
        } else {
            toReturn = new GreatThanAssertion((AssertionExpression<Integer>) left,
                    (AssertionExpression<Integer>) right);
        }
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public GreaterThanEqualToAssertion getAssertGreaterThanEqualTo(
            AssertionExpression<?> left, OPPLSyntaxTree leftNode,
            AssertionExpression<?> right, OPPLSyntaxTree rightNode,
            OPPLSyntaxTree parentExpression) {
        GreaterThanEqualToAssertion toReturn = null;
        if (left == null) {
            getErrorListener().illegalToken(leftNode,
                    "null left hand side assertion expression");
        } else if (right == null) {
            getErrorListener().illegalToken(leftNode,
                    "null right hand side assertion expression");
        } else if (!left.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(leftNode,
                    "Only integer values expressions can be used for comparisons");
        } else if (!right.accept(INTEGER_EXPRESSION_DETECTOR)) {
            getErrorListener().illegalToken(rightNode,
                    "Only integer values expressions can be used for comparisons");
        } else {
            toReturn = new GreaterThanEqualToAssertion(
                    (AssertionExpression<Integer>) left,
                    (AssertionExpression<Integer>) right);
        }
        return toReturn;
    }

    public BindingNodeCountAssertionExpression getBindingNodeCount(
            BindingNode bindingNode, ConstraintSystem constraintSystem,
            AbstractOPPLTestCaseFactory testCaseFactory) {
        return new BindingNodeCountAssertionExpression(bindingNode, constraintSystem,
                testCaseFactory);
    }
}
