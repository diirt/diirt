/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.Collections;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 * Simulated function for a ramp.
 *
 * @author carcassi
 */
class Ramp extends SimFunction<VDouble> {

    private double min;
    private double max;
    private double currentValue;
    private double step;
    private double range;
    private VDouble lastValue;

    public Ramp(Double min, Double max, Double step, Double interval) {
        super(interval, VDouble.class);
        if (interval <= 0.0) {
            throw new IllegalArgumentException("Interval must be greater than zero (was " + interval + ")");
        }
        this.min = min;
        this.max = max;
        this.currentValue = min - step;
        this.step = step;
        range = max - min;
        lastValue = ValueFactory.newVDouble(currentValue, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Constants.POSSIBLE_ALARM_STATUS, TimeStamp.now(), null,
                min, min + range * 0.1, min + range * 0.2, "x", Constants.DOUBLE_FORMAT,
                min + range * 0.8, min + range * 0.9, max, min, max);
    }

    @Override
    public VDouble nextValue() {
        currentValue = currentValue + step;
        if (currentValue > max) {
            currentValue = min;
        }

        return newValue(currentValue, lastValue);
    }
}
