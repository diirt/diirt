/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimestampTest {

    public TimestampTest() {
    }

    @Test
    public void time1() {
        Timestamp time = Timestamp.of(100, 10000000);
        assertThat(time.getSec(), equalTo(100L));
        assertThat(time.getNanoSec(), equalTo(10000000));
    }

    @Test(expected=IllegalArgumentException.class)
    public void time2() {
        Timestamp.of(100, 1000000000);
    }

    @Test
    public void ofDate1() {
        Date date = new Date(123456789);
        Timestamp time = Timestamp.of(date);
        assertThat(time.getSec(), equalTo(123456L));
        assertThat(time.getNanoSec(), equalTo(789000000));
    }

    @Test
    public void ofDate2() {
        Date date = new Date(-123456789);
        Timestamp time = Timestamp.of(date);
        assertThat(time.toDate(), equalTo(date));
        assertThat(time.getSec(), equalTo(-123457L));
        assertThat(time.getNanoSec(), equalTo(211000000));
    }

    @Test
    public void toDate1() {
        Timestamp time = Timestamp.of(123456, 789000000);
        assertThat(time.toDate(), equalTo(new Date(123456789)));
    }

    @Test
    public void plus1() {
        Timestamp time = Timestamp.of(0, 0);
        Timestamp newTime = time.plus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(Timestamp.of(0, 100000000)));
    }

    @Test
    public void plus2() {
        Timestamp time = Timestamp.of(100, 100000000);
        Timestamp newTime = time.plus(TimeDuration.ofNanos(999000000));
        assertThat(newTime, equalTo(Timestamp.of(101, 99000000)));
    }

    @Test
    public void plus3() {
        Timestamp time = Timestamp.of(100, 750000000);
        Timestamp newTime = time.plus(TimeDuration.ofSeconds(5.750));
        assertThat(newTime, equalTo(Timestamp.of(106, 500000000)));
    }

    @Test
    public void plus4() {
        Timestamp time = Timestamp.of(100, 750000000);
        Timestamp newTime = time.plus(TimeDuration.ofSeconds(-5.750));
        assertThat(newTime, equalTo(Timestamp.of(95, 000000000)));
    }

    @Test
    public void minus1() {
        Timestamp time = Timestamp.of(0, 0);
        Timestamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(Timestamp.of(-1, 900000000)));
    }

    @Test
    public void minus2() {
        Timestamp time = Timestamp.of(0, 0);
        Timestamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(Timestamp.of(-1, 900000000)));
    }

    @Test
    public void minus3() {
        Timestamp time = Timestamp.of(0, 0);
        Timestamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(Timestamp.of(-1, 900000000)));
    }

    @Test
    public void durationFrom1() {
        Timestamp reference = Timestamp.now();
        assertThat(reference.plus(TimeDuration.ofNanos(10)).durationFrom(reference), equalTo(TimeDuration.ofNanos(10)));
    }

    @Test
    public void durationFrom2() {
        Timestamp reference = Timestamp.now();
        assertThat(reference.minus(TimeDuration.ofNanos(10)).durationFrom(reference), equalTo(TimeDuration.ofNanos(-10)));
    }

    @Test
    public void durationFrom3() {
        Timestamp reference = Timestamp.of(10, 500000000);
        assertThat(reference.plus(TimeDuration.ofMillis(600)).durationFrom(reference), equalTo(TimeDuration.ofMillis(600)));
    }

    @Test
    public void durationFrom4() {
        Timestamp reference = Timestamp.of(10, 500000000);
        assertThat(reference.minus(TimeDuration.ofMillis(600)).durationFrom(reference), equalTo(TimeDuration.ofMillis(-600)));
    }

    @Test
    public void durationBetween1() {
        Timestamp reference = Timestamp.now();
        assertThat(reference.durationBetween(reference.plus(TimeDuration.ofNanos(10))), equalTo(TimeDuration.ofNanos(10)));
    }

    @Test
    public void durationBetween2() {
        Timestamp reference = Timestamp.now();
        assertThat(reference.durationBetween(reference.minus(TimeDuration.ofNanos(10))), equalTo(TimeDuration.ofNanos(10)));
    }

    @Test
    public void durationBetween3() {
        Timestamp reference = Timestamp.of(10, 500000000);
        assertThat(reference.durationBetween(reference.plus(TimeDuration.ofMillis(600))), equalTo(TimeDuration.ofMillis(600)));
    }

    @Test
    public void durationBetween4() {
        Timestamp reference = Timestamp.of(10, 500000000);
        assertThat(reference.durationBetween(reference.minus(TimeDuration.ofMillis(600))), equalTo(TimeDuration.ofMillis(600)));
    }

    @Test
    public void toString1() {
        Timestamp timestamp = Timestamp.of(0, 10000000);
        assertThat(timestamp.toString(), equalTo("0.010000000"));
    }

    @Test
    public void toString2() {
        Timestamp timestamp = Timestamp.of(1, 234500000);
        assertThat(timestamp.toString(), equalTo("1.234500000"));
    }

    @Test
    public void toString3() {
        Timestamp timestamp = Timestamp.of(1234, 567890000);
        assertThat(timestamp.toString(), equalTo("1234.567890000"));
    }

    @Test
    public void toString4() {
        Timestamp timestamp = Timestamp.of(1234, 100);
        assertThat(timestamp.toString(), equalTo("1234.000000100"));
    }

}