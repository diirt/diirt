grammar Formula;

options {
    language = Java;
}

@header {
  package org.epics.pvmanager.formula;
  import org.epics.pvmanager.expression.*;
  import static org.epics.pvmanager.formula.ExpressionLanguage.*;
  import static org.epics.pvmanager.ExpressionLanguage.*;
}

@lexer::header {
  package org.epics.pvmanager.formula;
}

formula returns [DesiredRateExpression<?> result]
    :   expression EOF
    ;

expression returns [DesiredRateExpression<?> result]
    :   additiveExpression;

additiveExpression returns [DesiredRateExpression<?> result]
    :   multiplicativeExpression
        (   '+' multiplicativeExpression
        |   '-' multiplicativeExpression
        )*
    ;

multiplicativeExpression returns [DesiredRateExpression<?> result]
    :   primary
        (   '*' primary
        |   '/' primary
        |   '%' primary
        )*
    ;
    
primary returns [DesiredRateExpression<?> result]
    :   parExpression
    |   pv 
    |   numericLiteral
    |   stringLiteral
    ;

parExpression returns [DesiredRateExpression<?> result]
    :   '(' expression ')'
    ;

pv returns [DesiredRateExpression<?> result]
    :   ID {result = cachedPv($ID.text);}
    |   QUOTED_ID {result = cachedPv(($QUOTED_ID.text).substring(1,($QUOTED_ID.text).length() - 1));}
    ;

numericLiteral returns [DesiredRateExpression<?> result]
    :   INT
    |   FLOAT
    ;

stringLiteral returns [DesiredRateExpression<?> result]
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
