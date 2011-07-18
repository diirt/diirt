/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

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
        TimeStamp newTime = time.plus(TimeDuration.nanos(999000000));
        assertEquals(TimeStamp.time(101, 99000000), newTime);

        newTime = time.plus(TimeDuration.ms(1000));
        assertEquals(TimeStamp.time(101, 100000000), newTime);

        newTime = time.plus(TimeDuration.ms(5443));
        assertEquals(TimeStamp.time(105, 543000000), newTime);

        newTime = time.minus(TimeDuration.ms(1000));
        assertEquals(TimeStamp.time(99, 100000000), newTime);

        newTime = time.minus(TimeDuration.nanos(999000000));
        assertEquals(TimeStamp.time(99, 101000000), newTime);
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

    @Test
    public void testHz() {
        TimeDuration duration = TimeDuration.hz(1.0);
        assertThat(duration.getNanoSec(), equalTo(1000000000L));
        
        assertTrue(TimeDuration.ms(100).equals(TimeDuration.hz(10)));
        assertTrue(TimeDuration.ms(1).equals(TimeDuration.hz(1000)));
    }

    @Test
    public void testDoubleMs() {
        assertTrue(TimeDuration.ms(100.0).equals(TimeDuration.ms(100)));
        assertTrue(TimeDuration.ms(2.5).equals(TimeDuration.hz(400)));
        assertTrue(TimeDuration.ms(0.2).equals(TimeDuration.nanos(200000)));
    }
}