/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.utils;

import org.diirt.graphene.profile.settings.Settings;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.util.Arrays;
import org.diirt.util.array.ArrayLong;
import org.diirt.util.array.ListLong;

/**
 * A timing system to track a set of durations of a process.
 * Useful in profiling where the stop watch starts counting
 * at the beginning of a process, stops counting at the end,
 * and then repeats multiple iterations.
 *
 * @author carcassi
 * @author asbarber
 */
public class StopWatch implements Settings{

    /**
     * Type of measurement for time.
     */
    public enum TimeType{
        /**
         * Uses Sytem.nano() time.
         */
        System,

        /**
         * Uses OperatinSystemMXBean bean.getProcessCpuTime()
         */
        Cpu
    };

    //Data Members
    //--------------------------------------------------------------------------
    private long            start;
    private int             nAttempts = 0;
    private final long[]    timings;

    private TimeType                timeType = TimeType.Cpu;
    private OperatingSystemMXBean   bean;
    //--------------------------------------------------------------------------


    //Constructor
    //--------------------------------------------------------------------------

    /**
     * Constructs and initializes the watch from the max number
     * of times the watch will be stopped.
     * The watch cannot <code>stop</code> more than the
     * number of <code>maxAttempts</code>.
     * @param maxAttempts max number of times that will be tracked
     */
    public StopWatch(int maxAttempts) {
        timings = new long[maxAttempts];
        bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    //--------------------------------------------------------------------------


    //Time Tracking
    //--------------------------------------------------------------------------

    /**
     * Starts the watch.
     * The watch should not <code>start</code> more than the max
     * number of attempts set in the constructor.
     */
    public void start() {
        if (nAttempts >= timings.length){
            throw new ArrayIndexOutOfBoundsException("The stop watch has reached the maximum timings it can track.");
        }

        if (timeType == TimeType.System){
            start = System.nanoTime();
        }
        else{
            start = bean.getProcessCpuTime();
        }
    }

    /**
     * Stops the watch.
     * The watch should not <code>stop</code> more than the max
     * number of attempts set in the constructor.
     * This is NOT verified in the method call as to improve efficiency.
     */
    public void stop() {
        if (timeType == TimeType.System){
            timings[nAttempts] = System.nanoTime() - start;
        }
        else{
            timings[nAttempts] = bean.getProcessCpuTime() - start;
        }

        nAttempts++;
    }

    //--------------------------------------------------------------------------


    //Getters
    //--------------------------------------------------------------------------

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

    /**
     * Gets the type of time used by the watch.
     * This corresponds to the utility used to measure time changes.
     * @return type of time (sytem or cpu)
     */
    public TimeType getTimeType(){
        return this.timeType;
    }

    //--------------------------------------------------------------------------


    //Setters
    //--------------------------------------------------------------------------

    /**
     * Sets the utility used to measure time changes.
     * @param type type of time (system or cpu)
     */
    public void setTimeType(TimeType type){
        this.timeType = type;
    }

    //--------------------------------------------------------------------------


    //FORMAT FOR OUTPUT FILES
    //--------------------------------------------------------------------------

    @Override
    public String[] getTitle() {
        return new String[]{
            "Average Time (ms)",
            "Total Time (ms)"
        };
    }

    @Override
    public Object[] getOutput() {
        return new String[]{
            ((Double)getAverageMs()).toString(),
            ((Long)getTotalMs()).toString()
        };
    }

    //--------------------------------------------------------------------------

}
