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
    public void atan1() {
        testFunction(set, "atan", 0.0, 0.0);
    }

    @Test
    public void cbrt1() {
        testFunction(set, "cbrt", 1.0, 1.0);
        testFunction(set, "cbrt", 8.0, 2.0);
        testFunction(set, "cbrt", 27.0, 3.0);
    }

    @Test
    public void ceil1() {
        testFunction(set, "ceil", 1.0, 1.0);
        testFunction(set, "ceil", 1.5, 2.0);
        testFunction(set, "ceil", 2.9, 3.0);
    }

    @Test
    public void cos1() {
        testFunction(set, "cos", 0.0, 1.0);
        testFunction(set, "cos", Math.PI / 2, 0.0);
        testFunction(set, "cos", Math.PI, -1.0);
    }

    @Test
    public void cosh1() {
        testFunction(set, "cosh", 0.0, 1.0);
        testFunction(set, "cosh", 1.0, 1.54308);
    }

    @Test
    public void exp1() {
        testFunction(set, "exp", 0.0, 1.0);
        testFunction(set, "exp", 1.0, Math.E);
    }

    @Test
    public void floor1() {
        testFunction(set, "floor", 1.0, 1.0);
        testFunction(set, "floor", 1.5, 1.0);
        testFunction(set, "floor", 2.1, 2.0);
    }

    @Test
    public void log1() {
        testFunction(set, "log", Math.E, 1.0);
        testFunction(set, "log", 1.0, 0.0);
    }

    @Test
    public void log101() {
        testFunction(set, "log10", 10.0, 1.0);
        testFunction(set, "log10", 1.0, 0.0);
    }

    @Test
    public void signum1() {
        testFunction(set, "signum", 10.0, 1.0);
        testFunction(set, "signum", -14.0, -1.0);
        testFunction(set, "signum", 0.0, 0.0);
    }

    @Test
    public void sin1() {
        testFunction(set, "sin", 0.0, 0.0);
        testFunction(set, "sin", Math.PI / 2 , 1.0);
        testFunction(set, "sin", -Math.PI / 2 , -1.0);
    }

    @Test
    public void sinh1() {
        testFunction(set, "sinh", 0.0, 0.0);
        testFunction(set, "sinh", 1.0, 1.17520);
    }
}
