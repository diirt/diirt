/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.extra;

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class InterpolationsTest {

    public InterpolationsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testNoInterpolation1() {
        // Should return the same value
        double[] x = new double[] {0.0, 1.0, 2.0};
        double[] y = new double[] {3.0, 4.0, 1.0};
        double[] result = Interpolations.noInterpolation().interpolate(x, y, 3);
        assertEquals(3, result.length);
        assertArrayEquals(y, result, 0.000000000001);
    }

    @Test
    public void testNoInterpolation2() {
        // Interpolate 2 values
        double[] x = new double[] {0.0, 1.0};
        double[] y = new double[] {10.0, 4.0};
        double[] result = Interpolations.noInterpolation().interpolate(x, y, 4);
        assertEquals(4, result.length);
        assertArrayEquals(new double[] {10.0, 10.0, 4.0, 4.0}, result, 0.000000000001);
    }

    @Test
    public void testNoInterpolation3() {
        // Interpolate 3 values, checks middle value is prolonged in both directions
        double[] x = new double[] {0.0, 1.0, 2.0};
        double[] y = new double[] {3.0, 4.0, 1.0};
        double[] result = Interpolations.noInterpolation().interpolate(x, y, 7);
        assertEquals(7, result.length);
        assertArrayEquals(new double[] {3.0, 3.0, 4.0, 4.0, 4.0, 1.0, 1.0}, result, 0.000000000001);
    }

    @Test
    public void testNoInterpolation4() {
        // 4th value is taken but 2 is interpolated
        double[] x = new double[] {0.0, 1.0, 1.5, 2.0};
        double[] y = new double[] {3.0, 4.0, 2.0, 1.0};
        double[] result = Interpolations.noInterpolation().interpolate(x, y, 5);
        assertEquals(5, result.length);
        assertArrayEquals(new double[] {3.0, 4.0, 4.0, 2.0, 1.0}, result, 0.000000000001);
    }

    @Test
    public void testNoInterpolation5() {
        // Should discard bunch of values
        double[] x = new double[] {0.0, 0.1, 1.0, 1.5, 1.9, 2.0};
        double[] y = new double[] {3.0, 10.0, 4.0, 2.0, 10.0, 1.0};
        double[] result = Interpolations.noInterpolation().interpolate(x, y, 3);
        assertEquals(3, result.length);
        assertArrayEquals(new double[] {3.0, 4.0, 1.0}, result, 0.000000000001);
    }
}
