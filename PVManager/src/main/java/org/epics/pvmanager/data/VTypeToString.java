/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.data;

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
}
