/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.epics.pvmanager.expression.DesiredRateExpression;

/**
 *
 * @author carcassi
 */
public class FormulaFunctions {
    
    public static boolean matchArgumentTypes(List<Object> values, FormulaFunction formula) {
        List<Class<?>> types = formula.getArgumentTypes();
        
        if (!matchArgumentCount(values.size(), formula)) {
            return false;
        }
        
        for (int i = 0; i < values.size(); i++) {
            int j = Math.min(i, types.size() - 1);
            if (!types.get(j).isInstance(values.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean matchArgumentCount(int nArguments, FormulaFunction formula) {
        // no varargs must match
        if (!formula.isVarargs() && (formula.getArgumentTypes().size() != nArguments)) {
            return false;
        }
        
        // varargs can have 0 arguments
        if (formula.isVarargs() && ((formula.getArgumentTypes().size() - 1) > nArguments)) {
            return false;
        }
        
        return true;
    }
    
    public static FormulaFunction findFirstMatch(List<Object> values, Collection<FormulaFunction> formulaFunctions) {
        for (FormulaFunction formulaFunction : formulaFunctions) {
            if (matchArgumentTypes(values, formulaFunction)) {
                return formulaFunction;
            }
        }
        
        return null;
    }
    
    private static Pattern postFixTwoArg = Pattern.compile("\\+");
    
    public static String format(String function, List<String> args) {
        if (args.size() == 2 && postFixTwoArg.matcher(function).matches()) {
            return formatPostFixTwoArgs(function, args);
        }
        return formatFunction(function, args);
    }
    
    private static String formatPostFixTwoArgs(String function, List<String> args) {
        StringBuilder sb = new StringBuilder();
        sb.append("(")
          .append(args.get(0))
          .append(" ")
          .append(function)
          .append(" ")
          .append(args.get(1))
          .append(")");
        return sb.toString();
    }
    
    private static String formatFunction(String function, List<String> args) {
        StringBuilder sb = new StringBuilder();
        sb.append(function).append('(');
        boolean first = true;
        for (String arg : args) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(arg);
        }
        sb.append(')');
        return sb.toString();
    }
    
}
