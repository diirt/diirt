/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Test;
import java.util.List;
import org.junit.BeforeClass;
import java.math.*;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class MultilineGraph2DRendererTest {
    /**
     * Tests the functions in SparklineGraph2DRenderer
     */
    public MultilineGraph2DRendererTest() {
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
     * @throws Exception Test fails
     */    
    @Test
    public void SingleValueSingleLineTest() throws Exception {
        double [][] initialData= new double [1][100]; 
        for(int i = 0; i < 1; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 1; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiline2D.SingleValueSingleLine", image);
    }
    
    @Test
    public void SingleValueMultipleLinesTest() throws Exception {
        double [][] initialData= new double [10][100]; 
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiline2D.SingleValueMultipleLines", image);
    }
    
    @Test
    public void MultipleCosineTest() throws Exception {
        double [][] initialData= new double [10][100]; 
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiline2D.MultipleCosine", image);
    }
    
    @Test
    public void MultipleCosineColorSchemeTest() throws Exception {
        double [][] initialData= new double [10][100]; 
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        MultilineGraph2DRendererUpdate update = new MultilineGraph2DRendererUpdate();
        update.valueColorScheme(ColorScheme.JET);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiline2D.MultipleCosineColorScheme", image);
    }
    
    @Test
    public void LinesEqualPixelsTest() throws Exception {
        double [][] initialData= new double [81][100]; 
        for(int i = 0; i < 81; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 81; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiline2D.LinesEqualsPixels", image);
    }
}
