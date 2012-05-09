/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimeRelativeIntervalTest {

    public TimeRelativeIntervalTest() {
    }

    // Trasform to absolute/relative?
    // create aa/ar/ra/rr
    @Test
    public void interval1() {
        TimeRelativeInterval interval = TimeRelativeInterval.of(TimeStamp.of(0, 0), TimeStamp.of(3600, 0));
        assertThat(interval.isIntervalAbsolute(), equalTo(true));
        assertThat(interval.getAbsoluteStart(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.getAbsoluteEnd(), equalTo(TimeStamp.of(3600, 0)));
        assertThat(interval.toAbsoluteInterval(TimeStamp.now()), equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0))));
    }
    
    @Test
    public void interval2() {
        TimeRelativeInterval interval = TimeRelativeInterval.of(TimeStamp.of(0, 0), null);
        assertThat(interval.isIntervalAbsolute(), equalTo(true));
        assertThat(interval.getAbsoluteStart(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.getAbsoluteEnd(), nullValue());
        assertThat(interval.toAbsoluteInterval(TimeStamp.now()), equalTo(TimeInterval.between(TimeStamp.of(0, 0), null)));
    }
    
    @Test
    public void interval3() {
        TimeRelativeInterval interval = TimeRelativeInterval.of(null, TimeStamp.of(0, 0));
        assertThat(interval.isIntervalAbsolute(), equalTo(true));
        assertThat(interval.getAbsoluteStart(), nullValue());
        assertThat(interval.getAbsoluteEnd(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.toAbsoluteInterval(TimeStamp.now()), equalTo(TimeInterval.between(null, TimeStamp.of(0, 0))));
    }
}