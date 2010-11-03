package org.coode.parsers.oppl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.Token;
import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.InCollectionConstraint;
import org.coode.oppl.InequalityConstraint;
import org.coode.oppl.PlainVariableVisitorEx;
import org.coode.oppl.Variable;
import org.coode.oppl.exceptions.OPPLException;
import org.coode.oppl.function.VariableAttribute;
import org.coode.oppl.generated.GeneratedVariable;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.coode.parsers.DefaultTypeVistorEx;
import org.coode.parsers.ManchesterOWLSyntaxTree;
import org.coode.parsers.OWLEntitySymbol;
import org.coode.parsers.OWLLiteralSymbol;
import org.coode.parsers.OWLType;
import org.coode.parsers.Scope;
import org.coode.parsers.Symbol;
import org.coode.parsers.SymbolTable;
import org.coode.parsers.Type;
import org.coode.parsers.oppl.variableattribute.CollectionVariableAttributeSymbol;
import org.coode.parsers.oppl.variableattribute.StringVariableAttributeSymbol;
import org.coode.parsers.oppl.variableattribute.VariableAttributeSymbol;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

public class OPPLSymbolTable extends SymbolTable {
	/**
	 * @param globalScope
	 * @param dataFactory
	 */
	public OPPLSymbolTable(Scope globalScope, OWLDataFactory dataFactory) {
		super(globalScope, dataFactory);
	}

	/**
	 * Defines a new Symbol in this OPPLSymbolTable under the input Token.
	 * 
	 * @param token
	 *            The input token. Cannot be {@code null}.
	 * @param symbol
	 *            The Symbol to be defined. Cannot be {@code null}.
	 * 
	 */
	public void define(Token token, Symbol symbol) {
		if (token == null) {
			throw new NullPointerException("The token cannot be null");
		}
		if (symbol == null) {
			throw new NullPointerException("The symbol cannot be null");
		}
		this.storeSymbol(token, symbol);
	}

	public void defineVariable(ManchesterOWLSyntaxTree identifier,
			ManchesterOWLSyntaxTree variableType, ConstraintSystem constraintSystem) {
		VariableType type = VariableType.getVariableType(variableType.getText());
		if (type == null) {
			this.reportIllegalToken(
					variableType,
					"The variable type is not amongst those recognised");
		} else {
			this.define(
					identifier.token,
					type.getSymbol(this.getDataFactory(), identifier.token.getText()));
			try {
				constraintSystem.createVariable(
						identifier.token.getText(),
						type.getOPPLVariableType());
			} catch (OPPLException e) {
				this.reportIllegalToken(identifier, "Error in creating variable: " + e.getMessage());
			}
		}
	}

	public Type getClassVariableScopeType(ManchesterOWLSyntaxTree parentExpression,
			ManchesterOWLSyntaxTree classExpression) {
		Type toReturn = null;
		if (!OWLType.isClassExpression(classExpression.getEvalType())) {
			this.reportIncompatibleSymbolType(
					classExpression,
					classExpression.getEvalType(),
					parentExpression);
		} else {
			toReturn = classExpression.getEvalType();
		}
		return toReturn;
	}

	public Type getPropertyVariableScopeType(ManchesterOWLSyntaxTree parentExpression,
			ManchesterOWLSyntaxTree propertyExpression) {
		Type toReturn = null;
		if (!OWLType.isPropertyExpression(propertyExpression.getEvalType())) {
			this.reportIncompatibleSymbolType(
					propertyExpression,
					propertyExpression.getEvalType(),
					parentExpression);
		} else {
			toReturn = propertyExpression.getEvalType();
		}
		return toReturn;
	}

	public Type getIndividualVariableScopeType(ManchesterOWLSyntaxTree parentExpression,
			ManchesterOWLSyntaxTree individualExpression) {
		Type toReturn = null;
		if (OWLType.OWL_INDIVIDUAL != individualExpression.getEvalType()) {
			this.reportIncompatibleSymbolType(
					individualExpression,
					individualExpression.getEvalType(),
					parentExpression);
		} else {
			toReturn = individualExpression.getEvalType();
		}
		return toReturn;
	}

	public Type getExpressionGeneratedVariableType(ManchesterOWLSyntaxTree parentExpression,
			ManchesterOWLSyntaxTree variableType, ManchesterOWLSyntaxTree expression) {
		Type toReturn = null;
		org.coode.oppl.VariableType opplVariableVariableType = org.coode.oppl.VariableType.valueOf(variableType.getText());
		if (opplVariableVariableType == null) {
			this.reportIllegalToken(variableType, "Unknown variable type");
		} else if (expression.getOWLObject() == null) {
			this.reportIllegalToken(expression, "Null OWL Object in expression");
		} else if (!opplVariableVariableType.isCompatibleWith(expression.getOWLObject())) {
			this.reportIncompatibleSymbols(parentExpression, variableType, expression);
		} else {
			toReturn = expression.getEvalType();
		}
		return toReturn;
	}

	public org.coode.oppl.VariableType getVaribaleType(ManchesterOWLSyntaxTree variableType) {
		return VariableType.getVariableType(variableType.getText()).getOPPLVariableType();
	}

	// public SingleValueGeneratedValue<String> getStringGeneratedValue(
	// ManchesterOWLSyntaxTree identifier, final ConstraintSystem
	// constraintSystem) {
	// Symbol symbol = this.resolve(identifier);
	// return symbol.accept(new
	// DefaultOPPLSymbolVisitorEx<SingleValueGeneratedValue<String>>() {
	// @Override
	// public SingleValueGeneratedValue<String>
	// visitStringVariableAttributeSymbol(
	// StringVariableAttributeSymbol stringVariableAttributeSymbol) {
	// return stringVariableAttributeSymbol.create(constraintSystem);
	// }
	//
	// @Override
	// protected SingleValueGeneratedValue<String> doDefault(Symbol symbol) {
	// return null;
	// }
	// });
	// }
	public <O extends OWLObject> void defineGroupAttributeReferenceSymbol(
			final OPPLSyntaxTree variableSyntaxTree, ManchesterOWLSyntaxTree indexNode,
			ConstraintSystem constraintSystem) {
		Variable v = constraintSystem.getVariable(variableSyntaxTree.getText());
		if (v != null) {
			try {
				final int index = Integer.parseInt(indexNode.getText());
				VariableAttributeSymbol<?> symbol = v.accept(new PlainVariableVisitorEx<VariableAttributeSymbol<?>>() {
					public VariableAttributeSymbol<?> visit(Variable v) {
						OPPLSymbolTable.this.reportIllegalToken(
								variableSyntaxTree,
								"The variable has to be a regural expression variable");
						return null;
					}

					public VariableAttributeSymbol<?> visit(GeneratedVariable<?> v) {
						OPPLSymbolTable.this.reportIllegalToken(
								variableSyntaxTree,
								"The variable has to be a regural expression variable");
						return null;
					}

					public VariableAttributeSymbol<?> visit(
							RegexpGeneratedVariable<?> regExpGenerated) {
						return StringVariableAttributeSymbol.getGroup(regExpGenerated, index);
					}
				});
				this.storeSymbol(symbol.getName(), symbol);
			} catch (NumberFormatException e) {
				this.getErrorListener().reportThrowable(
						e,
						indexNode.getLine(),
						indexNode.getCharPositionInLine(),
						indexNode.getText().length());
			}
		} else {
			this.reportUnrecognisedSymbol(variableSyntaxTree);
		}
	}

	public void defineRenderingAttributeReferenceSymbol(OPPLSyntaxTree variableSyntaxTree,
			ConstraintSystem constraintSystem) {
		Variable v = constraintSystem.getVariable(variableSyntaxTree.getText());
		if (v != null) {
			VariableAttributeSymbol<?> symbol = StringVariableAttributeSymbol.getRendering(v);
			this.storeSymbol(symbol.getName(), symbol);
		} else {
			this.reportUnrecognisedSymbol(variableSyntaxTree);
		}
	}

	public void defineValuesAttributeReferenceSymbol(OPPLSyntaxTree variableSyntaxTree,
			ConstraintSystem constraintSystem) {
		Variable v = constraintSystem.getVariable(variableSyntaxTree.getText());
		if (v != null) {
			VariableAttributeSymbol<?> symbol = CollectionVariableAttributeSymbol.getValues(v);
			this.storeSymbol(symbol.getName(), symbol);
		} else {
			this.reportUnrecognisedSymbol(variableSyntaxTree);
		}
	}

	// public AbstractCollectionGeneratedValue<OWLClass> getCollection(
	// ManchesterOWLSyntaxTree parentExpression, ManchesterOWLSyntaxTree
	// identifier,
	// final ConstraintSystem constraintSystem) {
	// Symbol resolvedSymbol = this.resolve(identifier);
	// AbstractCollectionGeneratedValue<OWLClass> toReturn = null;
	// if (resolvedSymbol != null) {
	// if (resolvedSymbol.getType() != VariableAttributeType.COLLECTION) {
	// this.reportIncompatibleSymbolType(
	// identifier,
	// resolvedSymbol.getType(),
	// parentExpression);
	// } else {
	// toReturn = resolvedSymbol.accept(new
	// DefaultOPPLSymbolVisitorEx<AbstractCollectionGeneratedValue<OWLClass>>()
	// {
	// @Override
	// protected AbstractCollectionGeneratedValue<OWLClass> doDefault(Symbol
	// symbol) {
	// return null;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public AbstractCollectionGeneratedValue<OWLClass>
	// visitCollectionVariableAttributeSymbol(
	// CollectionVariableAttributeSymbol<?> collectionVariableAttributeSymbol) {
	// return (AbstractCollectionGeneratedValue<OWLClass>)
	// collectionVariableAttributeSymbol.create(constraintSystem);
	// }
	// });
	// }
	// } else {
	// this.reportUnrecognisedSymbol(identifier);
	// }
	// return toReturn;
	// }
	public InequalityConstraint getInequalityConstraint(OPPLSyntaxTree parentExpression,
			OPPLSyntaxTree variableIdentifier, OPPLSyntaxTree expression,
			ConstraintSystem constraintSystem) {
		Symbol variableSymbol = this.resolve(variableIdentifier);
		final Type variableType = variableSymbol.getType();
		InequalityConstraint toReturn = null;
		if (variableType == null) {
			this.reportIncompatibleSymbolType(variableIdentifier, null, parentExpression);
		} else {
			final Type expressionType = expression.getEvalType();
			final boolean compatibleTypes = this.checkCompatibleTypes(variableType, expressionType);
			if (!compatibleTypes) {
				this.reportIncompatibleSymbols(parentExpression, variableIdentifier, expression);
			} else {
				Variable variable = constraintSystem.getVariable(variableIdentifier.getText());
				if (variable == null) {
					this.reportIllegalToken(variableIdentifier, "Unknown variable");
				} else if (expression.getOWLObject() != null) {
					toReturn = new InequalityConstraint(variable, expression.getOWLObject(),
							constraintSystem);
				} else {
					this.reportIllegalToken(expression, "No OWL object");
				}
			}
		}
		return toReturn;
	}

	/**
	 * @param variableType
	 * @param expressionType
	 * @return
	 */
	private boolean checkCompatibleTypes(final Type variableType, final Type expressionType) {
		return variableType.accept(new DefaultTypeVistorEx<Boolean>() {
			@Override
			protected Boolean doDefault(Type type) {
				return false;
			}

			@Override
			public Boolean visitOWLType(OWLType owlType) {
				boolean toReturn = false;
				switch (owlType) {
				case OWL_CLASS:
				case OWL_DATA_ALL_RESTRICTION:
				case OWL_DATA_EXACT_CARDINALITY_RESTRICTION:
				case OWL_DATA_MAX_CARDINALITY_RESTRICTION:
				case OWL_DATA_MIN_CARDINALITY_RESTRICTION:
				case OWL_DATA_SOME_RESTRICTION:
				case OWL_DATA_VALUE_RESTRICTION:
				case OWL_OBJECT_ALL_RESTRICTION:
				case OWL_OBJECT_COMPLEMENT_OF:
				case OWL_OBJECT_EXACT_CARDINALITY_RESTRICTION:
				case OWL_OBJECT_INTERSECTION_OF:
				case OWL_OBJECT_MAX_CARDINALITY_RESTRICTION:
				case OWL_OBJECT_MIN_CARDINALITY_RESTRICTION:
				case OWL_OBJECT_ONE_OF:
				case OWL_OBJECT_SELF_RESTRICTION:
				case OWL_OBJECT_SOME_RESTRICTION:
				case OWL_OBJECT_UNION_OF:
				case OWL_OBJECT_VALUE_RESTRICTION:
					toReturn = OWLType.isClassExpression(expressionType);
					break;
				case OWL_OBJECT_PROPERTY:
				case OWL_OBJECT_INVERSE_PROPERTY:
					toReturn = OWLType.isObjectPropertyExpression(expressionType);
					break;
				case OWL_DATA_PROPERTY:
					toReturn = OWLType.OWL_DATA_PROPERTY == expressionType;
					break;
				case OWL_INDIVIDUAL:
					toReturn = OWLType.OWL_INDIVIDUAL == expressionType;
					break;
				case OWL_CONSTANT:
					toReturn = OWLType.OWL_CONSTANT == expressionType;
					break;
				default:
					toReturn = false;
				}
				return toReturn;
			}
		});
	}

	public InCollectionConstraint<OWLObject> getInSetConstraint(OPPLSyntaxTree parentExpression,
			OPPLSyntaxTree v, ConstraintSystem constraintSystem, OPPLSyntaxTree... elements) {
		InCollectionConstraint<OWLObject> toReturn = null;
		boolean allCompatible = true;
		if (elements.length > 0) {
			List<OPPLSyntaxTree> incompatibles = new ArrayList<OPPLSyntaxTree>(elements.length);
			List<OWLObject> owlObjects = new ArrayList<OWLObject>(elements.length);
			Symbol variableSymbol = this.resolve(v);
			final Type variableType = variableSymbol.getType();
			for (OPPLSyntaxTree element : elements) {
				Symbol resolvedElement = this.resolve(element);
				if (resolvedElement == null) {
					this.reportUnrecognisedSymbol(element);
					allCompatible = false;
				}
			}
			if (allCompatible) {
				for (OPPLSyntaxTree element : elements) {
					Symbol resolvedElement = this.resolve(element);
					final boolean isCompatible = resolvedElement != null
							&& this.checkCompatibleTypes(variableType, resolvedElement.getType())
							&& element.getOWLObject() != null;
					allCompatible = allCompatible && isCompatible;
					if (!isCompatible) {
						incompatibles.add(element);
					} else {
						owlObjects.add(element.getOWLObject());
					}
				}
			}
			if (allCompatible) {
				Variable variable = constraintSystem.getVariable(v.getText());
				if (variable == null) {
					this.reportIllegalToken(v, "Unknown variable");
				} else {
					toReturn = new InCollectionConstraint<OWLObject>(variable, owlObjects,
							constraintSystem);
				}
			} else if (!incompatibles.isEmpty()) {
				incompatibles.add(0, v);
				this.reportIncompatibleSymbols(
						parentExpression,
						incompatibles.toArray(new OPPLSyntaxTree[incompatibles.size()]));
			}
		} else {
			this.reportIllegalToken(parentExpression, "Empty set");
		}
		return toReturn;
	}

	public Variable getVariable(OPPLSyntaxTree identifier, ConstraintSystem constraintSystem) {
		Symbol variableSymbol = this.resolve(identifier);
		Variable toReturn = null;
		if (variableSymbol == null) {
			this.reportIllegalToken(identifier, "Undefined variable ");
		} else {
			toReturn = constraintSystem.getVariable(variableSymbol.getName());
			if (toReturn == null) {
				this.reportIllegalToken(
						identifier,
						"Undefined variable in the script constraint system");
			}
		}
		return toReturn;
	}

	/**
	 * Import all the variables in the input ConstraintSystem into this
	 * OPPLSymbolTable.
	 * 
	 * @param constraintSystem
	 *            The input constraint system. It cannot be {@code null}.
	 * @throws NullPointerException
	 *             if the input is {@code null}.
	 */
	public void importConstraintSystem(ConstraintSystem constraintSystem) {
		Set<Variable> variables = constraintSystem.getVariables();
		for (Variable variable : variables) {
			this.importVariable(variable);
		}
	}

	private void importVariable(Variable variable) {
		VariableType type = VariableType.getVariableType(variable.getType().toString());
		Symbol symbol = type.getSymbol(this.getDataFactory(), variable.getName());
		this.storeSymbol(variable.getName(), symbol);
	}

	public VariableAttribute<String> getStringVariableAttribute(
			final OPPLSyntaxTree variableAttributeSyntaxTree) {
		Symbol symbol = this.retrieveSymbol(variableAttributeSyntaxTree.getText());
		VariableAttribute<String> toReturn = null;
		if (symbol != null) {
			toReturn = symbol.accept(new OPPLSymbolVisitorEx<VariableAttribute<String>>() {
				public VariableAttribute<String> visitSymbol(Symbol symbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							variableAttributeSyntaxTree,
							"Invalid symbol or variable attribute");
					return null;
				}

				public VariableAttribute<String> visitOWLLiteral(OWLLiteralSymbol owlConstantSymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							variableAttributeSyntaxTree,
							"Invalid symbol or variable attribute");
					return null;
				}

				public VariableAttribute<String> visitOWLEntity(OWLEntitySymbol owlEntitySymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							variableAttributeSyntaxTree,
							"Invalid symbol or variable attribute");
					return null;
				}

				public VariableAttribute<String> visitStringVariableAttributeSymbol(
						StringVariableAttributeSymbol stringVariableAttributeSymbol) {
					return stringVariableAttributeSymbol.getVariableAttribute();
				}

				public <P> VariableAttribute<String> visitCollectionVariableAttributeSymbol(
						CollectionVariableAttributeSymbol<P> collectionVariableAttributeSymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							variableAttributeSyntaxTree,
							"Invalid symbol or variable attribute");
					return null;
				}

				public VariableAttribute<String> visitCreateOnDemandIdentifier(
						CreateOnDemandIdentifier createOnDemandIdentifier) {
					OPPLSymbolTable.this.reportIllegalToken(
							variableAttributeSyntaxTree,
							"Invalid symbol or variable attribute");
					return null;
				}
			});
		} else {
			this.reportIllegalToken(
					variableAttributeSyntaxTree,
					"Invalid symbol or variable attribute");
		}
		return toReturn;
	}

	public <O> CollectionVariableAttributeSymbol<O> getCollectionVariableAttributeSymbol(
			final OPPLSyntaxTree attributeSyntaxTree) {
		Symbol symbol = this.retrieveSymbol(attributeSyntaxTree.getText());
		CollectionVariableAttributeSymbol<O> toReturn = null;
		if (symbol != null) {
			toReturn = symbol.accept(new OPPLSymbolVisitorEx<CollectionVariableAttributeSymbol<O>>() {
				public CollectionVariableAttributeSymbol<O> visitSymbol(Symbol symbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							attributeSyntaxTree,
							"Wrong kind of symbol ");
					return null;
				}

				public CollectionVariableAttributeSymbol<O> visitOWLLiteral(
						OWLLiteralSymbol owlConstantSymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							attributeSyntaxTree,
							"Wrong kind of symbol ");
					return null;
				}

				public CollectionVariableAttributeSymbol<O> visitOWLEntity(
						OWLEntitySymbol owlEntitySymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							attributeSyntaxTree,
							"Wrong kind of symbol ");
					return null;
				}

				public CollectionVariableAttributeSymbol<O> visitStringVariableAttributeSymbol(
						StringVariableAttributeSymbol stringVariableAttributeSymbol) {
					OPPLSymbolTable.this.reportIllegalToken(
							attributeSyntaxTree,
							"Wrong kind of symbol ");
					return null;
				}

				public <P> CollectionVariableAttributeSymbol<O> visitCollectionVariableAttributeSymbol(
						CollectionVariableAttributeSymbol<P> collectionVariableAttributeSymbol) {
					return (CollectionVariableAttributeSymbol<O>) collectionVariableAttributeSymbol;
				}

				public CollectionVariableAttributeSymbol<O> visitCreateOnDemandIdentifier(
						CreateOnDemandIdentifier createOnDemandIdentifier) {
					OPPLSymbolTable.this.reportIllegalToken(
							attributeSyntaxTree,
							"Wrong kind of symbol ");
					return null;
				}
			});
		} else {
			this.reportUnrecognisedSymbol(attributeSyntaxTree);
		}
		return toReturn;
	}
}
