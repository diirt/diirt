/**
 * Copyright (C) 2012-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

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
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 0));
        assertThat(interval.getStart(), equalTo(Timestamp.of(0, 0)));
        assertThat(interval.getEnd(), equalTo(Timestamp.of(3600, 0)));
    }

    @Test
    public void interval2() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(3600, 0), Timestamp.of(7200, 0));
        assertThat(interval.getStart(), equalTo(Timestamp.of(3600, 0)));
        assertThat(interval.getEnd(), equalTo(Timestamp.of(7200, 0)));
    }

    @Test
    public void interval3() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), null);
        assertThat(interval.getStart(), equalTo(Timestamp.of(0, 0)));
        assertThat(interval.getEnd(), nullValue());
    }

    @Test
    public void interval4() {
        TimeInterval interval = TimeInterval.between(null, Timestamp.of(0, 0));
        assertThat(interval.getStart(), nullValue());
        assertThat(interval.getEnd(), equalTo(Timestamp.of(0, 0)));
    }

    @Test
    public void equals1() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 0));
        assertThat(interval, equalTo(TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 0))));
    }

    @Test
    public void equals2() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 1), Timestamp.of(3600, 0));
        assertThat(interval, not(equalTo(TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 0)))));
    }

    @Test
    public void equals3() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 1));
        assertThat(interval, not(equalTo(TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 0)))));
    }

    @Test
    public void equals4() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), null);
        assertThat(interval, equalTo(TimeInterval.between(Timestamp.of(0, 0), null)));
    }

    @Test
    public void equals5() {
        TimeInterval interval = TimeInterval.between(null, Timestamp.of(0, 0));
        assertThat(interval, equalTo(TimeInterval.between(null, Timestamp.of(0, 0))));
    }

    @Test
    public void contains1() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), Timestamp.of(3600, 1));
        assertThat(interval.contains(Timestamp.of(3,0)), is(true));
        assertThat(interval.contains(Timestamp.of(0,110)), is(true));
        assertThat(interval.contains(Timestamp.of(3600,0)), is(true));
        assertThat(interval.contains(Timestamp.of(-1,110)), is(false));
        assertThat(interval.contains(Timestamp.of(3600,2)), is(false));
    }

    @Test
    public void contains2() {
        TimeInterval interval = TimeInterval.between(Timestamp.of(0, 0), null);
        assertThat(interval.contains(Timestamp.of(-3600,2)), is(false));
        assertThat(interval.contains(Timestamp.of(-1,110)), is(false));
        assertThat(interval.contains(Timestamp.of(0,110)), is(true));
        assertThat(interval.contains(Timestamp.of(3,0)), is(true));
        assertThat(interval.contains(Timestamp.of(3600,0)), is(true));
    }

    @Test
    public void contains3() {
        TimeInterval interval = TimeInterval.between(null, Timestamp.of(0, 0));
        assertThat(interval.contains(Timestamp.of(-3600,2)), is(true));
        assertThat(interval.contains(Timestamp.of(-1,110)), is(true));
        assertThat(interval.contains(Timestamp.of(0,110)), is(false));
        assertThat(interval.contains(Timestamp.of(3,0)), is(false));
        assertThat(interval.contains(Timestamp.of(3600,0)), is(false));
    }
}