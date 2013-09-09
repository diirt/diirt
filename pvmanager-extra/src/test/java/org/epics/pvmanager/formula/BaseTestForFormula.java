/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.displayNone;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.epics.vtype.ValueFactory.newVNumberArray;
import static org.epics.vtype.ValueFactory.newVNumber;
import static org.epics.vtype.ValueFactory.timeNow;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.epics.util.array.ListNumber;
import org.epics.vtype.Alarm;
import org.epics.vtype.VEnum;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueUtil;
import org.junit.BeforeClass;
import org.mockito.internal.matchers.InstanceOf;

/**
 * 
 * @author carcassi
 */
public class BaseTestForFormula {

    @BeforeClass
    public static void initMath() {
	if (FormulaRegistry.getDefault().findFunctionSet("math") == null) {
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new MathFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new NumberOperatorFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new ArrayFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new TableFunctionSet());
	}
    }

    public static void testTwoArgNumericFunction(FormulaFunctionSet set,
	    String name, double arg1, double arg2, double result) {
	FormulaFunction function = null;
	for (FormulaFunction formulaFunction : set.findFunctions(name)) {
	    if (formulaFunction.getArgumentTypes().size() == 2) {
		function = formulaFunction;
	    }
	}
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	VNumber value = (VNumber) function.calculate(Arrays.<Object> asList(
		newVDouble(arg1), newVDouble(arg2)));
	assertThat("Wrong result for function '" + name + "(" + arg1 + ", "
		+ arg2 + ")'.", value.getValue().doubleValue(),
		closeTo(result, 0.0001));
    }

    public static void testTwoArgArrayFunction(FormulaFunctionSet set,
	    String name, ListNumber arg1, ListNumber arg2, ListNumber result) {
	FormulaFunction function = null;
	for (FormulaFunction formulaFunction : set.findFunctions(name)) {
	    if (formulaFunction.getArgumentTypes().size() == 2) {
		function = formulaFunction;
	    }
	}
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	VNumberArray value = (VNumberArray) function.calculate(Arrays
		.<Object> asList(
			newVNumberArray(arg1, alarmNone(), timeNow(),
				displayNone()),
			newVNumberArray(arg2, alarmNone(), timeNow(),
				displayNone())));
	assertThat(
		"Wrong result for function '" + name + "(" + arg1 + ", " + arg2
			+ ")'.",
		compare(value,
			newVNumberArray(result, alarmNone(), timeNow(),
				displayNone())), equalTo(true));
    }
    
    public static void testTwoArgArrayFunction(FormulaFunctionSet set,
	    String name, ListNumber arg1, Number arg2, ListNumber result) {
	FormulaFunction function = null;
	for (FormulaFunction formulaFunction : set.findFunctions(name)) {
	    if (formulaFunction.getArgumentTypes().size() == 2) {
		function = formulaFunction;
	    }
	}
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	VNumberArray value = (VNumberArray) function.calculate(Arrays
		.<Object> asList(
			newVNumberArray(arg1, alarmNone(), timeNow(),
				displayNone()),
			newVNumber(arg2, alarmNone(), timeNow(),
				displayNone())));
	assertThat(
		"Wrong result for function '" + name + "(" + arg1 + ", " + arg2
			+ ")'.",
		compare(value,
			newVNumberArray(result, alarmNone(), timeNow(),
				displayNone())), equalTo(true));
    }

    public static void testFunction(FormulaFunctionSet set, String name,
	    double arg, double result) {
	FormulaFunction function = null;
	for (FormulaFunction formulaFunction : set.findFunctions(name)) {
	    if (formulaFunction.getArgumentTypes().size() == 1) {
		function = formulaFunction;
	    }
	}
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	VNumber value = (VNumber) function.calculate(Arrays
		.<Object> asList(newVDouble(arg)));
	assertThat("Wrong result for function '" + name + "(" + arg + ")'.",
		value.getValue().doubleValue(), closeTo(result, 0.0001));
    }

    public static void testFunction(FormulaFunctionSet set, String name,
	    Object expected, Object... args) {
	FormulaFunction function = FormulaFunctions.findFirstMatch(
		Arrays.asList(args), set.findFunctions(name));
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	Object result = function.calculate(Arrays.asList(args));
	assertThat(
		"Wrong result for function '" + name + "("
			+ Arrays.toString(args) + ")'. Was (" + result
			+ ") expected (" + expected + ")",
		compare(result, expected), equalTo(true));
    }

    public static void testFunctionAlarm(FormulaFunctionSet set, String name,
	    Alarm expected, Object... args) {
	FormulaFunction function = FormulaFunctions.findFirstMatch(
		Arrays.asList(args), set.findFunctions(name));
	assertThat("Function '" + name + "' not found.", function,
		not(nullValue()));
	Alarm result = ValueUtil.alarmOf(function.calculate(Arrays.asList(args)));
	assertThat(
		"Wrong result for function '" + name + "("
			+ Arrays.toString(args) + ")'. Was (" + VTypeToString.alarmToString(result)
			+ ") expected (" + VTypeToString.alarmToString(expected) + ")",
		compareAlarm(result, expected), equalTo(true));
    }

    public static boolean compareAlarm(Alarm alarm1, Alarm alarm2) {
	if (alarm1 == null && alarm2 == null) {
	    return true;
	}

	if (alarm1 == null || alarm2 == null) {
	    return false;
	}
        
        return alarm1.getAlarmSeverity().equals(alarm2.getAlarmSeverity()) &&
                alarm1.getAlarmName().equals(alarm2.getAlarmName());
    }

    public static boolean compare(Object obj1, Object obj2) {
	if (obj1 == null && obj2 == null) {
	    return true;
	}

	if (obj1 == null || obj2 == null) {
	    return false;
	}

	if ((obj1 instanceof VNumberArray) && (obj2 instanceof VNumberArray)) {
	    VNumberArray array1 = (VNumberArray) obj1;
	    VNumberArray array2 = (VNumberArray) obj2;
	    return array1.getData().equals(array2.getData());
	}

	if ((obj1 instanceof VStringArray) && (obj2 instanceof VStringArray)) {
	    VStringArray array1 = (VStringArray) obj1;
	    VStringArray array2 = (VStringArray) obj2;
	    return array1.getData().equals(array2.getData());
	}

	if ((obj1 instanceof VString) && (obj2 instanceof VString)) {
	    VString str1 = (VString) obj1;
	    VString str2 = (VString) obj2;
	    return str1.getValue().equals(str2.getValue());
	}

	if ((obj1 instanceof VEnum) && (obj2 instanceof VEnum)) {
	    VEnum enum1 = (VEnum) obj1;
	    VEnum enum2 = (VEnum) obj2;
	    return enum1.getValue().equals(enum2.getValue()) && enum1.getLabels().equals(enum2.getLabels());
	}
	
	if((obj1 instanceof VNumber) && (obj2 instanceof VNumber)) {
	    VNumber number1 = (VNumber) obj1;
	    VNumber number2 = (VNumber) obj2;
	    return number1.getValue().equals(number2.getValue());
	}

	return false;
    }
}
