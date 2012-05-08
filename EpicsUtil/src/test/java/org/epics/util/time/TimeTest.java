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

    @Test
    public void testHour() {
        assertThat(ofHours(1), equalTo(ofMinutes(60)));
        assertThat(ofHours(2), equalTo(ofSeconds(7200)));
        assertThat(ofHours(1.0), equalTo(ofMillis(3600000)));
        assertThat(ofHours(0.5), equalTo(ofMinutes(30.0)));
    }

    @Test
    public void testMin() {
        assertThat(ofMinutes(1.0), equalTo(ofSeconds(60.0)));
        assertThat(ofMinutes(1.0), equalTo(ofMillis(60000)));
        assertThat(ofMinutes(0.5), equalTo(ofSeconds(30.0)));
    }

    @Test
    public void testSec() {
        TimeDuration duration = TimeDuration.ofSeconds(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
        
        assertThat(TimeDuration.ofSeconds(0.1), equalTo(TimeDuration.ofMillis(100)));
    }

    @Test
    public void testHz() {
        TimeDuration duration = TimeDuration.ofHertz(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
        
        assertTrue(TimeDuration.ofMillis(100).equals(TimeDuration.ofHertz(10)));
        assertTrue(TimeDuration.ofMillis(1).equals(TimeDuration.ofHertz(1000)));
    }
}