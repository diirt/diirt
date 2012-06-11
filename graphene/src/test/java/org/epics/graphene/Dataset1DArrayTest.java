/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.CollectionNumbers;
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
    public void createAndAddData1() throws Exception {
        Point1DDataset dataset = new Point1DCircularBuffer(10);
        assertArrayEquals(new double[] {}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        assertEquals(Double.NaN, dataset.getMinValue().doubleValue(), 0.0001);
        assertEquals(Double.NaN, dataset.getMaxValue().doubleValue(), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(0.0));
        assertArrayEquals(new double[] {0.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        assertEquals(0.0, dataset.getMinValue().doubleValue(), 0.0001);
        assertEquals(0.0, dataset.getMaxValue().doubleValue(), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(1.0).addData(2.0));
        assertArrayEquals(new double[] {0.0, 1.0, 2.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(new double[] {3.0, 4.0, 5.0}));
        assertArrayEquals(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        assertEquals(0.0, dataset.getMinValue().doubleValue(), 0.0);
        assertEquals(5.0, dataset.getMaxValue().doubleValue(), 0.0);
        dataset.update(new Point1DDatasetUpdate().clearData());
        assertArrayEquals(new double[] {}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(0.0).clearData());
        assertArrayEquals(new double[] {}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(0.0).clearData().addData(3.0));
        assertArrayEquals(new double[] {3.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().clearData().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0}));
        dataset.update(new Point1DDatasetUpdate().addData(new double[] {6.0, 7.0, 8.0, 9.0, 10.0}));
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(11.0));
        assertArrayEquals(new double[] {2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().clearData().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}));
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
        dataset.update(new Point1DDatasetUpdate().addData(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}));
        assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.0001);
    }
    
    @Test
    public void createAndAddData2() throws Exception {
        Point1DDataset dataset = new Point1DCircularBuffer(10);
        dataset.update(new Point1DDatasetUpdate().addData(new double[] {0.000000145, 0.000000156, 0.000000130, 0.000000168, 0.000000111, 0.000000134}));
        assertArrayEquals(new double[] {0.000000145, 0.000000156, 0.000000130, 0.000000168, 0.000000111, 0.000000134}, CollectionNumbers.toDoubleArray(dataset.getValues()), 0.000000001);
        assertEquals(0.000000111, dataset.getMinValue().doubleValue(), 0.000000001);
        assertEquals(0.000000168, dataset.getMaxValue().doubleValue(), 0.000000001);
    }
    
}
