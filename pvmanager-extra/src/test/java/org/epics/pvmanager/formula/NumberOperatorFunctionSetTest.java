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
    
    public static void testTwoArgNumericFunctionHighestAlarm(FormulaFunctionSet set, String functionName) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        testFunctionAlarm(set, functionName, alarmNone(), newVDouble(0.0, display), newVDouble(1.0, display));
        testFunctionAlarm(set, functionName, newAlarm(AlarmSeverity.MINOR, "HIGH"), newVDouble(1.0, display), newVDouble(3.5, display));
        testFunctionAlarm(set, functionName, newAlarm(AlarmSeverity.MAJOR, "LOLO"), newVDouble(-5.0, display), newVDouble(3.5, display));
    }
    
    @Test
    public void add1() {
        testTwoArgNumericFunction(set, "+", 1.0, 2.0, 3.0);
        testTwoArgNumericFunction(set, "+", 1.0, -2.0, -1.0);
        testTwoArgNumericFunctionHighestAlarm(set, "+");
    }
    
    @Test
    public void subtract1() {
        testTwoArgNumericFunction(set, "-", 1.0, 2.0, -1.0);
        testTwoArgNumericFunction(set, "-", 1.0, -2.0, 3.0);
        testTwoArgNumericFunctionHighestAlarm(set, "-");
    }
    
    @Test
    public void negate1() {
        testFunction(set, "-", 1.0, -1.0);
        testFunction(set, "-", -2.0, 2.0);
    }
    
    @Test
    public void multiply1() {
        testTwoArgNumericFunction(set, "*", 2.0, 5.0, 10.0);
        testTwoArgNumericFunction(set, "*", 3.0, -2.0, -6.0);
        testTwoArgNumericFunctionHighestAlarm(set, "*");
    }
    
    @Test
    public void divide1() {
        testTwoArgNumericFunction(set, "/", 8.0, 2.0, 4.0);
        testTwoArgNumericFunction(set, "/", 1.0, -2.0, -0.5);
        testTwoArgNumericFunctionHighestAlarm(set, "/");
    }
    
    @Test
    public void remainder1() {
        testTwoArgNumericFunction(set, "%", 8.0, 2.0, 0.0);
        testTwoArgNumericFunction(set, "%", 3.0, 2.0, 1.0);
        testTwoArgNumericFunctionHighestAlarm(set, "%");
    }
    
    @Test
    public void pow1() {
        testTwoArgNumericFunction(set, "^", 8.0, 2.0, 64.0);
        testTwoArgNumericFunction(set, "^", 4.0, 0.5, 2.0);
        testTwoArgNumericFunctionHighestAlarm(set, "^");
    }
}
