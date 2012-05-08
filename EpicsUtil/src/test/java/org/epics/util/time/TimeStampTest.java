/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

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
        TimeStamp time = TimeStamp.time(100, 1000000000);
    }
}