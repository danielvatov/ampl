grammar AmplCombined;

options {
	output=AST;
//	tokenVocab = AmplLexer;
}

import AmplLexer;

tokens {
    PARAM_DECL;
    VAR_DECL;
    SET_DECL;
    DEFAULT_ASSIGN;
    INTEGER;
    BINARY;
    SYMBOLIC;
    MEMBER_ENUM;
    SET_RANGE;
}

@header {package net.vatov.ampl;}
@lexer::header {package net.vatov.ampl;}

model
    :
    (set_decl
    | param_decl
    | var_decl
    )+
    objective_decl*
    constraint* EOF
    ;

/*
 * Objective
 */

objective_decl
    : OBJ_ENTITY NAME alias? indexing? ':' expr ';' -> ^(OBJ_ENTITY NAME alias? indexing? expr)
    ;

/*
 * End objective
 */

/*
 * Constraint
 */

constr_relop
    : GTE
    | ASSIGN
    | LTE
    ;

constraint
    : SUBJ_TO? NAME alias? indexing? ':' e1=expr constr_relop e2=expr ';' 
        -> ^(NAME alias? indexing? constr_relop $e1 $e2)
    ;
    
/*
 * End constraint
 */

/*
 * sets
 */
set_decl
    : 'set' NAME alias? indexing? set_attributes? ';'
        -> ^(SET_DECL NAME alias? indexing? set_attributes?)
    ;

set_attributes
    : set_attribute (','? set_attribute)* -> set_attribute+
    ;

set_attribute:
      DIMEN INT -> ^(DIMEN INT)
    | WITHIN sexpr -> ^(WITHIN sexpr)
    | ASSIGN sexpr -> ^(ASSIGN sexpr)
    | DEFAULT sexpr -> ^(DEFAULT sexpr)
    | SET_ORDERED
    | SET_CIRCULAR
    ;

/*
 * end sets
 */

/*
 * parameters
 */
param_decl:
        'param' NAME alias? indexing? param_attributes? ';' -> ^(PARAM_DECL NAME alias? indexing? param_attributes?)
    ;

param_attributes:
        param_attribute (','? param_attribute)* -> param_attribute+
    ;

param_attribute
    : 'binary'
    | 'integer' -> INTEGER
    | 'symbolic'
//    | RELOP expr
    | 'in' sexpr
    | ASSIGN expr -> ^(ASSIGN expr)
    | 'default' expr -> ^(DEFAULT_ASSIGN expr)
    | DEFAULT_ASSIGN expr -> ^(DEFAULT_ASSIGN expr) 
    ;
/*
 * end parameters
 */

/*
 * variables
 */
var_decl:
        'var' NAME alias? indexing? var_attributes? ';' -> ^(VAR_DECL NAME alias? indexing? var_attributes?)
    ;

var_attributes:
        var_attribute (','? var_attribute)* -> var_attribute+
    ;

var_attribute
    : 'binary' -> BINARY
    | 'integer' -> INTEGER
    | 'symbolic' -> SYMBOLIC
    | GTE expr -> ^(GTE expr)
    | LTE expr -> ^(LTE expr)
    | DEFAULT_ASSIGN expr -> ^(DEFAULT_ASSIGN expr) //default (initial value)
    | 'default' expr -> ^(DEFAULT_ASSIGN expr)
    | ASSIGN expr -> ^(ASSIGN expr)
    | 'in' sexpr
    ;

/*
 * end variables
 */

/*
 * expressions
 */
expr
    : mult_expr ((PLUS^|MINUS^) mult_expr)*
    | ((PLUS^|MINUS^) expr)
    ;

mult_expr: pow_expr ((MULT^|DIV_SLASH^|MOD^|DIV^) pow_expr)* ;

pow_expr: atom_expr (POW^ atom_expr)* ;

builtin_function
    : ABS '(' expr ')' -> ^(ABS expr)
    | ACOS '(' expr ')' -> ^(ACOS expr)
    | ACOSH '(' expr ')' -> ^(ACOSH expr)
    | ASIN '(' expr ')' -> ^(ASIN expr)
    | ASINH '(' expr ')' -> ^(ASINH expr)
    | ATAN '(' expr ')' -> ^(ATAN expr)
    | ATAN2 '(' expr ',' expr ')' -> ^(ATAN2 expr expr)
    | ATANH '(' expr ')' -> ^(ATANH expr)
    | CEIL '(' expr ')' -> ^(CEIL expr)
    | CTIME '(' expr? ')' -> ^(CTIME expr?)
    | COS '(' expr ')' -> ^(COS expr)
    | EXP '(' expr ')' -> ^(EXP expr)
    | FLOOR '(' expr ')' -> ^(FLOOR expr)
    | LOG '(' expr ')' -> ^(LOG expr)
    | LOG10 '(' expr ')' -> ^(LOG10 expr)
    | MAX '(' expr (',' expr)+ ')' -> ^(MAX expr expr+)
    | MIN '(' expr (',' expr)+ ')' -> ^(MIN expr expr+)
    | PRECISION '(' expr (',' expr)? ')' -> ^(PRECISION expr expr?)
    | ROUND '(' expr (',' expr)? ')' -> ^(ROUND expr expr?)
    | SIN '(' expr ')' -> ^(SIN expr)
    | SINH '(' expr ')' -> ^(SINH expr)
    | SQRT '(' expr ')' -> ^(SQRT expr)
    | TAN '(' expr ')' -> ^(TAN expr)
    | TANH '(' expr ')' -> ^(TANH expr)
    | TIME '(' ')' -> TIME
    | TRUNC '(' expr (',' expr)? ')' -> ^(TRUNC expr expr?)
    ;

atom_expr
    : number
    | builtin_function
    | '(' expr ')' -> expr
    | NAME
    ;

/*
 * end expressions
 */

alias:
        NAME
    ;

indexing
    : '{' sexpr_list ( ':' lexpr )? '}'
    ;

sexpr_list:
      member 'in' sexpr
    | sexpr (',' sexpr)*
    ;

sexpr: uds_sexpr ((UNION^|DIFF^|SYMDIFF^) uds_sexpr)* ;

uds_sexpr : inter_sexpr (INTER^ inter_sexpr)*;

inter_sexpr : set_ctor (CROSS set_ctor)*;

set_ctor
options { backtrack = true; }
    : expr '..' expr ('by' expr)? -> ^(SET_RANGE expr expr expr?)
    | simple_sexpr
    ;

simple_sexpr
    : '{' (member (',' member)* )? '}' -> ^(MEMBER_ENUM member*)
//    | (OP_NAME indexing sexpr)
    | '(' sexpr ')' -> sexpr
    | NAME
    ;

lexpr
    : expr (COMPARE_OP expr)?
//    | lexpr LOGIC_OP lexpr
//    | '(' lexpr ')'
    ;

member: STRING|number|NAME;

number: (INT | FLOAT);
