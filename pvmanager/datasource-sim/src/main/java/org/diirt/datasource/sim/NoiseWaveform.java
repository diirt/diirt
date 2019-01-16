/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import java.time.Instant;
import java.util.Random;

import org.epics.util.array.ArrayDouble;
import org.epics.util.stats.Range;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDoubleArray;

/**
 * Function to simulate a waveform containing a uniformly distributed
 * random data.
 *
 * @author carcassi
 */
public class NoiseWaveform extends SimFunction<VDoubleArray> {

    private Random rand = new Random();
    private double min;
    private double max;
    private int nSamples;
    private double range;
    private VDoubleArray lastValue;

    /**
     * Creates a waveform with samples from a uniform distribution from -5 to 5,
     * updating every second.
     */
    public NoiseWaveform() {
        this(-5.0, 5.0, 1.0);
    }

    /**
     * Creates a gaussian waveform signal with a gaussian distribution, updating at the rate
     * specified.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param interval time between samples in seconds
     */
    public NoiseWaveform(Double min, Double max, Double interval) {
        this(min, max, 100.0, interval);
    }

    /**
     * Creates a gaussian waveform signal with a gaussian distribution, updating at the rate
     * specified.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param nSamples number of elements in the waveform
     * @param interval time between samples in seconds
     */
    public NoiseWaveform(Double min, Double max, Double nSamples, Double interval) {
        super(interval, VDoubleArray.class);
        this.min = min;
        this.max = max;
        range = this.max - this.min;
        this.nSamples = nSamples.intValue();
        if (this.nSamples <= 0) {
            throw new IllegalArgumentException("Number of sample must be a positive integer.");
        }
    }

    private double[] generateNewValue() {
        double[] newArray = new double[nSamples];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = min + rand.nextDouble() * (max - min);
        }
        return newArray;
    }

    @Override
    VDoubleArray nextValue() {
        if (lastTime == null)
            lastTime = Instant.now();
        return VDoubleArray.of(ArrayDouble.of(generateNewValue()),
                Alarm.none(),
                Time.of(lastTime),
                Display.of(Range.of(min, max),
                           Range.of(min + range * 0.1, min + range * 0.9),
                           Range.of(min + range * 0.2, min + range * 0.8),
                           Range.of(min, max),
                           "x",
                           Constants.DOUBLE_FORMAT));
    }
}
