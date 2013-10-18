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
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class SparklineGraph2DRendererTest {
    
    public SparklineGraph2DRendererTest() {
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
    
    //Testing: min = lastValue
    @Test
    public void test1() throws Exception {
        double[] initialDataX = new double[100];
        for(int i = 0; i < 50; i++){
            initialDataX[i] = i;
        }
        for(int i = 49; i>=0; i--){
            initialDataX[100-i-1] = i;
        }
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        ImageAssert.compareImages("sparkline2D.1", image);
    }
    
    //Testing: max = current value;
        @Test
    public void test2() throws Exception {
        double[] initialDataX = new double[100];
            for(int i = 49; i>=0; i--)
            {
                initialDataX[50-1-i]= i;
            }
            for(int i = 0; i< 50; i++)
            {
                initialDataX[50+i]= i;
            }
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        ImageAssert.compareImages("sparkline2D.2", image);
    }
        
        //Testing: max = min = lastValue.
        
        @Test
    public void test3() throws Exception {
        double[] initialDataX = new double[100];
            for(int i = 0; i < 100; i++){
                initialDataX[i] = 1;
            }
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        ImageAssert.compareImages("sparkline2D.3", image);
    }
}
