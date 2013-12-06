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
 * A timing system to track a set of durations of a process.
 * Useful in profiling where the stop watch starts counting
 * at the beginning of a process, stops counting at the end,
 * and then repeats multiple iterations.
 * 
 * @author carcassi
 */
public class StopWatch {
    private long start;
    private int nAttempts = 0;
    private final long[] timings;
    
    /**
     * Constructs and initializes the watch from the max number
     * of times the watch will be stopped.
     * The watch cannot <coe>stop</code> more than the
     * number of <code>maxAttempts</code>.
     * @param maxAttempts max number of times that will be tracked 
     */
    public StopWatch(int maxAttempts) {
        timings = new long[maxAttempts];
    }
    
    /**
     * Starts the watch.
     * The watch should not <code>start</code> more than the max
     * number of attempts set in the constructor.
     */
    public void start() {
        if (nAttempts >= timings.length){
            throw new ArrayIndexOutOfBoundsException("The stop watch has reached the maximum timings it can track.");
        }
        
        start = System.nanoTime();
    }
    
    /**
     * Stops the watch.
     * The watch should not <code>stop</code> more than the max
     * number of attempts set in the constructor.
     * This is NOT verified in the method call as to improve efficiency.
     */
    public void stop() {
        timings[nAttempts] = System.nanoTime() - start;
        nAttempts++;
    }
    
    /**
     * Returns the average time in ms in the set of timings.
     * @return average (start - stop) time in ms
     */
    public double getAverageMs() {
        double average = 0.0;
        for (int i = 0; i < nAttempts; i++) {
            average += timings[i] / 1000000.0;
        }
        return average / nAttempts;
    }
    
    /**
     * Returns the total time in ms in the set of timings.
     * @return total time tracked by watch
     */
    public long getTotalMs() {
        long total = 0;
        for (int i = 0; i < nAttempts; i++) {
            total += timings[i] / 1000000.0;
        }
        return total;
    }

    /**
     * Returns a copy of the set of times
     * @return copy of times
     */
    public ListLong getNanoTimings() {
        return new ArrayLong(Arrays.copyOfRange(timings, 0, nAttempts));
    }
    
    /**
     * Gets the average of all sets of timings after the first <code>start</code> timings.
     * Inclusive of <code>timings[start]</code> and inclusive of <code>timings[MAX]</code>.
     * 
     * Example:
     *          timings = [5 10 15 20 25]
     *          start = 2
     *          timings[start] = 15
     * 
     *          averages[0] = 15 / 1
     *          averages[1] = (15 + 20) / 2
     *          averages[2] = (15 + 20 + 25) / 2
     * @param start starting index of timings 
     * @return the set of averages of timings
     */
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
