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
    public void testHour() {
        assertThat(hour(1), equalTo(min(60)));
        assertThat(hour(2), equalTo(sec(7200)));
        assertThat(hour(1.0), equalTo(ms(3600000)));
        assertThat(hour(0.5), equalTo(min(30.0)));
    }

    @Test
    public void testMin() {
        assertThat(min(1.0), equalTo(sec(60.0)));
        assertThat(min(1.0), equalTo(ms(60000.0)));
        assertThat(min(0.5), equalTo(sec(30.0)));
    }

    @Test
    public void testSec() {
        TimeDuration duration = TimeDuration.sec(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
        
        assertThat(TimeDuration.sec(0.1), equalTo(TimeDuration.ms(100)));
    }

    @Test
    public void testHz() {
        TimeDuration duration = TimeDuration.hz(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
        
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