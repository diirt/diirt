/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.math;

import org.diirt.datasource.formula.FunctionTester;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class MathFunctionSetTest {

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
        FunctionTester.findByName(set, "log")
                .compareReturnValue(1.0, Math.E)
                .compareReturnValue(0.0, 1.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void log101() {
        FunctionTester.findByName(set, "log10")
                .compareReturnValue(1.0, 10.0)
                .compareReturnValue(0.0, 1.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void signum1() {
        FunctionTester.findByName(set, "signum")
                .compareReturnValue(1.0, 10.0)
                .compareReturnValue(-1.0, -14.0)
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void sin1() {
        FunctionTester.findByName(set, "sin")
                .compareReturnValue(1.0, Math.PI / 2)
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(-1.0, - Math.PI / 2)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void sinh1() {
        FunctionTester.findByName(set, "sinh")
                .compareReturnValue(1.17520, 1.0)
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void sqrt1() {
        FunctionTester.findByName(set, "sqrt")
                .compareReturnValue(1.0, 1.0)
                .compareReturnValue(3.0, 9.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void tan1() {
        FunctionTester.findByName(set, "tan")
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(1.0, Math.PI / 4)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void tanh1() {
        FunctionTester.findByName(set, "tanh")
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(0.991328, Math.E)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void toRadians1() {
        FunctionTester.findByName(set, "toRadians")
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(Math.PI, 180.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void toDegrees1() {
        FunctionTester.findByName(set, "toDegrees")
                .compareReturnValue(0.0, 0.0)
                .compareReturnValue(180.0, Math.PI)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
}
