/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TimeStampTest {

    public TimeStampTest() {
    }
    
    @Test
    public void time1() {
        TimeStamp time = TimeStamp.time(100, 10000000);
        assertThat(time.getSec(), equalTo(100L));
        assertThat(time.getNanoSec(), equalTo(10000000));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void time2() {
        TimeStamp.time(100, 1000000000);
    }
    
    @Test
    public void ofDate1() {
        Date date = new Date(123456789);
        TimeStamp time = TimeStamp.of(date);
        assertThat(time.getSec(), equalTo(123456L));
        assertThat(time.getNanoSec(), equalTo(789000000));
    }
    
    @Test
    public void toDate1() {
        TimeStamp time = TimeStamp.time(123456, 789000000);
        assertThat(time.toDate(), equalTo(new Date(123456789)));
    }
    
    @Test
    public void plus1() {
        TimeStamp time = TimeStamp.time(0, 0);
        TimeStamp newTime = time.plus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(TimeStamp.time(0, 100000000)));
    }
    
    @Test
    public void plus2() {
        TimeStamp time = TimeStamp.time(100, 100000000);
        TimeStamp newTime = time.plus(TimeDuration.ofNanos(999000000));
        assertThat(newTime, equalTo(TimeStamp.time(101, 99000000)));
    }
    
    @Test
    public void plus3() {
        TimeStamp time = TimeStamp.time(100, 750000000);
        TimeStamp newTime = time.plus(TimeDuration.ofSeconds(5.750));
        assertThat(newTime, equalTo(TimeStamp.time(106, 500000000)));
    }
    
    @Test
    public void plus4() {
        TimeStamp time = TimeStamp.time(100, 750000000);
        TimeStamp newTime = time.plus(TimeDuration.ofSeconds(-5.750));
        assertThat(newTime, equalTo(TimeStamp.time(95, 000000000)));
    }
    
    @Test
    public void minus1() {
        TimeStamp time = TimeStamp.time(0, 0);
        TimeStamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(TimeStamp.time(-1, 900000000)));
    }
    
    @Test
    public void minus2() {
        TimeStamp time = TimeStamp.time(0, 0);
        TimeStamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(TimeStamp.time(-1, 900000000)));
    }
    
    @Test
    public void minus3() {
        TimeStamp time = TimeStamp.time(0, 0);
        TimeStamp newTime = time.minus(TimeDuration.ofMillis(100));
        assertThat(newTime, equalTo(TimeStamp.time(-1, 900000000)));
    }
    
}