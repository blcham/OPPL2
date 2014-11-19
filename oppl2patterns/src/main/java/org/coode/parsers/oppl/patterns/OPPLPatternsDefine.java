// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g 2011-01-10 16:37:34
package org.coode.parsers.oppl.patterns;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteEmptyStreamException;
import org.antlr.runtime.tree.RewriteRuleNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeRewriter;
import org.antlr.runtime.tree.TreeRuleReturnScope;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.oppl.OPPLSyntaxTree;
import org.coode.patterns.OPPLPatternParser.PatternReferenceResolver;
import org.coode.patterns.PatternConstraintSystem;

@SuppressWarnings("javadoc")
public class OPPLPatternsDefine extends TreeRewriter {

    public static final String[] tokenNames = new String[] { "<invalid>",
            "<EOR>", "<DOWN>", "<UP>", "COMPOSITION", "OPEN_PARENTHESYS",
            "OPEN_CURLY_BRACES", "CLOSED_CURLY_BRACES", "CLOSED_PARENTHESYS",
            "WHITESPACE", "AND", "OR", "NOT", "SOME", "ONLY", "MIN", "MAX",
            "EXACTLY", "VALUE", "INVERSE", "SUBCLASS_OF", "SUB_PROPERTY_OF",
            "EQUIVALENT_TO", "SAME_AS", "DIFFERENT_FROM", "INVERSE_OF",
            "DISJOINT_WITH", "DOMAIN", "RANGE", "FUNCTIONAL", "SYMMETRIC",
            "ANTI_SYMMETRIC", "REFLEXIVE", "IRREFLEXIVE", "TRANSITIVE",
            "INVERSE_FUNCTIONAL", "POW", "COMMA", "INSTANCE_OF", "TYPES",
            "DBLQUOTE", "DIGIT", "INTEGER", "LETTER", "IDENTIFIER",
            "ENTITY_REFERENCE", "QUESTION_MARK", "Tokens", "SUB_CLASS_AXIOM",
            "EQUIVALENT_TO_AXIOM", "DISJOINT_WITH_AXIOM", "SUB_PROPERTY_AXIOM",
            "SAME_AS_AXIOM", "DIFFERENT_FROM_AXIOM", "UNARY_AXIOM",
            "DISJUNCTION", "CONJUNCTION", "PROPERTY_CHAIN",
            "NEGATED_EXPRESSION", "NEGATED_ASSERTION", "INVERSE_PROPERTY",
            "SOME_RESTRICTION", "ALL_RESTRICTION", "VALUE_RESTRICTION",
            "CARDINALITY_RESTRICTION", "ONE_OF", "TYPE_ASSERTION",
            "ROLE_ASSERTION", "INVERSE_OBJECT_PROPERTY_EXPRESSION",
            "EXPRESSION", "CONSTANT", "WHERE", "NOT_EQUAL", "EQUAL", "IN",
            "SELECT", "ASSERTED", "COLON", "DOT", "PLUS", "CREATE",
            "CREATE_INTERSECTION", "CREATE_DISJUNCTION", "BEGIN", "END",
            "OPEN_SQUARE_BRACKET", "CLOSED_SQUARE_BRACKET", "SUPER_CLASS_OF",
            "SUPER_PROPERTY_OF", "VARIABLE_TYPE", "ADD", "REMOVE",
            "ASSERTED_CLAUSE", "PLAIN_CLAUSE", "INEQUALITY_CONSTRAINT",
            "IN_SET_CONSTRAINT", "INPUT_VARIABLE_DEFINITION",
            "GENERATED_VARIABLE_DEFINITION", "CREATE_OPPL_FUNCTION",
            "VARIABLE_ATTRIBUTE", "OPPL_FUNCTION", "ACTIONS",
            "VARIABLE_DEFINITIONS", "QUERY", "VARIABLE_SCOPE",
            "SUBPROPERTY_OF", "VARIABLE_IDENTIFIER", "OPPL_STATEMENT",
            "HAS_KEY", "IRI", "ANNOTATION_ASSERTION", "IRI_ATTRIBUTE_NAME",
            "ARGUMENT", "AT", "ESCLAMATION_MARK", "CREATE_IDENTIFIER",
            "PLAIN_IDENTIFIER", "MATCH", "ATTRIBUTE_SELECTOR", "VALUES",
            "RENDERING", "GROUPS", "STRING_OPERATION", "DOLLAR", "RETURN",
            "THIS_CLASS", "ARGUMENTS", "OPPL_PATTERN", "PATTERN_REFERENCE",
            "SEMICOLON", "VARIABLE_NAME", "REGEXP_CONSTRAINT", "FAIL",
            "NAF_CONSTRAINT" };
    public static final int COMMA = 37;
    public static final int ASSERTED = 76;
    public static final int VARIABLE_DEFINITIONS = 102;
    public static final int REGEXP_CONSTRAINT = 465;
    public static final int END = 84;
    public static final int HYPHEN = 465;
    public static final int DIFFERENT_FROM = 24;
    public static final int TYPE_ASSERTION = 66;
    public static final int SAME_AS_AXIOM = 52;
    public static final int TYPES = 39;
    public static final int ROLE_ASSERTION = 67;
    public static final int CREATE_OPPL_FUNCTION = 98;
    public static final int ESCLAMATION_MARK = 149;
    public static final int VARIABLE_IDENTIFIER = 106;
    public static final int ASSERTED_CLAUSE = 92;
    public static final int DOT = 78;
    public static final int ALL_RESTRICTION = 62;
    public static final int QUESTION_MARK = 46;
    public static final int PLAIN_IDENTIFIER = 152;
    public static final int AND = 10;
    public static final int EXPRESSION = 69;
    public static final int CONSTANT = 70;
    public static final int VALUE_RESTRICTION = 63;
    public static final int ONE_OF = 65;
    public static final int SUBPROPERTY_OF = 105;
    public static final int THIS_CLASS = 416;
    public static final int SELECT = 75;
    public static final int CARDINALITY_RESTRICTION = 64;
    public static final int SAME_AS = 23;
    public static final int EXACTLY = 17;
    public static final int PLUS = 79;
    public static final int TRANSITIVE = 34;
    public static final int IN_SET_CONSTRAINT = 95;
    public static final int SUBCLASS_OF = 20;
    public static final int ENTITY_REFERENCE = 45;
    public static final int CONJUNCTION = 56;
    public static final int INVERSE_OF = 25;
    public static final int AT = 114;
    public static final int RANGE = 28;
    public static final int ARGUMENTS = 417;
    public static final int CLOSED_PARENTHESYS = 8;
    public static final int PROPERTY_CHAIN = 57;
    public static final int CREATE_INTERSECTION = 81;
    public static final int EQUIVALENT_TO_AXIOM = 49;
    public static final int OPEN_SQUARE_BRACKET = 85;
    public static final int NAF_CONSTRAINT = 467;
    public static final int SYMMETRIC = 30;
    public static final int DOLLAR = 400;
    public static final int DISJOINT_WITH = 26;
    public static final int VARIABLE_TYPE = 89;
    public static final int DISJUNCTION = 55;
    public static final int GROUPS = 356;
    public static final int NEGATED_EXPRESSION = 58;
    public static final int EQUAL = 73;
    public static final int SEMICOLON = 422;
    public static final int PATTERN_REFERENCE = 421;
    public static final int DIFFERENT_FROM_AXIOM = 53;
    public static final int ACTIONS = 101;
    public static final int EQUIVALENT_TO = 22;
    public static final int DOMAIN = 27;
    public static final int SUB_PROPERTY_OF = 21;
    public static final int INVERSE_OBJECT_PROPERTY_EXPRESSION = 68;
    public static final int INVERSE_PROPERTY = 60;
    public static final int COLON = 77;
    public static final int GENERATED_VARIABLE_DEFINITION = 97;
    public static final int VARIABLE_ATTRIBUTE = 99;
    public static final int SUB_CLASS_AXIOM = 48;
    public static final int SUB_PROPERTY_AXIOM = 51;
    public static final int IDENTIFIER = 44;
    public static final int UNARY_AXIOM = 54;
    public static final int ADD = 90;
    public static final int WHERE = 71;
    public static final int CREATE = 80;
    public static final int VARIABLE_SCOPE = 104;
    public static final int OPEN_CURLY_BRACES = 6;
    public static final int CLOSED_SQUARE_BRACKET = 86;
    public static final int INSTANCE_OF = 38;
    public static final int VALUES = 354;
    public static final int QUERY = 103;
    public static final int SOME_RESTRICTION = 61;
    public static final int IRI = 110;
    public static final int VALUE = 18;
    public static final int RENDERING = 355;
    public static final int INVERSE_FUNCTIONAL = 35;
    public static final int ATTRIBUTE_SELECTOR = 283;
    public static final int PLAIN_CLAUSE = 93;
    public static final int OR = 11;
    public static final int INTEGER = 42;
    public static final int INVERSE = 19;
    public static final int HAS_KEY = 109;
    public static final int DISJOINT_WITH_AXIOM = 50;
    public static final int SUPER_CLASS_OF = 87;
    public static final int OPPL_FUNCTION = 100;
    public static final int DIGIT = 41;
    public static final int COMPOSITION = 4;
    public static final int ANNOTATION_ASSERTION = 111;
    public static final int OPPL_STATEMENT = 107;
    public static final int FUNCTIONAL = 29;
    public static final int NOT_EQUAL = 72;
    public static final int LETTER = 43;
    public static final int MAX = 16;
    public static final int FAIL = 466;
    public static final int NEGATED_ASSERTION = 59;
    public static final int INPUT_VARIABLE_DEFINITION = 96;
    public static final int ONLY = 14;
    public static final int CREATE_DISJUNCTION = 82;
    public static final int REMOVE = 91;
    public static final int DBLQUOTE = 40;
    public static final int MIN = 15;
    public static final int POW = 36;
    public static final int MATCH = 176;
    public static final int BEGIN = 83;
    public static final int ARGUMENT = 113;
    public static final int OPPL_PATTERN = 419;
    public static final int WHITESPACE = 9;
    public static final int IN = 74;
    public static final int SUPER_PROPERTY_OF = 88;
    public static final int INEQUALITY_CONSTRAINT = 94;
    public static final int SOME = 13;
    public static final int RETURN = 415;
    public static final int EOF = -1;
    public static final int CREATE_IDENTIFIER = 151;
    public static final int ANTI_SYMMETRIC = 31;
    public static final int Tokens = 47;
    public static final int CLOSED_CURLY_BRACES = 7;
    public static final int IRI_ATTRIBUTE_NAME = 112;
    public static final int REFLEXIVE = 32;
    public static final int NOT = 12;
    public static final int STRING_OPERATION = 394;
    public static final int OPEN_PARENTHESYS = 5;
    public static final int VARIABLE_NAME = 464;
    public static final int IRREFLEXIVE = 33;

    // delegates
    // delegators
    public OPPLPatternsDefine(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public OPPLPatternsDefine(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    @Override
    public String[] getTokenNames() {
        return OPPLPatternsDefine.tokenNames;
    }

    @Override
    public String getGrammarFileName() {
        return "/Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g";
    }

    private OPPLPatternsSymbolTable symtab;
    private ErrorListener errorListener;
    private PatternConstraintSystem constraintSystem;
    private PatternReferenceResolver patternReferenceResolver;

    public OPPLPatternsDefine(TreeNodeStream input,
            OPPLPatternsSymbolTable symtab, ErrorListener errorListener,
            PatternReferenceResolver patternReferenceResolver,
            PatternConstraintSystem constraintSystem) {
        this(input);
        this.symtab = checkNotNull(symtab, "symtab");
        this.errorListener = checkNotNull(errorListener, "errorListener");
        this.constraintSystem = checkNotNull(constraintSystem,
                "constraintSystem");
        this.patternReferenceResolver = checkNotNull(patternReferenceResolver,
                "patternReferenceResolver");
    }

    public PatternReferenceResolver getPatternReferenceResolver() {
        return patternReferenceResolver;
    }

    public PatternConstraintSystem getConstraintSystem() {
        return constraintSystem;
    }

    public ErrorListener getErrorListener() {
        return errorListener;
    }

    public OPPLPatternsSymbolTable getSymbolTable() {
        return symtab;
    }

    @Override
    public void displayRecognitionError(String[] t, RecognitionException e) {
        getErrorListener().recognitionException(e, t);
    }

    protected void mismatch(IntStream in, int ttype,
            @SuppressWarnings("unused") BitSet follow)
            throws RecognitionException {
        throw new MismatchedTokenException(ttype, in);
    }

    @Override
    public Object recoverFromMismatchedSet(IntStream in,
            RecognitionException e, BitSet follow) throws RecognitionException {
        throw e;
    }

    public static class bottomup_return extends TreeRuleReturnScope {

        OPPLSyntaxTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "bottomup"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:90:1:
    // bottomup : thisClass ;
    @Override
    public final OPPLPatternsDefine.bottomup_return bottomup() {
        OPPLPatternsDefine.bottomup_return retval = new OPPLPatternsDefine.bottomup_return();
        retval.start = input.LT(1);
        OPPLSyntaxTree _first_0 = null;
        OPPLPatternsDefine.thisClass_return thisClass1 = null;
        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:90:11:
            // ( thisClass )
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:91:3:
            // thisClass
            {
                input.LT(1);
                pushFollow(FOLLOW_thisClass_in_bottomup80);
                thisClass1 = thisClass();
                state._fsp--;
                if (state.failed) {
                    return retval;
                }
                if (state.backtracking == 1) {
                    _first_0 = thisClass1.tree;
                }
                if (state.backtracking == 1) {
                    retval.tree = _first_0;
                    if (adaptor.getParent(retval.tree) != null
                            && adaptor.isNil(adaptor.getParent(retval.tree))) {
                        retval.tree = (OPPLSyntaxTree) adaptor
                                .getParent(retval.tree);
                    }
                }
            }
        } catch (RewriteEmptyStreamException exception) {
            if (errorListener != null) {
                errorListener.rewriteEmptyStreamException(exception);
            }
        } finally {}
        return retval;
    }

    // $ANTLR end "bottomup"
    public static class thisClass_return extends TreeRuleReturnScope {

        OPPLSyntaxTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "thisClass"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:96:1:
    // thisClass : ^(i= IDENTIFIER THIS_CLASS ) -> ^( $i) ;
    public final OPPLPatternsDefine.thisClass_return thisClass() {
        OPPLPatternsDefine.thisClass_return retval = new OPPLPatternsDefine.thisClass_return();
        retval.start = input.LT(1);
        OPPLSyntaxTree root_0 = null;
        OPPLSyntaxTree _last = null;
        OPPLSyntaxTree i = null;
        OPPLSyntaxTree THIS_CLASS2 = null;
        RewriteRuleNodeStream stream_IDENTIFIER = new RewriteRuleNodeStream(
                adaptor, "token IDENTIFIER");
        RewriteRuleNodeStream stream_THIS_CLASS = new RewriteRuleNodeStream(
                adaptor, "token THIS_CLASS");
        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:97:3:
            // ( ^(i= IDENTIFIER THIS_CLASS ) -> ^( $i) )
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:99:5:
            // ^(i= IDENTIFIER THIS_CLASS )
            {
                _last = (OPPLSyntaxTree) input.LT(1);
                {
                    OPPLSyntaxTree _save_last_1 = _last;
                    _last = (OPPLSyntaxTree) input.LT(1);
                    i = (OPPLSyntaxTree) match(input, IDENTIFIER,
                            FOLLOW_IDENTIFIER_in_thisClass104);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 1) {
                        stream_IDENTIFIER.add(i);
                    }
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return retval;
                    }
                    _last = (OPPLSyntaxTree) input.LT(1);
                    THIS_CLASS2 = (OPPLSyntaxTree) match(input, THIS_CLASS,
                            FOLLOW_THIS_CLASS_in_thisClass106);
                    if (state.failed) {
                        return retval;
                    }
                    if (state.backtracking == 1) {
                        stream_THIS_CLASS.add(THIS_CLASS2);
                    }
                    match(input, Token.UP, null);
                    if (state.failed) {
                        return retval;
                    }
                    _last = _save_last_1;
                }
                if (state.backtracking == 1) {
                    symtab.resolveThisClass(THIS_CLASS2, getConstraintSystem());
                }
                // AST REWRITE
                // elements: i
                // token labels: i
                // rule labels: retval
                // token list labels:
                // rule list labels:
                // wildcard labels:
                if (state.backtracking == 1) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_i = new RewriteRuleNodeStream(
                            adaptor, "token i", i);
                    root_0 = (OPPLSyntaxTree) adaptor.nil();
                    // 103:5: -> ^( $i)
                    {
                        // /Users/luigi/Documents/workspace/Parsers/src/OPPLPatternsDefine.g:104:5:
                        // ^( $i)
                        {
                            OPPLSyntaxTree root_1 = (OPPLSyntaxTree) adaptor
                                    .nil();
                            root_1 = (OPPLSyntaxTree) adaptor.becomeRoot(
                                    stream_i.nextNode(), root_1);
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    retval.tree = (OPPLSyntaxTree) adaptor
                            .rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                            adaptor.getChildIndex(retval.start),
                            adaptor.getChildIndex(_last), retval.tree);
                }
            }
        } catch (RecognitionException exception) {
            if (errorListener != null) {
                errorListener.recognitionException(exception);
            }
        } catch (RewriteEmptyStreamException exception) {
            if (errorListener != null) {
                errorListener.rewriteEmptyStreamException(exception);
            }
        } finally {}
        return retval;
    }

    // $ANTLR end "thisClass"
    // Delegated rules
    public static final BitSet FOLLOW_thisClass_in_bottomup80 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENTIFIER_in_thisClass104 = new BitSet(
            new long[] { 0x0000000000000004L });
    public static final BitSet FOLLOW_THIS_CLASS_in_thisClass106 = new BitSet(
            new long[] { 0x0000000000000008L });
}
