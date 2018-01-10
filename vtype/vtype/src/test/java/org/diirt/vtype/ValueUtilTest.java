/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.displayNone;
import static org.diirt.vtype.ValueFactory.newAlarm;
import static org.diirt.vtype.ValueFactory.newDisplay;
import static org.diirt.vtype.ValueFactory.newTime;
import static org.diirt.vtype.ValueFactory.newVDouble;
import static org.diirt.vtype.ValueFactory.newVDoubleArray;
import static org.diirt.vtype.ValueFactory.newVEnum;
import static org.diirt.vtype.ValueFactory.newVEnumArray;
import static org.diirt.vtype.ValueFactory.newVFloatArray;
import static org.diirt.vtype.ValueFactory.newVInt;
import static org.diirt.vtype.ValueFactory.newVIntArray;
import static org.diirt.vtype.ValueFactory.newVNumberArray;
import static org.diirt.vtype.ValueFactory.newVString;
import static org.diirt.vtype.ValueFactory.timeNow;
import static org.diirt.vtype.ValueUtil.colorFor;
import static org.diirt.vtype.ValueUtil.displayHasValidDisplayLimits;
import static org.diirt.vtype.ValueUtil.numericValueOf;
import static org.diirt.vtype.ValueUtil.subArray;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListNumber;
import org.diirt.util.text.NumberFormats;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class ValueUtilTest {

    public ValueUtilTest() {
    }

    @Test
    public void testTypeOf() {
        assertThat(ValueUtil.typeOf(newVString(null, alarmNone(), timeNow())),
                equalTo((Class) VString.class));
        assertThat(ValueUtil.typeOf(newVDouble(Double.NaN, alarmNone(), timeNow(), displayNone())),
                equalTo((Class) VDouble.class));
    }

    @Test
    public void displayEquals1() {
        assertThat(ValueUtil.displayEquals(displayNone(), displayNone()), equalTo(true));
        Display display1 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, displayNone()), equalTo(false));
        Display display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(true));
        display2 = newDisplay(0.1, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.1, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.1, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "a", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.format(2), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.1, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.1, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.1, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.1, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.1);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
    }

    @Test
    public void numericValueOf1() {
        assertThat(numericValueOf(newVDouble(1.0)), equalTo(1.0));
        assertThat(numericValueOf(newVInt(1, alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow())), equalTo(1.0));
        assertThat(numericValueOf(newVDoubleArray(new ArrayDouble(1.0), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVFloatArray(new ArrayFloat(1.0f), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVIntArray(new ArrayInt(1), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVEnumArray(new ArrayInt(1,0,2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow())), equalTo(1.0));
    }

    @Test
    public void displayHasValidDisplayLimits1() {
        assertThat(displayHasValidDisplayLimits(displayNone()), equalTo(false));
    }

    @Test
    public void displayHasValidDisplayLimits2() {
        Display display1 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(displayHasValidDisplayLimits(display1), equalTo(true));
    }

    @Test
    public void numericColumnOf1() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        assertThat(ValueUtil.numericColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.numericColumnOf(data, "x"), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(ValueUtil.numericColumnOf(data, "y"), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void numericColumnOf2() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c")));
        assertThat(ValueUtil.numericColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.numericColumnOf(data, "x"), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        ValueUtil.numericColumnOf(data, "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void numericColumnOf3() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        ValueUtil.numericColumnOf(data, "z");
    }

    @Test
    public void stringColumnOf1() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(Arrays.asList("1", "2", "3"), Arrays.asList("a", "b", "c")));
        assertThat(ValueUtil.stringColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.stringColumnOf(data, "x"), equalTo(Arrays.asList("1", "2", "3")));
        assertThat(ValueUtil.stringColumnOf(data, "y"), equalTo(Arrays.asList("a", "b", "c")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringColumnOf2() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c")));
        assertThat(ValueUtil.stringColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.stringColumnOf(data, "y"), equalTo(Arrays.asList("a", "b", "c")));
        ValueUtil.stringColumnOf(data, "x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sttringColumnOf3() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c")));
        ValueUtil.stringColumnOf(data, "z");
    }

    @Test
    public void colorFor1() {
        assertThat(colorFor(AlarmSeverity.NONE), equalTo(Color.GREEN.getRGB()));
        assertThat(colorFor(AlarmSeverity.MINOR), equalTo(Color.YELLOW.getRGB()));
        assertThat(colorFor(AlarmSeverity.MAJOR), equalTo(Color.RED.getRGB()));
        assertThat(colorFor(AlarmSeverity.INVALID), equalTo(Color.MAGENTA.getRGB()));
        assertThat(colorFor(AlarmSeverity.UNDEFINED), equalTo(Color.DARK_GRAY.getRGB()));
    }

    @Test
    public void highestSeverityOf1() {
        Alarm none = ValueFactory.alarmNone();
        Alarm minor = ValueFactory.newAlarm(AlarmSeverity.MINOR, "Minor alarm");
        Alarm otherMinor = ValueFactory.newAlarm(AlarmSeverity.MINOR, "Other minor alarm");
        Alarm major = ValueFactory.newAlarm(AlarmSeverity.MAJOR, "Major alarm");
        Alarm invalid = ValueFactory.newAlarm(AlarmSeverity.INVALID, "Invalid alarm");
        Alarm undefined = ValueFactory.newAlarm(AlarmSeverity.UNDEFINED, "Undefined alarm");
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, minor), false), sameInstance(minor));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, minor, otherMinor), false), sameInstance(minor));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(null, minor, otherMinor), false), sameInstance(minor));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(null, minor, otherMinor), true).getAlarmSeverity(), sameInstance(AlarmSeverity.UNDEFINED));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, major, minor, otherMinor), false), sameInstance(major));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, major, minor, otherMinor, invalid), false), sameInstance(invalid));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, major, minor, undefined, invalid), false), sameInstance(undefined));
        assertThat(ValueUtil.highestSeverityOf(Arrays.<Object>asList(none, major, minor, undefined, invalid, null), true), sameInstance(undefined));
    }

    @Test
    public void latestTimeOf1() {
        Time time1 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time2 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time3 = newTime(Instant.ofEpochSecond(12350000, 0));
        Time time4 = newTime(Instant.ofEpochSecond(12360000, 0));
        assertThat(ValueUtil.latestTimeOf(Arrays.<Object>asList(time1, time3)), sameInstance(time3));
        assertThat(ValueUtil.latestTimeOf(Arrays.<Object>asList(time3, time1)), sameInstance(time3));
        assertThat(ValueUtil.latestTimeOf(Arrays.<Object>asList(time1, time2)), sameInstance(time1));
        assertThat(ValueUtil.latestTimeOf(Arrays.<Object>asList(time2, time1)), sameInstance(time2));
        assertThat(ValueUtil.latestTimeOf(Arrays.<Object>asList(time1, time4, time2, time3)), sameInstance(time4));
    }

    @Test
    public void latestValidTimeOrNewOf1() {
        Time time1 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time2 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time3 = newTime(Instant.ofEpochSecond(12350000, 0));
        Time time4 = newTime(Instant.ofEpochSecond(12360000, 0));
        Time time5 = newTime(Instant.ofEpochSecond(12370000, 0), 1, false);
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time1, time3)), sameInstance(time3));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time3, time1)), sameInstance(time3));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time1, time2)), sameInstance(time1));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time2, time1)), sameInstance(time2));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time1, time4, time5, time3)), sameInstance(time4));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(time5)), not(sameInstance(time5)));
        assertThat(ValueUtil.latestValidTimeOrNowOf(Arrays.<Object>asList(null, null)), not(nullValue()));
    }

    @Test
    public void subArray1() {
        VNumberArray array = newVNumberArray(new ArrayDouble(1,2,3,4,5), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Instant.ofEpochSecond(123, 123)), displayNone());
        VNumberArray selection = subArray(array, 2);
        assertThat(selection.getData(), equalTo((ListNumber) new ArrayDouble(3)));
        assertThat(selection.getSizes(), equalTo((ListInt) new ArrayInt(1)));
        assertThat(selection.getDimensionDisplay().get(0).getCellBoundaries(), equalTo((ListNumber) new ArrayDouble(2,3)));
    }

}