/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class MathFunctionSetTest extends BaseTestForFormula {

    private static MathFunctionSet set = new MathFunctionSet();

    @Test
    public void abs1() {
        FunctionTester.findByName(set, "abs")
                .compareReturnValue(1.0, 1.0)
                .compareReturnValue(2.0, -2.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void acos1() {
        FunctionTester.findByName(set, "acos")
                .compareReturnValue(3.1415/2.0, 0.0)
                .compareReturnValue(0.0, 1.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void asin1() {
        FunctionTester.findByName(set, "asin")
                .compareReturnValue(3.1415/2.0, 1.0)
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void atan1() {
        FunctionTester.findByName(set, "atan")
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void cbrt1() {
        FunctionTester.findByName(set, "cbrt")
                .compareReturnValue(1.0, 1.0)
                .compareReturnValue(2.0, 8.0)
                .compareReturnValue(3.0, 27.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void ceil1() {
        FunctionTester.findByName(set, "ceil")
                .compareReturnValue(1.0, 1.0)
                .compareReturnValue(2.0, 1.5)
                .compareReturnValue(3.0, 2.9)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void cos1() {
        FunctionTester.findByName(set, "cos")
                .compareReturnValue(1.0, 0.0)
                .compareReturnValue(0.0, Math.PI / 2)
                .compareReturnValue(-1.0, Math.PI)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void cosh1() {
        FunctionTester.findByName(set, "cosh")
                .compareReturnValue(1.0, 0.0)
                .compareReturnValue(1.54308, 1.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void exp1() {
        FunctionTester.findByName(set, "exp")
                .compareReturnValue(1.0, 0.0)
                .compareReturnValue(Math.E, 1.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void floor1() {
        FunctionTester.findByName(set, "floor")
                .compareReturnValue(1.0, 1.0)
                .compareReturnValue(1.0, 1.5)
                .compareReturnValue(2.0, 2.1)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
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
