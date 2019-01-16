/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.stats.Range;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VNumberArray;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Function to simulate a waveform containing a square wave.
 *
 * @author carcassi
 */
public class SquareWaveform extends SimFunction<VDoubleArray> {

    private double periodInSeconds;
    private double wavelengthInSamples;
    private int nSamples;
    private Instant initialRefernce;

    /**
     * Creates a square wave of 100 samples, with period of 1 second, wavelength of
     * 100 samples, updating at 10 Hz.
     */
    public SquareWaveform() {
        this(1.0, 100.0, 0.1);
    }

    /**
     * Creates a square wave of 100 samples, with given period and given wavelength of
     * 100 samples, updating at given rate.
     *
     * @param periodInSeconds the period measured in seconds
     * @param wavelengthInSamples the wavelength measured in samples
     * @param updateRateInSeconds the update rate in seconds
     */
    public SquareWaveform(Double periodInSeconds, Double wavelengthInSamples, Double updateRateInSeconds) {
        this(periodInSeconds, wavelengthInSamples, 100.0, updateRateInSeconds);
    }

    /**
     * Creates a square wave of 100 samples, with given period and given wavelength of
     * given number of samples, updating at given rate.
     *
     * @param periodInSeconds the period measured in seconds
     * @param wavelengthInSamples the wavelength measured in samples
     * @param nSamples the number of samples
     * @param updateRateInSeconds the update rate in seconds
     */
    public SquareWaveform(Double periodInSeconds, Double wavelengthInSamples, Double nSamples, Double updateRateInSeconds) {
        super(updateRateInSeconds, VDoubleArray.class);
        this.periodInSeconds = periodInSeconds;
        this.wavelengthInSamples = wavelengthInSamples;
        this.nSamples = nSamples.intValue();
        if (this.nSamples <= 0) {
            throw new IllegalArgumentException("Number of sample must be a positive integer.");
        }
    }

    private ListDouble generateNewValue(final double omega, final double t, double k) {
        double[] newArray = new double[nSamples];
        for (int i = 0; i < newArray.length; i++) {
            double x = (omega * t + k * i) / (2 * Math.PI);
            double normalizedPositionInPeriod = x - (double) (long) x;
            if (normalizedPositionInPeriod < 0.5) {
                newArray[i] = 1.0;
            } else if (normalizedPositionInPeriod < 1.0) {
                newArray[i] = -1.0;
            } else {
                newArray[i] = 1.0;
            }
        }
        return ArrayDouble.of(newArray);
    }

    @Override
    VDoubleArray nextValue() {
        if (initialRefernce == null) {
            initialRefernce = lastTime;
        }
        double t = initialRefernce.until(lastTime, ChronoUnit.SECONDS);
        double omega = 2 * Math.PI / periodInSeconds;
        double k = 2 * Math.PI / wavelengthInSamples;
        double min = 1.0;
        double max = -1.0;
        double range = 0.0;
        return (VDoubleArray) VNumberArray.of(generateNewValue(omega, t, k),
                Alarm.none(),
                Time.now(),
                Display.of(Range.of(min, max),
                           Range.of(min + range * 0.1, min + range * 0.9),
                           Range.of(min + range * 0.2, min + range * 0.8),
                           Range.of(min, max),
                           "x",
                           Constants.DOUBLE_FORMAT));
    }
}
