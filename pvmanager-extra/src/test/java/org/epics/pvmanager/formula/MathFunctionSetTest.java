/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class MathFunctionSetTest extends BaseTestForFormula {

    private static MathFunctionSet set = new MathFunctionSet();

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

    @Test
    public void sqrt1() {
        testFunction(set, "sqrt", 1.0, 1.0);
        testFunction(set, "sqrt", 9.0, 3.0);
    }

    @Test
    public void tan1() {
        testFunction(set, "tan", 0.0, 0.0);
        testFunction(set, "tan", Math.PI / 4, 1.0);
    }

    @Test
    public void tanh1() {
        testFunction(set, "tanh", 0.0, 0.0);
        testFunction(set, "tanh", Math.E, 0.991328);
    }

    @Test
    public void toRadians1() {
        testFunction(set, "toRadians", 0.0, 0.0);
        testFunction(set, "toRadians", 180, Math.PI);
    }

    @Test
    public void toDegrees1() {
        testFunction(set, "toDegrees", 0.0, 0.0);
        testFunction(set, "toDegrees", Math.PI, 180);
    }
}
