package org.coode.parsers.oppl.patterns.test;

import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableVisitorEx;
import org.coode.oppl.generated.GeneratedVariable;
import org.coode.oppl.generated.RegexpGeneratedVariable;
import org.coode.oppl.variabletypes.InputVariable;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.ManchesterOWLSyntaxSimplify;
import org.coode.parsers.ManchesterOWLSyntaxTypes;
import org.coode.parsers.common.SilentListener;
import org.coode.parsers.common.SystemErrorEcho;
import org.coode.parsers.factory.SymbolTableFactory;
import org.coode.parsers.oppl.DefaultTypeEnforcer;
import org.coode.parsers.oppl.OPPLDefine;
import org.coode.parsers.oppl.OPPLSyntaxTree;
import org.coode.parsers.oppl.OPPLTypeEnforcement;
import org.coode.parsers.oppl.OPPLTypes;
import org.coode.parsers.oppl.patterns.OPPLPatternLexer;
import org.coode.parsers.oppl.patterns.OPPLPatternScriptParser;
import org.coode.parsers.oppl.patterns.OPPLPatternsDefine;
import org.coode.parsers.oppl.patterns.OPPLPatternsReferenceDefine;
import org.coode.parsers.oppl.patterns.OPPLPatternsSymbolTable;
import org.coode.parsers.oppl.patterns.OPPLPatternsTypes;
import org.coode.parsers.oppl.patterns.factory.SimpleSymbolTableFactory;
import org.coode.parsers.test.ComprehensiveAxiomTestCase;
import org.coode.patterns.AbstractPatternModelFactory;
import org.coode.patterns.OPPLPatternParser.PatternReferenceResolver;
import org.coode.patterns.PatternConstraintSystem;
import org.coode.patterns.PatternModel;
import org.coode.patterns.PatternModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

/**
 * Test for the AST generation for OPPL
 * 
 * @author Luigi Iannone
 * 
 */
public class OPPLPatternsTypesParserTest extends TestCase {
	private static TreeAdaptor adaptor = new CommonTreeAdaptor() {
		@Override
		public Object create(Token token) {
			return new OPPLSyntaxTree(token);
		}

		@Override
		public Object dupNode(Object t) {
			if (t == null) {
				return null;
			}
			return this.create(((OPPLSyntaxTree) t).token);
		}

		@Override
		public Object errorNode(TokenStream input, Token start, Token stop,
				RecognitionException e) {
			return new CommonErrorNode(input, start, stop, e);
		}
	};
	private static OWLOntologyManager ONTOLOGY_MANAGER = OWLManager
			.createOWLOntologyManager();
	private final static SymbolTableFactory<OPPLPatternsSymbolTable> SYMBOL_TABLE_FACTORY = new SimpleSymbolTableFactory(
			ONTOLOGY_MANAGER);
	private OPPLPatternsSymbolTable symtab;
	private static OWLOntology SYNTAX_ONTOLOGY;
	private final ErrorListener listener = new SystemErrorEcho();
	private AbstractPatternModelFactory patternModelFactory;

	/**
	 * @return
	 */
	public static PatternReferenceResolver getSimplePatternReferenceResolver() {
		return new PatternReferenceResolver() {
			public void resolvePattern(OPPLSyntaxTree reference,
					String patternName,
					PatternConstraintSystem constraintSystem,
					OPPLPatternsSymbolTable symbolTable, List<Object>... args) {
				symbolTable.resolvePattern(reference, patternName,
						constraintSystem, args);
			}
		};
	}

	static {
		try {
			ONTOLOGY_MANAGER
					.loadOntology(IRI
							.create("http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl"));
			SYNTAX_ONTOLOGY = ONTOLOGY_MANAGER.loadOntology(IRI
					.create(ComprehensiveAxiomTestCase.class.getResource(
							"syntaxTest.owl").toURI()));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void testFood() {
		OWLOntology referencedPatternOntology;
		try {
			referencedPatternOntology = ONTOLOGY_MANAGER
					.loadOntology(IRI
							.create("http://oppl2.sourceforge.net/patterns/ontologies/food.owl"));
			String patternString = "?x:CLASS, ?y:CLASS, ?forbiddenContent:CLASS = createUnion(?x.VALUES) BEGIN ADD $thisClass equivalentTo contains only (not ?forbiddenContent) END; A ?x free stuff; RETURN $thisClass";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			PatternModel patternModel = (PatternModel) parsed.getOPPLContent();
			Variable<?> variable = patternModel.getConstraintSystem()
					.getVariable("?forbiddenContent");
			assertNotNull(variable);
			boolean isGenerated = variable
					.accept(new VariableVisitorEx<Boolean>() {
						public <P extends OWLObject> Boolean visit(
								GeneratedVariable<P> v) {
							return true;
						}

						public <P extends OWLObject> Boolean visit(
								InputVariable<P> v) {
							return false;
						}

						public <P extends OWLObject> Boolean visit(
								RegexpGeneratedVariable<P> regExpGenerated) {
							return false;
						}
					});
			assertTrue(isGenerated);
			ONTOLOGY_MANAGER.removeOntology(referencedPatternOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testMenu() {
		String patternString = "?x:CLASS[subClassOf Food] BEGIN ADD $thisClass subClassOf Menu, ADD $thisClass subClassOf contains only (Course and contains only ($Free(?x))) END; A ?x free Menu";
		try {
			OWLOntology referencedPatternOntology = ONTOLOGY_MANAGER
					.loadOntology(IRI
							.create("http://oppl2.sourceforge.net/patterns/ontologies/food.owl"));
			FaCTPlusPlusReasonerFactory factory = new FaCTPlusPlusReasonerFactory();
			final OWLReasoner reasoner = factory
					.createReasoner(referencedPatternOntology);
			this.patternModelFactory = new PatternModelFactory(
					referencedPatternOntology, ONTOLOGY_MANAGER, reasoner);
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			System.out.println(((PatternModel) parsed.getOPPLContent())
					.render());
			ONTOLOGY_MANAGER.removeOntology(referencedPatternOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testPizza() {
		try {
			OWLOntology pizzaOntology = ONTOLOGY_MANAGER
					.loadOntology(IRI
							.create("http://oppl2.sourceforge.net/patterns/ontologies/food.owl"));
			String patternString = "?base:CLASS,?topping:CLASS, ?allToppings:CLASS = createUnion(?topping.VALUES) BEGIN ADD $thisClass subClassOf Pizza, ADD $thisClass subClassOf hasTopping some ?topping,  ADD $thisClass subClassOf hasTopping only ?allToppings, ADD $thisClass subClassOf hasBase some ?base  END; A pizza with ?base base and ?topping toppings";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			ONTOLOGY_MANAGER.removeOntology(pizzaOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testNoVariablePattern() {
		try {
			OWLOntology pizzaOntology = ONTOLOGY_MANAGER
					.loadOntology(IRI
							.create("http://oppl2.sourceforge.net/patterns/ontologies/food.owl"));
			String patternString = " BEGIN ADD Menu subClassOf Menu END; A variable free pattern";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			ONTOLOGY_MANAGER.removeOntology(pizzaOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testComplexExpressionConjuntionGeneratedVariablePattern() {
		try {
			OWLOntology ontology = ONTOLOGY_MANAGER.createOntology();
			ONTOLOGY_MANAGER
					.addAxiom(
							ontology,
							ONTOLOGY_MANAGER
									.getOWLDataFactory()
									.getOWLDeclarationAxiom(
											ONTOLOGY_MANAGER
													.getOWLDataFactory()
													.getOWLObjectProperty(
															IRI.create(" http://www.co-ode.org/ontologies/ont.owl#part_of"))));
			String patternString = "?cell:CLASS, ?anatomyPart:CLASS, ?partOfRestriction:CLASS = part_of some ?anatomyPart, ?anatomyIntersection:CLASS = createIntersection(?partOfRestriction.VALUES) BEGIN ADD ?cell equivalentTo ?anatomyIntersection END;";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			ONTOLOGY_MANAGER.removeOntology(ontology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testDOLCEInformationRealization() {
		try {
			OWLOntology dolceOntology = ONTOLOGY_MANAGER.loadOntology(IRI
					.create("http://www.loa-cnr.it/ontologies/DUL.owl"));
			String patternString = "?informationObject:CLASS, ?informationRealization:CLASS, ?realizationProperty:OBJECTPROPERTY BEGIN ADD ?informationRealization subClassOf InformationRealization, ADD ?informationObject subClassOf InformationObject, ADD ?realizationProperty subPropertyOf realizes, ADD ?informationRealization subClassOf PhysicalObject and ?realizationProperty some ?informationObject END; Information Realization Pattern: ?informationRealization ?realizationProperty ?informationObject";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			ONTOLOGY_MANAGER.removeOntology(dolceOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testDOLCEPersonRoleTimeInterval() {
		try {
			OWLOntology dolceOntology = ONTOLOGY_MANAGER.loadOntology(IRI
					.create("http://www.loa-cnr.it/ontologies/DUL.owl"));
			String patternString = "?person:CLASS, ?role:CLASS, ?timeInterval:CLASS BEGIN ADD $thisClass subClassOf Situation, ADD $thisClass subClassOf isSettingFor some ?person, ADD $thisClass subClassOf isSettingFor some ?role, ADD $thisClass subClassOf isSettingFor some ?timeInterval END; Situation where ?person play the role ?role during the time interval ?timeInterval";
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			assertNotNull(parsed.getOPPLContent());
			ONTOLOGY_MANAGER.removeOntology(dolceOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * @return the factory
	 */
	public AbstractPatternModelFactory getOPPLPatternFactory() {
		return this.patternModelFactory;
	}

	protected OPPLSyntaxTree parse(String input) {
		OPPLPatternsSymbolTable symtab = SYMBOL_TABLE_FACTORY
				.createSymbolTable();
		symtab.setErrorListener(this.getListener());
		ANTLRStringStream antlrStringStream = new ANTLRStringStream(input);
		OPPLPatternLexer lexer = new OPPLPatternLexer(antlrStringStream);
		PatternConstraintSystem constraintSystem = this.getOPPLPatternFactory()
				.createConstraintSystem();
		final TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		OPPLPatternScriptParser parser = new OPPLPatternScriptParser(tokens,
				this.getListener());
		parser.setTreeAdaptor(adaptor);
		try {
			RuleReturnScope r = parser.pattern();
			CommonTree tree = (CommonTree) r.getTree();
			if (tree != null) {
				CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
				nodes.setTokenStream(tokens); // where to find tokens
				nodes.setTreeAdaptor(adaptor);
				nodes.reset();
				// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
				ManchesterOWLSyntaxSimplify simplify = new ManchesterOWLSyntaxSimplify(
						nodes);
				simplify.setTreeAdaptor(adaptor);
				simplify.downup(tree);
				nodes.reset();
				OPPLDefine define = new OPPLDefine(nodes, symtab,
						this.getListener(), constraintSystem);
				define.setTreeAdaptor(adaptor);
				define.downup(tree);
				nodes.reset();
				OPPLPatternsDefine patternsDefine = new OPPLPatternsDefine(
						nodes, symtab, this.getListener(),
						getSimplePatternReferenceResolver(), constraintSystem);
				patternsDefine.setTreeAdaptor(adaptor);
				patternsDefine.downup(tree);
				nodes.reset();
				ErrorListener silentErrorListener = new SilentListener();
				symtab.setErrorListener(silentErrorListener);
				ManchesterOWLSyntaxTypes mOWLTypes = new ManchesterOWLSyntaxTypes(
						nodes, symtab, silentErrorListener);
				mOWLTypes.downup(tree);
				nodes.reset();
				OPPLTypeEnforcement typeEnforcement = new OPPLTypeEnforcement(
						nodes, symtab, new DefaultTypeEnforcer(symtab, this
								.getOPPLPatternFactory().getOPPLFactory()
								.getOWLEntityFactory(), this.getListener()),
						this.getListener());
				typeEnforcement.downup(tree);
				nodes.reset();
				mOWLTypes.downup(tree);
				nodes.reset();
				OPPLTypes opplTypes = new OPPLTypes(nodes, symtab,
						silentErrorListener, constraintSystem, this
								.getOPPLPatternFactory().getOPPLFactory());
				opplTypes.downup(tree);
				nodes.reset();
				OPPLPatternsReferenceDefine patternReferenceDefine = new OPPLPatternsReferenceDefine(
						nodes, symtab, this.getListener(),
						getSimplePatternReferenceResolver(), constraintSystem);
				patternReferenceDefine.setTreeAdaptor(adaptor);
				patternReferenceDefine.downup(tree);
				nodes.reset();
				symtab.setErrorListener(this.getListener());
				mOWLTypes = new ManchesterOWLSyntaxTypes(nodes, symtab,
						this.getListener());
				mOWLTypes.downup(tree);
				nodes.reset();
				opplTypes.downup(tree);
				nodes.reset();
				OPPLPatternsTypes opplPatternsTypes = new OPPLPatternsTypes(
						nodes, symtab, this.getListener(), constraintSystem,
						this.getOPPLPatternFactory());
				opplPatternsTypes.downup(tree);
			}
			return (OPPLSyntaxTree) tree;
		} catch (RecognitionException e) {
			this.listener.recognitionException(e);
			return null;
		}
	}

	@Override
	protected void setUp() throws Exception {
		this.symtab = SYMBOL_TABLE_FACTORY.createSymbolTable();
		this.symtab.setErrorListener(this.listener);
		this.patternModelFactory = new PatternModelFactory(SYNTAX_ONTOLOGY,
				ONTOLOGY_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		this.symtab.dispose();
	}

	/**
	 * @return the listener
	 */
	public ErrorListener getListener() {
		return this.listener;
	}
}