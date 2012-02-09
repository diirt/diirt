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
public class Histogram1DTest {
    
    public Histogram1DTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void computeHistogram() throws Exception {
        Dataset1D dataset = MockDataset1D.uniform(0.0, 100.0, 300);
        Histogram1D h = Histograms.createHistogram(dataset);
        assertEquals(100, h.getNBins());
        assertEquals(0.0, h.getMinValueRange(), 0.0000001);
        assertEquals(100.0, h.getMaxValueRange(), 0.0000001);
        for (int i = 0; i < 99; i++) {
            assertEquals(1.0 * i, h.getBinValueBoundary(i), 0.0001);
            assertEquals("Element " + i,3, h.getBinCount(i));
        }
        assertEquals(99, h.getBinValueBoundary(99), 0.0001);
        assertEquals(4, h.getBinCount(99));
    }
}
