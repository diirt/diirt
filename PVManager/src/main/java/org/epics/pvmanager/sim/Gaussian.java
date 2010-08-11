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
class Gaussian extends SimFunction<VDouble> {

    private Random rand = new Random();
    private double average;
    private double stdDev;
    private VDouble lastValue;

    public Gaussian() {
        this(1.0, 1.0, 0.1);
    }

    public Gaussian(Double average, Double stdDev, Double interval) {
        super(interval, VDouble.class);
        if (interval <= 0.0) {
            throw new IllegalArgumentException("Interval must be greater than zero (was " + interval + ")");
        }
        this.average = average;
        this.stdDev = stdDev;
        lastValue = ValueFactory.newVDouble(average, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Constants.POSSIBLE_ALARM_STATUS, TimeStamp.now(), null,
                average - 4 * stdDev, average - 2 * stdDev, average - stdDev, "x", Constants.DOUBLE_FORMAT,
                average + stdDev, average + 2 * stdDev, average + 4 * stdDev, average - 4 * stdDev, average + 4 * stdDev);
    }

    @Override
    public VDouble nextValue() {
        return newValue(average + rand.nextGaussian() * stdDev, lastValue);
    }
}
