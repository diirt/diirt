/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import java.util.Arrays;
import org.diirt.util.array.ArrayBoolean;
import org.diirt.util.array.ArrayByte;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.vtype.ValueFactory.*;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayLong;
import org.diirt.util.array.ArrayShort;
import org.diirt.util.array.ListBoolean;
import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListLong;
import org.diirt.util.array.ListNumber;
import org.diirt.util.array.ListShort;
import org.diirt.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class ValueFactoryTest {

    public ValueFactoryTest() {
    }

    @Test
    public void newAlarm1() {
        Alarm alarm = newAlarm(AlarmSeverity.MAJOR, "DEVICE");
        assertThat(alarm.getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(alarm.getAlarmName(), equalTo("DEVICE"));
        assertThat(alarm.toString(), equalTo("MAJOR(DEVICE)"));
    }

    @Test
    public void newDisplay1() {
        ArrayDimensionDisplay indexDimensionDisplay = newDisplay(5);
        assertThat(indexDimensionDisplay.getUnits(), equalTo(""));
        assertThat(indexDimensionDisplay.getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 1, 2, 3, 4, 5)));
    }

    @Test
    public void newDisplay2() {
        ArrayDimensionDisplay display = newDisplay(new ArrayDouble(-2, -1, 0, 1, 2), "m");
        assertThat(display.getUnits(), equalTo("m"));
        assertThat(display.getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(-2, -1, 0, 1, 2)));
    }

    @Test
    public void alarmNone1() {
        Alarm alarm = alarmNone();
        assertThat(alarm.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(alarm.getAlarmName(), equalTo("NONE"));
    }

    @Test
    public void newVString1() {
        VString value = newVString("Testing", alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getValue(), equalTo("Testing"));
        assertThat(value.toString(), equalTo("VString[Testing, 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVBoolean1() {
        VBoolean value = newVBoolean(true, alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getValue(), equalTo(true));
        assertThat(value.toString(), equalTo("VBoolean[true, 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVEnum1() {
        VEnum value = newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getValue(), equalTo("TWO"));
        assertThat(value.getIndex(), equalTo(1));
        assertThat(value.getLabels(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.toString(), equalTo("VEnum[TWO(1), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVDouble1() {
        VDouble value = newVDouble(1.0, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDouble[1.0, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVDoubleArray1() {
        VDoubleArray value = newVDoubleArray(new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getData(), equalTo((ListDouble) new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDoubleArray[[3.14, 6.28, 1.41, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVFloat1() {
        VFloat value = newVFloat(1.0f, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getValue(), equalTo(1.0f));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VFloat[1.0, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVFloatArray1() {
        VFloatArray value = newVFloatArray(new ArrayFloat(3.125f, 6.25f, 1.375f, 0.0f, 1.0f), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getData(), equalTo((ListFloat) new ArrayFloat(3.125f, 6.25f, 1.375f, 0.0f, 1.0f)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VFloatArray[[3.125, 6.25, 1.375, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVIntArray1() {
        VIntArray value = newVIntArray(new ArrayInt(3, 6, 1, 0, 1), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getData(), equalTo((ListInt) new ArrayInt(3, 6, 1, 0, 1)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VIntArray[[3, 6, 1, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVEnumArray1() {
        VEnumArray value = newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getData(), equalTo(Arrays.asList("TWO", "ONE", "THREE")));
        assertThat(value.getIndexes(), equalTo((ListInt) new ArrayInt(1, 0, 2)));
        assertThat(value.getSizes(), equalTo((ListInt) new ArrayInt(3)));
        assertThat(value.getLabels(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.toString(), equalTo("VEnumArray[[TWO, ONE, THREE], size 3, 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVBooleanArray1() {
        VBooleanArray value = newVBooleanArray(new ArrayBoolean(true, false, true, false), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getData(), equalTo((ListBoolean) new ArrayBoolean(true, false, true, false)));
        assertThat(value.getSizes(), equalTo((ListInt) new ArrayInt(4)));
        assertThat(value.toString(), equalTo("VBooleanArray[[true, false, true, ...], size 4, 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVStringArray1() {
        VStringArray value = newVStringArray(Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getData(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.getSizes(), equalTo((ListInt) new ArrayInt(3)));
        assertThat(value.toString(), equalTo("VStringArray[[ONE, TWO, THREE], size 3, 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray1() {
        VNumberArray result = newVNumberArray(new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0),
                new ArrayInt(5), null,
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VDoubleArray.class));
        VDoubleArray value = (VDoubleArray) result;
        assertThat(value.getData(), equalTo((ListDouble) new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0)));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0,1,2,3,4,5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDoubleArray[[3.14, 6.28, 1.41, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray2() {
        VNumberArray result = newVNumberArray(new ArrayInt(3,5,2,4,1),
                new ArrayInt(5), null,
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VIntArray.class));
        VIntArray value = (VIntArray) result;
        assertThat(value.getData(), equalTo((ListInt) new ArrayInt(3,5,2,4,1)));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0,1,2,3,4,5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VIntArray[[3, 5, 2, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray3() {
        VNumberArray result = newVNumberArray(new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VDoubleArray.class));
        VDoubleArray value = (VDoubleArray) result;
        assertThat(value.getData(), equalTo((ListDouble) new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0)));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDoubleArray[[3.14, 6.28, 1.41, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray4() {
        VNumberArray result = newVNumberArray(new ArrayInt(3,5,2,4,1),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VIntArray.class));
        VIntArray value = (VIntArray) result;
        assertThat(value.getData(), equalTo((ListInt) new ArrayInt(3,5,2,4,1)));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VIntArray[[3, 5, 2, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray5() {
        VNumberArray result = newVNumberArray(new ArrayByte(new byte[] {3,5,2,4,1}),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VByteArray.class));
        VByteArray value = (VByteArray) result;
        assertThat(value.getData(), equalTo((ListByte) new ArrayByte(new byte[] {3,5,2,4,1})));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VByteArray[[3, 5, 2, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray6() {
        VNumberArray result = newVNumberArray(new ArrayShort(new short[] {3,5,2,4,1}),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VShortArray.class));
        VShortArray value = (VShortArray) result;
        assertThat(value.getData(), equalTo((ListShort) new ArrayShort(new short[] {3,5,2,4,1})));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VShortArray[[3, 5, 2, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray7() {
        VNumberArray result = newVNumberArray(new ArrayFloat(new float[] {3,5,2,4,1}),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VFloatArray.class));
        VFloatArray value = (VFloatArray) result;
        assertThat(value.getData(), equalTo((ListFloat) new ArrayFloat(new float[] {3,5,2,4,1})));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VFloatArray[[3.0, 5.0, 2.0, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void newVNumberArray8() {
        VNumberArray result = newVNumberArray(new ArrayLong(new long[] {3,5,2,4,1}),
                new ArrayInt(5), Arrays.asList(newDisplay(new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5), "m")),
                newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(result, instanceOf(VLongArray.class));
        VLongArray value = (VLongArray) result;
        assertThat(value.getData(), equalTo((ListLong) new ArrayLong(new long[] {3,5,2,4,1})));
        assertThat(value.getDimensionDisplay().size(), equalTo(1));
        assertThat(value.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(0, 0.5, 1, 1.5, 2, 2.5)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VLongArray[[3, 5, 2, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }

    @Test
    public void toVType() {
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(1l), ValueFactory.newVNumber(1l, alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(1), ValueFactory.newVNumber(1, alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(1.0), ValueFactory.newVNumber(1.0, alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(new int[]{1,2,3}), ValueFactory.newVNumberArray(new ArrayInt(1,2,3), alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(new double[]{1,2,3}), ValueFactory.newVNumberArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(new ArrayInt(1,2,3)), ValueFactory.newVNumberArray(new ArrayInt(1,2,3), alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(new ArrayDouble(1,2,3)), ValueFactory.newVNumberArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType("A"), ValueFactory.newVString("A", alarmNone(), timeNow())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(new String[] {"A", "B", "C"}), ValueFactory.newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(Arrays.asList("A", "B", "C")), ValueFactory.newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow())), equalTo(true));
        assertThat(VTypeValueEquals.valueEquals(ValueFactory.toVType(true), ValueFactory.newVBoolean(true, alarmNone(), timeNow())), equalTo(true));
    }

}