/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.Collections;
import java.util.Random;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 * Simulated function for a noise.
 *
 * @author carcassi
 */
class Noise extends SimFunction<VDouble> {

    private Random rand = new Random();
    private double min;
    private double max;
    private double range;
    private double interval;
    private VDouble lastValue;

    public Noise() {
        this(-5.0, 5.0, 0.1);
    }

    public Noise(Double min, Double max, Double interval) {
        super(interval, VDouble.class);
        if (interval <= 0.0) {
            throw new IllegalArgumentException("Interval must be greater than zero (was " + interval + ")");
        }
        this.min = min;
        this.max = max;
        this.interval = interval;
        range = max - min;
        lastValue = ValueFactory.newVDouble(min, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Constants.POSSIBLE_ALARM_STATUS, TimeStamp.now(), null,
                min, min + range * 0.1, min + range * 0.2, "x", Constants.DOUBLE_FORMAT,
                min + range * 0.8, min + range * 0.9, max, min, max);
    }

    @Override
    public VDouble nextValue() {
        return newValue(min + rand.nextDouble() * range, lastValue);
    }
}
