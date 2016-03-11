/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import java.util.Arrays;
import org.diirt.util.array.ArrayBoolean;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.diirt.vtype.ValueFactory.*;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.time.Timestamp;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class VTypeToStringTest {

    public VTypeToStringTest() {
    }

    @Test
    public void toStringVNumber() {
        assertThat(VTypeToString.toString(newVDouble(3.0, alarmNone(), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDouble[3.0, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVDouble(133.0, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDouble[133.0, MINOR(LOW), 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDouble[3.14, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVInt(3, alarmNone(), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VInt[3, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVInt(4, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VInt[4, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVString() {
        assertThat(VTypeToString.toString(newVString("Testing", alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VString[Testing, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVString("Testing", newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VString[Testing, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVEnum() {
        assertThat(VTypeToString.toString(newVEnum(1, Arrays.asList("A", "B", "C"), alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VEnum[B(1), 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVEnum(1, Arrays.asList("A", "B", "C"), newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VEnum[B(1), MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVDoubleArray() {
        assertThat(VTypeToString.toString(newVDoubleArray(new ArrayDouble(1.0, 2.0, 3.0, 4.0), alarmNone(), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDoubleArray[[1.0, 2.0, 3.0, ...], size 4, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVDoubleArray(new ArrayDouble(1.0, 2.0, 3.0, 4.0), newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDoubleArray[[1.0, 2.0, 3.0, ...], size 4, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVStringArray() {
        assertThat(VTypeToString.toString(newVStringArray(Arrays.asList("A", "B", "C", "D"), alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VStringArray[[A, B, C, ...], size 4, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVStringArray(Arrays.asList("A", "B", "C", "D"), newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VStringArray[[A, B, C, ...], size 4, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVBooleanArray() {
        assertThat(VTypeToString.toString(newVBooleanArray(new ArrayBoolean(true, false, true, false), alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VBooleanArray[[true, false, true, ...], size 4, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVBooleanArray(new ArrayBoolean(true, false, true, false), newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VBooleanArray[[true, false, true, ...], size 4, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }

    @Test
    public void toStringVEnumArray() {
        assertThat(VTypeToString.toString(newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VEnumArray[[TWO, ONE, THREE], size 3, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VEnumArray[[TWO, ONE, THREE], size 3, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
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
        assertThat(VTypeToString.toString(newVBoolean(true, alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VBoolean[true, 1970/01/15 01:56:07.123]"));
        assertThat(VTypeToString.toString(newVBoolean(false, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VBoolean[false, MINOR(HIGH), 1970/01/15 01:56:07.123]"));
    }
}
