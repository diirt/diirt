/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.epics.pvmanager.ReadFunction;

/**
 * Converts the value of the argument to a VString.
 *
 * @author carcassi
 */
class VStringOfFunction implements ReadFunction<VString> {
    
    private final ReadFunction<? extends VType> argument;
    private final ValueFormat format;

    public VStringOfFunction(ReadFunction<? extends VType> argument, ValueFormat format) {
        this.argument = argument;
        this.format = format;
    }

    @Override
    public VString readValue() {
        VType value = argument.readValue();
        if (value == null) {
            return null;
        }
        String string = format.format(value);
        Alarm alarm = ValueUtil.alarmOf(value);
        if (alarm == null) {
            alarm = ValueFactory.alarmNone();
        }
        Time time = ValueUtil.timeOf(value);
        if (time == null) {
            time = ValueFactory.timeNow();
        }
        return ValueFactory.newVString(string, alarm, time);
    }
    
}
