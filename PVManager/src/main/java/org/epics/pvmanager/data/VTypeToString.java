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
    
    public static String toString(VNumber vNumber) {
        StringBuilder builder = new StringBuilder();
        Class type = ValueUtil.typeOf(vNumber);
        builder.append(type.getSimpleName())
                .append('[')
                .append(vNumber.getValue());
        if (!vNumber.getAlarmSeverity().equals(AlarmSeverity.NONE)) {
            builder.append(", ")
                    .append(vNumber.getAlarmSeverity())
                    .append("(")
                    .append(vNumber.getAlarmName())
                    .append(")");
        }
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
        if (!vString.getAlarmSeverity().equals(AlarmSeverity.NONE)) {
            builder.append(", ")
                    .append(vString.getAlarmSeverity())
                    .append("(")
                    .append(vString.getAlarmName())
                    .append(")");
        }
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
        if (!vEnum.getAlarmSeverity().equals(AlarmSeverity.NONE)) {
            builder.append(", ")
                    .append(vEnum.getAlarmSeverity())
                    .append("(")
                    .append(vEnum.getAlarmName())
                    .append(")");
        }
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
        if (!vNumberArray.getAlarmSeverity().equals(AlarmSeverity.NONE)) {
            builder.append(", ")
                    .append(vNumberArray.getAlarmSeverity())
                    .append("(")
                    .append(vNumberArray.getAlarmName())
                    .append(")");
        }
        builder.append(", ")
                .append(vNumberArray.getTimestamp())
                .append(']');
        return builder.toString();
    }
}
