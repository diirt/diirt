/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import static org.epics.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimeTest {

    public TimeTest() {
    }
    
    @Test
    public void nanoSecCarry() {
        TimeStamp time = TimeStamp.time(100, 100000000);
        TimeStamp newTime = time.plus(TimeDuration.ofNanos(999000000));
        assertEquals(TimeStamp.time(101, 99000000), newTime);

        newTime = time.plus(TimeDuration.ofMillis(1000));
        assertEquals(TimeStamp.time(101, 100000000), newTime);

        newTime = time.plus(TimeDuration.ofMillis(5443));
        assertEquals(TimeStamp.time(105, 543000000), newTime);

        newTime = time.minus(TimeDuration.ofMillis(1000));
        assertEquals(TimeStamp.time(99, 100000000), newTime);

        newTime = time.minus(TimeDuration.ofNanos(999000000));
        assertEquals(TimeStamp.time(99, 101000000), newTime);
    }

    @Test
    public void testDurationFrom() {
        TimeStamp reference = TimeStamp.now();
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ofMillis(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ofMillis(10))).getNanoSec());
        reference = TimeStamp.time(10, 999999999);
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ofMillis(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ofMillis(10))).getNanoSec());
        reference = TimeStamp.time(10, 1);
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.ofNanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ofMillis(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ofMillis(10))).getNanoSec());
    }
}