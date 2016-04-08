/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.array;

import org.diirt.datasource.formula.array.ArrayFunctionSet;

import static org.diirt.vtype.ValueFactory.*;

import java.time.Instant;
import java.util.Arrays;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FunctionTester;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListInt;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.Time;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.table.ListNumberProvider;
import org.diirt.vtype.table.VTableFactory;
import org.junit.Test;

/**
 * @author shroffk
 *
 */
public class ArrayFunctionSetTest {

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
    public void arraySum() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(4, 6, 8), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arraySum", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        FunctionTester.findBySignature(set, "arraySum", VNumber.class, VNumberArray.class)
                .compareReturnValue(array, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arraySub() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(0, 2, 4), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array3 = newVDoubleArray(new ArrayDouble(0, -2, -4), alarmNone(), timeNow(), displayNone());
        VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arraySub", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        FunctionTester.findBySignature(set, "arraySub", VNumber.class, VNumberArray.class)
                .compareReturnValue(array3, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void plusVNumberArrayVNumberArrayDouble() {
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
    public void plusVNumberArrayVNumberArrayInt() {
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
    public void minusVNumberArrayVNumberArray() {
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
        array = newVDoubleArray(new ArrayDouble(6, 3, 2), alarmNone(), timeNow(), displayNone());
        array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumber number = newVNumber(12, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayDiv", VNumber.class, VNumberArray.class)
                .compareReturnValue(array, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        array = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        array2 = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "arrayDiv", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
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
    public void multiplyVNumberArrayVNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(2, 4, 6), alarmNone(), timeNow(), displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(1, 2, 3), alarmNone(), timeNow(), displayNone());
        VNumber number = newVNumber(2, alarmNone(), timeNow(), displayNone());
        FunctionTester.findBySignature(set, "*", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        FunctionTester.findBySignature(set, "*", VNumber.class, VNumberArray.class)
                .compareReturnValue(array, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
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
        VNumberArray data = newVDoubleArray(new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), alarmNone(), timeNow(), displayNone());
        VNumberArray expected = newVDoubleArray(new ArrayDouble(2, 3, 4), alarmNone(), timeNow(), displayNone());
        VNumber start = newVNumber(2, alarmNone(), timeNow(), displayNone());
        VNumber end = newVNumber(5, alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "subArray", VNumberArray.class, VNumber.class, VNumber.class)
                .compareReturnValue(expected, data, start, end)
                .compareReturnValue(null, null, start, end)
                .compareReturnValue(null, data, null, end)
                .compareReturnValue(null, data, start, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void elementAtNumber() {
        VNumberArray array = newVDoubleArray(new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), alarmNone(), timeNow(), displayNone());
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = newTime(Instant.ofEpochSecond(16548379, 0));
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
    public void elementAtString() {
        VStringArray array = newVStringArray(Arrays.asList("A", "B", "C", "D", "E"), alarmNone(), timeNow());
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = newTime(Instant.ofEpochSecond(16548379, 0));
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
        FunctionTester.findBySignature(set, "histogramOf", VNumberArray.class)
                .compareReturnValue(expected, array)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayRangeOf(){
        Alarm alarm = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Time time = timeNow();
        VNumberArray array = newVDoubleArray(new ArrayDouble(1,2,3,4), alarmNone(), time, displayNone());
        VNumberArray array2 = newVDoubleArray(new ArrayDouble(1,2,3,4), alarm, timeNow(), displayNone());
        VNumberArray array3 = ValueFactory.newVNumberArray(new ArrayDouble(1,2,3,4), new ArrayInt(4),
                Arrays.asList(ValueFactory.newDisplay(new ArrayDouble(-1, -0.5, 0, 0.5, 1), "")), alarmNone(), timeNow(), displayNone());
        VNumberArray expected1 = ValueFactory.newVNumberArray(new ArrayDouble(0,4), alarmNone(), time, displayNone());
        VNumberArray expected2 = ValueFactory.newVNumberArray(new ArrayDouble(-1,1), alarmNone(), time, displayNone());

        FunctionTester.findBySignature(set, "arrayRangeOf", VNumberArray.class)
                .compareReturnValue(expected1, array)
                .compareReturnValue(expected2, array3)
                .compareReturnValue(null, (Object) null)
                .compareReturnAlarm(alarmNone(), array)
                .compareReturnAlarm(alarm, array2)
                .compareReturnTime(time, array);
    }

    @Test
    public void dimDisplay(){
        FunctionTester.findBySignature(set, "dimDisplay", VNumber.class, VBoolean.class)
                .compareReturnValue(ValueFactory.newDisplay(20), 20, false)
                .compareReturnValue(null, null, true)
                .compareReturnValue(null, 123, null);
    }

    @Test
    public void ndArray(){
        FunctionTester.findBySignature(set, "ndArray", VNumberArray.class, ArrayDimensionDisplay.class)
                .compareReturnValue(ValueFactory.toVType(new ArrayDouble(1,2,3)), new ArrayDouble(1,2,3), ValueFactory.newDisplay(3))
                .compareReturnValue(ValueFactory.ndArray((VNumberArray) ValueFactory.toVType(new ArrayDouble(1,2,3)), ValueFactory.newDisplay(3, VTableFactory.range(0, 9))), new ArrayDouble(1,2,3), ValueFactory.newDisplay(new ArrayDouble(0,3,6,9), ""))
                .compareReturnValue(null, null, ValueFactory.newDisplay(20))
                .compareReturnValue(null, new ArrayDouble(1,2,3), null);
    }
}
