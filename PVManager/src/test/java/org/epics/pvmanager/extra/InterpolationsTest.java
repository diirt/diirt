/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.extra;

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
    public void testNoInterpolationPreciseValues() {
        double[] x = new double[] {0.0, 1.0, 2.0};
        double[] y = new double[] {3.0, 4.0, 1.0};
        double[] result = Interpolations.noInterpolation(x, y, 3);
        assertEquals(3, result.length);
        assertArrayEquals(y, result, 0.000000000001);
    }
    
    @Test
    public void testNoInterpolation() {
        double[] x = new double[] {0.0, 1.0};
        double[] y = new double[] {10.0, 4.0};
        double[] result = Interpolations.noInterpolation(x, y, 4);
        assertEquals(4, result.length);
        assertArrayEquals(new double[] {10.0, 10.0, 4.0, 4.0}, result, 0.000000000001);
    }
}
