/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class LinearAbsoluteTimeScaleTest {

    @Test
    public void scaleNormalizedTime1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        assertThat(linearScale.scaleNormalizedTime(0.0, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleNormalizedTime(0.25, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleNormalizedTime(0.5, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleNormalizedTime(1.0, 1.0, 100.0), equalTo(100.0));
    }

    @Test
    public void scaleTimestamp1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = Timestamp.now();
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(8)));
        assertThat(linearScale.scaleTimestamp(start, timeInterval, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(2)), timeInterval, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(4)), timeInterval, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(8)), timeInterval, 1.0, 100.0), equalTo(100.0));
    }

    @Test
    public void references1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 123);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(20)));
        TimeAxis timeAxis = linearScale.references(timeInterval, 2, 11);
        assertAxisEquals(timeInterval, null, 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 50, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 52, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 54, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 56, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 58, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 0, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 2, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 4, 0)), null, timeAxis);
        
    }

    @Test
    public void references2() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 123);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(5)));
        TimeAxis timeAxis = linearScale.references(timeInterval, 2, 11);
        assertAxisEquals(timeInterval, null, 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 49, 0)), null, timeAxis);
        
    }

    public static void assertAxisEquals(TimeInterval timeInterval, ListDouble normalizedValues, List<Timestamp> timestamps, List<String> labels, TimeAxis axis) {
        assertThat(axis.getTimeInterval(), equalTo(timeInterval));
        assertThat(axis.getNormalizedValues(), equalTo(normalizedValues));
        assertThat(axis.getTimestamps(), equalTo(timestamps));
        assertThat(axis.getTickLabels(), equalTo(labels));
    }
}