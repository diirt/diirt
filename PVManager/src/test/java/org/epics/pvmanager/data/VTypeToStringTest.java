/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.pvmanager.data.ValueFactory.*;
import org.epics.util.time.Timestamp;
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
                equalTo("VDouble[3.0, 1234567.123000000]"));
        assertThat(VTypeToString.toString(newVDouble(133.0, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDouble[133.0, MINOR(LOW), 1234567.123000000]"));
        assertThat(VTypeToString.toString(newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VDouble[3.14, MINOR(HIGH), 1234567.123000000]"));
        assertThat(VTypeToString.toString(newVInt(3, alarmNone(), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VInt[3, 1234567.123000000]"));
        assertThat(VTypeToString.toString(newVInt(4, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)), displayNone())),
                equalTo("VInt[4, MINOR(HIGH), 1234567.123000000]"));
    }

    @Test
    public void toStringVString() {
        assertThat(VTypeToString.toString(newVString("Testing", alarmNone(), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VString[\"Testing\", 1234567.123000000]"));
        assertThat(VTypeToString.toString(newVString("Testing", newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(1234567, 123000000)))),
                equalTo("VString[\"Testing\", MINOR(HIGH), 1234567.123000000]"));
    }
}
