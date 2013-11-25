/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.epics.pvmanager.integration;

import org.epics.vtype.VTypeToString;
import org.epics.vtype.VTypeValueEquals;
import org.epics.vtype.ValueUtil;

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
        if (value && !VTypeValueEquals.typeEquals(actualValue, expectedValue)) {
            return "TYPE mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        } else if (value && !VTypeValueEquals.valueEquals(actualValue, expectedValue)) {
            return "VALUE mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        } else if (alarm && !VTypeValueEquals.alarmEquals(ValueUtil.alarmOf(actualValue), ValueUtil.alarmOf(expectedValue))) {
            return "ALARM mismatch: was " + VTypeToString.alarmToString(ValueUtil.alarmOf(actualValue))
                    + " (expected " + VTypeToString.alarmToString(ValueUtil.alarmOf(expectedValue)) + ")";
        } else if (time && !VTypeValueEquals.timeEquals(ValueUtil.timeOf(actualValue), ValueUtil.timeOf(expectedValue))) {
            return "TIME mismatch: was " + actualValue + " (expected " + expectedValue + ")";
        }
        return null;
    }
    
    
}
