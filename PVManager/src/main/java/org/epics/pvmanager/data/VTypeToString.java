/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.epics.pvmanager.util.NumberFormats;

/**
 * Helper class that provides default implementation of toString for VTypes.
 *
 * @author carcassi
 */
public class VTypeToString {
    private VTypeToString() {
        // Do not create
    }
    
    private static void appendAlarm(StringBuilder builder, Alarm alarm) {
        if (!alarm.getAlarmSeverity().equals(AlarmSeverity.NONE)) {
            builder.append(", ")
                    .append(alarm.getAlarmSeverity())
                    .append("(")
                    .append(alarm.getAlarmName())
                    .append(")");
        }
    }
    
    public static String toString(VNumber vNumber) {
        StringBuilder builder = new StringBuilder();
        Class type = ValueUtil.typeOf(vNumber);
        builder.append(type.getSimpleName())
                .append('[')
                .append(vNumber.getValue());
        appendAlarm(builder, vNumber);
        builder.append(", ")
                .append(vNumber.getTimestamp())
                .append(']');
        return builder.toString();
    }
    
    public static String toString(VString vString) {
        StringBuilder builder = new StringBuilder();
        Class type = ValueUtil.typeOf(vString);
        builder.append(type.getSimpleName())
                .append("[\"")
                .append(vString.getValue())
                .append('\"');
        appendAlarm(builder, vString);
        builder.append(", ")
                .append(vString.getTimestamp())
                .append(']');
        return builder.toString();
    }
    
    public static String toString(VEnum vEnum) {
        StringBuilder builder = new StringBuilder();
        Class type = ValueUtil.typeOf(vEnum);
        builder.append(type.getSimpleName())
                .append("[\"")
                .append(vEnum.getValue())
                .append("\"(")
                .append(vEnum.getIndex())
                .append(")");
        appendAlarm(builder, vEnum);
        builder.append(", ")
                .append(vEnum.getTimestamp())
                .append(']');
        return builder.toString();
    }
    
    private final static ValueFormat format = new SimpleValueFormat(3);
    
    static {
        format.setNumberFormat(NumberFormats.toStringFormat());
    }
    
    public static String toString(VNumberArray vNumberArray) {
        StringBuilder builder = new StringBuilder();
        Class type = ValueUtil.typeOf(vNumberArray);
        builder.append(type.getSimpleName())
                .append("[");
        builder.append(format.format(vNumberArray));
        builder.append(", size ")
                .append(vNumberArray.getData().size());
        appendAlarm(builder, vNumberArray);
        builder.append(", ")
                .append(vNumberArray.getTimestamp())
                .append(']');
        return builder.toString();
    }
}
