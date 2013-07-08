/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.newVString;
import static org.epics.vtype.ValueFactory.newVStringArray;
import static org.epics.vtype.ValueFactory.newVTable;
import static org.epics.vtype.ValueFactory.timeNow;

import java.util.Arrays;

import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.junit.Test;

import antlr.collections.List;

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
}
