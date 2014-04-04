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
public class MultilineGraph2DRendererTest extends BaseGraphTest<MultilineGraph2DRendererUpdate, MultilineGraph2DRenderer>{
    /**
     * Tests the functions in SparklineGraph2DRenderer
     */
    public MultilineGraph2DRendererTest() {
        super("multilineGraph2D");
    }

    @Override
    public MultilineGraph2DRenderer createRenderer() {
        return new MultilineGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(MultilineGraph2DRenderer renderer) {
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
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        renderer.draw(g, data);
        return image;
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
    public void singleValueSingleLine() throws Exception {
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
        ImageAssert.compareImages("multilineGraph2D.singleValueSingleLine", image);
    }
    
    @Test
    public void singleValueMultipleLines() throws Exception {
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
        ImageAssert.compareImages("multilineGraph2D.singleValueMultipleLines", image);
    }
    
    @Test
    public void multipleCosine() throws Exception {
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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multilineGraph2D.multipleCosine", image);
    }
    
    @Test
    public void multipleCosineColorScheme() throws Exception {
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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(640,480);
        MultilineGraph2DRendererUpdate update = new MultilineGraph2DRendererUpdate();
        update.valueColorScheme(new ValueColorSchemeJet());
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multilineGraph2D.multipleCosineColorScheme", image);
    }
    
    @Test
    public void linesEqualPixels() throws Exception {
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
        ImageAssert.compareImages("multilineGraph2D.linesEqualsPixels", image);
    }
    
    @Test
    public void manyLinesStress() throws Exception {
        double [][] initialData= new double [5000][100]; 
        for(int i = 0; i < 5000; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 5000; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(100,100);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multilineGraph2D.manyLinesStress", image);
    }
    
    @Test
    public void updateInterpolation() throws Exception {
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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultilineGraph2DRenderer renderer = new MultilineGraph2DRenderer(640,480);
        MultilineGraph2DRendererUpdate update = new MultilineGraph2DRendererUpdate();
        update.interpolation(InterpolationScheme.NEAREST_NEIGHBOUR);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multilineGraph2D.updateInterpolation", image);
    }
}
