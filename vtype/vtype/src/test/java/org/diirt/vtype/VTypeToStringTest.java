/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.displayNone;
import static org.diirt.vtype.ValueFactory.newAlarm;
import static org.diirt.vtype.ValueFactory.newTime;
import static org.diirt.vtype.ValueFactory.newVBoolean;
import static org.diirt.vtype.ValueFactory.newVBooleanArray;
import static org.diirt.vtype.ValueFactory.newVDouble;
import static org.diirt.vtype.ValueFactory.newVDoubleArray;
import static org.diirt.vtype.ValueFactory.newVEnum;
import static org.diirt.vtype.ValueFactory.newVEnumArray;
import static org.diirt.vtype.ValueFactory.newVInt;
import static org.diirt.vtype.ValueFactory.newVString;
import static org.diirt.vtype.ValueFactory.newVStringArray;
import static org.diirt.vtype.ValueFactory.newVTable;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.diirt.util.array.ArrayBoolean;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.config.TimeStampFormatter;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class VTypeToStringTest {

    private Time testTime;
    private String testTimeString;
    private static final DateTimeFormatter timeFormat = TimeStampFormatter.TIMESTAMP_FORMAT;

    public VTypeToStringTest() {
    }

    @Before
    public void setUp() {
        testTime = newTime(Instant.ofEpochSecond(1234567, 123000000));
        testTimeString = timeFormat.format(ZonedDateTime.ofInstant(testTime.getTimestamp(), ZoneId.systemDefault()));
    }

    @Test
    public void toStringVNumber() {
        assertThat(VTypeToString.toString(newVDouble(3.0, alarmNone(), testTime, displayNone())),
                equalTo(String.format("VDouble[3.0, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVDouble(133.0, newAlarm(AlarmSeverity.MINOR, "LOW"), testTime, displayNone())),
                equalTo(String.format("VDouble[133.0, MINOR(LOW), %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime, displayNone())),
                equalTo(String.format("VDouble[3.14, MINOR(HIGH), %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVInt(3, alarmNone(), testTime, displayNone())),
                equalTo(String.format("VInt[3, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVInt(4, newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime, displayNone())),
                equalTo(String.format("VInt[4, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVString() {
        assertThat(VTypeToString.toString(newVString("Testing", alarmNone(), testTime)),
                equalTo(String.format("VString[Testing, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVString("Testing", newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VString[Testing, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVEnum() {
        assertThat(VTypeToString.toString(newVEnum(1, Arrays.asList("A", "B", "C"), alarmNone(), testTime)),
                equalTo(String.format("VEnum[B(1), %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVEnum(1, Arrays.asList("A", "B", "C"), newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VEnum[B(1), MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVDoubleArray() {
        assertThat(VTypeToString.toString(newVDoubleArray(new ArrayDouble(1.0, 2.0, 3.0, 4.0), alarmNone(), testTime, displayNone())),
                equalTo(String.format("VDoubleArray[[1.0, 2.0, 3.0, ...], size 4, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVDoubleArray(new ArrayDouble(1.0, 2.0, 3.0, 4.0), newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime, displayNone())),
                equalTo(String.format("VDoubleArray[[1.0, 2.0, 3.0, ...], size 4, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVStringArray() {
        assertThat(VTypeToString.toString(newVStringArray(Arrays.asList("A", "B", "C", "D"), alarmNone(), testTime)),
                equalTo(String.format("VStringArray[[A, B, C, ...], size 4, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVStringArray(Arrays.asList("A", "B", "C", "D"), newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VStringArray[[A, B, C, ...], size 4, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVBooleanArray() {
        assertThat(VTypeToString.toString(newVBooleanArray(new ArrayBoolean(true, false, true, false), alarmNone(), testTime)),
                equalTo(String.format("VBooleanArray[[true, false, true, ...], size 4, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVBooleanArray(new ArrayBoolean(true, false, true, false), newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VBooleanArray[[true, false, true, ...], size 4, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVEnumArray() {
        assertThat(VTypeToString.toString(newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), testTime)),
                equalTo(String.format("VEnumArray[[TWO, ONE, THREE], size 3, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VEnumArray[[TWO, ONE, THREE], size 3, MINOR(HIGH), %s]", testTimeString)));
    }

    @Test
    public void toStringVTable() {
        assertThat(VTypeToString.toString(newVTable(Arrays.<Class<?>>asList(String.class, double.class, int.class),
                Arrays.asList("Name", "Value", "Index"),
                Arrays.<Object>asList(Arrays.asList("A", "B", "C", "D"),
                new ArrayDouble(1.3,4.23,13,321.4),
                new ArrayInt(1,2,3,4)))),
                equalTo("VTable[3x4, [Name, Value, Index]]"));
    }

    @Test
    public void toStringVBoolean() {
        assertThat(VTypeToString.toString(newVBoolean(true, alarmNone(), testTime)),
                equalTo(String.format("VBoolean[true, %s]", testTimeString)));
        assertThat(VTypeToString.toString(newVBoolean(false, newAlarm(AlarmSeverity.MINOR, "HIGH"), testTime)),
                equalTo(String.format("VBoolean[false, MINOR(HIGH), %s]", testTimeString)));
    }
}
