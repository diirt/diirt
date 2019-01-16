/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vnumber;

import org.diirt.datasource.formula.vnumber.VNumberFunctionSet;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FunctionTester;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.junit.Test;


/**
 *
 * @author carcassi
 */
public class VNumberFunctionSetTest {

    private static FormulaFunctionSet set = new VNumberFunctionSet();

    @Test
    public void sum() {
        FunctionTester.findByName(set, "+")
                .compareReturnValue(3.0, 1.0, 2.0)
                .compareReturnValue(-1.0, 1.0, -2.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void subtract() {
        FunctionTester.findBySignature(set, "-", VNumber.class, VNumber.class)
                .compareReturnValue(-1.0, 1.0, 2.0)
                .compareReturnValue(3.0, 1.0, -2.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void negate() {
        FunctionTester.findBySignature(set, "-", VNumber.class)
                .compareReturnValue(-1.0, 1.0)
                .compareReturnValue(2.0, -2.0)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void multiply() {
        FunctionTester.findByName(set, "*")
                .compareReturnValue(10.0, 2.0, 5.0)
                .compareReturnValue(-6.0, 3.0, -2.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void divide() {
        FunctionTester.findByName(set, "/")
                .compareReturnValue(4.0, 8.0, 2.0)
                .compareReturnValue(-0.5, 1.0, -2.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void remainder() {
        FunctionTester.findByName(set, "%")
                .compareReturnValue(0.0, 8.0, 2.0)
                .compareReturnValue(1.0, 3.0, 2.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void pow() {
        FunctionTester.findByName(set, "^")
                .compareReturnValue(64.0, 8.0, 2.0)
                .compareReturnValue(2.0, 4.0, 0.5)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void lessThanOrEqual() {
        FunctionTester.findByName(set, "<=")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void greaterThanOrEqual() {
        FunctionTester.findByName(set, ">=")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void lessThan() {
        FunctionTester.findByName(set, "<")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void greaterThan() {
        FunctionTester.findByName(set, ">")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void equal() {
        FunctionTester.findByName(set, "==")
                .compareReturnValue(false, 1.0, 2.0)
                .compareReturnValue(true, 2.0, 2.0)
                .compareReturnValue(false, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void notEqual() {
        FunctionTester.findByName(set, "!=")
                .compareReturnValue(true, 1.0, 2.0)
                .compareReturnValue(false, 2.0, 2.0)
                .compareReturnValue(true, 2.0, 1.0)
                .compareReturnValue(null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), null)
                .compareReturnValue(null, null, VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()))
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void conditionalOr() {
        FunctionTester.findByName(set, "||")
                .compareReturnValue(true, true, true)
                .compareReturnValue(true, true, false)
                .compareReturnValue(true, false, true)
                .compareReturnValue(false, false, false)
                .compareReturnValue(null, true, null)
                .compareReturnValue(null, null, true)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void conditionalAnd() {
        FunctionTester.findByName(set, "&&")
                .compareReturnValue(true, true, true)
                .compareReturnValue(false, true, false)
                .compareReturnValue(false, false, true)
                .compareReturnValue(false, false, false)
                .compareReturnValue(null, true, null)
                .compareReturnValue(null, null, true)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void bitwiseXOR() {
        Alarm none = Alarm.none();
        Alarm minor = Alarm.of(AlarmSeverity.MINOR, null, "LOW");
        Time time1 = Time.now();
        Time time2 = Time.of(time1.getTimestamp().plus(Duration.ofMillis(100)));
        FunctionTester.findByName(set, "xor")
                .compareReturnValue(0b0110, 0b1100, 0b1010)
                .compareReturnValue(null, 0b1100, null)
                .compareReturnValue(null, null, 0b1010)
                .compareReturnAlarm(none, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, minor, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, minor, Time.now(), Display.none()))
                .compareReturnTime(time1, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time2, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time2, Display.none()));
    }

    @Test
    public void bitwiseOR() {
        Alarm none = Alarm.none();
        Alarm minor = Alarm.of(AlarmSeverity.MINOR, null, "LOW");
        Time time1 = Time.now();
        Time time2 = Time.of(time1.getTimestamp().plus(Duration.ofMillis(100)));
        FunctionTester.findByName(set, "or")
                .compareReturnValue(0b1110, 0b1100, 0b1010)
                .compareReturnValue(null, 0b1100, null)
                .compareReturnValue(null, null, 0b1010)
                .compareReturnAlarm(none, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, minor, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, minor, Time.now(), Display.none()))
                .compareReturnTime(time1, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time2, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time2, Display.none()));
        FunctionTester.findByName(set, "|")
                .compareReturnValue(0b1110, 0b1100, 0b1010)
                .compareReturnValue(null, 0b1100, null)
                .compareReturnValue(null, null, 0b1010)
                .compareReturnAlarm(none, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, minor, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, minor, Time.now(), Display.none()))
                .compareReturnTime(time1, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time2, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time2, Display.none()));
    }

    @Test
    public void bitwiseAND() {
        Alarm none = Alarm.none();
        Alarm minor = Alarm.of(AlarmSeverity.MINOR, null, "LOW");
        Time time1 = Time.now();
        Time time2 = Time.of(time1.getTimestamp().plus(Duration.ofMillis(100)));
        FunctionTester.findByName(set, "and")
                .compareReturnValue(0b1000, 0b1100, 0b1010)
                .compareReturnValue(null, 0b1100, null)
                .compareReturnValue(null, null, 0b1010)
                .compareReturnAlarm(none, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, minor, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, minor, Time.now(), Display.none()))
                .compareReturnTime(time1, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time2, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time2, Display.none()));
        FunctionTester.findByName(set, "&")
                .compareReturnValue(0b1000, 0b1100, 0b1010)
                .compareReturnValue(null, 0b1100, null)
                .compareReturnValue(null, null, 0b1010)
                .compareReturnAlarm(none, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, minor, Time.now(), Display.none()), VNumber.of(1, none, Time.now(), Display.none()))
                .compareReturnAlarm(minor, VNumber.of(1, none, Time.now(), Display.none()), VNumber.of(1, minor, Time.now(), Display.none()))
                .compareReturnTime(time1, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time2, Display.none()), VNumber.of(1, minor, time1, Display.none()))
                .compareReturnTime(time2, VNumber.of(1, none, time1, Display.none()), VNumber.of(1, minor, time2, Display.none()));
    }

    @Test
    public void logicalNot() {
        FunctionTester.findByName(set, "!")
                .compareReturnValue(true, false)
                .compareReturnValue(false, true)
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void conditionalOperator() {
        Alarm alarm1 = Alarm.none();
        Alarm alarm2 = Alarm.of(AlarmSeverity.MINOR, null, "LOW");
        Time time1 = Time.now();
        Time time2 = Time.of(time1.getTimestamp().plus(Duration.ofMillis(100)));
        FunctionTester.findByName(set, "?:")
                .compareReturnValue(1, true, 1, 0)
                .compareReturnValue(0, false, 1, 0)
                .compareReturnValue(null, null, 1, 0)
                .compareReturnValue(null, true, null, 0)
                .compareReturnValue(1, true, 1, null)
                .compareReturnValue(0, false, null, 0)
                .compareReturnValue(null, false, 1, null)
                .compareReturnAlarm(alarm1, true, alarm1, alarm2)
                .compareReturnAlarm(alarm2, false, alarm1, alarm2)
                .compareReturnTime(time1, true, time1, time2)
                .compareReturnTime(time2, false, time1, time2);
    }
}
