/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.epics.pvmanager.ReadFunction;
import org.epics.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
class FormulaReadFunction implements ReadFunction<Object> {
    
    public final List<ReadFunction<?>> argumentFunctions;
    public final Collection<FormulaFunction> formulaMatches;
    public final List<Object> argumentValues;
    public final String functionName;
    public FormulaFunction lastFormula;

    FormulaReadFunction(List<ReadFunction<?>> argumentFunctions, Collection<FormulaFunction> formulaMatches) {
        this.argumentFunctions = argumentFunctions;
        this.formulaMatches = formulaMatches;
        this.argumentValues = new ArrayList<>(argumentFunctions.size());
        for (ReadFunction<?> argumentFunction : argumentFunctions) {
            argumentValues.add(null);
        }
        this.functionName = formulaMatches.iterator().next().getName();
    }

    @Override
    public Object readValue() {
        for (int i = 0; i < argumentFunctions.size(); i++) {
            argumentValues.set(i, argumentFunctions.get(i).readValue());
        }
        
        if (lastFormula == null || !matchArgumentTypes(argumentValues, lastFormula.getArgumentTypes())) {
            lastFormula = findMatch(argumentValues, formulaMatches);
        }
        
        if (lastFormula == null) {
            List<String> typeNames = new ArrayList<>(argumentValues.size());
            for (Object object : argumentValues) {
                Class<?> clazz = ValueUtil.typeOf(object);
                if (clazz == null || Object.class.equals(clazz)) {
                    clazz = object.getClass();
                }
                typeNames.add(clazz.getSimpleName());
            }
            throw new RuntimeException("Can't find match for function '" + functionName + "'  and arguments " + typeNames);
        }
        
        return lastFormula.calculate(argumentValues);
    }
    
    static boolean matchArgumentTypes(List<Object> values, List<Class<?>> types) {
        for (int i = 0; i < values.size(); i++) {
            if (!types.get(i).isInstance(values.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    static FormulaFunction findMatch(List<Object> values, Collection<FormulaFunction> formulaFunctions) {
        for (FormulaFunction formulaFunction : formulaFunctions) {
            if (matchArgumentTypes(values, formulaFunction.getArgumentTypes())) {
                return formulaFunction;
            }
        }
        
        return null;
    }
    
}
