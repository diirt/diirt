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
import org.junit.Ignore;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class NLineGraphs2DRendererTest extends BaseGraphTest<NLineGraphs2DRendererUpdate, NLineGraphs2DRenderer> {
    /**
     * Tests the functions in SparklineGraph2DRenderer
     */
    public NLineGraphs2DRendererTest() {
        super("nLineGraphs2D");
    }

    @Override
    public NLineGraphs2DRenderer createRenderer() {
        return new NLineGraphs2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(NLineGraphs2DRenderer renderer) {
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
    public void evenDivide() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.evenDivide", image);
    }
    
@Test
    public void oneGraph() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.oneGraph", image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noGraphs() throws Exception {

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
     
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        renderer.draw(g, data);
    
    }

    @Test
    public void multipleCosine() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.multipleCosine", image);
    }


    @Test
    public void unevenDivide() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.unevenDivide", image);
    }
 
    
    @Test
    public void excessGraphs() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.excessGraphs", image);
    }
    
    @Test
    public void excessGraphsUpdate() throws Exception {
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
        ImageAssert.compareImages("nLineGraphsD.excessGraphsUpdate", image);
    }
    
    @Test
    public void excessGraphsAfterUpdate() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.excessGraphsAfterUpdate", image);
    }
    
    @Test
    public void updateMargins() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.updateMargins", image);
    }
    
    @Test
    public void updateRanges() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.updateRanges", image);
    }
    
    @Test
    public void marginsTooBig() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.marginsTooBig", image);
    }
    
    @Test
    //@Ignore("TODO: Currently this test crashes with an exception")
    public void resizing() throws Exception {
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DTestDatasets.sineDataset(100, 50, 0, 1, 0, RangeUtil.range(0, 99)));
        }
        
        BufferedImage image = new BufferedImage(640, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        NLineGraphs2DRenderer renderer = new NLineGraphs2DRenderer(640,480);
        
        // Gradually reduce the image to simulate window being stretched
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(400));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(390));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(380));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(370));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(360));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(350));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR).imageHeight(340));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("nLineGraphs2D.resizing", image);
    }
    
    @Test
    public void updateMinimumGraphHeights() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.updateMinimumGraphHeights", image);
    }
    
    @Test
    public void updateInterpolations() throws Exception {
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
        ImageAssert.compareImages("nLineGraphs2D.updateInterpolations", image);
    }
    
}
