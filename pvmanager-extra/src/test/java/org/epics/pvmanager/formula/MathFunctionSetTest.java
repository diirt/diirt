/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class MathFunctionSetTest {

    private static MathFunctionSet set = new MathFunctionSet();
    
    public static void testFunction(FormulaFunctionSet set, String name, double arg, double result) {
        Collection<FormulaFunction> functions = set.findFunctions(name);
        assertThat("Function '" + name + "' not found.", functions.size(), equalTo(1));
        FormulaFunction function = functions.iterator().next();
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(arg)));
        assertThat("Wrong result for function '" + name + "(" + arg + ")'.", value.getValue().doubleValue(), closeTo(result, 0.0001));
    }

    @Test
    public void abs1() {
        testFunction(set, "abs", 1.0, 1.0);
        testFunction(set, "abs", -1.0, 1.0);
    }

    @Test
    public void acos1() {
        testFunction(set, "acos", 0.0, 3.1415/2.0);
        testFunction(set, "acos", 1.0, 0.0);
    }

    @Test
    public void asin1() {
        testFunction(set, "asin", 1.0, 3.1415/2.0);
        testFunction(set, "asin", 0.0, 0.0);
    }

    @Test
    public void log1() {
        testFunction(set, "log", Math.E, 1.0);
        testFunction(set, "log", 1.0, 0.0);
    }
}
