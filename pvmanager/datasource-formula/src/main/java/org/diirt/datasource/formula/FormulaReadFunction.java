/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.diirt.datasource.PVDirector;
import org.diirt.datasource.ReadFunction;
import org.diirt.vtype.ValueUtil;

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
    public Object lastValue;
    public volatile PVDirector<?> director;

    FormulaReadFunction(List<ReadFunction<?>> argumentFunctions, Collection<FormulaFunction> formulaMatches, String functionName) {
        this.argumentFunctions = argumentFunctions;
        this.formulaMatches = formulaMatches;
        this.argumentValues = new ArrayList<>(argumentFunctions.size());
        for (ReadFunction<?> argumentFunction : argumentFunctions) {
            argumentValues.add(null);
        }
        this.functionName = functionName;
    }

    void setDirector(PVDirector<?> director) {
        this.director = director;
    }

   @Override
    public Object readValue() {
        if (formulaMatches.isEmpty()) {
            throw new RuntimeException("No function named '" + functionName + "'  is defined");
        }

        List<Object> previousValues = new ArrayList<>(argumentValues);
        for (int i = 0; i < argumentFunctions.size(); i++) {
            argumentValues.set(i, argumentFunctions.get(i).readValue());
        }
        if (previousValues.equals(argumentValues) && lastFormula != null && lastFormula.isPure()) {
            return lastValue;
        }

        if (lastFormula == null || !FormulaFunctions.matchArgumentTypes(argumentValues, lastFormula)) {
            if (lastFormula instanceof StatefulFormulaFunction) {
                ((StatefulFormulaFunction) lastFormula).dispose();
            }

            lastFormula = FormulaFunctions.findFirstMatch(argumentValues, formulaMatches);
            // If the function is stateful, create a new copy
            // The copy will be kept until the same match works:
            // is that the right behavior?
            if (lastFormula instanceof StatefulFormulaFunction) {
                lastFormula = FormulaFunctions.createInstance((StatefulFormulaFunction) lastFormula);
            }

            if (lastFormula instanceof DynamicFormulaFunction) {
                ((DynamicFormulaFunction) lastFormula).setDirector(director);
            }
        }

        if (lastFormula == null) {
            List<String> typeNames = new ArrayList<>(argumentValues.size());
            for (Object object : argumentValues) {
                Class<?> clazz = ValueUtil.typeOf(object);
                if (Object.class.equals(clazz)) {
                    clazz = object.getClass();
                }
                if (clazz != null) {
                    typeNames.add(clazz.getSimpleName());
                } else {
                    typeNames.add("null");
                }
            }
            throw new RuntimeException("Can't find match for function '" + functionName + "'  and arguments " + typeNames);
        }

        lastValue = lastFormula.calculate(argumentValues);
        return lastValue;
    }

}
