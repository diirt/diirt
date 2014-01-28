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
import org.epics.util.array.*;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class NLineGraphs2DRendererTest {
    /**
     * Tests the functions in SparklineGraph2DRenderer
     */
    public NLineGraphs2DRendererTest() {
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
    public void EvenSplitTest() throws Exception {
        double [][] initialData= new double [10][100]; 
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.EvenSplit", image);
    }
    @Test
    public void UnevenSplitTest() throws Exception {
        double [][] initialData= new double [9][100]; 
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 9; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UnevenSplit", image);
    }
    
    @Test
    public void TooManyGraphsTest() throws Exception {
        double [][] initialData= new double [120][100]; 
        for(int i = 0; i < 120; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

                     

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 120; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.TooManyGraphs", image);
    }
    
    @Test
    public void UpdateRatios() throws Exception {
        double [][] initialData= new double [10][100]; 
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        ArrayList<Double> new_Ratios = new ArrayList<Double>();
        for(double i = 0; i <= 10; i++){
            new_Ratios.add(i/10);
        }
        for(double i = 1; i < 10; i++){
            new_Ratios.set((int)i,new_Ratios.get((int)i)+.07);
        }
        update.GraphBoundaryRatios(new_Ratios);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UpdateRatios", image);
    }
    
}
