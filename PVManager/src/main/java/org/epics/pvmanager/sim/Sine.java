/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 * Simulated function for a sine.
 *
 * @author carcassi
 */
class Sine extends SimFunction<VDouble> {

    private double min;
    private double max;
    private long currentValue;
    private double samplesPerCycle;
    private double range;
    private VDouble lastValue;

    public Sine(Double min, Double max, Double samplesPerCycle, Double secondsBeetwenSamples) {
        super(secondsBeetwenSamples, VDouble.class);
        this.min = min;
        this.max = max;
        this.currentValue = 0;
        this.samplesPerCycle = samplesPerCycle;
        range = max - min;
        lastValue = ValueFactory.newVDouble(0.0, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Constants.POSSIBLE_ALARM_STATUS, TimeStamp.now(), null,
                min, min + range * 0.1, min + range * 0.2, "x", Constants.DOUBLE_FORMAT,
                min + range * 0.8, min + range * 0.9, max, min, max);
    }

    @Override
    public VDouble nextValue() {
        double value = Math.sin(currentValue * 2 * Math.PI /samplesPerCycle) * range / 2 + min + (range / 2);
        currentValue++;

        return newValue(value, lastValue);
    }
}
