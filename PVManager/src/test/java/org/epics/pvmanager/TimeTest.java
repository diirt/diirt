/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class TimeTest {

    public TimeTest() {
    }

    @Test
    public void nanoSecCarry() {
        TimeStamp time = TimeStamp.epicsTime(100, 100000000);
        TimeStamp newTime = time.plus(TimeDuration.nanos(999000000));
        assertEquals(TimeStamp.epicsTime(101, 99000000), newTime);

        newTime = time.plus(TimeDuration.ms(1000));
        assertEquals(TimeStamp.epicsTime(101, 100000000), newTime);

        newTime = time.plus(TimeDuration.ms(5443));
        assertEquals(TimeStamp.epicsTime(105, 543000000), newTime);

        newTime = time.minus(TimeDuration.ms(1000));
        assertEquals(TimeStamp.epicsTime(99, 100000000), newTime);

        newTime = time.minus(TimeDuration.nanos(999000000));
        assertEquals(TimeStamp.epicsTime(99, 101000000), newTime);
    }

    @Test
    public void testDurationFrom() {
        TimeStamp reference = TimeStamp.now();
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ms(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ms(10))).getNanoSec());
        reference = TimeStamp.time(10, 999999999);
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ms(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ms(10))).getNanoSec());
        reference = TimeStamp.time(10, 1);
        assertEquals(10, reference.durationFrom(reference.plus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10, reference.durationFrom(reference.minus(TimeDuration.nanos(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.plus(TimeDuration.ms(10))).getNanoSec());
        assertEquals(10000000, reference.durationFrom(reference.minus(TimeDuration.ms(10))).getNanoSec());
    }

}