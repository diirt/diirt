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
    public void add1() {
        FunctionTester.findByName(set, "+")
                .compareReturnValue(3.0, 1.0, 2.0)
                .compareReturnValue(-1.0, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void subtract1() {
        FunctionTester.findBySignature(set, "-", VNumber.class, VNumber.class)
                .compareReturnValue(-1.0, 1.0, 2.0)
                .compareReturnValue(3.0, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void negate1() {
        FunctionTester.findBySignature(set, "-", VNumber.class)
                .compareReturnValue(-1.0, 1.0)
                .compareReturnValue(2.0, -2.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void multiply1() {
        FunctionTester.findByName(set, "*")
                .compareReturnValue(10.0, 2.0, 5.0)
                .compareReturnValue(-6.0, 3.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void divide1() {
        FunctionTester.findByName(set, "/")
                .compareReturnValue(4.0, 8.0, 2.0)
                .compareReturnValue(-0.5, 1.0, -2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void remainder1() {
        FunctionTester.findByName(set, "%")
                .compareReturnValue(0.0, 8.0, 2.0)
                .compareReturnValue(1.0, 3.0, 2.0)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void pow1() {
        FunctionTester.findByName(set, "^")
                .compareReturnValue(64.0, 8.0, 2.0)
                .compareReturnValue(2.0, 4.0, 0.5)
                .compareReturnValue(null, newVDouble(1.0), null)
                .compareReturnValue(null, null, newVDouble(1.0))
                .highestAlarmReturned()
                .latestTimeReturned();
    }
}
