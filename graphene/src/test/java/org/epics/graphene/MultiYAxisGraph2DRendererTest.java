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
 * @author smaceul
 */
public class MultiYAxisGraph2DRendererTest {
    /**
     * Tests the functions in MultiYAxisGraph2DRenderer
     */
    public MultiYAxisGraph2DRendererTest() {
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
    
    @Test
    public void singleGraphTest() throws Exception {
        double [][] initialData= new double [1][100]; 
        for(int i = 0; i < 1; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 1; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.singleGraph", image);
    }
    
    @Test
    public void TwoGraphsTest() throws Exception {
        double [][] initialData= new double [2][100]; 
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 2; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.TwoGraphs", image);
    }
    
    @Test
    public void ThreeGraphsTest() throws Exception {
        double [][] initialData= new double [3][100]; 
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.ThreeGraphs", image);
    }
    
    @Test
    public void FiveGraphsTest() throws Exception {
        double [][] initialData= new double [5][100]; 
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 5; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.FiveGraphs", image);
    }
    
    @Test
    public void SixGraphsTest() throws Exception {
        double [][] initialData= new double [6][100]; 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 6; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.SixGraphs", image);
    }
    
    @Test
    public void SixGraphsWRangeTest() throws Exception {
        double [][] initialData= new double [6][100]; 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = Math.pow((double)j, ((double)i)/5);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 6; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.SixGraphsWRange", image);
    }
    
    @Test
    public void MinGraphWidthTest() throws Exception {
        double [][] initialData= new double [6][100]; 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = Math.pow((double)j, ((double)i)/5);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 6; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        MultiYAxisGraph2DRendererUpdate update = new MultiYAxisGraph2DRendererUpdate();
        update.minimumGraphWidth(600);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiyaxisgraph2D.MinGraphWidth", image);
    }
}
