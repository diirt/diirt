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
public class TimeIntervalTest {

    public TimeIntervalTest() {
    }

    @Test
    public void interval1() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0));
        assertThat(interval.getStart(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.getEnd(), equalTo(TimeStamp.of(3600, 0)));
    }

    @Test
    public void interval2() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(3600, 0), TimeStamp.of(7200, 0));
        assertThat(interval.getStart(), equalTo(TimeStamp.of(3600, 0)));
        assertThat(interval.getEnd(), equalTo(TimeStamp.of(7200, 0)));
    }

    @Test
    public void interval3() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), null);
        assertThat(interval.getStart(), equalTo(TimeStamp.of(0, 0)));
        assertThat(interval.getEnd(), nullValue());
    }

    @Test
    public void interval4() {
        TimeInterval interval = TimeInterval.between(null, TimeStamp.of(0, 0));
        assertThat(interval.getStart(), nullValue());
        assertThat(interval.getEnd(), equalTo(TimeStamp.of(0, 0)));
    }

    @Test
    public void equals1() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0));
        assertThat(interval, equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0))));
    }

    @Test
    public void equals2() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 1), TimeStamp.of(3600, 0));
        assertThat(interval, not(equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0)))));
    }

    @Test
    public void equals3() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 1));
        assertThat(interval, not(equalTo(TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 0)))));
    }

    @Test
    public void equals4() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), null);
        assertThat(interval, equalTo(TimeInterval.between(TimeStamp.of(0, 0), null)));
    }

    @Test
    public void equals5() {
        TimeInterval interval = TimeInterval.between(null, TimeStamp.of(0, 0));
        assertThat(interval, equalTo(TimeInterval.between(null, TimeStamp.of(0, 0))));
    }

    @Test
    public void contains1() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), TimeStamp.of(3600, 1));
        assertThat(interval.contains(TimeStamp.of(3,0)), is(true));
        assertThat(interval.contains(TimeStamp.of(0,110)), is(true));
        assertThat(interval.contains(TimeStamp.of(3600,0)), is(true));
        assertThat(interval.contains(TimeStamp.of(-1,110)), is(false));
        assertThat(interval.contains(TimeStamp.of(3600,2)), is(false));
    }

    @Test
    public void contains2() {
        TimeInterval interval = TimeInterval.between(TimeStamp.of(0, 0), null);
        assertThat(interval.contains(TimeStamp.of(-3600,2)), is(false));
        assertThat(interval.contains(TimeStamp.of(-1,110)), is(false));
        assertThat(interval.contains(TimeStamp.of(0,110)), is(true));
        assertThat(interval.contains(TimeStamp.of(3,0)), is(true));
        assertThat(interval.contains(TimeStamp.of(3600,0)), is(true));
    }

    @Test
    public void contains3() {
        TimeInterval interval = TimeInterval.between(null, TimeStamp.of(0, 0));
        assertThat(interval.contains(TimeStamp.of(-3600,2)), is(true));
        assertThat(interval.contains(TimeStamp.of(-1,110)), is(true));
        assertThat(interval.contains(TimeStamp.of(0,110)), is(false));
        assertThat(interval.contains(TimeStamp.of(3,0)), is(false));
        assertThat(interval.contains(TimeStamp.of(3600,0)), is(false));
    }
}