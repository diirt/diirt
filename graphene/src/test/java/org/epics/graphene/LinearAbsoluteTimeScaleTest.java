/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayDouble;
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
        assertAxisEquals(timeInterval, new ArrayDouble(1877.0/20000.0,
                3877.0/20000.0,
                5877.0/20000.0,
                7877.0/20000.0,
                9877.0/20000.0,
                11877.0/20000.0,
                13877.0/20000.0,
                15877.0/20000.0,
                17877.0/20000.0,
                19877.0/20000.0), 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 50, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 52, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 54, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 56, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 58, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 0, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 2, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 14, 4, 0)),
                Arrays.asList("2013/05/10 16:13:46",
                "16:13:48",
                "16:13:50",
                "16:13:52",
                "16:13:54",
                "16:13:56",
                "16:13:58",
                "16:14:00",
                "16:14:02",
                "16:14:04"), timeAxis);
        
    }

    @Test
    public void references2() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 100);
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(5)));
        TimeAxis timeAxis = linearScale.references(timeInterval, 2, 11);
        assertAxisEquals(timeInterval, new ArrayDouble(4.0/50.0,
                9.0/50.0,
                14.0/50.0,
                19.0/50.0,
                24.0/50.0,
                29.0/50.0,
                34.0/50.0,
                39.0/50.0,
                44.0/50.0,
                49.0/50.0), 
                Arrays.asList(TimeScalesTest.create(2013, 5, 10, 16, 13, 44, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 45, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 46, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 47, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 0),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 48, 500),
                TimeScalesTest.create(2013, 5, 10, 16, 13, 49, 0)),
                Arrays.asList("2013/05/10 16:13:44.5",
                "16:13:45.0",
                ".5",
                "16:13:46.0",
                ".5",
                "16:13:47.0",
                ".5",
                "16:13:48.0",
                ".5",
                "16:13:49.0"), timeAxis);
        
    }

    public static void assertAxisEquals(TimeInterval timeInterval, ListDouble normalizedValues, List<Timestamp> timestamps, List<String> labels, TimeAxis axis) {
        assertThat(axis.getTimeInterval(), equalTo(timeInterval));
        assertThat(axis.getNormalizedValues(), equalTo(normalizedValues));
        assertThat(axis.getTimestamps(), equalTo(timestamps));
        assertThat(axis.getTickLabels(), equalTo(labels));
    }
}