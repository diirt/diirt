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
public class ValueAxisTest {
    
    public ValueAxisTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /*
    @Test
    public void testCreateTicks() {
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, RangeUtil.createTicks(0.0, 10.0, 1.0), 0.000001);
        assertArrayEquals(new double[] {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5}, RangeUtil.createTicks(0.29876, 3.986, 0.5), 0.000001);
        assertArrayEquals(new double[] {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0,
            1100.0, 1200.0, 1300.0, 1400.0, 1500.0, 1600.0, 1700.0, 1800.0, 1900.0, 2000.0,
            2100.0, 2200.0, 2300.0, 2400.0, 2500.0, 2600.0, 2700.0, 2800.0, 2900.0}, RangeUtil.createTicks(19.4, 2968, 100.0), 0.000001);
    }*/
    
    @Test
    public void testTicksForRange1() {
        ValueAxis axis = ValueAxis.createAutoAxis(1.0, 9.0, 4);
        assertAxisEquals(1.0, 9.0, new double[]{2.0, 4.0, 6.0, 8.0}, new String[]{"2", "4", "6", "8"}, axis);
    }
    
    @Test
    public void testTicksForRange2() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.0, 10.0, 11);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, axis);
    }
    
    @Test
    public void testTicksForRange3() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.0, 10.0, 21);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0},
                new String[]{"0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5",
                    "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0"}, axis);
    }
    
    @Test
    public void testTicksForRange4() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.0, 10.0, 101, 1.0);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, axis);
    }
    
    @Test
    public void testTicksForRange5() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.0, 10.0, 6);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 2.0, 4.0, 6.0, 8.0, 10.0},
                new String[]{"0", "2", "4", "6", "8", "10"}, axis);
    }
    
    @Test
    public void testTicksForRange6() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.0, 10.0, 8);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 2.0, 4.0, 6.0, 8.0, 10.0},
                new String[]{"0", "2", "4", "6", "8", "10"}, axis);
    }
    
    @Test
    public void testTicksForRange7() {
        ValueAxis axis = ValueAxis.createAutoAxis(-10.0, -1.0, 11);
        assertAxisEquals(-10.0, -1.0, new double[]{-10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0},
                new String[]{"-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1"}, axis);
    }
    
    @Test
    public void testTicksForRange8() {
        ValueAxis axis = ValueAxis.createAutoAxis(-10.0, 0.0, 11);
        assertAxisEquals(-10.0, 0.0, new double[]{-10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 0.0},
                new String[]{"-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0"}, axis);
    }
    
    @Test
    public void testTicksForRange9() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.9, 1.3, 10);
        assertAxisEquals(0.9, 1.3, new double[]{0.9, 0.95, 1.0, 1.05, 1.1, 1.15, 1.2, 1.25, 1.3},
                new String[]{"0.90", "0.95", "1.00", "1.05", "1.10", "1.15", "1.20", "1.25", "1.30"}, axis);
    }
    
    @Test
    public void testTicksForRange10() {
        ValueAxis axis = ValueAxis.createAutoAxis(0.77777, 0.88888, 15);
        assertAxisEquals(0.77777, 0.88888, new double[]{0.78, 0.79, 0.80, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86, 0.87, 0.88},
                new String[]{"0.78", "0.79", "0.80", "0.81", "0.82", "0.83", "0.84", "0.85", "0.86", "0.87", "0.88"}, axis);
    }
    
    @Test
    public void testTicksForRange11() {
        ValueAxis axis = ValueAxis.createAutoAxis(100.77777, 100.88888, 15);
        assertAxisEquals(100.77777, 100.88888, new double[]{100.78, 100.79, 100.80, 100.81, 100.82, 100.83, 100.84, 100.85, 100.86, 100.87, 100.88},
                new String[]{"100.78", "100.79", "100.80", "100.81", "100.82", "100.83", "100.84", "100.85", "100.86", "100.87", "100.88"}, axis);
    }

    private void assertAxisEquals(double minValue, double maxValue, double[] tickValues, String[] tickLabels, org.epics.graphene.ValueAxis axis) {
        assertEquals(minValue, axis.getMinValue(), 0.000001);
        assertEquals(maxValue, axis.getMaxValue(), 0.000001);
        assertArrayEquals(tickValues, axis.getTickValues(), 0.000001);
        assertArrayEquals(tickLabels, axis.getTickLabels());
    }
}
