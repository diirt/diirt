/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Jiakung
 * General test class in which LineGraph@DRenderer, SparklineGraph2DRenderer extend from 
 */
public class GeneralLineTest {
     
    public GeneralLineTest() {
    }

    private static Point2DDataset largeDataset;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random rand = new Random(1);
        int nSamples = 100000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        largeDataset = org.epics.graphene.Point2DDatasets.lineData(waveform);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }
    
    @Test
    //Tests test case in which there is one data point
    public void testOneDataPoint() throws Exception{
    
    }
    
    @Test
    //Tests test case in which there are two data points
    public void testTwoDataPoints() throws Exception{
    
    }
    
    @Test
    //Tests test case in which there are multiple data points
    public void testMultipleDataPoints() throws Exception{
    
    }
    
    @Test
    //Tests test case in which the dataset contains negative values
    public void testNegativeDataPoints() throws Exception{
        
    }
    
    @Test
    //Tests LINEAR interpolation scheme
    public void testLinearInterpolation() throws Exception{
        
    }
    
    @Test
    //Tests NEAREST NEIGHBOR interpolation scheme
    public void testNearestNeighborInterpolation() throws Exception{
        
    }
    
    @Test
    //Tests Cubic interpolation scheme
    public void testCubicInterpolation() throws Exception{
        
    }
}
