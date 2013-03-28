/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.util.GregorianCalendar;
import java.util.List;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.graphene.TimeScales.TimePeriod;
import static java.util.GregorianCalendar.*;

/**
 *
 * @author carcassi
 */
public class TimeScalesTest {
    
    @Test
    public void nextUp1() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 1));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 2)));
    }
    
    @Test
    public void nextUp2() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 2));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 5)));
    }
    
    @Test
    public void nextUp3() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 5));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 10)));
    }
    
    @Test
    public void nextUp4() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 10));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 15)));
    }
    
    @Test
    public void nextUp5() {
        TimeScales.TimePeriod nextUp = TimeScales.nextUp(new TimePeriod(SECOND, 15));
        assertThat(nextUp, equalTo(new TimePeriod(SECOND, 30)));
    }
    
    @Test
    public void roundSeconds1() {
        TimeScales.TimePeriod period = TimeScales.roundSeconds(30.0);
        assertThat(period, equalTo(new TimePeriod(SECOND, 30.0)));
    }
    
    @Test
    public void roundSeconds2() {
        TimeScales.TimePeriod period = TimeScales.roundSeconds(61.0);
        assertThat(period, equalTo(new TimePeriod(MINUTE, 61.0/60.0)));
    }
}