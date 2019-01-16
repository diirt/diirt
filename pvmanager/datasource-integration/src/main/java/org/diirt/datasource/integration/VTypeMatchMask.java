/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import org.epics.vtype.Alarm;
import org.epics.vtype.Time;

/**
 *
 * @author carcassi
 */
public enum VTypeMatchMask {

    VALUE(true, false, false, false),
    ALARM(false, true, false, false),
    TIME(false, false, true, false),
//    DISPLAY(false, false, false, true),
    ALL(true, true, true, true),
    ALL_EXCEPT_TIME(true, true, false, true);

    private final boolean value;
    private final boolean alarm;
    private final boolean time;
    private final boolean display;

    private VTypeMatchMask(boolean value, boolean alarm, boolean time, boolean display) {
        this.value = value;
        this.alarm = alarm;
        this.time = time;
        this.display = display;
    }

    public String match(Object expectedValue, Object actualValue) {
        if (value && !expectedValue.equals(actualValue)) {
            return "TYPE mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        } else if (value && !expectedValue.equals(actualValue)) {
            return "VALUE mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        } else if (alarm && !Alarm.alarmOf(expectedValue).equals(Alarm.alarmOf(actualValue))) {
            return "ALARM mismatch: was " + Alarm.alarmOf(actualValue)
                    + " (expected " + Alarm.alarmOf(expectedValue) + ")";
        } else if (time && !Time.timeOf(expectedValue).equals(Time.timeOf(actualValue))) {
            return "TIME mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        }
        return null;
    }


}
