/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.epics.util.array.*;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class IntensityGraph2DRendererTest {
    public IntensityGraph2DRendererTest(){  
    }
        private static Cell2DDataset largeDataset;
    
    /**
     * Sets up the large dataset used in the tests
     * @throws Exception 
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random rand = new Random(1);
        int nSamples = 1000*1000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        ArrayDouble data = new ArrayDouble(waveform);
        largeDataset = Cell2DDatasets.linearRange(data,RangeUtil.range(0, 1000), 1000, RangeUtil.range(0, 1000), 1000);
    }

    /**
     * Empties the memory used in the large dataset
     * @throws Exception 
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }
    
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Min Value = Last Value</li>
     *      <li>There exists more than one min value</li>
     * </ul>
     * 
     * @throws Exception Test fails
     */    
    @Test
    public void test1() throws Exception {
    }
}
