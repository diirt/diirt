package org.epics.util.time;

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */


import org.epics.util.time.*;
import static org.epics.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimeDurationTest {

    public TimeDurationTest() {
    }
    
    // TimeDuration
    // - test that factory method create expected sec/nano, including negative
    // - test equality between different factory methods, including negative
    // - test plus/minus with equlity, including carry and negative

    
    // Test factory methods

    @Test
    public void nanos1() {
        TimeDuration duration = TimeDuration.nanos(100L);
        assertThat(duration.getNanoSec(), equalTo(100));
        assertThat(duration.getSec(), equalTo(0L));
    }

    @Test
    public void nanos2() {
        TimeDuration duration = TimeDuration.nanos(1234567890L);
        assertThat(duration.getNanoSec(), equalTo(234567890));
        assertThat(duration.getSec(), equalTo(1L));
    }

    @Test
    public void nanos3() {
        TimeDuration duration = TimeDuration.nanos(123456789012L);
        assertThat(duration.getNanoSec(), equalTo(456789012));
        assertThat(duration.getSec(), equalTo(123L));
    }

    @Test
    public void nanos4() {
        TimeDuration duration = TimeDuration.nanos(-1234567890L);
        assertThat(duration.getNanoSec(), equalTo(765432110));
        assertThat(duration.getSec(), equalTo(-2L));
    }

}