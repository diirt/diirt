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
    public void startNanEndValCubic() throws Exception{
    double[] dataSet = {Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphstartNaNEndValCubic", image);
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
    public void startValEndNaNCubic() throws Exception{
    double[] dataSet = {5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphstartValEndNaNCubic", image);
    }
    
    @Test
    public void startThreeNaNEndValCubic() throws Exception{
    double[] dataSet = {Double.NaN, Double.NaN, Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphstartThreeNaNEndValCubic", image);
    }
    
    @Test
    public void startValEndThreeNaNCubic() throws Exception{
    double[] dataSet = {5, Double.NaN, Double.NaN, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphstartValEndThreeNaNCubic", image);
    }
    
    @Test
    public void testNaNMiddleCubic() throws Exception{
    double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sqrt(i);
        }
        dataSet[50] = Double.NaN;
        for(int i = 51; i < 100; i++){
                dataSet[i] = Math.sqrt(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNaNMiddleCubic", image);
    }
    
    @Test
    public void testNaNMiddleCurveCubic() throws Exception{
    double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sin(i);
        }
        dataSet[50] = Double.NaN;
        for(int i = 51; i < 100; i++){
                dataSet[i] = Math.sin(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNaNMiddleCurveCubic", image);
    }
    
    @Test
    public void testNaNFirstLastMiddleCubic() throws Exception{
    double[] dataSet = new double[20];
    dataSet[0] = Double.NaN;
        for (int i = 1; i < 10; i++) {
            dataSet[i] = -2 * i;
        }
    dataSet[10] = Double.NaN;
        for(int i = 11; i < 19; i++){
                dataSet[i] = 2 * i;
        }
    dataSet[19] = Double.NaN;
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraphNaNFirstLastMiddleCubic", image);
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
}
