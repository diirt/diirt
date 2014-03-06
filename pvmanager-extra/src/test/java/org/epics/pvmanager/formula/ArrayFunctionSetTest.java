/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.*;

import java.util.Arrays;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.ValueFactory;
import org.epics.vtype.table.ListNumberProvider;
import org.epics.vtype.table.VTableFactory;
import org.junit.Test;

/**
 * @author shroffk
 * 
 */
public class ArrayFunctionSetTest extends BaseTestForFormula {

    private FormulaFunctionSet set = new ArrayFunctionSet();

    @Test
    public void arrayOfString() {
        VString s1 = newVString("x", alarmNone(), timeNow());
        VString s2 = newVString("y", alarmNone(), timeNow());
        VString s3 = newVString("z", alarmNone(), timeNow());
        VStringArray expected1 = newVStringArray(Arrays.asList("x", "y", "z"),
                alarmNone(), timeNow());
        VStringArray expected2 = newVStringArray(Arrays.asList("x", "y", null),
                alarmNone(), timeNow());
        FunctionTester.findBySignature(set, "arrayOf", VString.class)
                .compareReturnValue(expected1, s1, s2, s3)
                .compareReturnValue(expected2, s1, s2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayOfNumber() {
        VNumber n1 = newVDouble(Double.valueOf(1), alarmNone(), timeNow(), displayNone());
        VNumber n2 = newVDouble(Double.valueOf(2), alarmNone(), timeNow(), displayNone());
        VNumber n3 = newVDouble(Double.valueOf(3), alarmNone(), timeNow(), displayNone());
        VNumberArray expected1 = newVNumberArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray expected2 = newVNumberArray(new ArrayDouble(1, 2, Double.NaN), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayOf", VNumber.class)
                .compareReturnValue(expected1, n1, n2, n3)
                .compareReturnValue(expected2, n1, n2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void plusVNumberArrayVNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(4, 6, 8), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
	VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "+", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void minusVNumberArrayVNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(0, 2, 4), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(0, -2, -4), alarmNone(), timeNow(), displayNone());
	VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "-", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        FunctionTester.findBySignature(set, "-", VNumber.class, VNumberArray.class)
                .compareReturnValue(array3, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void addArrayDoubleOfNumber() {
        VNumberArray array = newVNumberArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVNumberArray(new ArrayDouble(4, 5, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVNumberArray(new ArrayDouble(5, 7, 9), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "+", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void addArrayIntOfNumber() {
        VNumberArray array = newVNumberArray(new ArrayInt(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVNumberArray(new ArrayInt(4, 5, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVNumberArray(new ArrayDouble(5, 7, 9), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "+", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void subtractArrayOfNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(4, 5, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(4, 3, 2), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(0, 2, 4), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "-", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayMult() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(3, 2, 0), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(3, 4, 0), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayMult", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void arrayDiv() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(6, 5, 4), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(3, 2, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(2, 2.5, 4), alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayDiv", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void arrayPow() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(1, 4, 9), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(2, 4, 8), alarmNone(), timeNow(), displayNone());
	VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayPow", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        FunctionTester.findBySignature(set, "arrayPow", VNumber.class, VNumberArray.class)
                .compareReturnValue(array3, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void multiplyArrayWithNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
	VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "*", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void divideArrayWithNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
	VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "/", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void rescaleArray() {
        VNumberArray data = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumberArray expected = newVDoubleArray(new ArrayDouble(3, 5, 7), alarmNone(), timeNow(), displayNone());
	VNumber offset = newVNumber(1, alarmNone(), timeNow(), displayNone());
	VNumber scale = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "rescale", VNumberArray.class, VNumber.class, VNumber.class)
                .compareReturnValue(expected, data, scale, offset)
                .compareReturnValue(null, null, scale, offset)
                .compareReturnValue(null, data, null, offset)
                .compareReturnValue(null, data, scale, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }
    
    @Test
    public void subArray() {
	ListDouble data = new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	ListDouble expectedData = new ArrayDouble(2, 3, 4);
	VNumberArray expected = newVDoubleArray(expectedData, alarmNone(),
		timeNow(), displayNone());
	testFunction(set, "subArray", expected,
		newVDoubleArray(data, alarmNone(), timeNow(), displayNone()),
		newVNumber(2, alarmNone(), timeNow(), displayNone()),
		newVNumber(5, alarmNone(), timeNow(), displayNone()));
    }
    
    @Test
    public void elementAtArray1() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), alarmNone(), timeNow(), displayNone());
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = newTime(Timestamp.of(16548379, 0));
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), alarm, time, displayNone());
	VNumber index = newVNumber(5, alarmNone(), timeNow(), displayNone());
	VNumber expected = newVNumber(5.0, alarmNone(),timeNow(), displayNone());
        
        FunctionTester.findBySignature(set, "elementAt", VNumberArray.class, VNumber.class)
                .compareReturnValue(expected, array, index)
                .compareReturnValue(null, array, null)
                .compareReturnValue(null, null, index)
                .compareReturnAlarm(alarmNone(), array, index)
                .compareReturnAlarm(alarm, array2, index)
                .compareReturnTime(time, array2, index);
    }
    
    @Test
    public void elementAtArray2() {
        VStringArray array = newVStringArray(Arrays.asList("A", "B", "C", "D", "E"), alarmNone(), timeNow());
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = newTime(Timestamp.of(16548379, 0));
        VStringArray array2 = newVStringArray(Arrays.asList("A", "B", "C", "D", "E"), alarm, time);
	VNumber index = newVNumber(2, alarmNone(), timeNow(), displayNone());
	VString expected = newVString("C", alarmNone(),timeNow());
        
        FunctionTester.findBySignature(set, "elementAt", VStringArray.class, VNumber.class)
                .compareReturnValue(expected, array, index)
                .compareReturnValue(null, array, null)
                .compareReturnValue(null, null, index)
                .compareReturnAlarm(alarmNone(), array, index)
                .compareReturnAlarm(alarm, array2, index)
                .compareReturnTime(time, array2, index);
    }
    
    @Test
    public void arrayWithBoundaries(){
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = timeNow();
        VNumberArray array = newVDoubleArray(new ArrayDouble(1,2,3,4), alarmNone(), time, displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(1,2,3,4), alarm, timeNow(), displayNone());
        ListNumberProvider generator = VTableFactory.step(-1, 0.5);
        VNumberArray expected = ValueFactory.newVNumberArray(new ArrayDouble(1,2,3,4), new ArrayInt(4),
                Arrays.asList(ValueFactory.newDisplay(new ArrayDouble(-1, -0.5, 0, 0.5, 1), "")), alarmNone(), timeNow(), displayNone());
	
        FunctionTester.findBySignature(set, "arrayWithBoundaries", VNumberArray.class, ListNumberProvider.class)
                .compareReturnValue(expected, array, generator)
                .compareReturnValue(null, array, null)
                .compareReturnValue(null, null, generator)
                .compareReturnAlarm(alarmNone(), array, generator)
                .compareReturnAlarm(alarm, array2, generator)
                .compareReturnTime(time, array, generator);
    }
    
    @Test
    public void histogramOf() {
	ListDouble data = new ArrayDouble(0, 10, 3, 3, 3.5, 4, 4.5, 3, 7, 3.1);
        ListInt expectedData = new ArrayInt(1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           3, 1, 0, 0, 0, 1, 0, 0, 0, 0,
                                           1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
	VNumberArray array = newVDoubleArray(data, alarmNone(),
		timeNow(), displayNone());
	VNumberArray expected = newVIntArray(expectedData, alarmNone(),
		timeNow(), displayNone());
        
        FunctionTester.findByName(set, "histogramOf")
                .compareReturnValue(expected, array);
    }
   
}
