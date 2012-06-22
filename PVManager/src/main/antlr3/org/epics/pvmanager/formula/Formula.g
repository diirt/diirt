grammar Formula;

options {
    language = Java;
}

@header {
  package org.epics.pvmanager.formula;
  import org.epics.pvmanager.expression.*;
  import static org.epics.pvmanager.ExpressionLanguage.*;
  import static org.epics.pvmanager.data.ExpressionLanguage.*;
  import static org.epics.pvmanager.formula.ExpressionLanguage.*;
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
    :   op1=multiplicativeExpression {result = $op1.result;}
        (   '+' op2=multiplicativeExpression {result = addCast($op1.result, $op2.result);}
        |   '-' op2=multiplicativeExpression
        )*
    ;

multiplicativeExpression returns [DesiredRateExpression<?> result]
    :   op1=primary {result = $op1.result;}
        (   '*' op2=primary {result = multiplyCast($op1.result, $op2.result);}
        |   '/' op2=primary {result = divideCast($op1.result, $op2.result);}
        |   '%' op2=primary {result = reminderCast($op1.result, $op2.result);}
        )*
    ;
    
primary returns [DesiredRateExpression<?> result]
    :   parExpression {result = $parExpression.result;}
    |   pv {result = $pv.result;}
    |   numericLiteral {result = $numericLiteral.result;}
    |   stringLiteral {result = $stringLiteral.result;}
    ;

parExpression returns [DesiredRateExpression<?> result]
    :   '(' expression ')'
    ;

pv returns [DesiredRateExpression<?> result]
    :   ID {result = cachedPv($ID.text);}
    |   QUOTED_ID {result = cachedPv(($QUOTED_ID.text).substring(1,($QUOTED_ID.text).length() - 1));}
    ;

numericLiteral returns [DesiredRateExpression<?> result]
    :   INT {result = vConst(Integer.parseInt($INT.text));}
    |   FLOAT {result = vConst(Double.parseDouble($FLOAT.text));}
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
