/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.math.BigInteger;
import java.util.Arrays;
import org.epics.util.array.ArrayLong;
import org.epics.util.array.ListLong;

/**
 *
 * @author carcassi
 */
public class StopWatch {
    private long start;
    private int nAttempts = 0;
    private final long[] timings;
    
    public StopWatch(int maxAttempts) {
        timings = new long[maxAttempts];
    }
    
    public void start() {
        start = System.nanoTime();
    }
    
    public void stop() {
        timings[nAttempts] = System.nanoTime() - start;
        nAttempts++;
    }
    
    public double getAverageMs() {
        double average = 0.0;
        for (int i = 0; i < nAttempts; i++) {
            average += timings[i] / 1000000.0;
        }
        return average / nAttempts;
    }
    
    public long getTotalMs() {
        long total = 0;
        for (int i = 0; i < nAttempts; i++) {
            total += timings[i] / 1000000.0;
        }
        return total;
    }
    
    public double[] getData() {
        double[] data = new double[nAttempts];
        for (int i = 0; i < nAttempts; i++) {
            data[i] += timings[i] / 1000000.0;
        }
        return data;
    }
    
    public ListLong getNanoTimings() {
        return new ArrayLong(Arrays.copyOfRange(timings, 0, nAttempts));
    }
    
    public ListLong getNanoAverages(int start) {
        long[] averages = new long[nAttempts - start];
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < averages.length; i++) {
            total = total.add(BigInteger.valueOf(timings[i + start]));
            averages[i] = total.divide(BigInteger.valueOf(i+1)).longValue();
        }
        return new ArrayLong(averages);
    }
}
