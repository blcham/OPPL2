/**
 * 
 */
package org.coode.parsers.oppl.testcase.assertions;

import java.util.Set;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.bindingtree.BindingNode;

/**
 * An expression that could be used as an argument in Assertions. It can be
 * resolved against a Set of bindings by calling the resolve methods. It
 * transforms the AssertionExpression into an object
 * 
 * @author Luigi Iannone
 * 
 * @param <T>
 *            The kind of object this AssertionExpression resolves to.
 * 
 */
public interface AssertionExpression<T> {
	void accept(AssertionExpressionVisitor assertionExpressionVisitor);

	<O> O accept(AssertionExpressionVisitorEx<O> assertionExpressionVisitor);

	/**
	 * Resolves the expression according to the input set of bindings.
	 * 
	 * @param bindings
	 *            The Set of bindings providing value for the variables. Cannot
	 *            be <code>null</code>.
	 * @param constraintSystem
	 *            Cannot be <code>null</code>.
	 * @return an object of class <code>T</code>.
	 * @throws NullPointerException
	 *             if either input is <code>null</code>.
	 */
	T resolve(Set<? extends BindingNode> bindings,
			ConstraintSystem constraintSystem);
}