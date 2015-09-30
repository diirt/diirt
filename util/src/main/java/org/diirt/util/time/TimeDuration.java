/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

import java.time.Duration;

/**
 * 
 * @author carcassi
 */
public class TimeDuration {
    
    private static final int NANOSEC_IN_SEC = 1000000000;

    private TimeDuration() {
    }

    public static Duration createDuration(long sec, int nanoSec) {
        return Duration.ofSeconds(sec).plusNanos(nanoSec);
    }

    /**
     * A new duration in hours.
     * 
     * @param hour hours
     * @return a new duration
     */
    public static Duration ofHours(double hour) {
        return Duration.ofNanos((long) (hour * 60 * 60 * 1000000000));
    }
    
    /**
     * A new duration in minutes.
     * 
     * @param min minutes
     * @return a new duration
     */
    public static Duration ofMinutes(double min) {
        return Duration.ofNanos((long) (min * 60 * 1000000000));
    }
    
    /**
     * A new duration in seconds.
     * 
     * @param sec seconds
     * @return a new duration
     */
    public static Duration ofSeconds(double sec) {
        return Duration.ofNanos((long) (sec * 1000000000));
    }

    /**
     * A new duration in hertz, will convert to the length of the period.
     * 
     * @param hz frequency to be converted to a duration
     * @return a new duration
     */
    public static Duration ofHertz(double hz) {
        if (hz <= 0.0) {
            throw new IllegalArgumentException("Frequency has to be greater than 0.0");
        }
        return Duration.ofNanos((long) (1000000000.0 / hz));
    }

    /**
     * Returns the number of times the given duration is present in this duration.
     * 
     * @param duration another duration
     * @return the result of the division
     */
    public static int dividedBy(Duration dividendDuration, Duration divisorDuration) {
        // (a + b)/(c + d) = 1 / ((c + d) / a) + 1 / ((c + d) / b)
        // XXX This will not have the precision it should
        double thisDuration = (double) dividendDuration.getSeconds() * (double) NANOSEC_IN_SEC + (double) dividendDuration.getNano();
        double otherDuration = (double) divisorDuration.getSeconds() * (double) NANOSEC_IN_SEC + (double) divisorDuration.getNano();
        return (int) (thisDuration / otherDuration);
    }

}
