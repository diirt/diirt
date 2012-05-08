package org.epics.util.time;

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */


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
    
    // Test factory methods

    @Test
    public void nanos1() {
        TimeDuration duration = TimeDuration.ofNanos(100L);
        assertThat(duration.getNanoSec(), equalTo(100));
        assertThat(duration.getSec(), equalTo(0L));
    }

    @Test
    public void nanos2() {
        TimeDuration duration = TimeDuration.ofNanos(1234567890L);
        assertThat(duration.getNanoSec(), equalTo(234567890));
        assertThat(duration.getSec(), equalTo(1L));
    }

    @Test
    public void nanos3() {
        TimeDuration duration = TimeDuration.ofNanos(123456789012L);
        assertThat(duration.getNanoSec(), equalTo(456789012));
        assertThat(duration.getSec(), equalTo(123L));
    }

    @Test
    public void nanos4() {
        TimeDuration duration = TimeDuration.ofNanos(-1234567890L);
        assertThat(duration.getNanoSec(), equalTo(765432110));
        assertThat(duration.getSec(), equalTo(-2L));
    }

    @Test
    public void ms1() {
        TimeDuration duration = TimeDuration.ofMillis(100);
        assertThat(duration.getNanoSec(), equalTo(100000000));
        assertThat(duration.getSec(), equalTo(0L));
    }

    @Test
    public void ms2() {
        TimeDuration duration = TimeDuration.ofMillis(12345);
        assertThat(duration.getNanoSec(), equalTo(345000000));
        assertThat(duration.getSec(), equalTo(12L));
    }

    @Test
    public void ms3() {
        TimeDuration duration = TimeDuration.ofMillis(-12345);
        assertThat(duration.getNanoSec(), equalTo(655000000));
        assertThat(duration.getSec(), equalTo(-13L));
    }

    @Test
    public void sec1() {
        TimeDuration duration = TimeDuration.ofSeconds(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
    }

    @Test
    public void sec2() {
        TimeDuration duration = TimeDuration.ofSeconds(0.123456789);
        assertThat(duration.getNanoSec(), equalTo(123456789));
        assertThat(duration.getSec(), equalTo(0L));
    }

    @Test
    public void sec3() {
        TimeDuration duration = TimeDuration.ofSeconds(-1.23456789);
        assertThat(duration.getNanoSec(), equalTo(765432110));
        assertThat(duration.getSec(), equalTo(-2L));
    }

    @Test
    public void min1() {
        TimeDuration duration = TimeDuration.ofMinutes(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(60L));
    }

    @Test
    public void min2() {
        TimeDuration duration = TimeDuration.ofMinutes(0.123456789);
        assertThat(duration.getNanoSec(), equalTo(407407340));
        assertThat(duration.getSec(), equalTo(7L));
    }

    @Test
    public void min3() {
        TimeDuration duration = TimeDuration.ofMinutes(-1.23456789);
        assertThat(duration.getNanoSec(), equalTo(925926601));
        assertThat(duration.getSec(), equalTo(-75L));
    }

    @Test
    public void hour1() {
        TimeDuration duration = TimeDuration.ofHours(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(3600L));
    }

    @Test
    public void hour2() {
        TimeDuration duration = TimeDuration.ofHours(0.123456789);
        assertThat(duration.getNanoSec(), equalTo(444440399));
        assertThat(duration.getSec(), equalTo(444L));
    }

    @Test
    public void hour3() {
        TimeDuration duration = TimeDuration.ofHours(-1.23456789);
        assertThat(duration.getNanoSec(), equalTo(555596001));
        assertThat(duration.getSec(), equalTo(-4445L));
    }

    @Test
    public void hz1() {
        TimeDuration duration = TimeDuration.ofHertz(1.0);
        assertThat(duration.getNanoSec(), equalTo(0));
        assertThat(duration.getSec(), equalTo(1L));
    }

    @Test
    public void hz2() {
        TimeDuration duration = TimeDuration.ofHertz(100.0);
        assertThat(duration.getNanoSec(), equalTo(10000000));
        assertThat(duration.getSec(), equalTo(0L));
    }

    @Test
    public void hz3() {
        TimeDuration duration = TimeDuration.ofHertz(0.123456789);
        assertThat(duration.getNanoSec(), equalTo(100000073));
        assertThat(duration.getSec(), equalTo(8L));
    }
    
    // Test equality
    
    @Test
    public void equals1() {
        TimeDuration duration = TimeDuration.ofNanos(1000000);
        assertThat(duration, equalTo(TimeDuration.ofMillis(1)));
        assertThat(duration, equalTo(TimeDuration.ofSeconds(0.001)));
        assertThat(duration, equalTo(TimeDuration.ofMinutes(0.0000166666666667)));
        assertThat(duration, equalTo(TimeDuration.ofHours(0.0000002777777778)));
    }
    
    @Test
    public void equals2() {
        TimeDuration duration = TimeDuration.ofNanos(1000000000);
        assertThat(duration, equalTo(TimeDuration.ofMillis(1000)));
        assertThat(duration, equalTo(TimeDuration.ofSeconds(1)));
        assertThat(duration, equalTo(TimeDuration.ofMinutes(0.0166666666667)));
        assertThat(duration, equalTo(TimeDuration.ofHours(0.0002777777778)));
    }
    
    @Test
    public void equals3() {
        TimeDuration duration = TimeDuration.ofNanos(60000000000L);
        assertThat(duration, equalTo(TimeDuration.ofMillis(60000)));
        assertThat(duration, equalTo(TimeDuration.ofSeconds(60)));
        assertThat(duration, equalTo(TimeDuration.ofMinutes(1)));
        assertThat(duration, equalTo(TimeDuration.ofHours(0.0166666666667)));
    }
    
    @Test
    public void equals4() {
        TimeDuration duration = TimeDuration.ofNanos(3600000000000L);
        assertThat(duration, equalTo(TimeDuration.ofMillis(3600000)));
        assertThat(duration, equalTo(TimeDuration.ofSeconds(3600)));
        assertThat(duration, equalTo(TimeDuration.ofMinutes(6)));
        assertThat(duration, equalTo(TimeDuration.ofHours(1)));
    }
    
    // Test operations
    
    @Test
    public void plus1() {
        TimeDuration duration = TimeDuration.ofMillis(800);
        assertThat(duration.plus(TimeDuration.ofMillis(300)), equalTo(TimeDuration.ofSeconds(1.1)));
    }
    
    @Test
    public void plus2() {
        TimeDuration duration = TimeDuration.ofMillis(-100);
        assertThat(duration.plus(TimeDuration.ofMillis(300)), equalTo(TimeDuration.ofSeconds(0.2)));
    }
    
    @Test
    public void plus3() {
        TimeDuration duration = TimeDuration.ofMillis(100);
        assertThat(duration.plus(TimeDuration.ofMillis(-200)), equalTo(TimeDuration.ofSeconds(-0.1)));
    }
    
    @Test
    public void plus4() {
        TimeDuration duration = TimeDuration.ofSeconds(1.250);
        assertThat(duration.plus(TimeDuration.ofSeconds(1.250)), equalTo(TimeDuration.ofSeconds(2.5)));
    }
    
    @Test
    public void plus5() {
        TimeDuration duration = TimeDuration.ofSeconds(10.250);
        assertThat(duration.plus(TimeDuration.ofSeconds(-1.750)), equalTo(TimeDuration.ofSeconds(8.5)));
    }
    
    @Test
    public void minus1() {
        TimeDuration duration = TimeDuration.ofMillis(800);
        assertThat(duration.minus(TimeDuration.ofMillis(300)), equalTo(TimeDuration.ofSeconds(0.5)));
    }
    
    @Test
    public void minus2() {
        TimeDuration duration = TimeDuration.ofMillis(800);
        assertThat(duration.minus(TimeDuration.ofMillis(-300)), equalTo(TimeDuration.ofSeconds(1.1)));
    }
    
    @Test
    public void minus3() {
        TimeDuration duration = TimeDuration.ofMillis(1300);
        assertThat(duration.minus(TimeDuration.ofMillis(800)), equalTo(TimeDuration.ofSeconds(0.5)));
    }
    
    @Test
    public void minus4() {
        TimeDuration duration = TimeDuration.ofMillis(800);
        assertThat(duration.minus(TimeDuration.ofMillis(1300)), equalTo(TimeDuration.ofSeconds(-0.5)));
    }
    
    @Test
    public void minus5() {
        TimeDuration duration = TimeDuration.ofSeconds(10.250);
        assertThat(duration.minus(TimeDuration.ofMillis(1750)), equalTo(TimeDuration.ofSeconds(8.5)));
    }
    
    @Test
    public void multipliedBy1() {
        TimeDuration duration = TimeDuration.ofMillis(300);
        assertThat(duration.multipliedBy(5), equalTo(TimeDuration.ofSeconds(1.5)));
    }
    
    @Test
    public void multipliedBy2() {
        TimeDuration duration = TimeDuration.ofSeconds(10.500);
        assertThat(duration.multipliedBy(5), equalTo(TimeDuration.ofSeconds(52.5)));
    }
    
    @Test
    public void multipliedBy3() {
        TimeDuration duration = TimeDuration.ofSeconds(10.500);
        assertThat(duration.multipliedBy(-5), equalTo(TimeDuration.ofSeconds(-52.5)));
    }
    
    @Test
    public void dividedBy1() {
        TimeDuration duration = TimeDuration.ofMillis(600);
        assertThat(duration.dividedBy(3), equalTo(TimeDuration.ofSeconds(0.2)));
    }
    
    @Test
    public void dividedBy2() {
        TimeDuration duration = TimeDuration.ofMillis(1200);
        assertThat(duration.dividedBy(3), equalTo(TimeDuration.ofSeconds(0.4)));
    }
    
    @Test
    public void dividedBy3() {
        TimeDuration duration = TimeDuration.ofMillis(1200);
        assertThat(duration.dividedBy(-3), equalTo(TimeDuration.ofSeconds(-0.4)));
    }
    
    @Test
    public void dividedBy4() {
        TimeDuration duration = TimeDuration.ofSeconds(10.4);
        assertThat(duration.dividedBy(4), equalTo(TimeDuration.ofSeconds(2.6)));
    }
    
    @Test
    public void toString1() {
        TimeDuration duration = TimeDuration.ofMillis(10);
        assertThat(duration.toString(), equalTo("0.010000000"));
    }
    
    @Test
    public void toString2() {
        TimeDuration duration = TimeDuration.ofSeconds(1.2345);
        assertThat(duration.toString(), equalTo("1.234500000"));
    }
    
    @Test
    public void toString3() {
        TimeDuration duration = TimeDuration.ofSeconds(1234.56789);
        assertThat(duration.toString(), equalTo("1234.567890000"));
    }
    
}