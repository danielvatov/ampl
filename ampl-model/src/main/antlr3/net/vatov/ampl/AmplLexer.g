lexer grammar AmplLexer;

OBJ_ENTITY
    :'minimize'
    |'maximize'
    ;

SUBJ_TO: 'subject to' ;

DIF: '<>';
EQ : '==';
NEQ: '!=';
GTE : '>=';
LTE : '<=';
ASSIGN: '=';
GT : '>';
LT : '<';
DEFAULT_ASSIGN : ':=';

UNION : 'union';
DIFF : 'diff';
SYMDIFF : 'symdiff';
INTER : 'inter';
CROSS : 'cross';

SET_ORDERED : 'ordered';
SET_CIRCULAR : 'circular';
/*
RELOP
    : CONSTR_RELOP
    | LT
    | EQ
    | GTE
    | LTE
    | NEQ
    | DIF
    | GT
    ;
*/
  
fragment COMPARE_OP
    : LT
    | EQ
    | GTE
    | LTE
    | NEQ
    | GT
    ;

        
fragment OR: 'or' | '||';

fragment AND: 'and' | '&&';

LOGIC_OP
    : OR
    | AND
    ;

PLUS: '+';
MINUS: '-';
MULT: '*';
DIV_SLASH: '/';
MOD: 'mod';
DIV: 'div';
POW: '^';

ABS: 'abs';
ACOS: 'acos';
ACOSH: 'acosh';
ASIN: 'asin';
ASINH: 'asinh';
ATAN: 'atan';
ATAN2: 'atan2';
ATANH: 'atanh';
CEIL: 'ceil';
CTIME: 'ctime';
COS: 'cos';
EXP: 'exp';
FLOOR: 'floor';
LOG: 'log';
LOG10: 'log10';
MAX: 'max';
MIN: 'min';
PRECISION: 'precision';
ROUND: 'round';
SIN: 'sin';
SINH: 'sinh';
SQRT: 'sqrt';
TAN: 'tan';
TANH: 'tanh';
TIME: 'time';
TRUNC: 'trunc';

DIMEN: 'dimen';
WITHIN: 'within';
DEFAULT: 'default';

NAME: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|DEC_DIGIT|'_')* ;

fragment
DEC_DIGIT: '0'..'9'	;

INT: DEC_DIGIT+ ;


FLOAT
    : INT '.' INT? EXPONENT?
    | '.' INT EXPONENT?
    | INT EXPONENT
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

STRING:
        '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
ESC_SEQ:
        '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\') |
        UNICODE_ESC |
        OCTAL_ESC
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
EXPONENT:
        ('e'|'E') (PLUS|MINUS)? INT
    ;

fragment
HEX_DIGIT:
        ('0'..'9'|'a'..'f'|'A'..'F')
    ;

fragment
OCTAL_ESC:
        '\\' ('0'..'3') ('0'..'7') ('0'..'7') |
        '\\' ('0'..'7') ('0'..'7') |
        '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC:
        '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

WS:
        ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;