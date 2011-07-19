/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import static org.epics.pvmanager.data.DataUtils.createValue;
import static org.epics.pvmanager.util.TimeDuration.ms;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.epics.pvmanager.util.TimeStamp;
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
        TimeStamp reference = TimeStamp.now();
        data.add(createValue(reference.minus(ms(5)), -2.0));
        data.add(createValue(reference.minus(ms(1)), -1.0));
        data.add(createValue(reference, 0.0));
        data.add(createValue(reference.plus(ms(2)), 1.0));
        data.add(createValue(reference.plus(ms(3)), 2.0));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, ms(10).around(reference), reference);
        assertEquals(0.0, result.getValue(), 0.000001);
    }

    @Test
    public void testClosestElement2() {
        List<VDouble> data = new ArrayList<VDouble>();
        TimeStamp reference = TimeStamp.now();
        data.add(createValue(reference.minus(ms(5)), -2.0));
        data.add(createValue(reference.minus(ms(1)), -1.0));
        data.add(createValue(reference.plus(ms(2)), 1.0));
        data.add(createValue(reference.plus(ms(3)), 2.0));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, ms(10).around(reference), reference);
        assertEquals(-1.0, result.getValue(), 0.000001);
    }
}