/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.VEnum;

import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.junit.Test;

import static org.epics.vtype.ValueFactory.*;


/**
 * @author shroffk
 * 
 */
public class StringFunctionSetTest extends BaseTestForFormula {

    private FormulaFunctionSet set = new StringFunctionSet();

    @Test
    public void concatStringArray() {
        FunctionTester.findBySignature(set, "concat", VStringArray.class)
                .compareReturnValue("xyz", (Object) new String[] {"x", "y", "z"})
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void concatStrings() {
        FunctionTester.findBySignature(set, "concat", VString.class)
                .compareReturnValue("xyz", "x", "y", "z")
                .compareReturnValue(null, "a", null)
                .compareReturnValue(null, null, "b")
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void highestSeverity() {
	VString dataA = newVString("a", alarmNone(), timeNow());
	VString dataB = newVString("b", alarmNone(), timeNow());
	VString dataC = newVString("c", alarmNone(), timeNow());
	VString dataD = newVString("d", newAlarm(AlarmSeverity.MAJOR, "Help!"), timeNow());

        VEnum expected1 = newVEnum(0, AlarmSeverity.labels(), alarmNone(), timeNow());
        VEnum expected2 = newVEnum(2, AlarmSeverity.labels(), alarmNone(), timeNow());
        
        FunctionTester.findByName(set, "highestSeverity")
                .compareReturnValue(expected1, dataA, dataB, dataC)
                .compareReturnValue(expected2, dataB, dataC, dataD);
    }
}
