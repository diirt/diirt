/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class RangeUtilTest {
    
    public RangeUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCountTicks() {
        assertEquals(11, RangeUtil.countTicks(0.0, 10.0, 1.0));
        assertEquals(7, RangeUtil.countTicks(0.29876, 3.986, 0.5));
        assertEquals(29, RangeUtil.countTicks(19.4, 2968, 100.0));
    }

    @Test
    public void testCreateTicks() {
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, RangeUtil.createTicks(0.0, 10.0, 1.0), 0.000001);
        assertArrayEquals(new double[] {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5}, RangeUtil.createTicks(0.29876, 3.986, 0.5), 0.000001);
        assertArrayEquals(new double[] {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0,
            1100.0, 1200.0, 1300.0, 1400.0, 1500.0, 1600.0, 1700.0, 1800.0, 1900.0, 2000.0,
            2100.0, 2200.0, 2300.0, 2400.0, 2500.0, 2600.0, 2700.0, 2800.0, 2900.0}, RangeUtil.createTicks(19.4, 2968, 100.0), 0.000001);
    }

    @Test
    public void testTicksForRange() {
        assertArrayEquals(new double[] {2.0, 4.0, 6.0, 8.0}, RangeUtil.ticksForRange(1.0, 9.0, 4), 0.000001);
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, RangeUtil.ticksForRange(0.0, 10.0, 11), 0.000001);
        assertArrayEquals(new double[] {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0}, RangeUtil.ticksForRange(0.0, 10.0, 21), 0.000001);
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, RangeUtil.ticksForRange(0.0, 10.0, 101, 1.0), 0.000001);
        assertArrayEquals(new double[] {0.0, 2.0, 4.0, 6.0, 8.0, 10.0}, RangeUtil.ticksForRange(0.0, 10.0, 6), 0.000001);
        assertArrayEquals(new double[] {0.0, 2.0, 4.0, 6.0, 8.0, 10.0}, RangeUtil.ticksForRange(0.0, 10.0, 8), 0.000001);
    }
}
