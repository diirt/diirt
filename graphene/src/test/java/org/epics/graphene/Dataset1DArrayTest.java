/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class Dataset1DArrayTest {
    
    public Dataset1DArrayTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void createAndAddData() throws Exception {
        Dataset1D dataset = new Dataset1DArray(10);
        assertArrayEquals(new double[] {}, Iterators.toArray(dataset.getValues()), 0.0001);
        assertEquals(Double.NaN, dataset.getMinValue(), 0.0001);
        assertEquals(Double.NaN, dataset.getMaxValue(), 0.0001);
        dataset.update().addData(0.0);
        assertArrayEquals(new double[] {}, Iterators.toArray(dataset.getValues()), 0.0001);
        assertEquals(Double.NaN, dataset.getMinValue(), 0.0001);
        assertEquals(Double.NaN, dataset.getMaxValue(), 0.0001);
        dataset.update().addData(0.0).commit();
        assertArrayEquals(new double[] {0.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        assertEquals(0.0, dataset.getMinValue(), 0.0001);
        assertEquals(0.0, dataset.getMaxValue(), 0.0001);
        dataset.update().addData(1.0).addData(2.0).commit();
        assertArrayEquals(new double[] {0.0, 1.0, 2.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().addData(new double[] {3.0, 4.0, 5.0}).commit();
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().clearData();
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().clearData().commit();
        assertEquals(0.0, dataset.getMinValue(), 0.0);
        assertEquals(0.0, dataset.getMaxValue(), 5.0);
        assertArrayEquals(new double[] {}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().addData(0.0).clearData().commit();
        assertArrayEquals(new double[] {}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().addData(0.0).clearData().addData(3.0).commit();
        assertArrayEquals(new double[] {3.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().clearData().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0}).commit();
        dataset.update().addData(new double[] {6.0, 7.0, 8.0, 9.0, 10.0}).commit();
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().addData(11.0).commit();
        assertArrayEquals(new double[] {2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().clearData().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}).commit();
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, Iterators.toArray(dataset.getValues()), 0.0001);
        dataset.update().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}).commit();
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, Iterators.toArray(dataset.getValues()), 0.0001);
    }
    
}
