/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

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
    
    public double[] getData() {
        double[] data = new double[nAttempts];
        for (int i = 0; i < nAttempts; i++) {
            data[i] += timings[i] / 1000000.0;
        }
        return data;
    }
}
