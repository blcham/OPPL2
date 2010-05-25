package org.coode.parsers.oppl.patterns.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

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
import org.coode.parsers.ErrorListener;
import org.coode.parsers.ManchesterOWLSyntaxSimplify;
import org.coode.parsers.Symbol;
import org.coode.parsers.factory.SymbolTableFactory;
import org.coode.parsers.oppl.OPPLDefine;
import org.coode.parsers.oppl.OPPLSyntaxTree;
import org.coode.parsers.oppl.patterns.OPPLPatternLexer;
import org.coode.parsers.oppl.patterns.OPPLPatternScriptParser;
import org.coode.parsers.oppl.patterns.OPPLPatternsDefine;
import org.coode.parsers.oppl.patterns.OPPLPatternsSymbolTable;
import org.coode.parsers.oppl.patterns.factory.SimpleSymbolTableFactory;
import org.coode.parsers.test.ComprehensiveAxiomTestCase;
import org.coode.parsers.test.SystemErrorEcho;
import org.coode.patterns.AbstractPatternModelFactory;
import org.coode.patterns.PatternConstraintSystem;
import org.coode.patterns.PatternModelFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 * Test for the AST generation for OPPL
 * 
 * @author Luigi Iannone
 * 
 */
public class OPPLPatternsDefineParserTest extends TestCase {
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
		public Object errorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
			return new CommonErrorNode(input, start, stop, e);
		}
	};
	private static OWLOntologyManager ONTOLOGY_MANAGER = OWLManager.createOWLOntologyManager();
	private final static SymbolTableFactory SYMBOL_TABLE_FACTORY = new SimpleSymbolTableFactory(
			ONTOLOGY_MANAGER);
	private OPPLPatternsSymbolTable symtab;
	private static OWLOntology SYNTAX_ONTOLOGY;
	private final ErrorListener listener = new SystemErrorEcho();
	static {
		try {
			ONTOLOGY_MANAGER.loadOntologyFromPhysicalURI(URI.create("http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl"));
			SYNTAX_ONTOLOGY = ONTOLOGY_MANAGER.loadOntology(ComprehensiveAxiomTestCase.class.getResource(
					"syntaxTest.owl").toURI());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void testFood() {
		String patternString = "?x:CLASS, ?y:CLASS, ?forbiddenContent:CLASS = createUnion(?x.VALUES) BEGIN ADD $thisClass equivalentTo contains only (not ?forbiddenContent) END; A ?x free stuff; RETURN $thisClass";
		OPPLSyntaxTree parsed = this.parse(patternString);
		System.out.println(parsed.toStringTree());
		assertNotNull(parsed);
		Set<Symbol> definedSymbols = this.symtab.getDefinedSymbols();
		assertTrue(
				"Exected 5 actual " + definedSymbols.size() + " " + definedSymbols,
				definedSymbols.size() == 5);
	}

	public void testMenu() {
		String patternString = "?x:CLASS[subClassOf Food] BEGIN ADD $thisClass subClassOf Menu, ADD $thisClass subClassOf contains only (Course and contains only ($Free(?x))) END; A ?x free Menu";
		try {
			OWLOntology referencedPatternOntology = ONTOLOGY_MANAGER.loadOntologyFromPhysicalURI(URI.create("http://oppl2.sourceforge.net/patterns/ontologies/food.owl"));
			OPPLSyntaxTree parsed = this.parse(patternString);
			System.out.println(parsed.toStringTree());
			assertNotNull(parsed);
			Set<Symbol> definedSymbols = this.symtab.getDefinedSymbols();
			assertTrue(
					"Exected 3 actual " + definedSymbols.size() + " " + definedSymbols,
					definedSymbols.size() == 3);
			ONTOLOGY_MANAGER.removeOntology(referencedPatternOntology.getURI());
		} catch (OWLOntologyCreationException e) {
			fail();
			e.printStackTrace();
		}
	}

	public void testPizza() {
		String patternString = "?base:CLASS,?topping:CLASS, ?allToppings:CLASS = createUnion(?topping.VALUES) BEGIN ADD $thisClass subClassOf Pizza, ADD $thisClass subClassOf hasTopping some ?topping,  ADD $thisClass subClassOf hasTopping only ?allToppings, ADD $thisClass subClassOf hasBase some ?base  END; A pizza with ?base base and ?topping toppings";
		OPPLSyntaxTree parsed = this.parse(patternString);
		System.out.println(parsed.toStringTree());
		assertNotNull(parsed);
		Set<Symbol> definedSymbols = this.symtab.getDefinedSymbols();
		assertTrue(
				"Exected 5 actual " + definedSymbols.size() + " " + definedSymbols,
				definedSymbols.size() == 5);
	}

	public void testDOLCEInformationRealization() {
		String patternString = "?informationObject:CLASS, ?informationRealization:CLASS, ?realizationProperty:OBJECTPROPERTY BEGIN ADD ?informationRealization subClassOf InformationRealization, ADD ?informationObject subClassOf InformationObject, ADD ?realizationProperty subPropertyOf realizes, ADD ?informationRealization subClassOf PhysicalObject and ?realizationProperty some ?InformationObject END;Information Realization Pattern: ?informationRealization ?realizationProperty ?informationObject";
		OPPLSyntaxTree parsed = this.parse(patternString);
		System.out.println(parsed.toStringTree());
		assertNotNull(parsed);
		Set<Symbol> definedSymbols = this.symtab.getDefinedSymbols();
		assertTrue(
				"Exected 3 actual " + definedSymbols.size() + " " + definedSymbols,
				definedSymbols.size() == 3);
	}

	public void testDOLCEPersonRoleTimeInterval() {
		String patternString = "?person:CLASS, ?role:CLASS, ?timeInterval:CLASS BEGIN ADD $thisClass subClassOf Situation, ADD $thisClass subClassOf isSettingFor some ?person, ADD $thisClass subClassOf isSettingFor some ?role, ADD $thisClass subClassOf isSettingFor some ?timeInterval END; Situation where ?person play the role ?role during the time interval ?timeInterval";
		OPPLSyntaxTree parsed = this.parse(patternString);
		System.out.println(parsed.toStringTree());
		assertNotNull(parsed);
		Set<Symbol> definedSymbols = this.symtab.getDefinedSymbols();
		assertTrue(
				"Exected 4 actual " + definedSymbols.size() + " " + definedSymbols,
				definedSymbols.size() == 4);
	}

	protected OPPLSyntaxTree parse(String input) {
		ANTLRStringStream antlrStringStream = new ANTLRStringStream(input);
		OPPLPatternLexer lexer = new OPPLPatternLexer(antlrStringStream);
		final TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		OPPLPatternScriptParser parser = new OPPLPatternScriptParser(tokens);
		parser.setTreeAdaptor(adaptor);
		try {
			RuleReturnScope r = parser.pattern();
			CommonTree tree = (CommonTree) r.getTree();
			CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
			nodes.setTokenStream(tokens); // where to find tokens
			nodes.setTreeAdaptor(adaptor);
			nodes.reset();
			// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
			ManchesterOWLSyntaxSimplify simplify = new ManchesterOWLSyntaxSimplify(nodes);
			simplify.setTreeAdaptor(adaptor);
			simplify.downup(tree);
			AbstractPatternModelFactory factory = new PatternModelFactory(SYNTAX_ONTOLOGY,
					ONTOLOGY_MANAGER);
			PatternConstraintSystem constraintSystem = factory.createConstraintSystem();
			OPPLDefine define = new OPPLDefine(nodes, this.symtab, this.listener, constraintSystem);
			define.setTreeAdaptor(adaptor);
			define.downup(tree);
			nodes.reset();
			OPPLPatternsDefine patternsDefine = new OPPLPatternsDefine(nodes, this.symtab,
					this.listener, constraintSystem);
			patternsDefine.setTreeAdaptor(adaptor);
			patternsDefine.downup(tree);
			return (OPPLSyntaxTree) r.getTree();
		} catch (RecognitionException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void setUp() throws Exception {
		this.symtab = (OPPLPatternsSymbolTable) SYMBOL_TABLE_FACTORY.createSymbolTable();
		this.symtab.setErrorListener(this.listener);
	}

	@Override
	protected void tearDown() throws Exception {
		this.symtab.dispose();
	}
}