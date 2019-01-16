/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.array;

import java.time.Instant;
import java.util.Arrays;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FunctionTester;
import org.diirt.vtype.table.ListNumberProvider;
import org.diirt.vtype.table.VTableFactory;
import org.diirt.vtype.util.ArrayDimensionDisplay;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInteger;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VType;
import org.junit.Test;

/**
 * @author shroffk
 *
 */
public class ArrayFunctionSetTest {

    private FormulaFunctionSet set = new ArrayFunctionSet();

    @Test
    public void arrayOfString() {
        VString s1 = VString.of("x", Alarm.none(), Time.now());
        VString s2 = VString.of("y", Alarm.none(), Time.now());
        VString s3 = VString.of("z", Alarm.none(), Time.now());
        VStringArray expected1 = VStringArray.of(Arrays.asList("x", "y", "z"),
                Alarm.none(), Time.now());
        VStringArray expected2 = VStringArray.of(Arrays.asList("x", "y", null),
                Alarm.none(), Time.now());
        FunctionTester.findBySignature(set, "arrayOf", VString.class)
                .compareReturnValue(expected1, s1, s2, s3)
                .compareReturnValue(expected2, s1, s2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayOfNumber() {
        VNumber n1 = VDouble.of(Double.valueOf(1), Alarm.none(), Time.now(), Display.none());
        VNumber n2 = VDouble.of(Double.valueOf(2), Alarm.none(), Time.now(), Display.none());
        VNumber n3 = VDouble.of(Double.valueOf(3), Alarm.none(), Time.now(), Display.none());
        VNumberArray expected1 = VNumberArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray expected2 = VNumberArray.of(ArrayDouble.of(1, 2, Double.NaN), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "arrayOf", VNumber.class)
                .compareReturnValue(expected1, n1, n2, n3)
                .compareReturnValue(expected2, n1, n2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arraySum() {
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(4, 6, 8), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(2, 4, 6), Alarm.none(), Time.now(), Display.none());
        VNumber number = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
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
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(0, 2, 4), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(2, 4, 6), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VDoubleArray.of(ArrayDouble.of(0, -2, -4), Alarm.none(), Time.now(), Display.none());
        VNumber number = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
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
        VNumberArray array = VNumberArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VNumberArray.of(ArrayDouble.of(4, 5, 6), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VNumberArray.of(ArrayDouble.of(5, 7, 9), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "+", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void plusVNumberArrayVNumberArrayInt() {
        VNumberArray array = VNumberArray.of(ArrayInteger.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VNumberArray.of(ArrayInteger.of(4, 5, 6), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VNumberArray.of(ArrayDouble.of(5, 7, 9), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "+", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void minusVNumberArrayVNumberArray() {
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(4, 5, 6), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(4, 3, 2), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VDoubleArray.of(ArrayDouble.of(0, 2, 4), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "-", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayMult() {
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(3, 2, 0), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VDoubleArray.of(ArrayDouble.of(3, 4, 0), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "arrayMult", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayDiv() {
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(6, 5, 4), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(3, 2, 1), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VDoubleArray.of(ArrayDouble.of(2, 2.5, 4), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "arrayDiv", VNumberArray.class, VNumberArray.class)
                .compareReturnValue(array3, array, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, array, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        array = VDoubleArray.of(ArrayDouble.of(6, 3, 2), Alarm.none(), Time.now(), Display.none());
        array2 = VDoubleArray.of(ArrayDouble.of(2, 4, 6), Alarm.none(), Time.now(), Display.none());
        VNumber number = VNumber.of(12, Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "arrayDiv", VNumber.class, VNumberArray.class)
                .compareReturnValue(array, number, array2)
                .compareReturnValue(null, null, array2)
                .compareReturnValue(null, number, null)
                .highestAlarmReturned()
                .latestTimeReturned();
        array = VDoubleArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        array2 = VDoubleArray.of(ArrayDouble.of(2, 4, 6), Alarm.none(), Time.now(), Display.none());
        number = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
        FunctionTester.findBySignature(set, "arrayDiv", VNumberArray.class, VNumber.class)
                .compareReturnValue(array, array2, number)
                .compareReturnValue(null, null, number)
                .compareReturnValue(null, array2, null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void arrayPow() {
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(1, 4, 9), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray array3 = VDoubleArray.of(ArrayDouble.of(2, 4, 8), Alarm.none(), Time.now(), Display.none());
        VNumber number = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
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
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(2, 4, 6), Alarm.none(), Time.now(), Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumber number = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
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
        VNumberArray data = VDoubleArray.of(ArrayDouble.of(1, 2, 3), Alarm.none(), Time.now(), Display.none());
        VNumberArray expected = VDoubleArray.of(ArrayDouble.of(3, 5, 7), Alarm.none(), Time.now(), Display.none());
        VNumber offset = VNumber.of(1, Alarm.none(), Time.now(), Display.none());
        VNumber scale = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
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
        VNumberArray data = VDoubleArray.of(ArrayDouble.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), Alarm.none(), Time.now(), Display.none());
        VNumberArray expected = VDoubleArray.of(ArrayDouble.of(2, 3, 4), Alarm.none(), Time.now(), Display.none());
        VNumber start = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
        VNumber end = VNumber.of(5, Alarm.none(), Time.now(), Display.none());

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
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), Alarm.none(), Time.now(), Display.none());
        Alarm alarm = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
        Time time = Time.of(Instant.ofEpochSecond(16548379, 0));
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), alarm, time, Display.none());
        VNumber index = VNumber.of(5, Alarm.none(), Time.now(), Display.none());
        VNumber expected = VNumber.of(5.0, Alarm.none(),Time.now(), Display.none());

        FunctionTester.findBySignature(set, "elementAt", VNumberArray.class, VNumber.class)
                .compareReturnValue(expected, array, index)
                .compareReturnValue(null, array, null)
                .compareReturnValue(null, null, index)
                .compareReturnAlarm(Alarm.none(), array, index)
                .compareReturnAlarm(alarm, array2, index)
                .compareReturnTime(time, array2, index);
    }

    @Test
    public void elementAtString() {
        VStringArray array = VStringArray.of(Arrays.asList("A", "B", "C", "D", "E"), Alarm.none(), Time.now());
        Alarm alarm = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
        Time time = Time.of(Instant.ofEpochSecond(16548379, 0));
        VStringArray array2 = VStringArray.of(Arrays.asList("A", "B", "C", "D", "E"), alarm, time);
        VNumber index = VNumber.of(2, Alarm.none(), Time.now(), Display.none());
        VString expected = VString.of("C", Alarm.none(),Time.now());

        FunctionTester.findBySignature(set, "elementAt", VStringArray.class, VNumber.class)
                .compareReturnValue(expected, array, index)
                .compareReturnValue(null, array, null)
                .compareReturnValue(null, null, index)
                .compareReturnAlarm(Alarm.none(), array, index)
                .compareReturnAlarm(alarm, array2, index)
                .compareReturnTime(time, array2, index);
    }

//    @Test
//    public void arrayWithBoundaries(){
//        Alarm alarm = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
//        Time time = Time.now();
//        VNumberArray array = VDoubleArray.of(ArrayDouble.of(1,2,3,4), Alarm.none(), time, Display.none());
//        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(1,2,3,4), alarm, Time.now(), Display.none());
//        ListNumberProvider generator = VTableFactory.step(-1, 0.5);
//        VNumberArray expected = VNumberArray(ArrayDouble.of(1,2,3,4), ArrayInteger.of(4),
//                Arrays.asList(Display(ArrayDouble.of(-1, -0.5, 0, 0.5, 1), "")), Alarm.none(), Time.now(), Display.none());
//
//        FunctionTester.findBySignature(set, "arrayWithBoundaries", VNumberArray.class, ListNumberProvider.class)
//                .compareReturnValue(expected, array, generator)
//                .compareReturnValue(null, array, null)
//                .compareReturnValue(null, null, generator)
//                .compareReturnAlarm(Alarm.none(), array, generator)
//                .compareReturnAlarm(alarm, array2, generator)
//                .compareReturnTime(time, array, generator);
//    }

    @Test
    public void histogramOf() {
        ListDouble data = ArrayDouble.of(0, 10, 3, 3, 3.5, 4, 4.5, 3, 7, 3.1);
        ListInteger expectedData = ArrayInteger.of(1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           3, 1, 0, 0, 0, 1, 0, 0, 0, 0,
                                           1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                           0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
        VNumberArray array = VDoubleArray.of(data, Alarm.none(),
                Time.now(), Display.none());
        VNumberArray expected = VIntArray.of(expectedData, Alarm.none(),
                Time.now(), Display.none());

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
        Alarm alarm = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
        Time time = Time.now();
        VNumberArray array = VDoubleArray.of(ArrayDouble.of(1,2,3,4), Alarm.none(), time, Display.none());
        VNumberArray array2 = VDoubleArray.of(ArrayDouble.of(1,2,3,4), alarm, Time.now(), Display.none());
        VNumberArray array3 = VNumberArray.of(ArrayDouble.of(1,2,3,4), ArrayInteger.of(4), Alarm.none(), Time.now(), Display.none());
        VNumberArray expected1 = VNumberArray.of(ArrayDouble.of(0,4), Alarm.none(), time, Display.none());
        VNumberArray expected2 = VNumberArray.of(ArrayDouble.of(-1,1), Alarm.none(), time, Display.none());

        FunctionTester.findBySignature(set, "arrayRangeOf", VNumberArray.class)
                .compareReturnValue(expected1, array)
                .compareReturnValue(expected2, array3)
                .compareReturnValue(null, (Object) null)
                .compareReturnAlarm(Alarm.none(), array)
                .compareReturnAlarm(alarm, array2)
                .compareReturnTime(time, array);
    }

//    @Test
//    public void dimDisplay(){
//        FunctionTester.findBySignature(set, "dimDisplay", VNumber.class, VBoolean.class)
//                .compareReturnValue(Display(20), 20, false)
//                .compareReturnValue(null, null, true)
//                .compareReturnValue(null, 123, null);
//    }
//
//    @Test
//    public void ndArray(){
//        FunctionTester.findBySignature(set, "ndArray", VNumberArray.class, ArrayDimensionDisplay.class)
//                .compareReturnValue(VType.toVType(ArrayDouble.of(1,2,3)), ArrayDouble.of(1,2,3), Display(3))
//                .compareReturnValue(ValueFactory.ndArray((VNumberArray) ValueFactory.toVType(ArrayDouble.of(1,2,3)), Display(3, VTableFactory.range(0, 9))), ArrayDouble.of(1,2,3), Display(ArrayDouble.of(0,3,6,9), ""))
//                .compareReturnValue(null, null, Display(20))
//                .compareReturnValue(null, ArrayDouble.of(1,2,3), null);
//    }
}
