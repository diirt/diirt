grammar Formula;

options {
    language = Java;
}

@header {
  package org.diirt.datasource.formula;
  import org.diirt.datasource.expression.*;
  import static org.diirt.datasource.ExpressionLanguage.*;
  import static org.diirt.util.text.StringUtil.*;
  import static org.diirt.datasource.vtype.ExpressionLanguage.*;
  import static org.diirt.datasource.formula.ExpressionLanguage.*;
}

@lexer::header {
  package org.diirt.datasource.formula;
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
    :   conditionalExpression {result = $conditionalExpression.result;}
    ;

conditionalExpression returns [DesiredRateExpression<?> result]
    :   op1=conditionalOrExpression {result = $op1.result;}
        (   '?' op2=expression ':' op3=conditionalExpression {result = threeArgOp("?:", $result, $op2.result, $op3.result);}
        )?
    ;

conditionalOrExpression returns [DesiredRateExpression<?> result]
    :   op1=conditionalAndExpression {result = $op1.result;}
        (   '||' op2=conditionalAndExpression {result = twoArgOp("||", $result, $op2.result);}
        )*
    ;

conditionalAndExpression returns [DesiredRateExpression<?> result]
    :   op1=inclusiveOrExpression {result = $op1.result;}
        (   '&&' op2=inclusiveOrExpression {result = twoArgOp("&&", $result, $op2.result);}
        )*
    ;

inclusiveOrExpression returns [DesiredRateExpression<?> result]
    :   op1=andExpression {result = $op1.result;}
        (   '|' op2=andExpression {result = twoArgOp("|", $result, $op2.result);}
        )*
    ;

andExpression returns [DesiredRateExpression<?> result]
    :   op1=equalityExpression {result = $op1.result;}
        (   '&' op2=equalityExpression {result = twoArgOp("&", $result, $op2.result);}
        )*
    ;

equalityExpression returns [DesiredRateExpression<?> result]
    :   op1=relationalExpression {result = $op1.result;}
        (   '==' op2=relationalExpression {result = twoArgOp("==", $result, $op2.result);}
        |   '!=' op2=relationalExpression {result = twoArgOp("!=", $result, $op2.result);}
        )*
    ;

relationalExpression returns [DesiredRateExpression<?> result]
    :   op1=additiveExpression {result = $op1.result;}
        (   '<' '=' op2=additiveExpression {result = twoArgOp("<=", $result, $op2.result);}
        |   '>' '=' op2=additiveExpression {result = twoArgOp(">=", $result, $op2.result);}
        |   '<' op2=additiveExpression {result = twoArgOp("<", $result, $op2.result);}
        |   '>' op2=additiveExpression {result = twoArgOp(">", $result, $op2.result);}
        )*
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
        (   '^' op2=unaryExpression {result = twoArgOp("^", $result, $op2.result);}
        |   '**' op2=unaryExpression {result = twoArgOp("^", $result, $op2.result);}
        )*
    ;

unaryExpression returns [DesiredRateExpression<?> result]
    :   '-' op=unaryExpression {result = oneArgOp("-", $op.result);}
    |   op=unaryExpressionNotPlusMinus {result = $op.result;}
    ;

unaryExpressionNotPlusMinus returns [DesiredRateExpression<?> result]
    :   '!' op=unaryExpression {result = oneArgOp("!", $op.result);}
    |   op=primary {result = $op.result;}
    ;

primary returns [DesiredRateExpression<?> result]
    :   functionExpression {result = $functionExpression.result;}
    |   parExpression {result = $parExpression.result;}
    |   pv {result = $pv.result;}
    |   numericLiteral {result = $numericLiteral.result;}
    |   stringLiteral {result = $stringLiteral.result;}
    |   constant {result = $constant.result;}
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

constant returns [DesiredRateExpression<?> result]
    :	FUNCTION {result = namedConstant($FUNCTION.text);}
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
