/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
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
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.EvenSplit", image);
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
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.singleGraph", image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NoGraphsTest() throws Exception {

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
     
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
    
    }

    @Test
    public void MultiCosTest() throws Exception {
        double [][] initialData= new double [3][100]; 
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.MultiCos", image);
    }


    @Test
    public void UnevenSplitTest() throws Exception {
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
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UnevenSplit", image);
    }
 
    
    @Test
    public void TooManyGraphsTest() throws Exception {
        double [][] initialData= new double [5][100]; 
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }
        
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 5; i++){
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
    public void TooManyGraphsUpdateTest() throws Exception {
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
        BufferedImage image = new BufferedImage(640, 550, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.imageHeight(550);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.TooManyGraphsUpdate", image);
    }
    
    @Test
    public void TooManyGraphsAfterUpdateTest() throws Exception {
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
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,550);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.imageHeight(480);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.TooManyGraphsAfterUpdate", image);
    }
    
    @Test
    public void UpdateMargin() throws Exception {
        double [][] initialData= new double [2][100]; 
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 2; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.marginBetweenGraphs(50);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UpdateMargin", image);
    }
    
    @Test
    public void UpdateRangeTest() throws Exception {
        double [][] initialData= new double [4][100]; 
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 4; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        HashMap<Integer, Range> map = new HashMap<Integer, Range>();
        map.put(1, RangeUtil.range(-50,50));
        update.setRanges(map);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UpdateRange", image);
    }
    
    @Test
    public void MarginTooBig() throws Exception {
        double [][] initialData= new double [3][100]; 
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.marginBetweenGraphs(100);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.MarginTooBig", image);
    }
    
    @Test
    public void UpdateMinimumGraphHeightTest() throws Exception {
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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.minimumGraphHeight(50);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UpdateMinimumGraphHeight", image);
    }
    
    @Test
    public void UpdateInterpolation() throws Exception {
        double [][] initialData= new double [2][100]; 
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)(i+1)*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }
        
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 2; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        NLineGraphs2DRendererUpdate update = new NLineGraphs2DRendererUpdate();
        update.interpolation(InterpolationScheme.LINEAR);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nlinegraphs2D.UpdateInterpolation", image);
    }
    
}
