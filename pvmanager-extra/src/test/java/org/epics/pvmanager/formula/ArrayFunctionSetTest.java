/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.displayNone;
import static org.epics.vtype.ValueFactory.newVNumber;
import static org.epics.vtype.ValueFactory.newVNumberArray;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.epics.vtype.ValueFactory.newVDoubleArray;
import static org.epics.vtype.ValueFactory.newVString;
import static org.epics.vtype.ValueFactory.newVStringArray;
import static org.epics.vtype.ValueFactory.timeNow;

import java.util.Arrays;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.junit.Test;

/**
 * @author shroffk
 * 
 */
public class ArrayFunctionSetTest extends BaseTestForFormula {

    private FormulaFunctionSet set = new ArrayFunctionSet();

    @Test
    public void arrayOfString() {
	VString[] data = { newVString("x", alarmNone(), timeNow()),
		newVString("y", alarmNone(), timeNow()),
		newVString("z", alarmNone(), timeNow()) };
	VStringArray expected = newVStringArray(Arrays.asList("x", "y", "z"),
		alarmNone(), timeNow());
	testFunction(set, "arrayOf", expected, (Object[]) data);
    }

    @Test
    public void arrayOfNumber() {
	VNumber[] data = {
		newVDouble(Double.valueOf(1), alarmNone(), timeNow(),
			displayNone()),
		newVDouble(Double.valueOf(2), alarmNone(), timeNow(),
			displayNone()),
		newVDouble(Double.valueOf(3), alarmNone(), timeNow(),
			displayNone()) };
	double[] expectedData = { 1, 2, 3 };
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(set, "arrayOf", expected, (Object[]) data);
    }

    @Test
    public void addArrayDoubleOfNumber() {
	testTwoArgArrayFunction(set, "+", new ArrayDouble(1, 2, 3),
		new ArrayDouble(4, 5, 6), new ArrayDouble(5, 7, 9));
    }

    @Test
    public void addArrayIntOfNumber() {
	testTwoArgArrayFunction(set, "+", new ArrayInt(1, 2, 3), new ArrayInt(
		4, 5, 6), new ArrayDouble(5, 7, 9));
    }

    @Test
    public void subtractArrayOfNumber() {
	testTwoArgArrayFunction(set, "-", new ArrayDouble(4, 5, 6),
		new ArrayDouble(4, 3, 2), new ArrayDouble(0, 2, 4));
    }

    @Test
    public void multiplyArrayWithNumber() {
	testTwoArgArrayFunction(set, "*", new ArrayDouble(1, 2, 3),
		Double.valueOf(2), new ArrayDouble(2, 4, 6));
    }

    @Test
    public void divideArrayWithNumber() {
	testTwoArgArrayFunction(set, "/", new ArrayDouble(2, 4, 6),
		Double.valueOf(2), new ArrayDouble(1, 2, 3));
    }

    @Test
    public void rescaleArray() {
//	VNumber[] data = {
//		newVDouble(Double.valueOf(1), alarmNone(), timeNow(),
//			displayNone()),
//		newVDouble(Double.valueOf(2), alarmNone(), timeNow(),
//			displayNone()),
//		newVDouble(Double.valueOf(3), alarmNone(), timeNow(),
//			displayNone()) };
	// factor = 1 and
	double[] data = {1, 2, 3};
	double[] expectedData = { 2, 3, 4 };
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(
		set,
		"rescale",
		expected,
		newVDoubleArray(data , alarmNone(), timeNow(),
			displayNone()),
		newVNumber(1.0, alarmNone(), timeNow(),
			displayNone()),
		newVNumber(1.0, alarmNone(), timeNow(),
			displayNone()));
    }
}
