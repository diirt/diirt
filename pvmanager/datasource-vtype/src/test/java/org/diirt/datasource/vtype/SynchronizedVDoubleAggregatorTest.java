/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.datasource.vtype.SynchronizedVDoubleAggregator;
import org.diirt.datasource.vtype.DataTypeSupport;
import org.diirt.util.time.TimeInterval;
import org.diirt.vtype.VDouble;

import static org.diirt.vtype.ValueFactory.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.diirt.datasource.vtype.DataTypeSupport;
import org.diirt.datasource.vtype.SynchronizedVDoubleAggregator;
import org.diirt.vtype.VDouble;

import static java.time.Duration.*;

import java.time.Instant;

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
        Instant reference = Instant.now();
        data.add(newVDouble(-2.0, newTime(reference.minus(ofMillis(5)))));
        data.add(newVDouble(-1.0, newTime(reference.minus(ofMillis(1)))));
        data.add(newVDouble(0.0, newTime(reference)));
        data.add(newVDouble(1.0, newTime(reference.plus(ofMillis(2)))));
        data.add(newVDouble(2.0, newTime(reference.plus(ofMillis(3)))));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, TimeInterval.around(ofMillis(10), reference), reference);
        assertEquals(0.0, result.getValue(), 0.000001);
    }

    @Test
    public void testClosestElement2() {
        List<VDouble> data = new ArrayList<VDouble>();
        Instant reference = Instant.now();
        data.add(newVDouble(-2.0, newTime(reference.minus(ofMillis(5)))));
        data.add(newVDouble(-1.0, newTime(reference.minus(ofMillis(1)))));
        data.add(newVDouble(1.0, newTime(reference.plus(ofMillis(2)))));
        data.add(newVDouble(2.0, newTime(reference.plus(ofMillis(3)))));
        VDouble result = SynchronizedVDoubleAggregator.closestElement(data, TimeInterval.around(ofMillis(10), reference), reference);
        assertEquals(-1.0, result.getValue(), 0.000001);
    }
}