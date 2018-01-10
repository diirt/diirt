/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.displayNone;
import static org.diirt.vtype.ValueFactory.newAlarm;
import static org.diirt.vtype.ValueFactory.newTime;
import static org.diirt.vtype.ValueFactory.newVDouble;
import static org.diirt.vtype.ValueFactory.newVInt;
import static org.diirt.vtype.ValueFactory.newVString;
import static org.diirt.vtype.ValueFactory.timeNow;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.Instant;

import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class VTypeValueEqualsTest {

    public VTypeValueEqualsTest() {
    }

    @Test
    public void alarmEquals1() {
        assertThat(VTypeValueEquals.alarmEquals(newAlarm(AlarmSeverity.MINOR, "HIGH"), newAlarm(AlarmSeverity.MINOR, "HIGH")),
                equalTo(true));
        assertThat(VTypeValueEquals.alarmEquals(alarmNone(), alarmNone()),
                equalTo(true));
        assertThat(VTypeValueEquals.alarmEquals(alarmNone(), null),
                equalTo(false));
        assertThat(VTypeValueEquals.alarmEquals(null, alarmNone()),
                equalTo(false));
        assertThat(VTypeValueEquals.alarmEquals(null, null),
                equalTo(true));
        assertThat(VTypeValueEquals.alarmEquals(newAlarm(AlarmSeverity.MINOR, "HIGH"), newAlarm(AlarmSeverity.MINOR, "LOW")),
                equalTo(false));
        assertThat(VTypeValueEquals.alarmEquals(newAlarm(AlarmSeverity.MINOR, "HIGH"), newAlarm(AlarmSeverity.MAJOR, "HIGH")),
                equalTo(false));
    }

    @Test
    public void timeEquals1() {
        assertThat(VTypeValueEquals.timeEquals(newTime(Instant.ofEpochSecond(12340000, 0)), newTime(Instant.ofEpochSecond(12340000, 0))),
                equalTo(true));
        assertThat(VTypeValueEquals.timeEquals(newTime(Instant.ofEpochSecond(12340000, 1)), newTime(Instant.ofEpochSecond(12340000, 0))),
                equalTo(false));
        assertThat(VTypeValueEquals.timeEquals(newTime(Instant.ofEpochSecond(12340000, 0), 12, true), newTime(Instant.ofEpochSecond(12340000, 0), 12, true)),
                equalTo(true));
        assertThat(VTypeValueEquals.timeEquals(newTime(Instant.ofEpochSecond(12340000, 0), 11, false), newTime(Instant.ofEpochSecond(12340000, 0), 12, false)),
                equalTo(false));
        assertThat(VTypeValueEquals.timeEquals(newTime(Instant.ofEpochSecond(12340000, 0), 12, true), newTime(Instant.ofEpochSecond(12340000, 0), 12, false)),
                equalTo(false));
    }

    @Test
    public void typeEquals1() {
        assertThat(VTypeValueEquals.typeEquals(newVInt(0, alarmNone(), timeNow(), displayNone()),
                newVDouble(0.0)), equalTo(false));
        assertThat(VTypeValueEquals.typeEquals(newVInt(0, alarmNone(), timeNow(), displayNone()),
                newVInt(1, alarmNone(), timeNow(), displayNone())), equalTo(true));
        assertThat(VTypeValueEquals.typeEquals(newVString("Test", alarmNone(), timeNow()),
                newVDouble(0.0)), equalTo(false));
        assertThat(VTypeValueEquals.typeEquals(newVString("Test", alarmNone(), timeNow()),
                newVString("A", alarmNone(), timeNow())), equalTo(true));
    }
}
