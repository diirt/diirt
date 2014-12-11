/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.Arrays;
import java.util.List;
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
        return new FormulaAst(Type.ID, children, opName);
    }
    
}
