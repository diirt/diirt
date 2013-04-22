/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.pvmanager.formula.LastOfChannelExpression;
import org.epics.vtype.VNumber;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class NumberOperatorFunctionSetTest {

    private static FormulaFunctionSet set = new NumberOperatorFunctionSet();
    
    public static void testTwoArgNumericFunction(FormulaFunctionSet set, String name, double arg1, double arg2, double result) {
        Collection<FormulaFunction> functions = set.findFunctions(name);
        assertThat("Function '" + name + "' not found.", functions.size(), equalTo(1));
        FormulaFunction function = functions.iterator().next();
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(arg1), newVDouble(arg2)));
        assertThat("Wrong result for function '" + name + "(" + arg1 + ", " + arg2 + ")'.", value.getValue().doubleValue(), closeTo(result, 0.0001));
    }
    
    @Test
    public void add1() {
        testTwoArgNumericFunction(set, "+", 1.0, 2.0, 3.0);
        testTwoArgNumericFunction(set, "+", 1.0, -2.0, -1.0);
    }
    
    @Test
    public void subtract1() {
        testTwoArgNumericFunction(set, "-", 1.0, 2.0, -1.0);
        testTwoArgNumericFunction(set, "-", 1.0, -2.0, 3.0);
    }
}
