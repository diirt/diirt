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
 * @author sjdallst
 */
public class MultiYAxisGraph2DRendererTest extends BaseGraphTest<MultiYAxisGraph2DRendererUpdate, MultiYAxisGraph2DRenderer>{
    /**
     * Tests the functions in MultiYAxisGraph2DRenderer
     */
    public MultiYAxisGraph2DRendererTest() {
        super("multiYAxisGraph2D");
    }

    @Override
    public MultiYAxisGraph2DRenderer createRenderer() {
        return new MultiYAxisGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(MultiYAxisGraph2DRenderer renderer) {
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.oneGraph", image);
    }
    
    @Test
    public void twoGraph() throws Exception {
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
     
        ImageAssert.compareImages("multiYAxisGraph2D.twoGraph", image);
    }
    
    @Test
    public void threeGraph() throws Exception {
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
        ImageAssert.compareImages("multiYAxisGraph2D.threeGraph", image);
    }
    
    @Test
    public void fiveGraph() throws Exception {
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
        ImageAssert.compareImages("multiYAxisGraph2D.fiveGraph", image);
    }
    
    @Test
    public void sixGraph() throws Exception {
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
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraph", image);
    }
    
    @Test
    public void sixGraphWithRange() throws Exception {
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
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraphWithRange", image);
    }
    
    @Test
    public void minGraphWidth1() throws Exception {
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
        ImageAssert.compareImages("multiYAxisGraph2D.minGraphWidth1", image);
    }
    
    @Test
    public void updateInterpolations() throws Exception {
        double [][] initialData= new double [6][100]; 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = Math.pow((double)j, ((double)i)/5);
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 6; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        MultiYAxisGraph2DRendererUpdate update = new MultiYAxisGraph2DRendererUpdate();
        update.interpolation(InterpolationScheme.LINEAR);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.updateInterpolations", image);
    }
    
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.evenDivide", image);
    }
    
    @Test
    public void oneGraphSplit() throws Exception {
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
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.oneGraph", image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noGraphs() throws Exception {

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
     
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.multipleCosine", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.unevenDivide", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphs", image);
    }
    
    @Test
    public void excessGraphsUpdate() throws Exception {
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
        
        BufferedImage image = new BufferedImage(640, 550, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().imageHeight(550));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphsUpdate", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,550);
        renderer.update(renderer.newUpdate().imageHeight(550));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphsAfterUpdate", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().marginBetweenGraphs(50));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateMargins", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        HashMap<Integer, Range> map = new HashMap<Integer, Range>();
        map.put(1, RangeUtil.range(-50,50));
        renderer.update(renderer.newUpdate().setRanges(map));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateRanges", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().marginBetweenGraphs(100));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.marginsTooBig", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().split(true));
        
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
        ImageAssert.compareImages("multiYAxisGraph2D.split.resizing", image);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().minimumGraphHeight(50));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateMinimumGraphHeights", image);
    }
    
    @Test
    public void updateInterpolationsSplit() throws Exception {
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateInterpolations", image);
    }
}
