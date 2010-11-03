// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g 2010-11-03 11:39:14

  package org.coode.parsers.oppl;
  import org.coode.parsers.ErrorListener;
  import org.coode.parsers.ManchesterOWLSyntaxTree;
  import org.coode.oppl.ConstraintSystem;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class OPPLDefine extends TreeRewriter {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMPOSITION", "OPEN_PARENTHESYS", "OPEN_CURLY_BRACES", "CLOSED_CURLY_BRACES", "CLOSED_PARENTHESYS", "WHITESPACE", "AND", "OR", "NOT", "SOME", "ONLY", "MIN", "MAX", "EXACTLY", "VALUE", "INVERSE", "SUBCLASS_OF", "SUB_PROPERTY_OF", "EQUIVALENT_TO", "SAME_AS", "DIFFERENT_FROM", "INVERSE_OF", "DISJOINT_WITH", "DOMAIN", "RANGE", "FUNCTIONAL", "SYMMETRIC", "ANTI_SYMMETRIC", "REFLEXIVE", "IRREFLEXIVE", "TRANSITIVE", "INVERSE_FUNCTIONAL", "POW", "COMMA", "INSTANCE_OF", "TYPES", "DBLQUOTE", "DIGIT", "INTEGER", "LETTER", "IDENTIFIER", "ENTITY_REFERENCE", "QUESTION_MARK", "Tokens", "SUB_CLASS_AXIOM", "EQUIVALENT_TO_AXIOM", "DISJOINT_WITH_AXIOM", "SUB_PROPERTY_AXIOM", "SAME_AS_AXIOM", "DIFFERENT_FROM_AXIOM", "UNARY_AXIOM", "DISJUNCTION", "CONJUNCTION", "PROPERTY_CHAIN", "NEGATED_EXPRESSION", "NEGATED_ASSERTION", "INVERSE_PROPERTY", "SOME_RESTRICTION", "ALL_RESTRICTION", "VALUE_RESTRICTION", "CARDINALITY_RESTRICTION", "ONE_OF", "TYPE_ASSERTION", "ROLE_ASSERTION", "INVERSE_OBJECT_PROPERTY_EXPRESSION", "EXPRESSION", "CONSTANT", "WHERE", "NOT_EQUAL", "EQUAL", "IN", "SELECT", "ASSERTED", "COLON", "DOT", "PLUS", "CREATE", "CREATE_INTERSECTION", "CREATE_DISJUNCTION", "BEGIN", "END", "OPEN_SQUARE_BRACKET", "CLOSED_SQUARE_BRACKET", "SUPER_CLASS_OF", "SUPER_PROPERTY_OF", "VARIABLE_TYPE", "ADD", "REMOVE", "ASSERTED_CLAUSE", "PLAIN_CLAUSE", "INEQUALITY_CONSTRAINT", "IN_SET_CONSTRAINT", "INPUT_VARIABLE_DEFINITION", "GENERATED_VARIABLE_DEFINITION", "CREATE_OPPL_FUNCTION", "VARIABLE_ATTRIBUTE", "OPPL_FUNCTION", "ACTIONS", "VARIABLE_DEFINITIONS", "QUERY", "VARIABLE_SCOPE", "SUBPROPERTY_OF", "VARIABLE_IDENTIFIER", "OPPL_STATEMENT", "ESCLAMATION_MARK", "MATCH", "ATTRIBUTE_SELECTOR", "VALUES", "RENDERING", "GROUPS", "STRING_OPERATION", "VARIABLE_NAME", "REGEXP_CONSTRAINT", "FAIL", "NAF_CONSTRAINT"
    };
    public static final int COMMA=37;
    public static final int ASSERTED=76;
    public static final int VARIABLE_DEFINITIONS=102;
    public static final int REGEXP_CONSTRAINT=465;
    public static final int END=84;
    public static final int DIFFERENT_FROM=24;
    public static final int TYPE_ASSERTION=66;
    public static final int SAME_AS_AXIOM=52;
    public static final int TYPES=39;
    public static final int ROLE_ASSERTION=67;
    public static final int CREATE_OPPL_FUNCTION=98;
    public static final int ESCLAMATION_MARK=149;
    public static final int VARIABLE_IDENTIFIER=106;
    public static final int ASSERTED_CLAUSE=92;
    public static final int DOT=78;
    public static final int ALL_RESTRICTION=62;
    public static final int QUESTION_MARK=46;
    public static final int AND=10;
    public static final int EXPRESSION=69;
    public static final int CONSTANT=70;
    public static final int VALUE_RESTRICTION=63;
    public static final int ONE_OF=65;
    public static final int SUBPROPERTY_OF=105;
    public static final int SELECT=75;
    public static final int CARDINALITY_RESTRICTION=64;
    public static final int SAME_AS=23;
    public static final int EXACTLY=17;
    public static final int PLUS=79;
    public static final int TRANSITIVE=34;
    public static final int IN_SET_CONSTRAINT=95;
    public static final int SUBCLASS_OF=20;
    public static final int ENTITY_REFERENCE=45;
    public static final int CONJUNCTION=56;
    public static final int INVERSE_OF=25;
    public static final int RANGE=28;
    public static final int CLOSED_PARENTHESYS=8;
    public static final int PROPERTY_CHAIN=57;
    public static final int CREATE_INTERSECTION=81;
    public static final int EQUIVALENT_TO_AXIOM=49;
    public static final int OPEN_SQUARE_BRACKET=85;
    public static final int NAF_CONSTRAINT=467;
    public static final int SYMMETRIC=30;
    public static final int DISJOINT_WITH=26;
    public static final int VARIABLE_TYPE=89;
    public static final int DISJUNCTION=55;
    public static final int GROUPS=356;
    public static final int NEGATED_EXPRESSION=58;
    public static final int EQUAL=73;
    public static final int DIFFERENT_FROM_AXIOM=53;
    public static final int ACTIONS=101;
    public static final int EQUIVALENT_TO=22;
    public static final int DOMAIN=27;
    public static final int SUB_PROPERTY_OF=21;
    public static final int INVERSE_OBJECT_PROPERTY_EXPRESSION=68;
    public static final int INVERSE_PROPERTY=60;
    public static final int COLON=77;
    public static final int GENERATED_VARIABLE_DEFINITION=97;
    public static final int VARIABLE_ATTRIBUTE=99;
    public static final int SUB_CLASS_AXIOM=48;
    public static final int SUB_PROPERTY_AXIOM=51;
    public static final int IDENTIFIER=44;
    public static final int UNARY_AXIOM=54;
    public static final int ADD=90;
    public static final int WHERE=71;
    public static final int CREATE=80;
    public static final int VARIABLE_SCOPE=104;
    public static final int OPEN_CURLY_BRACES=6;
    public static final int CLOSED_SQUARE_BRACKET=86;
    public static final int INSTANCE_OF=38;
    public static final int VALUES=354;
    public static final int QUERY=103;
    public static final int SOME_RESTRICTION=61;
    public static final int VALUE=18;
    public static final int RENDERING=355;
    public static final int INVERSE_FUNCTIONAL=35;
    public static final int ATTRIBUTE_SELECTOR=283;
    public static final int PLAIN_CLAUSE=93;
    public static final int OR=11;
    public static final int INTEGER=42;
    public static final int INVERSE=19;
    public static final int DISJOINT_WITH_AXIOM=50;
    public static final int SUPER_CLASS_OF=87;
    public static final int OPPL_FUNCTION=100;
    public static final int DIGIT=41;
    public static final int COMPOSITION=4;
    public static final int OPPL_STATEMENT=107;
    public static final int FUNCTIONAL=29;
    public static final int NOT_EQUAL=72;
    public static final int LETTER=43;
    public static final int MAX=16;
    public static final int FAIL=466;
    public static final int NEGATED_ASSERTION=59;
    public static final int INPUT_VARIABLE_DEFINITION=96;
    public static final int ONLY=14;
    public static final int CREATE_DISJUNCTION=82;
    public static final int REMOVE=91;
    public static final int DBLQUOTE=40;
    public static final int MIN=15;
    public static final int POW=36;
    public static final int MATCH=176;
    public static final int BEGIN=83;
    public static final int WHITESPACE=9;
    public static final int IN=74;
    public static final int SUPER_PROPERTY_OF=88;
    public static final int INEQUALITY_CONSTRAINT=94;
    public static final int SOME=13;
    public static final int EOF=-1;
    public static final int ANTI_SYMMETRIC=31;
    public static final int Tokens=47;
    public static final int CLOSED_CURLY_BRACES=7;
    public static final int REFLEXIVE=32;
    public static final int NOT=12;
    public static final int STRING_OPERATION=394;
    public static final int OPEN_PARENTHESYS=5;
    public static final int VARIABLE_NAME=464;
    public static final int IRREFLEXIVE=33;

    // delegates
    // delegators


        public OPPLDefine(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public OPPLDefine(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return OPPLDefine.tokenNames; }
    public String getGrammarFileName() { return "/Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g"; }


      private  OPPLSymbolTable symtab;
      private  ErrorListener errorListener;
      private ConstraintSystem constraintSystem;
      public OPPLDefine(TreeNodeStream input, OPPLSymbolTable symtab, ErrorListener errorListener, ConstraintSystem constraintSystem) {
        this(input);
        if(symtab==null){
        	throw new NullPointerException("The symbol table cannot be null");
        }
        if(errorListener == null){
        	throw new NullPointerException("The error listener cannot be null");
        }
        if(constraintSystem == null){
          throw new NullPointerException("The constraint system cannot be null");
        }
        this.symtab = symtab;
        this.errorListener = errorListener;
        this.constraintSystem= constraintSystem;
      }
      
      public ConstraintSystem getConstraintSystem(){
        return this.constraintSystem;
      }
      
      public ErrorListener getErrorListener(){
      	return this.errorListener;
      }
      
      public OPPLSymbolTable getSymbolTable(){
      	return this.symtab;
      }
      
      public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
            getErrorListener().recognitionException(e, tokenNames);
      }
      
      protected void mismatch (IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype,input);
      }
      

      public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException{
        throw e;
      }
      


    public static class bottomup_return extends TreeRuleReturnScope {
        OPPLSyntaxTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bottomup"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:79:1: bottomup : ( variableDefinition | groupAttributeReferences | variableAttributeReferences );
    public final OPPLDefine.bottomup_return bottomup() throws RecognitionException {
        OPPLDefine.bottomup_return retval = new OPPLDefine.bottomup_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree root_0 = null;

        OPPLSyntaxTree _first_0 = null;
        OPPLSyntaxTree _last = null;

        OPPLDefine.variableDefinition_return variableDefinition1 = null;

        OPPLDefine.groupAttributeReferences_return groupAttributeReferences2 = null;

        OPPLDefine.variableAttributeReferences_return variableAttributeReferences3 = null;



        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:79:11: ( variableDefinition | groupAttributeReferences | variableAttributeReferences )
            int alt1=3;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=INPUT_VARIABLE_DEFINITION && LA1_0<=GENERATED_VARIABLE_DEFINITION)) ) {
                alt1=1;
            }
            else if ( (LA1_0==IDENTIFIER) ) {
                int LA1_2 = input.LA(2);

                if ( (LA1_2==DOWN) ) {
                    int LA1_3 = input.LA(3);

                    if ( (LA1_3==VARIABLE_NAME) ) {
                        int LA1_4 = input.LA(4);

                        if ( (LA1_4==DOT) ) {
                            int LA1_5 = input.LA(5);

                            if ( (LA1_5==GROUPS) ) {
                                alt1=2;
                            }
                            else if ( ((LA1_5>=VALUES && LA1_5<=RENDERING)) ) {
                                alt1=3;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 1, 5, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 1, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:80:5: variableDefinition
                    {
                    _last = (OPPLSyntaxTree)input.LT(1);
                    pushFollow(FOLLOW_variableDefinition_in_bottomup82);
                    variableDefinition1=variableDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==1 ) 
                     
                    if ( _first_0==null ) _first_0 = variableDefinition1.tree;

                    if ( state.backtracking==1 ) {
                    retval.tree = (OPPLSyntaxTree)_first_0;
                    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        retval.tree = (OPPLSyntaxTree)adaptor.getParent(retval.tree);}
                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:81:7: groupAttributeReferences
                    {
                    _last = (OPPLSyntaxTree)input.LT(1);
                    pushFollow(FOLLOW_groupAttributeReferences_in_bottomup90);
                    groupAttributeReferences2=groupAttributeReferences();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==1 ) 
                     
                    if ( _first_0==null ) _first_0 = groupAttributeReferences2.tree;

                    if ( state.backtracking==1 ) {
                    retval.tree = (OPPLSyntaxTree)_first_0;
                    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        retval.tree = (OPPLSyntaxTree)adaptor.getParent(retval.tree);}
                    }
                    break;
                case 3 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:82:7: variableAttributeReferences
                    {
                    _last = (OPPLSyntaxTree)input.LT(1);
                    pushFollow(FOLLOW_variableAttributeReferences_in_bottomup98);
                    variableAttributeReferences3=variableAttributeReferences();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==1 ) 
                     
                    if ( _first_0==null ) _first_0 = variableAttributeReferences3.tree;

                    if ( state.backtracking==1 ) {
                    retval.tree = (OPPLSyntaxTree)_first_0;
                    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                        retval.tree = (OPPLSyntaxTree)adaptor.getParent(retval.tree);}
                    }
                    break;

            }
        }

          catch(RecognitionException exception){
            if(errorListener!=null){
              errorListener.recognitionException(exception);
            }
          }
          
          catch(RewriteEmptyStreamException exception){
            if(errorListener!=null){
              errorListener.rewriteEmptyStreamException(exception);
            }
          }
        finally {
        }
        return retval;
    }
    // $ANTLR end "bottomup"

    public static class variableDefinition_return extends TreeRuleReturnScope {
        OPPLSyntaxTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variableDefinition"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:87:1: variableDefinition : ^( ( INPUT_VARIABLE_DEFINITION | GENERATED_VARIABLE_DEFINITION ) VARIABLE_NAME VARIABLE_TYPE ( . )* ) ;
    public final OPPLDefine.variableDefinition_return variableDefinition() throws RecognitionException {
        OPPLDefine.variableDefinition_return retval = new OPPLDefine.variableDefinition_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree root_0 = null;

        OPPLSyntaxTree _first_0 = null;
        OPPLSyntaxTree _last = null;

        OPPLSyntaxTree set4=null;
        OPPLSyntaxTree VARIABLE_NAME5=null;
        OPPLSyntaxTree VARIABLE_TYPE6=null;
        OPPLSyntaxTree wildcard7=null;

        OPPLSyntaxTree set4_tree=null;
        OPPLSyntaxTree VARIABLE_NAME5_tree=null;
        OPPLSyntaxTree VARIABLE_TYPE6_tree=null;
        OPPLSyntaxTree wildcard7_tree=null;

        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:88:2: ( ^( ( INPUT_VARIABLE_DEFINITION | GENERATED_VARIABLE_DEFINITION ) VARIABLE_NAME VARIABLE_TYPE ( . )* ) )
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:89:3: ^( ( INPUT_VARIABLE_DEFINITION | GENERATED_VARIABLE_DEFINITION ) VARIABLE_NAME VARIABLE_TYPE ( . )* )
            {
            _last = (OPPLSyntaxTree)input.LT(1);
            {
            OPPLSyntaxTree _save_last_1 = _last;
            OPPLSyntaxTree _first_1 = null;
            set4=(OPPLSyntaxTree)input.LT(1);
            if ( (input.LA(1)>=INPUT_VARIABLE_DEFINITION && input.LA(1)<=GENERATED_VARIABLE_DEFINITION) ) {
                input.consume();


                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = set4;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (OPPLSyntaxTree)input.LT(1);
            VARIABLE_NAME5=(OPPLSyntaxTree)match(input,VARIABLE_NAME,FOLLOW_VARIABLE_NAME_in_variableDefinition124); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = VARIABLE_NAME5;
            _last = (OPPLSyntaxTree)input.LT(1);
            VARIABLE_TYPE6=(OPPLSyntaxTree)match(input,VARIABLE_TYPE,FOLLOW_VARIABLE_TYPE_in_variableDefinition127); if (state.failed) return retval;
             
            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = VARIABLE_TYPE6;
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:89:93: ( . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=COMPOSITION && LA2_0<=NAF_CONSTRAINT)) ) {
                    alt2=1;
                }
                else if ( (LA2_0==UP) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:89:93: .
            	    {
            	    _last = (OPPLSyntaxTree)input.LT(1);
            	    wildcard7=(OPPLSyntaxTree)input.LT(1);
            	    matchAny(input); if (state.failed) return retval;
            	     
            	    if ( state.backtracking==1 )
            	    if ( _first_1==null ) _first_1 = wildcard7;

            	    if ( state.backtracking==1 ) {
            	    retval.tree = (OPPLSyntaxTree)_first_0;
            	    if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
            	        retval.tree = (OPPLSyntaxTree)adaptor.getParent(retval.tree);}
            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {

              			getSymbolTable().defineVariable(VARIABLE_NAME5, VARIABLE_TYPE6, getConstraintSystem());
              		
            }

            if ( state.backtracking==1 ) {
            retval.tree = (OPPLSyntaxTree)_first_0;
            if ( adaptor.getParent(retval.tree)!=null && adaptor.isNil( adaptor.getParent(retval.tree) ) )
                retval.tree = (OPPLSyntaxTree)adaptor.getParent(retval.tree);}
            }

        }

          catch(RecognitionException exception){
            if(errorListener!=null){
              errorListener.recognitionException(exception);
            }
          }
          
          catch(RewriteEmptyStreamException exception){
            if(errorListener!=null){
              errorListener.rewriteEmptyStreamException(exception);
            }
          }
        finally {
        }
        return retval;
    }
    // $ANTLR end "variableDefinition"

    public static class groupAttributeReferences_return extends TreeRuleReturnScope {
        OPPLSyntaxTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "groupAttributeReferences"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:95:1: groupAttributeReferences : ^(i= IDENTIFIER VARIABLE_NAME DOT GROUPS ^( ATTRIBUTE_SELECTOR INTEGER ) ) -> ^( $i) ;
    public final OPPLDefine.groupAttributeReferences_return groupAttributeReferences() throws RecognitionException {
        OPPLDefine.groupAttributeReferences_return retval = new OPPLDefine.groupAttributeReferences_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree root_0 = null;

        OPPLSyntaxTree _first_0 = null;
        OPPLSyntaxTree _last = null;

        OPPLSyntaxTree i=null;
        OPPLSyntaxTree VARIABLE_NAME8=null;
        OPPLSyntaxTree DOT9=null;
        OPPLSyntaxTree GROUPS10=null;
        OPPLSyntaxTree ATTRIBUTE_SELECTOR11=null;
        OPPLSyntaxTree INTEGER12=null;

        OPPLSyntaxTree i_tree=null;
        OPPLSyntaxTree VARIABLE_NAME8_tree=null;
        OPPLSyntaxTree DOT9_tree=null;
        OPPLSyntaxTree GROUPS10_tree=null;
        OPPLSyntaxTree ATTRIBUTE_SELECTOR11_tree=null;
        OPPLSyntaxTree INTEGER12_tree=null;
        RewriteRuleNodeStream stream_GROUPS=new RewriteRuleNodeStream(adaptor,"token GROUPS");
        RewriteRuleNodeStream stream_INTEGER=new RewriteRuleNodeStream(adaptor,"token INTEGER");
        RewriteRuleNodeStream stream_IDENTIFIER=new RewriteRuleNodeStream(adaptor,"token IDENTIFIER");
        RewriteRuleNodeStream stream_ATTRIBUTE_SELECTOR=new RewriteRuleNodeStream(adaptor,"token ATTRIBUTE_SELECTOR");
        RewriteRuleNodeStream stream_VARIABLE_NAME=new RewriteRuleNodeStream(adaptor,"token VARIABLE_NAME");
        RewriteRuleNodeStream stream_DOT=new RewriteRuleNodeStream(adaptor,"token DOT");

        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:96:3: ( ^(i= IDENTIFIER VARIABLE_NAME DOT GROUPS ^( ATTRIBUTE_SELECTOR INTEGER ) ) -> ^( $i) )
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:97:4: ^(i= IDENTIFIER VARIABLE_NAME DOT GROUPS ^( ATTRIBUTE_SELECTOR INTEGER ) )
            {
            _last = (OPPLSyntaxTree)input.LT(1);
            {
            OPPLSyntaxTree _save_last_1 = _last;
            OPPLSyntaxTree _first_1 = null;
            _last = (OPPLSyntaxTree)input.LT(1);
            i=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_groupAttributeReferences155); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_IDENTIFIER.add(i);


            if ( state.backtracking==1 )
            if ( _first_0==null ) _first_0 = i;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (OPPLSyntaxTree)input.LT(1);
            VARIABLE_NAME8=(OPPLSyntaxTree)match(input,VARIABLE_NAME,FOLLOW_VARIABLE_NAME_in_groupAttributeReferences157); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_VARIABLE_NAME.add(VARIABLE_NAME8);

            _last = (OPPLSyntaxTree)input.LT(1);
            DOT9=(OPPLSyntaxTree)match(input,DOT,FOLLOW_DOT_in_groupAttributeReferences159); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_DOT.add(DOT9);

            _last = (OPPLSyntaxTree)input.LT(1);
            GROUPS10=(OPPLSyntaxTree)match(input,GROUPS,FOLLOW_GROUPS_in_groupAttributeReferences161); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_GROUPS.add(GROUPS10);

            _last = (OPPLSyntaxTree)input.LT(1);
            {
            OPPLSyntaxTree _save_last_2 = _last;
            OPPLSyntaxTree _first_2 = null;
            _last = (OPPLSyntaxTree)input.LT(1);
            ATTRIBUTE_SELECTOR11=(OPPLSyntaxTree)match(input,ATTRIBUTE_SELECTOR,FOLLOW_ATTRIBUTE_SELECTOR_in_groupAttributeReferences164); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_ATTRIBUTE_SELECTOR.add(ATTRIBUTE_SELECTOR11);


            if ( state.backtracking==1 )
            if ( _first_1==null ) _first_1 = ATTRIBUTE_SELECTOR11;
            match(input, Token.DOWN, null); if (state.failed) return retval;
            _last = (OPPLSyntaxTree)input.LT(1);
            INTEGER12=(OPPLSyntaxTree)match(input,INTEGER,FOLLOW_INTEGER_in_groupAttributeReferences166); if (state.failed) return retval; 
            if ( state.backtracking==1 ) stream_INTEGER.add(INTEGER12);


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
            }


            match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
            }

            if ( state.backtracking==1 ) {


                    getSymbolTable().defineGroupAttributeReferenceSymbol(VARIABLE_NAME8,INTEGER12, getConstraintSystem());
                  
            }


            // AST REWRITE
            // elements: i
            // token labels: i
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==1 ) {
            retval.tree = root_0;
            RewriteRuleNodeStream stream_i=new RewriteRuleNodeStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (OPPLSyntaxTree)adaptor.nil();
            // 102:5: -> ^( $i)
            {
                // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:102:8: ^( $i)
                {
                OPPLSyntaxTree root_1 = (OPPLSyntaxTree)adaptor.nil();
                root_1 = (OPPLSyntaxTree)adaptor.becomeRoot(stream_i.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = (OPPLSyntaxTree)adaptor.rulePostProcessing(root_0);
            input.replaceChildren(adaptor.getParent(retval.start),
                                  adaptor.getChildIndex(retval.start),
                                  adaptor.getChildIndex(_last),
                                  retval.tree);}
            }

        }

          catch(RecognitionException exception){
            if(errorListener!=null){
              errorListener.recognitionException(exception);
            }
          }
          
          catch(RewriteEmptyStreamException exception){
            if(errorListener!=null){
              errorListener.rewriteEmptyStreamException(exception);
            }
          }
        finally {
        }
        return retval;
    }
    // $ANTLR end "groupAttributeReferences"

    public static class variableAttributeReferences_return extends TreeRuleReturnScope {
        OPPLSyntaxTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variableAttributeReferences"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:105:1: variableAttributeReferences : ( ^(i= IDENTIFIER VARIABLE_NAME DOT RENDERING ) -> ^( $i) | ^(i= IDENTIFIER VARIABLE_NAME DOT VALUES ) -> ^( $i) );
    public final OPPLDefine.variableAttributeReferences_return variableAttributeReferences() throws RecognitionException {
        OPPLDefine.variableAttributeReferences_return retval = new OPPLDefine.variableAttributeReferences_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree root_0 = null;

        OPPLSyntaxTree _first_0 = null;
        OPPLSyntaxTree _last = null;

        OPPLSyntaxTree i=null;
        OPPLSyntaxTree VARIABLE_NAME13=null;
        OPPLSyntaxTree DOT14=null;
        OPPLSyntaxTree RENDERING15=null;
        OPPLSyntaxTree VARIABLE_NAME16=null;
        OPPLSyntaxTree DOT17=null;
        OPPLSyntaxTree VALUES18=null;

        OPPLSyntaxTree i_tree=null;
        OPPLSyntaxTree VARIABLE_NAME13_tree=null;
        OPPLSyntaxTree DOT14_tree=null;
        OPPLSyntaxTree RENDERING15_tree=null;
        OPPLSyntaxTree VARIABLE_NAME16_tree=null;
        OPPLSyntaxTree DOT17_tree=null;
        OPPLSyntaxTree VALUES18_tree=null;
        RewriteRuleNodeStream stream_VALUES=new RewriteRuleNodeStream(adaptor,"token VALUES");
        RewriteRuleNodeStream stream_IDENTIFIER=new RewriteRuleNodeStream(adaptor,"token IDENTIFIER");
        RewriteRuleNodeStream stream_RENDERING=new RewriteRuleNodeStream(adaptor,"token RENDERING");
        RewriteRuleNodeStream stream_VARIABLE_NAME=new RewriteRuleNodeStream(adaptor,"token VARIABLE_NAME");
        RewriteRuleNodeStream stream_DOT=new RewriteRuleNodeStream(adaptor,"token DOT");

        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:106:3: ( ^(i= IDENTIFIER VARIABLE_NAME DOT RENDERING ) -> ^( $i) | ^(i= IDENTIFIER VARIABLE_NAME DOT VALUES ) -> ^( $i) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==IDENTIFIER) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==DOWN) ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2==VARIABLE_NAME) ) {
                        int LA3_3 = input.LA(4);

                        if ( (LA3_3==DOT) ) {
                            int LA3_4 = input.LA(5);

                            if ( (LA3_4==RENDERING) ) {
                                alt3=1;
                            }
                            else if ( (LA3_4==VALUES) ) {
                                alt3=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 3, 4, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 3, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:107:4: ^(i= IDENTIFIER VARIABLE_NAME DOT RENDERING )
                    {
                    _last = (OPPLSyntaxTree)input.LT(1);
                    {
                    OPPLSyntaxTree _save_last_1 = _last;
                    OPPLSyntaxTree _first_1 = null;
                    _last = (OPPLSyntaxTree)input.LT(1);
                    i=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableAttributeReferences208); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_IDENTIFIER.add(i);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = i;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (OPPLSyntaxTree)input.LT(1);
                    VARIABLE_NAME13=(OPPLSyntaxTree)match(input,VARIABLE_NAME,FOLLOW_VARIABLE_NAME_in_variableAttributeReferences211); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_VARIABLE_NAME.add(VARIABLE_NAME13);

                    _last = (OPPLSyntaxTree)input.LT(1);
                    DOT14=(OPPLSyntaxTree)match(input,DOT,FOLLOW_DOT_in_variableAttributeReferences213); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DOT.add(DOT14);

                    _last = (OPPLSyntaxTree)input.LT(1);
                    RENDERING15=(OPPLSyntaxTree)match(input,RENDERING,FOLLOW_RENDERING_in_variableAttributeReferences217); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_RENDERING.add(RENDERING15);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }

                    if ( state.backtracking==1 ) {

                            getSymbolTable().defineRenderingAttributeReferenceSymbol(VARIABLE_NAME13,getConstraintSystem());
                          
                    }


                    // AST REWRITE
                    // elements: i
                    // token labels: i
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_i=new RewriteRuleNodeStream(adaptor,"token i",i);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (OPPLSyntaxTree)adaptor.nil();
                    // 111:4: -> ^( $i)
                    {
                        // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:111:7: ^( $i)
                        {
                        OPPLSyntaxTree root_1 = (OPPLSyntaxTree)adaptor.nil();
                        root_1 = (OPPLSyntaxTree)adaptor.becomeRoot(stream_i.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (OPPLSyntaxTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:112:5: ^(i= IDENTIFIER VARIABLE_NAME DOT VALUES )
                    {
                    _last = (OPPLSyntaxTree)input.LT(1);
                    {
                    OPPLSyntaxTree _save_last_1 = _last;
                    OPPLSyntaxTree _first_1 = null;
                    _last = (OPPLSyntaxTree)input.LT(1);
                    i=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableAttributeReferences246); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_IDENTIFIER.add(i);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = i;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (OPPLSyntaxTree)input.LT(1);
                    VARIABLE_NAME16=(OPPLSyntaxTree)match(input,VARIABLE_NAME,FOLLOW_VARIABLE_NAME_in_variableAttributeReferences249); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_VARIABLE_NAME.add(VARIABLE_NAME16);

                    _last = (OPPLSyntaxTree)input.LT(1);
                    DOT17=(OPPLSyntaxTree)match(input,DOT,FOLLOW_DOT_in_variableAttributeReferences251); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DOT.add(DOT17);

                    _last = (OPPLSyntaxTree)input.LT(1);
                    VALUES18=(OPPLSyntaxTree)match(input,VALUES,FOLLOW_VALUES_in_variableAttributeReferences254); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_VALUES.add(VALUES18);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }

                    if ( state.backtracking==1 ) {

                            getSymbolTable().defineValuesAttributeReferenceSymbol(VARIABLE_NAME16,getConstraintSystem());
                          
                    }


                    // AST REWRITE
                    // elements: i
                    // token labels: i
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_i=new RewriteRuleNodeStream(adaptor,"token i",i);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (OPPLSyntaxTree)adaptor.nil();
                    // 116:5: -> ^( $i)
                    {
                        // /Users/luigi/Documents/workspace/Parsers/src/OPPLDefine.g:116:8: ^( $i)
                        {
                        OPPLSyntaxTree root_1 = (OPPLSyntaxTree)adaptor.nil();
                        root_1 = (OPPLSyntaxTree)adaptor.becomeRoot(stream_i.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (OPPLSyntaxTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;

            }
        }

          catch(RecognitionException exception){
            if(errorListener!=null){
              errorListener.recognitionException(exception);
            }
          }
          
          catch(RewriteEmptyStreamException exception){
            if(errorListener!=null){
              errorListener.rewriteEmptyStreamException(exception);
            }
          }
        finally {
        }
        return retval;
    }
    // $ANTLR end "variableAttributeReferences"

    // Delegated rules


 

    public static final BitSet FOLLOW_variableDefinition_in_bottomup82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_groupAttributeReferences_in_bottomup90 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableAttributeReferences_in_bottomup98 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_variableDefinition117 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_NAME_in_variableDefinition124 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_VARIABLE_TYPE_in_variableDefinition127 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF8L,0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFFFL,0x00000000000FFFFFL});
    public static final BitSet FOLLOW_IDENTIFIER_in_groupAttributeReferences155 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_NAME_in_groupAttributeReferences157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_DOT_in_groupAttributeReferences159 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_GROUPS_in_groupAttributeReferences161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_ATTRIBUTE_SELECTOR_in_groupAttributeReferences164 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INTEGER_in_groupAttributeReferences166 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableAttributeReferences208 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_NAME_in_variableAttributeReferences211 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_DOT_in_variableAttributeReferences213 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_RENDERING_in_variableAttributeReferences217 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableAttributeReferences246 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_NAME_in_variableAttributeReferences249 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_DOT_in_variableAttributeReferences251 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_VALUES_in_variableAttributeReferences254 = new BitSet(new long[]{0x0000000000000008L});

}