package org.coode.oppl.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.PatternSyntaxException;

import junit.framework.TestCase;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;
import org.coode.oppl.AbstractConstraint;
import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.ManchesterVariableSyntax;
import org.coode.oppl.OPPLParser;
import org.coode.oppl.ParserFactory;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableVisitor;
import org.coode.oppl.VariableVisitorEx;
import org.coode.oppl.bindingtree.Assignment;
import org.coode.oppl.bindingtree.BindingNode;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.function.SimpleValueComputationParameters;
import org.coode.oppl.function.ValueComputationParameters;
import org.coode.oppl.generated.GeneratedVariable;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.coode.oppl.log.Logging;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.oppl.variabletypes.InputVariable;
import org.coode.oppl.variabletypes.VariableType;
import org.coode.oppl.variabletypes.VariableTypeFactory;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.ManchesterOWLSyntaxTree;
import org.coode.parsers.common.SystemErrorEcho;
import org.coode.parsers.oppl.OPPLSymbolTable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyDocumentAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLRuntimeException;

public class OPPLPartsTestCase extends TestCase {
	// ontology manager
	private final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	private final ErrorListener errorListener = new SystemErrorEcho();
	private final Map<String, OWLOntology> loadedOntologies = new HashMap<String, OWLOntology>();
	private final RuntimeExceptionHandler handler = new RuntimeExceptionHandler() {
		public void handlePatternSyntaxExcpetion(PatternSyntaxException e) {
			OPPLPartsTestCase.this.errorListener.reportThrowable(e, 0, 0, 0);
		}

		public void handleOWLRuntimeException(OWLRuntimeException e) {
			OPPLPartsTestCase.this.errorListener.reportThrowable(e, 0, 0, 0);
		}

		public void handleException(RuntimeException e) {
			OPPLPartsTestCase.this.errorListener.reportThrowable(e, 0, 0, 0);
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected OPPLParser getParser(String ontology) {
		OPPLParser parser = new ParserFactory(this.ontologyManager, this.getOntology(ontology),
				null).build(this.errorListener);
		return parser;
	}

	private OWLOntology getOntology(String name) {
		OWLOntology o = this.loadedOntologies.get(name);
		if (o == null) {
			try {
				URL resource = this.getClass().getResource(name);
				if (resource != null) {
					IRI iri = IRI.create(resource.toURI());
					o = this.ontologyManager.contains(iri) ? this.ontologyManager.getOntology(iri)
							: this.ontologyManager.loadOntology(iri);
				} else {
					fail("Could not load the ontology " + name);
				}
			} catch (OWLOntologyDocumentAlreadyExistsException e) {
				o = this.ontologyManager.getOntology(e.getOntologyDocumentIRI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
			this.loadedOntologies.put(name, o);
		}
		return o;
	}

	public void testParseInSetConstraint() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		AbstractConstraint constraint = parser.parseConstraint(
				"?x!= Thing",
				symbolTable,
				constraintSystem);
		assertNotNull("The constraint cannot be null", constraint);
		constraint = parser.parseConstraint("?x IN {?y, Thing}", symbolTable, constraintSystem);
		assertNotNull("The constraint cannot be null", constraint);
		Logging.getParseTestLogging().log(Level.INFO, constraint.toString());
		constraint = parser.parseConstraint("?Thing and ?y", symbolTable, constraintSystem);
		assertNull("The constraint should be null", constraint);
	}

	public void testParseInequalityConstraint() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		AbstractConstraint constraint = parser.parseConstraint(
				"?x!= Thing",
				symbolTable,
				constraintSystem);
		assertNotNull("The constraint cannot be null", constraint);
		constraint = parser.parseConstraint("?x!= ?y", symbolTable, constraintSystem);
		Logging.getParseTestLogging().log(Level.INFO, constraint.toString());
		assertNotNull("The constraint cannot be null", constraint);
		constraint = parser.parseConstraint("?x!= Thing and ?y", symbolTable, constraintSystem);
		Logging.getParseTestLogging().log(Level.INFO, constraint.toString());
		assertNotNull("The constraint cannot be null", constraint);
	}

	public void testParseOPPLFunction() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		Variable<?> tempVariable = this.createTempVariable(
				"?z",
				VariableTypeFactory.getCLASSVariableType());
		Variable<?> opplFunction = parser.parseOPPLFunction(
				"CreateIntersection(?x.VALUES)",
				tempVariable,
				symbolTable,
				constraintSystem);
		assertNotNull("The oppl function cannot be null", opplFunction);
		Logging.getParseTestLogging().log(Level.INFO, opplFunction.render(constraintSystem));
	}

	public void testParseOPPLFunctionAggregatingLooseObjectAndVariableValues() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		Variable<?> tempVariable = this.createTempVariable(
				"?z",
				VariableTypeFactory.getCLASSVariableType());
		Variable<?> opplFunction = parser.parseOPPLFunction(
				"CreateIntersection(?x.VALUES, Thing)",
				tempVariable,
				symbolTable,
				constraintSystem);
		assertNotNull("The oppl function cannot be null", opplFunction);
		BindingNode bindingNode = BindingNode.createNewEmptyBindingNode();
		bindingNode.addAssignment(new Assignment(constraintSystem.getVariable("?x"),
				constraintSystem.getOntologyManager().getOWLDataFactory().getOWLClass(
						IRI.create("blah#Luigi"))));
		BindingNode anotherBindingNode = BindingNode.createNewEmptyBindingNode();
		anotherBindingNode.addAssignment(new Assignment(constraintSystem.getVariable("?x"),
				constraintSystem.getOntologyManager().getOWLDataFactory().getOWLClass(
						IRI.create("blah#Monica"))));
		constraintSystem.setLeaves(new HashSet<BindingNode>(Arrays.asList(
				bindingNode,
				anotherBindingNode)));
		final ValueComputationParameters parameters = new SimpleValueComputationParameters(
				constraintSystem, BindingNode.createNewEmptyBindingNode(), this.handler);
		Logging.getParseTestLogging().log(Level.INFO, opplFunction.render(constraintSystem));
		opplFunction.accept(new VariableVisitor() {
			public <P extends OWLObject> void visit(RegexpGeneratedVariable<P> regExpGenerated) {
				fail("Wrong kind of variable expected generated variable found regexp ");
			}

			public <P extends OWLObject> void visit(GeneratedVariable<P> v) {
				P value = v.getOPPLFunction().compute(parameters);
				assertNotNull(value);
				ManchesterSyntaxRenderer renderer = parameters.getConstraintSystem().getOPPLFactory().getManchesterSyntaxRenderer(
						parameters.getConstraintSystem());
				value.accept(renderer);
				System.out.println(renderer.toString());
			}

			public <P extends OWLObject> void visit(InputVariable<P> v) {
				fail("Wrong kind of variable expected generated variable found input ");
			}
		});
	}

	public void testParseAxiom() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		OWLAxiom axiom = parser.parseAxiom(
				"?x subClassOf !hasP some ?y",
				symbolTable,
				constraintSystem);
		assertNotNull("The axiom cannot be null", axiom);
		Logging.getParseTestLogging().log(Level.INFO, axiom.toString());
	}

	public void testParseNAFConstraint() {
		OPPLParser parser = this.getParser("NAF.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		AbstractConstraint constraint = parser.parseConstraint(
				"FAIL ?x subClassOf hasP some ?y",
				symbolTable,
				constraintSystem);
		assertNotNull("The constraint cannot be null", constraint);
		Logging.getParseTestLogging().log(Level.INFO, constraint.toString());
	}

	public void testParseRegExpConstraint() {
		OPPLParser parser = this.getParser("test.owl");
		ConstraintSystem constraintSystem = parser.getOPPLAbstractFactory().createConstraintSystem();
		OPPLSymbolTable symbolTable = parser.getSymbolTableFactory().createSymbolTable();
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?x")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		symbolTable.defineVariable(
				this.createImaginaryTreeNode(this.createImaginaryToken("?y")),
				this.createImaginaryTreeNode(this.createImaginaryToken("CLASS")),
				constraintSystem);
		AbstractConstraint constraint = parser.parseConstraint(
				"?x Match(\"Island\")",
				symbolTable,
				constraintSystem);
		assertNotNull("The constraint cannot be null", constraint);
		Logging.getParseTestLogging().log(Level.INFO, constraint.toString());
	}

	private Token createImaginaryToken(final String text) {
		return new Token() {
			public void setType(int ttype) {
			}

			public void setTokenIndex(int index) {
			}

			public void setText(String text) {
			}

			public void setLine(int line) {
			}

			public void setInputStream(CharStream input) {
			}

			public void setCharPositionInLine(int pos) {
			}

			public void setChannel(int channel) {
			}

			public int getType() {
				return 0;
			}

			public int getTokenIndex() {
				return 0;
			}

			public String getText() {
				return text;
			}

			public int getLine() {
				return 0;
			}

			public CharStream getInputStream() {
				return null;
			}

			public int getCharPositionInLine() {
				return 0;
			}

			public int getChannel() {
				return 0;
			}
		};
	}

	private ManchesterOWLSyntaxTree createImaginaryTreeNode(Token token) {
		return new ManchesterOWLSyntaxTree(token);
	}

	private <O extends OWLObject> Variable<O> createTempVariable(final String name,
			final VariableType<O> type) {
		return new Variable<O>() {
			public IRI getIRI() {
				return IRI.create(URI.create(ManchesterVariableSyntax.NAMESPACE + this.getName()));
			}

			public VariableType<O> getType() {
				return type;
			}

			public String getName() {
				return name;
			}

			public void accept(VariableVisitor visitor) {
			}

			public <T> T accept(VariableVisitorEx<T> visitor) {
				return null;
			}

			public String render(ConstraintSystem constraintSystem) {
				return String.format("%s:%s", this.getName(), this.getType());
			}
		};
	}
}