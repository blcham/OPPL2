// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g 2010-07-13 16:41:35

  package org.coode.parsers.oppl;
      import org.coode.oppl.entity.OWLEntityRenderer;
  import org.coode.oppl.AbstractConstraint;
import org.coode.oppl.ConstraintSystem;
  import org.coode.oppl.OPPLQuery;
import org.coode.oppl.Variable;
import org.coode.oppl.VariableScope;
import org.coode.oppl.VariableScopes;
  import org.coode.oppl.exceptions.OPPLException;
import org.coode.oppl.generated.AbstractCollectionGeneratedValue;
import org.coode.oppl.generated.ConcatGeneratedValues;
import org.coode.oppl.generated.RegExpGenerated;
import org.coode.oppl.generated.RegExpGeneratedValue;
import org.coode.oppl.generated.SingleValueGeneratedValue;
import org.coode.oppl.generated.StringGeneratedValue;
import org.coode.oppl.generated.VariableExpressionGeneratedVariable;
  import org.coode.oppl.OPPLAbstractFactory;
  import org.coode.oppl.InCollectionRegExpConstraint;
  import org.semanticweb.owl.model.OWLAxiom;
  import org.semanticweb.owl.model.OWLObject;
  import org.semanticweb.owl.model.OWLClass;
  import org.semanticweb.owl.model.OWLAxiomChange;
  import org.semanticweb.owl.model.OWLPropertyExpression;
  import org.semanticweb.owl.model.OWLDescription;
  import org.semanticweb.owl.model.RemoveAxiom;
  import org.semanticweb.owl.model.AddAxiom;    
  import org.coode.parsers.ErrorListener;
  import org.coode.parsers.Type;
  import org.coode.parsers.oppl.OPPLSymbolTable;
  import org.coode.parsers.ManchesterOWLSyntaxTree;
  import java.util.Set;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class OPPLTypesParts extends TreeFilter {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMPOSITION", "OPEN_PARENTHESYS", "OPEN_CURLY_BRACES", "CLOSED_CURLY_BRACES", "CLOSED_PARENTHESYS", "WHITESPACE", "AND", "OR", "NOT", "SOME", "ONLY", "MIN", "MAX", "EXACTLY", "VALUE", "INVERSE", "SUBCLASS_OF", "SUB_PROPERTY_OF", "EQUIVALENT_TO", "SAME_AS", "DIFFERENT_FROM", "INVERSE_OF", "DISJOINT_WITH", "DOMAIN", "RANGE", "FUNCTIONAL", "SYMMETRIC", "ANTI_SYMMETRIC", "REFLEXIVE", "IRREFLEXIVE", "TRANSITIVE", "INVERSE_FUNCTIONAL", "POW", "COMMA", "INSTANCE_OF", "TYPES", "DBLQUOTE", "DIGIT", "INTEGER", "LETTER", "IDENTIFIER", "ENTITY_REFERENCE", "QUESTION_MARK", "Tokens", "SUB_CLASS_AXIOM", "EQUIVALENT_TO_AXIOM", "DISJOINT_WITH_AXIOM", "SUB_PROPERTY_AXIOM", "SAME_AS_AXIOM", "DIFFERENT_FROM_AXIOM", "UNARY_AXIOM", "DISJUNCTION", "CONJUNCTION", "PROPERTY_CHAIN", "NEGATED_EXPRESSION", "NEGATED_ASSERTION", "INVERSE_PROPERTY", "SOME_RESTRICTION", "ALL_RESTRICTION", "VALUE_RESTRICTION", "CARDINALITY_RESTRICTION", "ONE_OF", "TYPE_ASSERTION", "ROLE_ASSERTION", "INVERSE_OBJECT_PROPERTY_EXPRESSION", "EXPRESSION", "CONSTANT", "WHERE", "NOT_EQUAL", "EQUAL", "IN", "SELECT", "ASSERTED", "COLON", "DOT", "PLUS", "CREATE", "CREATE_INTERSECTION", "CREATE_DISJUNCTION", "BEGIN", "END", "OPEN_SQUARE_BRACKET", "CLOSED_SQUARE_BRACKET", "SUPER_CLASS_OF", "SUPER_PROPERTY_OF", "VARIABLE_TYPE", "ADD", "REMOVE", "ASSERTED_CLAUSE", "PLAIN_CLAUSE", "INEQUALITY_CONSTRAINT", "IN_SET_CONSTRAINT", "INPUT_VARIABLE_DEFINITION", "GENERATED_VARIABLE_DEFINITION", "CREATE_OPPL_FUNCTION", "VARIABLE_ATTRIBUTE", "OPPL_FUNCTION", "ACTIONS", "VARIABLE_DEFINITIONS", "QUERY", "VARIABLE_SCOPE", "SUBPROPERTY_OF", "VARIABLE_IDENTIFIER", "OPPL_STATEMENT", "ESCLAMATION_MARK", "MATCH", "ATTRIBUTE_SELECTOR", "VALUES", "RENDERING", "GROUPS", "STRING_OPERATION", "VARIABLE_NAME", "REGEXP_CONSTRAINT"
    };
    public static final int VALUE_RESTRICTION=63;
    public static final int LETTER=43;
    public static final int REMOVE=91;
    public static final int TYPES=39;
    public static final int SAME_AS_AXIOM=52;
    public static final int INVERSE_OF=25;
    public static final int NOT=12;
    public static final int SUBCLASS_OF=20;
    public static final int EOF=-1;
    public static final int ESCLAMATION_MARK=149;
    public static final int ACTIONS=101;
    public static final int CREATE=80;
    public static final int POW=36;
    public static final int INPUT_VARIABLE_DEFINITION=96;
    public static final int NOT_EQUAL=72;
    public static final int INVERSE_OBJECT_PROPERTY_EXPRESSION=68;
    public static final int INSTANCE_OF=38;
    public static final int BEGIN=83;
    public static final int VARIABLE_SCOPE=104;
    public static final int INEQUALITY_CONSTRAINT=94;
    public static final int QUESTION_MARK=46;
    public static final int SYMMETRIC=30;
    public static final int CARDINALITY_RESTRICTION=64;
    public static final int SELECT=75;
    public static final int ROLE_ASSERTION=67;
    public static final int DIFFERENT_FROM_AXIOM=53;
    public static final int CREATE_OPPL_FUNCTION=98;
    public static final int TRANSITIVE=34;
    public static final int ANTI_SYMMETRIC=31;
    public static final int ALL_RESTRICTION=62;
    public static final int CONJUNCTION=56;
    public static final int OPPL_STATEMENT=107;
    public static final int NEGATED_ASSERTION=59;
    public static final int WHITESPACE=9;
    public static final int MATCH=176;
    public static final int IN_SET_CONSTRAINT=95;
    public static final int VALUE=18;
    public static final int GROUPS=356;
    public static final int OPEN_CURLY_BRACES=6;
    public static final int DISJUNCTION=55;
    public static final int INVERSE=19;
    public static final int DBLQUOTE=40;
    public static final int STRING_OPERATION=394;
    public static final int OR=11;
    public static final int CONSTANT=70;
    public static final int QUERY=103;
    public static final int ENTITY_REFERENCE=45;
    public static final int END=84;
    public static final int COMPOSITION=4;
    public static final int CLOSED_SQUARE_BRACKET=86;
    public static final int SAME_AS=23;
    public static final int WHERE=71;
    public static final int DISJOINT_WITH=26;
    public static final int SUPER_PROPERTY_OF=88;
    public static final int VARIABLE_TYPE=89;
    public static final int ATTRIBUTE_SELECTOR=283;
    public static final int CLOSED_PARENTHESYS=8;
    public static final int ONLY=14;
    public static final int EQUIVALENT_TO_AXIOM=49;
    public static final int SUB_PROPERTY_OF=21;
    public static final int NEGATED_EXPRESSION=58;
    public static final int MAX=16;
    public static final int CREATE_DISJUNCTION=82;
    public static final int AND=10;
    public static final int ASSERTED_CLAUSE=92;
    public static final int INVERSE_PROPERTY=60;
    public static final int VARIABLE_NAME=464;
    public static final int DIFFERENT_FROM=24;
    public static final int IN=74;
    public static final int EQUIVALENT_TO=22;
    public static final int UNARY_AXIOM=54;
    public static final int COMMA=37;
    public static final int CLOSED_CURLY_BRACES=7;
    public static final int IDENTIFIER=44;
    public static final int SOME=13;
    public static final int EQUAL=73;
    public static final int OPEN_PARENTHESYS=5;
    public static final int REFLEXIVE=32;
    public static final int PLUS=79;
    public static final int DIGIT=41;
    public static final int DOT=78;
    public static final int SUPER_CLASS_OF=87;
    public static final int EXPRESSION=69;
    public static final int SOME_RESTRICTION=61;
    public static final int ADD=90;
    public static final int INTEGER=42;
    public static final int GENERATED_VARIABLE_DEFINITION=97;
    public static final int EXACTLY=17;
    public static final int SUB_PROPERTY_AXIOM=51;
    public static final int OPEN_SQUARE_BRACKET=85;
    public static final int VALUES=354;
    public static final int REGEXP_CONSTRAINT=465;
    public static final int RANGE=28;
    public static final int ONE_OF=65;
    public static final int VARIABLE_DEFINITIONS=102;
    public static final int MIN=15;
    public static final int SUB_CLASS_AXIOM=48;
    public static final int PLAIN_CLAUSE=93;
    public static final int Tokens=47;
    public static final int DOMAIN=27;
    public static final int SUBPROPERTY_OF=105;
    public static final int OPPL_FUNCTION=100;
    public static final int COLON=77;
    public static final int DISJOINT_WITH_AXIOM=50;
    public static final int CREATE_INTERSECTION=81;
    public static final int INVERSE_FUNCTIONAL=35;
    public static final int RENDERING=355;
    public static final int VARIABLE_IDENTIFIER=106;
    public static final int IRREFLEXIVE=33;
    public static final int VARIABLE_ATTRIBUTE=99;
    public static final int ASSERTED=76;
    public static final int FUNCTIONAL=29;
    public static final int PROPERTY_CHAIN=57;
    public static final int TYPE_ASSERTION=66;

    // delegates
    // delegators


        public OPPLTypesParts(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public OPPLTypesParts(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return OPPLTypesParts.tokenNames; }
    public String getGrammarFileName() { return "/Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g"; }


      private  OPPLSymbolTable symtab;
      private  ErrorListener errorListener;
      private ConstraintSystem constraintSystem;
      private OPPLAbstractFactory opplFactory;
      private Variable variable = null;

      
      public OPPLTypesParts(TreeNodeStream input, OPPLSymbolTable symtab, ErrorListener errorListener, ConstraintSystem constraintSystem, OPPLAbstractFactory opplFactory) {
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
        if(opplFactory == null){
          throw new NullPointerException("The OPPL Factory cannot be null");
        }
        this.symtab = symtab;
        this.errorListener = errorListener;
        this.opplFactory = opplFactory;
        this.constraintSystem = constraintSystem;
        
      }
      
      public void setVariable(Variable variable){
      	this.variable = variable;
      }
      
      public Variable getVariable(){
      	return variable;
      }
      
      public ErrorListener getErrorListener(){
      	return this.errorListener;
      }
      
      public ConstraintSystem getConstraintSystem(){
        return this.constraintSystem;
      }
      
      public OPPLSymbolTable getSymbolTable(){
      	return this.symtab;
      }
      
      public OPPLAbstractFactory getOPPLFactory(){
        return this.opplFactory;
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
      



    // $ANTLR start "bottomup"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:128:1: bottomup : ( constraint | opplFunction );
    public final void bottomup() throws RecognitionException {
        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:129:5: ( constraint | opplFunction )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=INEQUALITY_CONSTRAINT && LA1_0<=IN_SET_CONSTRAINT)||LA1_0==REGEXP_CONSTRAINT) ) {
                alt1=1;
            }
            else if ( ((LA1_0>=CREATE_INTERSECTION && LA1_0<=CREATE_DISJUNCTION)||LA1_0==CREATE_OPPL_FUNCTION) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:130:6: constraint
                    {
                    pushFollow(FOLLOW_constraint_in_bottomup81);
                    constraint();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:131:8: opplFunction
                    {
                    pushFollow(FOLLOW_opplFunction_in_bottomup90);
                    opplFunction();

                    state._fsp--;
                    if (state.failed) return ;

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
        return ;
    }
    // $ANTLR end "bottomup"


    // $ANTLR start "stringOperation"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:134:1: stringOperation returns [SingleValueGeneratedValue<String> value] : ^( STRING_OPERATION (valuesToAggregate= stringExpression )+ ) ;
    public final SingleValueGeneratedValue<String> stringOperation() throws RecognitionException {
        SingleValueGeneratedValue<String> value = null;

        SingleValueGeneratedValue<String> valuesToAggregate = null;



            List<SingleValueGeneratedValue<String>> values = new ArrayList<SingleValueGeneratedValue<String>>();
          
        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:139:3: ( ^( STRING_OPERATION (valuesToAggregate= stringExpression )+ ) )
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:140:5: ^( STRING_OPERATION (valuesToAggregate= stringExpression )+ )
            {
            match(input,STRING_OPERATION,FOLLOW_STRING_OPERATION_in_stringOperation121); if (state.failed) return value;

            match(input, Token.DOWN, null); if (state.failed) return value;
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:140:25: (valuesToAggregate= stringExpression )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==DBLQUOTE||LA2_0==IDENTIFIER) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:140:26: valuesToAggregate= stringExpression
            	    {
            	    pushFollow(FOLLOW_stringExpression_in_stringOperation128);
            	    valuesToAggregate=stringExpression();

            	    state._fsp--;
            	    if (state.failed) return value;
            	    if ( state.backtracking==1 ) {
            	      values.add(valuesToAggregate);
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
            	    if (state.backtracking>0) {state.failed=true; return value;}
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return value;
            if ( state.backtracking==1 ) {

                    value = new ConcatGeneratedValues(values); 
                  
            }

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
        return value;
    }
    // $ANTLR end "stringOperation"


    // $ANTLR start "stringExpression"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:147:1: stringExpression returns [SingleValueGeneratedValue<String> value] : ( DBLQUOTE | IDENTIFIER );
    public final SingleValueGeneratedValue<String> stringExpression() throws RecognitionException {
        SingleValueGeneratedValue<String> value = null;

        OPPLSyntaxTree DBLQUOTE1=null;
        OPPLSyntaxTree IDENTIFIER2=null;

        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:148:3: ( DBLQUOTE | IDENTIFIER )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==DBLQUOTE) ) {
                alt3=1;
            }
            else if ( (LA3_0==IDENTIFIER) ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:149:7: DBLQUOTE
                    {
                    DBLQUOTE1=(OPPLSyntaxTree)match(input,DBLQUOTE,FOLLOW_DBLQUOTE_in_stringExpression165); if (state.failed) return value;
                    if ( state.backtracking==1 ) {

                              value = new StringGeneratedValue(DBLQUOTE1.getText());
                            
                    }

                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:153:7: IDENTIFIER
                    {
                    IDENTIFIER2=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_stringExpression181); if (state.failed) return value;
                    if ( state.backtracking==1 ) {

                              value =symtab.getStringGeneratedValue(IDENTIFIER2, getConstraintSystem());
                            
                    }

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
        return value;
    }
    // $ANTLR end "stringExpression"

    public static class opplFunction_return extends TreeRuleReturnScope {
        public Variable variable;
    };

    // $ANTLR start "opplFunction"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:160:1: opplFunction returns [Variable variable] : ( ^( CREATE_OPPL_FUNCTION value= stringOperation ) | ^( CREATE_INTERSECTION va= IDENTIFIER ) | ^( CREATE_DISJUNCTION va= IDENTIFIER ) );
    public final OPPLTypesParts.opplFunction_return opplFunction() throws RecognitionException {
        OPPLTypesParts.opplFunction_return retval = new OPPLTypesParts.opplFunction_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree va=null;
        SingleValueGeneratedValue<String> value = null;


        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:165:2: ( ^( CREATE_OPPL_FUNCTION value= stringOperation ) | ^( CREATE_INTERSECTION va= IDENTIFIER ) | ^( CREATE_DISJUNCTION va= IDENTIFIER ) )
            int alt4=3;
            switch ( input.LA(1) ) {
            case CREATE_OPPL_FUNCTION:
                {
                alt4=1;
                }
                break;
            case CREATE_INTERSECTION:
                {
                alt4=2;
                }
                break;
            case CREATE_DISJUNCTION:
                {
                alt4=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:166:3: ^( CREATE_OPPL_FUNCTION value= stringOperation )
                    {
                    match(input,CREATE_OPPL_FUNCTION,FOLLOW_CREATE_OPPL_FUNCTION_in_opplFunction216); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_stringOperation_in_opplFunction223);
                    value=stringOperation();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                      	       if(getVariable()!=null){
                      		       retval.variable = constraintSystem.createStringGeneratedVariable(getVariable().getName(),getVariable().getType(), value);
                      		}else{
                      			getErrorListener().illegalToken(((OPPLSyntaxTree)retval.start), "No variable type to evaluate this OPPL Function");
                      		}
                      	     
                    }

                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:174:6: ^( CREATE_INTERSECTION va= IDENTIFIER )
                    {
                    match(input,CREATE_INTERSECTION,FOLLOW_CREATE_INTERSECTION_in_opplFunction240); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    va=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_opplFunction246); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                             if(getVariable()!=null){
                               
                               AbstractCollectionGeneratedValue<OWLClass> collection = symtab.getCollection(((OPPLSyntaxTree)retval.start),va, getConstraintSystem());
                               retval.variable = constraintSystem.createIntersectionGeneratedVariable(getVariable().getName(),getVariable().getType(),collection);         
                              }else{
                      			getErrorListener().illegalToken(((OPPLSyntaxTree)retval.start), "No variable type to evaluate this OPPL Function");
                      		}
                             
                    }

                    }
                    break;
                case 3 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:184:9: ^( CREATE_DISJUNCTION va= IDENTIFIER )
                    {
                    match(input,CREATE_DISJUNCTION,FOLLOW_CREATE_DISJUNCTION_in_opplFunction267); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    va=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_opplFunction273); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                             if(getVariable()!=null){
                               AbstractCollectionGeneratedValue<OWLClass> collection = symtab.getCollection(((OPPLSyntaxTree)retval.start),va, getConstraintSystem());
                               retval.variable = constraintSystem.createUnionGeneratedVariable(getVariable().getName(),getVariable().getType(),collection);         
                             } 
                             else{
                      			getErrorListener().illegalToken(((OPPLSyntaxTree)retval.start), "No variable type to evaluate this OPPL Function");
                      		}
                      	
                    }

                    }
                    break;

            }
            if ( state.backtracking==1 ) {

              		((OPPLSyntaxTree)retval.start).setOPPLContent(retval.variable);
              	
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
    // $ANTLR end "opplFunction"

    public static class constraint_return extends TreeRuleReturnScope {
        public AbstractConstraint constraint;
    };

    // $ANTLR start "constraint"
    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:196:1: constraint returns [AbstractConstraint constraint] : ( ^( INEQUALITY_CONSTRAINT IDENTIFIER ^( EXPRESSION expression= . ) ) | ^( IN_SET_CONSTRAINT v= IDENTIFIER ^( ONE_OF (i= IDENTIFIER )+ ) ) | ^( REGEXP_CONSTRAINT IDENTIFIER se= stringOperation ) );
    public final OPPLTypesParts.constraint_return constraint() throws RecognitionException {
        OPPLTypesParts.constraint_return retval = new OPPLTypesParts.constraint_return();
        retval.start = input.LT(1);

        OPPLSyntaxTree v=null;
        OPPLSyntaxTree i=null;
        OPPLSyntaxTree IDENTIFIER3=null;
        OPPLSyntaxTree IDENTIFIER4=null;
        OPPLSyntaxTree expression=null;
        SingleValueGeneratedValue<String> se = null;



            List<OPPLSyntaxTree> identifiers = new ArrayList<OPPLSyntaxTree>();
          
        try {
            // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:205:1: ( ^( INEQUALITY_CONSTRAINT IDENTIFIER ^( EXPRESSION expression= . ) ) | ^( IN_SET_CONSTRAINT v= IDENTIFIER ^( ONE_OF (i= IDENTIFIER )+ ) ) | ^( REGEXP_CONSTRAINT IDENTIFIER se= stringOperation ) )
            int alt6=3;
            switch ( input.LA(1) ) {
            case INEQUALITY_CONSTRAINT:
                {
                alt6=1;
                }
                break;
            case IN_SET_CONSTRAINT:
                {
                alt6=2;
                }
                break;
            case REGEXP_CONSTRAINT:
                {
                alt6=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:206:3: ^( INEQUALITY_CONSTRAINT IDENTIFIER ^( EXPRESSION expression= . ) )
                    {
                    match(input,INEQUALITY_CONSTRAINT,FOLLOW_INEQUALITY_CONSTRAINT_in_constraint314); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    IDENTIFIER3=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_constraint316); if (state.failed) return retval;
                    match(input,EXPRESSION,FOLLOW_EXPRESSION_in_constraint319); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    expression=(OPPLSyntaxTree)input.LT(1);
                    matchAny(input); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                      			retval.constraint = symtab.getInequalityConstraint(((OPPLSyntaxTree)retval.start), IDENTIFIER3,expression, getConstraintSystem());
                      		
                    }

                    }
                    break;
                case 2 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:209:5: ^( IN_SET_CONSTRAINT v= IDENTIFIER ^( ONE_OF (i= IDENTIFIER )+ ) )
                    {
                    match(input,IN_SET_CONSTRAINT,FOLLOW_IN_SET_CONSTRAINT_in_constraint333); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    v=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_constraint339); if (state.failed) return retval;
                    match(input,ONE_OF,FOLLOW_ONE_OF_in_constraint342); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:209:49: (i= IDENTIFIER )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==IDENTIFIER) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:209:50: i= IDENTIFIER
                    	    {
                    	    i=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_constraint349); if (state.failed) return retval;
                    	    if ( state.backtracking==1 ) {
                    	      identifiers.add(i);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                      			retval.constraint = symtab.getInSetConstraint(((OPPLSyntaxTree)retval.start),v,constraintSystem,identifiers.toArray(new OPPLSyntaxTree[identifiers.size()]));
                      		
                    }

                    }
                    break;
                case 3 :
                    // /Users/luigi/Documents/workspace/Parsers/src/OPPLTypesParts.g:212:5: ^( REGEXP_CONSTRAINT IDENTIFIER se= stringOperation )
                    {
                    match(input,REGEXP_CONSTRAINT,FOLLOW_REGEXP_CONSTRAINT_in_constraint363); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    IDENTIFIER4=(OPPLSyntaxTree)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_constraint365); if (state.failed) return retval;
                    pushFollow(FOLLOW_stringOperation_in_constraint371);
                    se=stringOperation();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;
                    if ( state.backtracking==1 ) {

                      			Variable variable = symtab.getVariable(IDENTIFIER4,getConstraintSystem());
                      			retval.constraint =   new InCollectionRegExpConstraint(variable, se, getConstraintSystem());
                      		
                    }

                    }
                    break;

            }
            if ( state.backtracking==1 ) {

              		((OPPLSyntaxTree)retval.start).setOPPLContent(retval.constraint);
              	
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
    // $ANTLR end "constraint"

    // Delegated rules


 

    public static final BitSet FOLLOW_constraint_in_bottomup81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opplFunction_in_bottomup90 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_OPERATION_in_stringOperation121 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_stringExpression_in_stringOperation128 = new BitSet(new long[]{0x0000110000000008L});
    public static final BitSet FOLLOW_DBLQUOTE_in_stringExpression165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_stringExpression181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_OPPL_FUNCTION_in_opplFunction216 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_stringOperation_in_opplFunction223 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_INTERSECTION_in_opplFunction240 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_opplFunction246 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_DISJUNCTION_in_opplFunction267 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_opplFunction273 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INEQUALITY_CONSTRAINT_in_constraint314 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_constraint316 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_EXPRESSION_in_constraint319 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IN_SET_CONSTRAINT_in_constraint333 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_constraint339 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_ONE_OF_in_constraint342 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_constraint349 = new BitSet(new long[]{0x0000100000000008L});
    public static final BitSet FOLLOW_REGEXP_CONSTRAINT_in_constraint363 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_constraint365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_stringOperation_in_constraint371 = new BitSet(new long[]{0x0000000000000008L});

}