/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import static org.epics.pvmanager.data.DataUtils.createValue;
import static org.epics.util.time.TimeDuration.*;
import static org.epics.pvmanager.data.ValueFactory.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.util.TimeDuration;

import org.epics.pvmanager.util.TimeStamp;
import org.epics.util.time.Timestamp;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class SynchronizedVDoubleAggregatorTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        DataTypeSupport.install();
    }

    /**
     * Test of closestElement method, of class SynchronizedVDoubleAggregator.
     */
    @Test
    public void testClosestElement() {
        List<VDouble> data = new ArrayList<VDouble>();
        Timestamp reference = Timestamp.now();
        data.add(newVDouble(-2.0, newTime(reference.minus(ofMillis(5)))));
        data.add(newVDouble(-1.0, newTime(reference.minus(ofMillis(1)))));
        data.add(newVDouble(0.0, newTime(reference)));
        data.add(newVDouble(1.0, newTime(reference.plus(ofMillis(2)))));
        data.add(newVDouble(2.0, newTime(reference.plus(ofMillis(3)))));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, TimeDuration.ms(10).around(TimeStamp.timestampOf(reference)), TimeStamp.timestampOf(reference));
        assertEquals(0.0, result.getValue(), 0.000001);
    }

    @Test
    public void testClosestElement2() {
        List<VDouble> data = new ArrayList<VDouble>();
        Timestamp reference = Timestamp.now();
        data.add(newVDouble(-2.0, newTime(reference.minus(ofMillis(5)))));
        data.add(newVDouble(-1.0, newTime(reference.minus(ofMillis(1)))));
        data.add(newVDouble(1.0, newTime(reference.plus(ofMillis(2)))));
        data.add(newVDouble(2.0, newTime(reference.plus(ofMillis(3)))));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, TimeDuration.ms(10).around(TimeStamp.timestampOf(reference)), TimeStamp.timestampOf(reference));
        assertEquals(-1.0, result.getValue(), 0.000001);
    }
}