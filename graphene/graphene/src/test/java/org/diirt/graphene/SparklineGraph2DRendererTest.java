/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.diirt.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 *
 * @author asbarber,
 * @author jkfeng,
 * @author sjdallst
 */
public class SparklineGraph2DRendererTest extends BaseGraphTest<SparklineGraph2DRendererUpdate, SparklineGraph2DRenderer> {

    public SparklineGraph2DRendererTest() {
        super("sparklineGraph2D");
    }

    @Override
    public SparklineGraph2DRenderer createRenderer() {
        return new SparklineGraph2DRenderer(300, 200);
    }

    private Point2DDataset sharpPeakData() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = i;
        }
        for (int i = 50; i < 100; i++) {
            dataSet[i] = 100 - i;
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset twoNaNDataset() {

        double[] dataSet = {1, Double.NaN, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset oneNaNCubicDataset() {

        double[] dataSet = {1, 8, 27, Double.NaN, 125, 216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset inverseSquareDataset() {
        double[] initialDataY = new double[200];
        for (int i = 0; i < 200; i++) {
            initialDataY[i] = i / (Math.pow(i, 2));
        }

        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        return data;
    }

    private Point2DDataset maxLastValueDataset() {
        double[] initialDataY = new double[101];

        //Creates the function:
        //f(x) = 50 - x         for  0  <= x <  50
        //f(x) = x - 50         for  50 <= x <= 100
        for (int x = 0; x <= 49; x++) {
            initialDataY[x] = 50 - x;
        }
        for (int x = 50; x <= 100; x++) {
            initialDataY[x] = x - 50;
        }

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);

        return data;
    }

    private Point2DDataset maxMinLastValueDataset() {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(0, 1, 2, 3, 4, 5),
                new ArrayDouble(3, 3, 3, 3, 3, 3));
        return data;
    }

    private Point2DDataset multipleMaxMinDataset() {
        double[] initialDataY = new double[100];

        //Creates the function:
        //f(x) = 1      for 0 <= x < 33
        //f(x) = -1     for 33 <= x < 67
        //f(x) = 0      for 67 <= x < 100
        for (int x = 0; x < 33; x++) {
            initialDataY[x] = 1;
        }
        for (int x = 33; x < 67; x++) {
            initialDataY[x] = -1;
        }
        for (int x = 67; x < 100; x++) {
            initialDataY[x] = 0;
        }

        //Creates a sparkline graph
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        return data;
    }

    private Point2DDataset maxLastOverlapDataset() {
        double[] initialDataY = new double[100];

        for (int x = 0; x < 98; x++) {
            initialDataY[x] = 90;
        }
        initialDataY[98] = 100;
        initialDataY[99] = 99;

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        return data;
    }

    private Point2DDataset ratioOutOfBoundsDataset() {
        double[] initialDataY = new double[200];

        int index = 0;
        for (int m = 1; index < 200; m++) {
            for (int i = 0; i < m * 5; i++) {
                if (index < 200) {
                    initialDataY[index] = m + i;
                    index++;
                }
            }
        }

        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        return data;
    }

    private Point2DDataset heightLimitingFactorDataset() {
        double[] sampleData = new double[10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                sampleData[i] = -1.5 * i;
            } else {
                sampleData[i] = 1.5 * i;
            }
        }
        Point2DDataset data = Point2DDatasets.lineData(sampleData);
        return data;
    }

    private Point2DDataset widthLimitingFactorDataset() {
        double[] sampleData = new double[10];

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                sampleData[i] = -1.5 * i;
            } else {
                sampleData[i] = 1.5 * i;
            }
        }

        Point2DDataset data = Point2DDatasets.lineData(sampleData);
        return data;
    }

    private Point2DDataset powerDataset() {
        double[] initialDataY = new double[200];

        int index = 0;
        for (int m = 1; index < 200; m++) {
            for (int i = 0; i < m * 5; i++) {
                if (index < 200) {
                    initialDataY[index] = Math.pow(m + i, 4);
                    index++;
                }
            }
        }
        Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        return data;
    }

    @Override
    public BufferedImage draw(SparklineGraph2DRenderer renderer) {
        Point2DDataset data = sharpPeakData();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), data);
        return image;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //CIRCLE DRAWING TESTS
    /**
     * Tests case of:
     * <ul>
     * <li>Min Value = Last Value</li>
     * </ul>
     *
     * @throws Exception test fails
     */
    @Test
    public void minEqualsLast() throws Exception {
        Point2DDataset data = sharpPeakData();

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 100);

        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.minEqualsLast", image);
    }

    @Test
    public void maxEqualsLast() throws Exception {

        Point2DDataset data = maxLastValueDataset();

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 100);

        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.maxEqualsLast", image);
    }

    @Test
    public void maxMinEqualsLast() throws Exception {


        //Point2DDataset data = Point2DDatasets.lineData(initialDataY);
        Point2DDataset data = maxMinLastValueDataset();
        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 100);

        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.maxMinEqualsLast", image);

    }

    @Test
    public void multipleMaxAndMin() throws Exception {

        //Creates a sparkline graph
        Point2DDataset data = multipleMaxMinDataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().aspectRatio(5));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.multipleMaxAndMin", image);
    }

    @Test
    public void maxAndLastOverlap() throws Exception {
        Point2DDataset data = maxLastOverlapDataset();

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 25, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 25);

        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.maxAndLastOverlap", image);
    }

    @Test
    public void ratioOutOfBounds() throws Exception {

        Point2DDataset data = ratioOutOfBoundsDataset();

        //Creates a sparkline graph
        BufferedImage image = new BufferedImage(100, 20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();

        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 20);
        renderer.update(renderer.newUpdate().aspectRatio(5));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.ratioOutOfBounds", image);
    }

    @Test
    public void ratioHeightLimitingFactor() throws Exception {

        Point2DDataset data = heightLimitingFactorDataset();

        //Graphics
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();

        //Sparkline
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(200, 200);
        renderer.update(renderer.newUpdate().aspectRatio(5));
        renderer.draw(g, data);

        //Assert true
        ImageAssert.compareImages("sparklineGraph2D.ratioHeightLimitingFactor", image);
    }

    @Test
    public void ratioWidthLimitingFactor() throws Exception {

        Point2DDataset data = widthLimitingFactorDataset();

        //Graphics
        BufferedImage image = new BufferedImage(200, 20, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();

        //Sparkline
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(200, 20);
        renderer.update(renderer.newUpdate().aspectRatio(5));
        renderer.draw(g, data);

        //Assert true
        ImageAssert.compareImages("sparklineGraph2D.ratioWidthLimitingFactor", image);
    }

    @Test
    public void nearestNeighbor() throws Exception {
        Point2DDataset data = powerDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.nearestNeighbor", image);
    }

    @Test
    public void linearInterpolation() throws Exception {
        Point2DDataset data = powerDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.linearInterpolation", image);
    }

    @Test
    public void cubicInterpolation() throws Exception {

        //Creates a sparkline graph
        Point2DDataset data = powerDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.CUBIC));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.cubicInterpolation", image);
    }

    @Test
    public void oneDataPoint() throws Exception {
        //Creates a sparkline graph
        Point2DDataset data = Point2DTestDatasets.oneValueDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.linear.oneDataPoint", image);
    }

    @Test
    public void twoDataPoints() throws Exception {
        //Creates a sparkline graph
        Point2DDataset data = Point2DTestDatasets.twoValueDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.linear.twoDataPoints", image);
    }

    @Test
    public void multiplePoints() throws Exception {

        //Creates a sparkline graph
        Point2DDataset data = inverseSquareDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.linear.multiplePoints", image);

    }

    @Test
    public void negativeValues() throws Exception {

        //Creates a sparkline graph
        Point2DDataset data = Point2DTestDatasets.negativeDataset();
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 50);
        renderer.update(renderer.newUpdate().aspectRatio(5).interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("sparklineGraph2D.linear.negativeValues", image);


    }

    @Test
    public void oneNaNLinear() throws Exception {

        Point2DDataset data = Point2DTestDatasets.oneNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.linear.NaN.1", image);
    }

    @Test
    public void twoNaNLinear() throws Exception {
        Point2DDataset data = twoNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.linear.NaN.2", image);
    }

    @Test
    public void oneNaNNearestNeighbor() throws Exception {
        Point2DDataset data = Point2DTestDatasets.oneNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.nearestNeighbor.NaN.1", image);
    }

    @Test
    public void twoNaNNearestNeighbor() throws Exception {
        Point2DDataset data = twoNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.nearestNeighbor.NaN.2", image);
    }

    @Test
    public void oneNaNCubic() throws Exception {
        Point2DDataset data = oneNaNCubicDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.cubic.NaN.1", image);
    }

    @Test
    public void twoNaNCubic() throws Exception {
        Point2DDataset data = Point2DTestDatasets.twoSpacedNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparklineGraph2D.cubic.NaN.2", image);
    }
}
