/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunctionAlarm;
import static org.epics.pvmanager.formula.BaseTestForFormula.testTwoArgNumericFunction;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import static org.epics.vtype.ValueFactory.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class NumberOperatorFunctionSetTest extends BaseTestForFormula {

    private static FormulaFunctionSet set = new NumberOperatorFunctionSet();
    
    @Test
    public void add1() {
        FunctionTester.findByName(set, "+")
                .compareReturnValue(3.0, 1.0, 2.0)
                .compareReturnValue(-1.0, 1.0, -2.0)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void subtract1() {
        testTwoArgNumericFunction(set, "-", 1.0, 2.0, -1.0);
        testTwoArgNumericFunction(set, "-", 1.0, -2.0, 3.0);
        testTwoArgNumericFunctionHighestAlarm(set, "-");
        testTwoArgNumericFunctionLatestTime(set, "-");
    }
    
    @Test
    public void negate1() {
        testFunction(set, "-", 1.0, -1.0);
        testFunction(set, "-", -2.0, 2.0);
        testOneArgNumericFunctionHighestAlarm(set, "-");
    }
    
    @Test
    public void multiply1() {
        FunctionTester.findByName(set, "*")
                .compareReturnValue(10.0, 2.0, 5.0)
                .compareReturnValue(-6.0, 3.0, -2.0)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void divide1() {
        FunctionTester.findByName(set, "/")
                .compareReturnValue(4.0, 8.0, 2.0)
                .compareReturnValue(-0.5, 1.0, -2.0)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void remainder1() {
        FunctionTester.findByName(set, "%")
                .compareReturnValue(0.0, 8.0, 2.0)
                .compareReturnValue(1.0, 3.0, 2.0)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void pow1() {
        FunctionTester.findByName(set, "^")
                .compareReturnValue(64.0, 8.0, 2.0)
                .compareReturnValue(2.0, 4.0, 0.5)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
}
