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
    
    public void ofDate1() {
        Date date = new Date(123456789);
        TimeStamp time = TimeStamp.of(date);
        assertThat(time.getSec(), equalTo(123456L));
        assertThat(time.getNanoSec(), equalTo(789000000));
    }
    
    public void toDate1() {
        TimeStamp time = TimeStamp.time(123456, 789000000);
        assertThat(time.toDate(), equalTo(new Date(123456789)));
    }
    
}