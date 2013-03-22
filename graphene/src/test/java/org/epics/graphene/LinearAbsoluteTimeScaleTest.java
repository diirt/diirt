/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

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
    public void scaleValue1() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        assertThat(linearScale.scaleNormalizedTime(0.0, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleNormalizedTime(0.25, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleNormalizedTime(0.5, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleNormalizedTime(1.0, 1.0, 100.0), equalTo(100.0));
    }

    @Test
    public void scaleValue2() {
        TimeScale linearScale = TimeScales.linearAbsoluteScale();
        Timestamp start = Timestamp.now();
        TimeInterval timeInterval = TimeInterval.between(start, start.plus(TimeDuration.ofSeconds(8)));
        assertThat(linearScale.scaleTimestamp(start, timeInterval, 1.0, 100.0), equalTo(1.0));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(2)), timeInterval, 1.0, 100.0), equalTo(25.75));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(4)), timeInterval, 1.0, 100.0), equalTo(50.5));
        assertThat(linearScale.scaleTimestamp(start.plus(TimeDuration.ofSeconds(8)), timeInterval, 1.0, 100.0), equalTo(100.0));
    }
//
//    public static void assertAxisEquals(double minValue, double maxValue, double[] tickValues, String[] tickLabels, org.epics.graphene.ValueAxis axis) {
//        assertEquals(minValue, axis.getMinValue(), 0.000001);
//        assertEquals(maxValue, axis.getMaxValue(), 0.000001);
//        assertArrayEquals(tickValues, axis.getTickValues(), 0.000001);
//        assertArrayEquals(tickLabels, axis.getTickLabels());
//    }
}