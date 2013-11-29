/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class SparklineGraph2DRendererTest {
    
    public SparklineGraph2DRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    //CIRCLE DRAWING TESTS
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Min Value = Last Value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */    
    @Test
    public void test1() throws Exception {
        double[] initialDataY = new double[101];

            //Creates the function:
                //f(x) = x          for  0  <= x <  50
                //f(x) = 100 - x    for  50 <= x <= 100
            for(int x = 0; x < 50; x++){
                initialDataY[x] = x;
            }
            for (int x = 50; x <= 100; x++){
                initialDataY[x] = 100 - x;
            }         
        
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);

        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.1", image);
    }
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Max Value = Last Value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */        
    @Test
    public void test2() throws Exception {
        double[] initialDataY = new double[101];
        
            //Creates the function:
                //f(x) = 50 - x         for  0  <= x <  50
                //f(x) = x - 50         for  50 <= x <= 100
            for (int x = 0; x <= 49; x++){
                initialDataY[x] = 50 - x;
            }
            for (int x =50; x <= 100; x++){
                initialDataY[x] = x - 50;
            }
            
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
            
        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);

        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.2", image);
    }
        
    /**
     * Tests case of:
     * <ul>
     *      <li>Max Value = Min Value = Last Value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */    
    @Test
    public void test3() throws Exception {
        double[] initialDataY = new double[100];
        
            //Creates the function:
                //f(x) = 1      for 0 <= x < 100
            for(int i = 0; i < 100; i++){
                initialDataY[i] = 1;
            }
            
        //Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(0,1,2,3,4,5), 
                new ArrayDouble(3,3,3,3,3,3));    
        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);

        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.3", image);
    }
    
    /**
     * Tests case of:
     * <ul>
     *      <li>There exists more than one max value</li>
     *      <li>There exists more than one min value</li>
     * </ul>
     * 
     * @throws Exception test fails
     */   
    @Test
    public void test4() throws Exception {
        double[] initialDataY = new double[100];
        
            //Creates the function:
                //f(x) = 1      for 0 <= x < 33
                //f(x) = -1     for 33 <= x < 67
                //f(x) = 0      for 67 <= x < 100
            for(int x = 0; x < 33; x++){
                initialDataY[x] = 1;
            }
            for (int x = 33; x < 67; x++){
                initialDataY[x] = -1;
            }
            for (int x = 67; x < 100; x++){
                initialDataY[x] = 0;
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
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
     * Tests case where a maximum circle (blue) and last value circle (red)
     * are close to the same pixel point and overlap.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test5() throws Exception {
        double[] initialDataY = new double[100];
        
            for (int x = 0; x < 98; x++){
                initialDataY[x] = 90;
            }
            initialDataY[98] = 100;
            initialDataY[99] = 99;

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
            
        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 25, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,25);

        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.5", image);          
    }
        
    
    //ASPECT RATIO TESTS
    
    /**
     * Tests case when the aspect ratio would cause the image to draw out of bounds.
     * The size of the data is greater than the size of the image.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test6() throws Exception {
        double[] initialDataY = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataY[index] = m + i; 
                        index++;
                    }
                }
            }

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,20);
        renderer.update( renderer.newUpdate().aspectRatio(5) );
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.6", image);          
    }
    
    /**
     * Tests case where the aspect ratio is set and the height is the limiting factor.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test7() throws Exception{
        
        double[] sampleData = new double[10];
            for(int i = 0; i < 10; i++){
                if(i % 2 == 0)
                    sampleData[i] = -1.5 * i;
                else
                    sampleData[i] = 1.5 * i;
            }
        Point2DDataset data = Point2DDatasets.lineData(sampleData);
        
        //Graphics
        BufferedImage image = new BufferedImage(200,200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        
        //Sparkline
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(200,200);
        renderer.update( renderer.newUpdate().aspectRatio(5) );
        renderer.draw(g, data);
        
        //Assert true
        ImageAssert.compareImages("sparkline2D.7", image);
    }
    
    /**
     * Tests case where the aspect ratio is set and the width is the limiting factor.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test8() throws Exception{
        double[] sampleData = new double[10];
        
            for(int i = 0; i < 10; i++){
                if(i % 2 == 0)
                    sampleData[i] = -1.5 * i;
                else
                    sampleData[i] = 1.5 * i;
            }
            
        Point2DDataset data = Point2DDatasets.lineData(sampleData);
        
        //Graphics
        BufferedImage image = new BufferedImage(200,20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        
        //Sparkline
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(200,20);
        renderer.update( renderer.newUpdate().aspectRatio(5) );
        renderer.draw(g, data);
        
        //Assert true
        ImageAssert.compareImages("sparkline2D.8", image);
    }    
    
    
    //GENERAL SPARKLINE TESTS
    
    /**
     * Tests case of a non-LINEAR interpolation scheme (uses NEAREST NEIGHBOR).
     * 
     * @throws Exception test fails
     */    
    @Test
    public void test9() throws Exception{
        double[] initialDataY = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataY[index] = Math.pow(m + i, 4); 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,50);
        renderer.update( renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.NEAREST_NEIGHBOUR) );
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.9", image);               
    }
    
    /**
     * Tests data that is similar to a general Sparkline graph.
     * The general shape is slopes shifting up and down for small line segments.
     * 
     * This test uses an aspect-ratio similar to a general Sparkline graph.
     * This general aspect-ratio is a 5:1 (width:height).
     * The aspect ratio is set manually by setting the width to be 5 times as big as the height.
     * 
     * @throws Exception test fails
     */
    @Test
    public void test10() throws Exception {
        double[] initialDataY = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataY[index] = m + i; 
                        index++;
                    }
                }
            }

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
            
        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,20);

        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.10", image);          
    }   
    
    /**
     * Tests case when aspect ratio is not set.
     * 
     * @throws Exception 
     */
    @Test
    public void test11() throws Exception {
        double[] initialDataY = new double[200];
        
            int index = 0;
            for (int m = 1; index < 200; m++){
                for (int i = 0; i < m * 5; i++){
                    if (index < 200){
                        initialDataY[index] = m + i; 
                        index++;
                    }
                }
            }
            
        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("sparkline2D.11", image);          
    }    
    
}
