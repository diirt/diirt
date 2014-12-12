/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.Arrays;
import java.util.List;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionList;
import org.diirt.datasource.expression.DesiredRateExpressionListImpl;
import org.diirt.util.text.StringUtil;

/**
 *
 * @author carcassi
 */
public class FormulaAst {
    public enum Type {OP, STRING, INTEGER, FLOATING_POINT, CHANNEL, ID};
    
    private final Type type;
    private final List<FormulaAst> children;
    private final Object value;

    private FormulaAst(Type type, List<FormulaAst> children, Object value) {
        this.type = type;
        this.children = children;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getToken() {
        return value;
    }

    public List<FormulaAst> getChildren() {
        return children;
    }
    
    public static FormulaAst stringFromToken(String token) {
        return string(StringUtil.unquote(token));
    }
    
    public static FormulaAst string(String unquotedString) {
        return new FormulaAst(Type.STRING, null, unquotedString);
    }
    
    public static FormulaAst integerFromToken(String token) {
        return integer(Integer.parseInt(token));
    }
    
    public static FormulaAst integer(int integer) {
        return new FormulaAst(Type.INTEGER, null, integer);
    }
    
    public static FormulaAst floatingPointFromToken(String token) {
        return floatingPoint(Double.parseDouble(token));
    }
    
    public static FormulaAst floatingPoint(double floatingPoint) {
        return new FormulaAst(Type.FLOATING_POINT, null, floatingPoint);
    }
    
    public static FormulaAst channelFromToken(String token) {
        return channel(StringUtil.unquote(token));
    }
    
    public static FormulaAst channel(String channelName) {
        return new FormulaAst(Type.CHANNEL, null, channelName);
    }
    
    public static FormulaAst id(String id) {
        return new FormulaAst(Type.ID, null, id);
    }
    
    public static FormulaAst op(String opName, FormulaAst... children) {
        return op(opName, Arrays.asList(children));
    }
    
    public static FormulaAst op(String opName, List<FormulaAst> children) {
        return new FormulaAst(Type.OP, children, opName);
    }
    
    static FormulaParser createParser(String text) {
        CharStream stream = new ANTLRStringStream(text);
        FormulaLexer lexer = new FormulaLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new FormulaParser(tokenStream);
    }
    
    public static FormulaAst formula(String formula) {
        FormulaAst ast = staticChannel(formula);
        if (ast != null) {
            return ast;
        }
        formula = formula.substring(1);
        
        try {
            ast = createParser(formula).formula();
            if (ast == null) {
                throw new IllegalArgumentException("Parsing failed");
            }
            return ast;
        } catch (RecognitionException ex) {
            throw new IllegalArgumentException("Error parsing formula: " + ex.getMessage(), ex);
        }
    }
    
    private static FormulaAst staticChannel(String formula) {
        if (formula.startsWith("=")) {
            return null;
        }
        
        if (formula.trim().matches(StringUtil.SINGLEQUOTED_STRING_REGEX)) {
            return channel(formula.trim());
        }
        return channel(formula);
    }
    
    public static FormulaAst singleChannel(String formula) {
        FormulaAst ast = staticChannel(formula);
        if (ast != null) {
            return ast;
        }
        formula = formula.substring(1);
        
        try {
            ast = createParser(formula).singleChannel();
            return ast;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public DesiredRateExpression<?> toExpression() {
        switch(getType()) {
            case CHANNEL:
                return new LastOfChannelExpression<>((String) getToken(), Object.class);
            case FLOATING_POINT:
                return org.diirt.datasource.vtype.ExpressionLanguage.vConst((Double) getToken());
            case INTEGER:
                return org.diirt.datasource.vtype.ExpressionLanguage.vConst((Integer) getToken());
            case STRING:
                return org.diirt.datasource.vtype.ExpressionLanguage.vConst((String) getToken());
            case ID:
                return ExpressionLanguage.namedConstant((String) getToken());
            case OP:
                DesiredRateExpressionList<Object> expressions = new DesiredRateExpressionListImpl<>();
                for (FormulaAst child : getChildren()) {
                    expressions.and(child.toExpression());
                }
                return ExpressionLanguage.function((String) getToken(), expressions);
            default:
                throw new IllegalArgumentException("Unsupported type " + getType() + " for ast");
        }
    }
    
}
