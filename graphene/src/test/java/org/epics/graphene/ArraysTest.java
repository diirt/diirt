/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class ArraysTest {
    
    public ArraysTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testArrayLineData() {
        Point2DDataset dataset = Arrays.lineData(new ArrayDouble(1, 2, 3));
        assertEquals(3, dataset.getCount());
        assertEquals(0.0, dataset.getXValue(0), 0.001);
        assertEquals(1.0, dataset.getXValue(1), 0.001);
        assertEquals(2.0, dataset.getXValue(2), 0.001);
        assertEquals(1.0, dataset.getYValue(0), 0.001);
        assertEquals(2.0, dataset.getYValue(1), 0.001);
        assertEquals(3.0, dataset.getYValue(2), 0.001);
    }

    @Test
    public void testArrayScaledLineData() {
        Point2DDataset dataset = Arrays.lineData(new ArrayDouble(1, 2, 3), 10, 5);
        assertEquals(3, dataset.getCount());
        assertEquals(10.0, dataset.getXValue(0), 0.001);
        assertEquals(15.0, dataset.getXValue(1), 0.001);
        assertEquals(20.0, dataset.getXValue(2), 0.001);
        assertEquals(1.0, dataset.getYValue(0), 0.001);
        assertEquals(2.0, dataset.getYValue(1), 0.001);
        assertEquals(3.0, dataset.getYValue(2), 0.001);
    }

    @Test
    public void testDoubleArrayLineData() {
        Point2DDataset dataset = Arrays.lineData(new ArrayDouble(1, 2, 3), new ArrayDouble(3,7,5));
        assertEquals(3, dataset.getCount());
        assertEquals(1.0, dataset.getXValue(0), 0.001);
        assertEquals(2.0, dataset.getXValue(1), 0.001);
        assertEquals(3.0, dataset.getXValue(2), 0.001);
        assertEquals(3.0, dataset.getYValue(0), 0.001);
        assertEquals(7.0, dataset.getYValue(1), 0.001);
        assertEquals(5.0, dataset.getYValue(2), 0.001);
    }
}
