/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class LineGraph2DRendererTest {
    
    public LineGraph2DRendererTest() {
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
    
    @Test
    public void test1() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.1", image);
    }
    
    @Test
    public void test2() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.2", image);
    }
    
    @Test
    public void test3() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.3", image);
    }
    
    @Test
    public void test4() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3, Double.NaN, 4, 5, 6), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.4", image);
    }
    
    @Test
    public void test5() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1, Double.NaN, 3, Double.NaN, 4, 5, 6), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.5", image);
    }
    
    @Test
    public void test6() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(5,3,1,4,2,0), 
                new ArrayDouble(25,9,1,16,4,0));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.6", image);
    }
    
    @Test
    public void test7() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1,2,3,4,5,6), 
                new ArrayDouble(0.01,0.1,1,10,100,1000));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR)
                .yValueScale(ValueScales.logScale()));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.7", image);
    }
    
    @Test(timeout = 500)
    public void test8() throws Exception {
        Point2DDataset dataset = largeDataset;
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraph.8", image);
    }
    
    @Test(timeout = 500)
    public void test8b() throws Exception {
        Point2DDataset dataset = largeDataset;
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraph.8", image);
    }
    
    @Test(timeout = 300)
    public void test9() throws Exception {
        Point2DDataset dataset = largeDataset;
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR)
                .xAxisRange(AxisRanges.absolute(10, 20)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraph.9", image);
    }
    
    @Test
    public void test10() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(5,3,1,4,2,0), 
                new ArrayDouble(25,9,1,16,4,0));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR)
                .focusPixel(250).highlightFocusValue(true));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        assertThat(renderer.getFocusValueIndex(), equalTo(3));
        ImageAssert.compareImages("lineGraph.10", image);
    }
    
    /**
     *LineGraph2DRendererTest
     * Tests Linear Interpolation Scheme
     * @throws Exception
     */
    @Test
    public void test11() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(0,1,2,3,4,5), 
                new ArrayDouble(3,3,3,3,3,3));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.11", image);
    }  
    
    /**
     *LineGraph2DRendererTest
     * Makes sure that graph is drawn correctly with multiple Double.NaN data points
     * @throws Exception
     */
    @Test
   
    public void test12() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(Double.NaN, Double.NaN, 1, 3, 5, Double.NaN, 1), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.12", image);
    }
   
    /**
     *LineGraph2DRendererTest
     * Tests linear interpolation accuracy
     * @throws Exception
     */
    @Test
   
    public void test13() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(10,10,5,6,2), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.13", image);
    }
    /**
     *LineGraph2DRendererTest
     * Tests graph with negative values
     * @throws Exception
     */
    @Test
   
   public void test14() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(-100,-200,0,10,20));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.14", image);
    }
   
    /**
     *LineGraph2DRendererTest
     * Tests graph with larger dataset; should be a space where Double.NaN is located
     * @throws Exception
     */
    @Test
   public void test15() throws Exception {
        double[] dataSet = new double[100];
        for(int i = 0; i < 50; i++)
            dataSet[i] = i;
        dataSet[50] = Double.NaN;
        for(int i = 51; i < 100; i++)
            dataSet[i] = 100 - i;
        
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph.15", image);
    }
}
