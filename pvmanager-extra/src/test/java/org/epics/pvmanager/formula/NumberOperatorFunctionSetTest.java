/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import org.epics.vtype.VNumber;
import org.junit.Test;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class NumberOperatorFunctionSetTest extends BaseTestForFormula {

    private static FormulaFunctionSet set = new NumberOperatorFunctionSet();
    
    @Test
    public void sum() {
        FunctionTester.findByName(set, "+")
                .compareReturnValue(3.0, 1.0, 2.0)
                .compareReturnValue(-1.0, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void subtract() {
        FunctionTester.findBySignature(set, "-", VNumber.class, VNumber.class)
                .compareReturnValue(-1.0, 1.0, 2.0)
                .compareReturnValue(3.0, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void negate() {
        FunctionTester.findBySignature(set, "-", VNumber.class)
                .compareReturnValue(-1.0, 1.0)
                .compareReturnValue(2.0, -2.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void multiply() {
        FunctionTester.findByName(set, "*")
                .compareReturnValue(10.0, 2.0, 5.0)
                .compareReturnValue(-6.0, 3.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void divide() {
        FunctionTester.findByName(set, "/")
                .compareReturnValue(4.0, 8.0, 2.0)
                .compareReturnValue(-0.5, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void remainder() {
        FunctionTester.findByName(set, "%")
                .compareReturnValue(0.0, 8.0, 2.0)
                .compareReturnValue(1.0, 3.0, 2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void pow() {
        FunctionTester.findByName(set, "^")
                .compareReturnValue(64.0, 8.0, 2.0)
                .compareReturnValue(2.0, 4.0, 0.5)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void lessThanOrEqual() {
        FunctionTester.findByName(set, "<=")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void greaterThanOrEqual() {
        FunctionTester.findByName(set, ">=")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void lessThan() {
        FunctionTester.findByName(set, "<")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void greaterThan() {
        FunctionTester.findByName(set, ">")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void equal() {
        FunctionTester.findByName(set, "==")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void notEqual() {
        FunctionTester.findByName(set, "!=")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void conditionalOr() {
        FunctionTester.findByName(set, "||")
                .compareReturnValue(true, true, true)
                .compareReturnValue(true, true, false)
                .compareReturnValue(true, false, true)
                .compareReturnValue(false, false, false)
                .compareReturnValue(null, true, null)
                .compareReturnValue(null, null, true)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void conditionalAnd() {
        FunctionTester.findByName(set, "&&")
                .compareReturnValue(true, true, true)
                .compareReturnValue(false, true, false)
                .compareReturnValue(false, false, true)
                .compareReturnValue(false, false, false)
                .compareReturnValue(null, true, null)
                .compareReturnValue(null, null, true)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
}
