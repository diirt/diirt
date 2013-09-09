/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;
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
	VStringArray data = newVStringArray(Arrays.asList("x", "y", "z"),
		alarmNone(), timeNow());
	VString expected = newVString("xyz", alarmNone(), timeNow());
	testFunction(set, "concat", expected, data);
    }

    @Test
    public void concatStrings() {
	VString dataA = newVString("a", alarmNone(), timeNow());
	VString dataB = newVString("b", alarmNone(), timeNow());
	VString dataC = newVString("c", alarmNone(), timeNow());

	VString expected = newVString("abc", alarmNone(), timeNow());
	testFunction(set, "concat", expected, dataA, dataB, dataC);
    }

    @Test
    public void highestSeverity1() {
	VString dataA = newVString("a", alarmNone(), timeNow());
	VString dataB = newVString("b", alarmNone(), timeNow());
	VString dataC = newVString("c", alarmNone(), timeNow());

        VEnum expected = newVEnum(0, AlarmSeverity.labels(), alarmNone(), timeNow());
        testFunction(set, "highestSeverity", expected, dataA, dataB, dataC);
    }

    @Test
    public void highestSeverity2() {
	VString dataA = newVString("a", alarmNone(), timeNow());
	VString dataB = newVString("b", newAlarm(AlarmSeverity.MAJOR, "Help!"), timeNow());
	VString dataC = newVString("c", alarmNone(), timeNow());

        VEnum expected = newVEnum(2, AlarmSeverity.labels(), alarmNone(), timeNow());
        testFunction(set, "highestSeverity", expected, dataA, dataB, dataC);
    }
}
