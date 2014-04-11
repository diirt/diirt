/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class LineGraph2DRendererTest extends BaseGraphTest<LineGraph2DRendererUpdate, LineGraph2DRenderer> {

    public LineGraph2DRendererTest() {
        super("lineGraph2D");
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
    public void oneDataPointLinear() throws Exception {
        double[] dataSet = new double[1];
        dataSet[0] = 1.5;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.oneDataPoint", image);
    }

    @Test
    public void twoDataPointsLinear() throws Exception {
        double[] dataSet = new double[2];
        dataSet[0] = 10;
        dataSet[1] = 20;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.twoDataPoints", image);
    }

    @Test
    public void multipleDataPointsLinear() throws Exception {
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
        ImageAssert.compareImages("lineGraph2D.linear.multiplePoints", image);
    }

    @Test
    public void negativePoints() throws Exception {
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
        ImageAssert.compareImages("lineGraph2D.linear.negativePoints", image);
    }

    @Test
    public void endsNaNLinear() throws Exception {
        double[] dataSet = {Double.NaN, 2, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.ends", image);
    }

    @Test
    public void consecutiveNaNLinear() throws Exception {
        double[] dataSet = {1, Double.NaN, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.consecutive", image);
    }

    @Test
    public void twoNaNLinear() throws Exception {
        double[] dataSet = {1, Double.NaN, 10, Double.NaN, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.2", image);
    }

    @Test
    public void oneNaNNearestNeighbor() throws Exception {
        double[] dataSet = {1, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighbor.NaN.1", image);
    }

    @Test
    public void twoNaNNearestNeighbor() throws Exception {
        double[] dataSet = {1, Double.NaN, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighbor.NaN.2", image);
    }

    @Test
    public void oneValueCubic() throws Exception {
        double[] dataSet = {5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.oneValue", image);
    }

    @Test
    public void startNanEndValueCubic() throws Exception {
        double[] dataSet = {Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startNaNEndValue", image);
    }

    @Test
    public void twoValuesCubic() throws Exception {
        double[] dataSet = {125, 216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.twoValues", image);
    }

    @Test
    public void startValueEndNaNCubic() throws Exception {
        double[] dataSet = {5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startValueEndNaN", image);
    }

    @Test
    public void startThreeNaNEndValueCubic() throws Exception {
        double[] dataSet = {Double.NaN, Double.NaN, Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startThreeNaNEndValue", image);
    }

    @Test
    public void startValueEndThreeNaNCubic() throws Exception {
        double[] dataSet = {5, Double.NaN, Double.NaN, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startValueEndThreeNaN", image);
    }

    @Test
    public void middleNaNCubic() throws Exception {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sqrt(i);
        }
        dataSet[50] = Double.NaN;
        for (int i = 51; i < 100; i++) {
            dataSet[i] = Math.sqrt(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.middle", image);
    }

    @Test
    public void middleOfCurveNaNCubic() throws Exception {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sin(i);
        }
        dataSet[50] = Double.NaN;
        for (int i = 51; i < 100; i++) {
            dataSet[i] = Math.sin(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.middleOfCurve", image);
    }

    @Test
    public void firstMiddleLastNaNCubic() throws Exception {
        double[] dataSet = new double[20];
        dataSet[0] = Double.NaN;
        for (int i = 1; i < 10; i++) {
            dataSet[i] = -2 * i;
        }
        dataSet[10] = Double.NaN;
        for (int i = 11; i < 19; i++) {
            dataSet[i] = 2 * i;
        }
        dataSet[19] = Double.NaN;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.firstMiddleLast", image);
    }

    @Test
    public void oneNaNCubic() throws Exception {
        double[] dataSet = {1, 8, 27, Double.NaN, 125, 216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.1", image);
    }

    @Test
    public void twoNaNCubic() throws Exception {
        double[] dataSet = {1, 8, 27, Double.NaN, 125, Double.NaN, 349};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.2", image);
    }

    @Test
    public void consecutiveNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, Double.NaN, 2, 5, 9, 15};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.consecutive", image);
    }
    @Test
    public void threeNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, 5, Double.NaN, 2, 3, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.3", image);
    }

    @Test
    public void regularDataCubic() throws Exception {
        double[] dataSet = new double[10];
        for (int i = 0; i < 10; i++) {
            dataSet[i] = i * i;
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.regularData", image);
    }

    @Test
    public void endsNaNCubic() throws Exception {
        double[] dataSet = {Double.NaN, 125, 200, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.ends", image);
    }

    @Test
    public void linearInterpolation() throws Exception {
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
        ImageAssert.compareImages("lineGraph2D.linearInterpolation", image);
    }

    @Test
    public void nearestNeighborInterpolation() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3, Double.NaN, 4, 5, 6), 50, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighborInterpolation", image);
    }
   
    public void linearInterpolationOrderedDataset() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.orderedDataset", image);
    }

    @Test
    public void cubicInterpolationOrderedDataset() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.orderedDataset", image);
    }
    @Test
    public void highlightFocusValue() throws Exception {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(5, 3, 1, 4, 2, 0),
                new ArrayDouble(25, 9, 1, 16, 4, 0));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR)
                .focusPixel(250).highlightFocusValue(true));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        assertThat(renderer.getFocusValueIndex(), equalTo(3));
        ImageAssert.compareImages("lineGraph2D.linear.highlightFocusValue", image);
    }

    @Test
    public void xAxisRange() throws Exception {
        Point2DDataset dataset = largeDataset;
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOUR)
                .xAxisRange(AxisRanges.absolute(10, 20)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraph2D.linear.xAxisRange", image);
    }
    
    
    
    //here begin the tests of multiline
    
    
    
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        LineGraph2DRendererUpdate update = new LineGraph2DRendererUpdate();
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        update.valueColorScheme(ValueColorSchemes.JET);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.multipleCosineColorScheme", image);
    }
    
    @Test
    @Ignore("TODO: draws incorrect image")
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.singleValueSingleLine", image);
    }
    
    @Test
    @Ignore("draws wrong image")
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.singleValueMultipleLines", image);
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.multipleCosine", image);
    }
    
    @Test
    @Ignore("TODO:draws incorrectly")
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.linesEqualsPixels", image);
    }
    
    @Test
    @Ignore("TODO: draws incorrectly")
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.manyLinesStress", image);
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
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640,480);
        LineGraph2DRendererUpdate update = new LineGraph2DRendererUpdate();
        update.interpolation(InterpolationScheme.NEAREST_NEIGHBOUR);
        renderer.update(update);
        renderer.draw(g, data);
        
        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.updateInterpolation", image);
    }
}
