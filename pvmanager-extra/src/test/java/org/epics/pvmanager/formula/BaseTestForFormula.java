/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
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
import java.util.Date;
import java.util.Objects;

import org.epics.util.array.ListNumber;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VEnum;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueFactory;
import static org.epics.vtype.ValueFactory.*;
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
	    return array1.getData().equals(array2.getData()) && array1.getSizes().equals(array2.getSizes());
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
	
	if((obj1 instanceof VBoolean) && (obj2 instanceof VBoolean)) {
	    VBoolean number1 = (VBoolean) obj1;
	    VBoolean number2 = (VBoolean) obj2;
	    return number1.getValue().equals(number2.getValue());
	}
	
	if((obj1 instanceof VTable) && (obj2 instanceof VTable)) {
	    VTable table1 = (VTable) obj1;
	    VTable table2 = (VTable) obj2;
	    if (table1.getColumnCount() != table2.getColumnCount() ||
                    table1.getRowCount() != table2.getRowCount()) {
                return false;
            }
            for (int i = 0; i < table1.getColumnCount(); i++) {
                if (!Objects.equals(table1.getColumnType(i), table2.getColumnType(i)) ||
                        !Objects.equals(table1.getColumnName(i), table2.getColumnName(i)) ||
                        !Objects.equals(table1.getColumnData(i), table2.getColumnData(i))) {
                    return false;
                }
            }
            return true;
	}

	return false;
    }
}
