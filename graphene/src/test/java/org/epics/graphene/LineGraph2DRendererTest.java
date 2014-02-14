/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
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

public class LineGraph2DRendererTest extends BaseGraphTest<LineGraph2DRendererUpdate, LineGraph2DRenderer> {

    public LineGraph2DRendererTest() {
        super("lineGraph");
    }

    @Override
    public LineGraph2DRenderer createRenderer() {
        return new LineGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(LineGraph2DRenderer renderer) {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = i;
        }
        for (int i = 50; i < 100; i++) {
            dataSet[i] = 100 - i;
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), data);
        return image;
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
    public void testOneDataPoint() throws Exception {
        double[] dataSet = new double[1];
        dataSet[0] = 1.5;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphOneDataPoint", image);
    }

    @Test
    public void testTwoDataPoints() throws Exception {
        double[] dataSet = new double[2];
        dataSet[0] = 10;
        dataSet[1] = 20;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphTwoDataPoints", image);
    }

    @Test
    public void testMultipleDataPoints() throws Exception {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = i;
        }
        for (int i = 50; i < 100; i++) {
            dataSet[i] = 100 - i;
        }

        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphMultiplePoints", image);
    }

    @Test
    public void testNegativePoints() throws Exception {
        double[] dataSet = new double[100];
        for (int i = 0; i < 100; i++) {
            dataSet[i] = (i * -.5) / Math.pow(i, 2);
        }

        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNegativePoints", image);
    }

    @Test
    public void testEndsNaNLinear() throws Exception {
        double[] dataSet = {Double.NaN, 2, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphEndsNaNLinear", image);
    }

    @Test
    public void testConsecutiveNaNLinear() throws Exception {
        double[] dataSet = {1, Double.NaN, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphConsecutiveNaNLinear", image);
    }
    
    @Test
    public void testMultipleNaNLinear() throws Exception{
        double[] dataSet = {1, Double.NaN, 10, Double.NaN, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphMultipleNaNLinear", image);
    }

    @Test
    public void testOneNaNNeighbor() throws Exception {
        double[] dataSet = {1, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphOneNaNNeighbor", image);
    }

    @Test
    public void testMultipleNaNNeighbor() throws Exception {
        double[] dataSet = {1, Double.NaN, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphMultipleNaNNeighbor", image);
    }

    @Test
    public void testOneValueCubic() throws Exception{
    double[] dataSet = {5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphOneValueCubic", image);
    }
    
    @Test
    public void testTwoValuesCubic() throws Exception{
    double[] dataSet = {125, 216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphTwoValuesCubic", image);
    }
    @Test
    public void testOneNaNCubic() throws Exception {
        double[] dataSet = {1, 8, 27, Double.NaN, 125, 216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphOneNaNCubic", image);
    }

    @Test
    public void testMultipleNaNCubic() throws Exception {
        double[] dataSet = {1, 8, 27, Double.NaN, 125, Double.NaN, 349};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphMultipleNaNCubic", image);
    }
    
    @Test
    public void testConsecNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, Double. NaN, 2, 5, 9, 15};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphConsecNaNCubic", image);
    }
    
    @Test
    public void testRandomNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, 5, Double. NaN, 2, 3, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphRandomNaNCubic", image);
    }
    
    @Test
    public void testNormalCubic() throws Exception {
        double[] dataSet = new double[10];
        for(int i = 0;  i < 10; i++)
            dataSet[i] = i * i;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNormalCubic", image);
    }
    
    @Test
    public void testEndsNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, 125, 200, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphEndsNaNCubic", image);
    }
    @Test
    public void testLinearInterpolation() throws Exception {
        double[] dataSet = new double[100];
        for (int i = 0; i < 100; i++) {
            dataSet[i] = Math.pow(i, 2);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphLinearInterpolation", image);
    }

    @Test
    public void testNearestNeighborInterpolation() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3, Double.NaN, 4, 5, 6), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNearestNeighbor", image);
    }

    @Test
    public void testCubicInterpolation() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphCubicInterpolation", image);
    }

    @Test
    public void testHighlightFocusValue() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(5, 3, 1, 4, 2, 0),
                new ArrayDouble(25, 9, 1, 16, 4, 0));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR)
                .focusPixel(250).highlightFocusValue(true));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        assertThat(renderer.getFocusValueIndex(), equalTo(3));
        ImageAssert.compareImages("lineGraphFocusValue", image);
    }

    @Test
    public void testXAxisRange() throws Exception {
        Point2DDataset dataset = largeDataset;
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR)
                .xAxisRange(AxisRanges.absolute(10, 20)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraphXAxisRange", image);
    }
    /*
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
    
    
     *LineGraph2DRendererTest
     * Tests Linear Interpolation Scheme
     * @throws Exception
     
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
    
     *LineGraph2DRendererTest
     * Makes sure that graph is drawn correctly with multiple Double.NaN data points
     * @throws Exception
     
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
   
    
     *LineGraph2DRendererTest
     * Tests linear interpolation accuracy
     * @throws Exception
     
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
    
     *LineGraph2DRendererTest
     * Tests graph with negative values
     * @throws Exception
     
     @Test
   
     public void test14() throws Exception {
     Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(-100,-200,0,10,20));
     BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
     LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
     Graphics2D graphics = (Graphics2D) image.getGraphics();
     renderer.draw(graphics, data);
     ImageAssert.compareImages("lineGraph.14", image);
     }
   
    
     *LineGraph2DRendererTest
     * Tests graph with larger dataset; should be a space where Double.NaN is located
     * @throws Exception
     
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
     */

}
