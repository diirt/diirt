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

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class SparklineGraph2DRendererTest {
    /**
     * Tests the functions in SparklineGraph2DRenderer
     */
    public SparklineGraph2DRendererTest() {
    }

    private static Point2DDataset largeDataset;
    
    /**
     * Sets up the large dataset used in the tests
     * @throws Exception 
     */
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
     * @throws Exception test fails
     */    
    @Test
    public void test1() throws Exception {
        double[] initialDataX = new double[101];
        
            //Creates the function:
                //f(x) = x          for  0  <= x <  50
                //f(x) = 100 - x    for  50 <= x <= 100
            for(int x = 0; x < 50; x++){
                initialDataX[x] = x;
            }
            for (int x = 50; x <= 100; x++){
                initialDataX[x] = 100 - x;
            }         

        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.1", image);
    }
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Max Value = Last Value</li>
     *      <li>There exists more than one max value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */        
    @Test
    public void test2() throws Exception {
        double[] initialDataX = new double[101];
        
            //Creates the function:
                //f(x) = 50 - x         for  0  <= x <  50
                //f(x) = x - 50         for  50 <= x <= 100
            for (int x = 0; x <= 49; x++){
                initialDataX[x] = 50 - x;
            }
            for (int x =50; x <= 100; x++){
                initialDataX[x] = x - 50;
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.2", image);
    }
        
    /**
     * Tests case of:
     * <ul>
     *      <li>Max Value = Min Value = Last Value</li>
     *      <li>There exists more than one max value</li>
     *      <li>There exists more than one min value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */    
    @Test
    public void test3() throws Exception {
        double[] initialDataX = new double[100];
        
            //Creates the function:
                //f(x) = 1      for 0 <= x < 100
            for(int i = 0; i < 100; i++){
                initialDataX[i] = 1;
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.3", image);
    }
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Max Value != Last Value</li>
     *      <li>Min Value != Last Value</li>
     *      <li>There exists more than one max value</li>
     *      <li>There exists more than one min value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */   
    @Test
    public void test4() throws Exception {
        double[] initialDataX = new double[100];
        
            //Creates the function:
                //f(x) = 1      for 0 <= x < 33
                //f(x) = -1     for 33 <= x < 67
                //f(x) = 0      for 67 <= x < 100
            for(int x = 0; x < 33; x++){
                initialDataX[x] = 1;
            }
            for (int x = 33; x < 67; x++){
                initialDataX[x] = -1;
            }
            for (int x = 67; x < 100; x++){
                initialDataX[x] = 0;
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.4", image);        
    }
    
    /**
     * Tests data that is similar to a general Sparkline graph.
     * The general shape is slopes shifting up and down for small line segments.
     * 
     * This test uses an aspect-ratio similar to a general Sparkline graph.
     * This general aspect-ratio is a 5:1 (width:height).
     * 
     * @throws Exception test fails
     */
    @Test
    public void test5() throws Exception {
        double[] initialDataX = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataX[index] = m + i; 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 25, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,25);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.5", image);          
    }
    
    /**
     * Tests case where a maximum circle (blue) and last value circle (red)
     * are close to the same pixel point and overlap.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test6() throws Exception {
        double[] initialDataX = new double[100];
        
            for (int x = 0; x < 98; x++){
                initialDataX[x] = 90;
            }
            initialDataX[98] = 100;
            initialDataX[99] = 99;

        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 25, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,25);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.6", image);          
    }
    
    //Tests the case when the aspect ratio would cause the image to draw out of bounds. 
    @Test
    public void test7() throws Exception {
        double[] initialDataX = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataX[index] = m + i; 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,20);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.7", image);          
    }
    
    //Tests the case when the aspect ratio is not set.
    @Test
    public void test8() throws Exception {
        double[] initialDataX = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataX[index] = m + i; 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.8", image);          
    }
    
    //Tests the case of multiple draws with aspect ratio.
    @Test
    public void test9() throws Exception {
        double[] initialDataX = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataX[index] = m + i; 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        SparklineGraph2DRendererUpdate update = new SparklineGraph2DRendererUpdate();
        update.aspectRatio(5);
        renderer.draw(g, data);
        double[] changedData = new double[50];
        for(int i = 0; i < 50; i++){
            changedData[i] = initialDataX[i+100]; 
        }
        Point2DDataset data1 = Point2DDatasets.lineData(changedData);
        renderer.draw(g,data1);
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.9", image);          
    }
}
