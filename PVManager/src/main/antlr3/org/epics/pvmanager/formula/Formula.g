grammar Formula;

options {
    language = Java;
}

@header {
  package org.epics.pvmanager.formula;
}

@lexer::header {
  package org.epics.pvmanager.formula;
}

formula
    :   expression EOF
    ;

expression
    :   additiveExpression;

additiveExpression 
    :   multiplicativeExpression
        (   '+' multiplicativeExpression
        |   '-' multiplicativeExpression
        )*
    ;

multiplicativeExpression 
    :   primary
        (   '*' primary
        |   '/' primary
        |   '%' primary
        )*
    ;
    
primary
    :   parExpression
    |   pv 
    |   numericLiteral
    |   stringLiteral
    ;

parExpression 
    :   '(' expression ')'
    ;

pv  :   ID
    |   QUOTED_ID
    ;

numericLiteral
    :   INT
    |   FLOAT
    ;

stringLiteral
    :	STRING
    ;


ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

QUOTED_ID
    :  '\'' ( ESC_SEQ | ~('\\'|'"') )* '\''
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
