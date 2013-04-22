/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.Collection;
import org.epics.vtype.VNumber;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class BaseTestForFormula {
        
    @BeforeClass
    public static void initMath() {
        if (FormulaRegistry.getDefault().findFunctionSet("math") == null) {
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new MathFunctionSet());
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new NumberOperatorFunctionSet());
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new ArrayFunctionSet());
        }
    }
    
    public static void testTwoArgNumericFunction(FormulaFunctionSet set, String name, double arg1, double arg2, double result) {
        FormulaFunction function = null;
        for (FormulaFunction formulaFunction : set.findFunctions(name)) {
            if (formulaFunction.getArgumentTypes().size() == 2) {
                function = formulaFunction;
            }
        }
        assertThat("Function '" + name + "' not found.", function, not(nullValue()));
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(arg1), newVDouble(arg2)));
        assertThat("Wrong result for function '" + name + "(" + arg1 + ", " + arg2 + ")'.", value.getValue().doubleValue(), closeTo(result, 0.0001));
    }
    
    public static void testFunction(FormulaFunctionSet set, String name, double arg, double result) {
        FormulaFunction function = null;
        for (FormulaFunction formulaFunction : set.findFunctions(name)) {
            if (formulaFunction.getArgumentTypes().size() == 1) {
                function = formulaFunction;
            }
        }
        assertThat("Function '" + name + "' not found.", function, not(nullValue()));
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(arg)));
        assertThat("Wrong result for function '" + name + "(" + arg + ")'.", value.getValue().doubleValue(), closeTo(result, 0.0001));
    }
}
