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
        Dataset1D dataset = MockDataset1D.uniform(0.0, 1.0, 300);
        Histogram1D histogram = Histograms.createHistogram(dataset);
        assertEquals(100, histogram.getNBins());
        assertEquals(0.0, histogram.getMinValueRange(), 0.00001);
        assertEquals(1.0, histogram.getMaxValueRange(), 0.00001);
    }
}
