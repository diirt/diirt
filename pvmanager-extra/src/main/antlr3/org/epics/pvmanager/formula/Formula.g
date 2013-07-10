grammar Formula;

options {
    language = Java;
}

@header {
  package org.epics.pvmanager.formula;
  import org.epics.pvmanager.expression.*;
  import static org.epics.pvmanager.ExpressionLanguage.*;
  import static org.epics.util.text.StringUtil.*;
  import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
  import static org.epics.pvmanager.formula.ExpressionLanguage.*;
}

@lexer::header {
  package org.epics.pvmanager.formula;
}

@members {
  @Override
public void reportError(RecognitionException e) {
    throw new RuntimeException(e);
}
}

singlePv returns [DesiredRateExpression<?> result]
    :   pv EOF {result = $pv.result;}
    ;

formula returns [DesiredRateExpression<?> result]
    :   expression EOF {result = $expression.result;}
    ;

expression returns [DesiredRateExpression<?> result]
    :   additiveExpression {result = $additiveExpression.result;}
    ;

additiveExpression returns [DesiredRateExpression<?> result]
    :   op1=multiplicativeExpression {result = $op1.result;}
        (   '+' op2=multiplicativeExpression {result = twoArgOp("+", $result, $op2.result);}
        |   '-' op2=multiplicativeExpression {result = twoArgOp("-", $result, $op2.result);}
        )*
    ;

multiplicativeExpression returns [DesiredRateExpression<?> result]
    :   op1=exponentialExpression {result = $op1.result;}
        (   '*' op2=exponentialExpression {result = twoArgOp("*", $result, $op2.result);}
        |   '/' op2=exponentialExpression {result = twoArgOp("/", $result, $op2.result);}
        |   '%' op2=exponentialExpression {result = twoArgOp("\%", $result, $op2.result);}
        )*
    ;

exponentialExpression returns [DesiredRateExpression<?> result]
    :   op1=unaryExpression {result = $op1.result;}
        (   '^' op2=unaryExpression {result = powCast($result, $op2.result);}
        |   '**' op2=unaryExpression {result = powCast($result, $op2.result);}
        )*
    ;

unaryExpression returns [DesiredRateExpression<?> result]
    :   '-' op=unaryExpression {result = negateCast($op.result);}
    |   op=primary {result = $op.result;}
    ;

primary returns [DesiredRateExpression<?> result]
    :   functionExpression {result = $functionExpression.result;}
    |   parExpression {result = $parExpression.result;}
    |   pv {result = $pv.result;}
    |   numericLiteral {result = $numericLiteral.result;}
    |   stringLiteral {result = $stringLiteral.result;}
    ;

functionExpression returns [DesiredRateExpression<?> result]
    :   FUNCTION '(' op=expression {String name = $FUNCTION.text; DesiredRateExpressionList args = new DesiredRateExpressionListImpl().and($op.result);}
        (   ',' op2=expression {args.and($op2.result);}
        )* ')' {result = function(name, args);}
    ;

parExpression returns [DesiredRateExpression<?> result]
    :   '(' expression ')' {result = $expression.result;}
    ;

pv returns [DesiredRateExpression<?> result]
    :   PV {result = cachedPv(unquote($PV.text));}
    ;

numericLiteral returns [DesiredRateExpression<?> result]
    :   INT {result = vConst(Integer.parseInt($INT.text));}
    |   FLOAT {result = vConst(Double.parseDouble($FLOAT.text));}
    ;

stringLiteral returns [DesiredRateExpression<?> result]
    :	STRING {result = vConst(unquote($STRING.text));}
    ;


INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

FUNCTION  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9')*
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

PV
    :  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\''
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
