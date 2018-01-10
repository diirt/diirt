/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.diirt.util.array.ArrayDouble;
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

    private Point2DDataset endNaNDataset() {
        double[] dataSet = {Double.NaN, 2, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset oneNaNCubicDataset(){
        double[] dataSet = {1,8,27,Double.NaN, 125,216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }
    private Point2DDataset startNaNEndValueDataset() {
        double[] dataSet = {Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }
    private Point2DDataset startValueEndNaNDataset() {
        double[] dataSet = {5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }
    private Point2DDataset startValEndThreeNaNDataset() {
        double[] dataSet = {5, Double.NaN, Double.NaN, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset startThreeNaNEndValDataset() {
        double[] dataSet = {Double.NaN, Double.NaN, Double.NaN, 5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset peakMiddleNaNDataset() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sqrt(i);
        }
        dataSet[50] = Double.NaN;
        for (int i = 51; i < 100; i++) {
            dataSet[i] = Math.sqrt(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset middleCurveNaNDataset() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = Math.sin(i);
        }
        dataSet[50] = Double.NaN;
        for (int i = 51; i < 100; i++) {
            dataSet[i] = Math.sin(i);
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset firstMiddleLastNaNDataset() {
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
        return data;
    }

    private Point2DDataset threeNaNDataset() {
        double[] dataSet = {Double.NaN, 5, Double.NaN, 2, 3, 5, Double.NaN};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset squaredDataset() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 100; i++) {
            dataSet[i] = i * i;
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    private Point2DDataset scatteredPerfectSquaresDataset() {
        Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(5, 3, 1, 4, 2, 0),
                new ArrayDouble(25, 9, 1, 16, 4, 0));
        return data;
    }

    private List<Point2DDataset> cosineDataset() {
        double[][] initialData = new double[10][100];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = (double) i * Math.cos((double) j / 100 * 6 * Math.PI);
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 10; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }

    private List<Point2DDataset> linearDataset() {
        double[][] initialData = new double[1][100];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 1; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear10Dataset() {
        double[][] initialData = new double[10][100];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 10; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> pixelEqualLineDataset() {
        double[][] initialData = new double[81][100];
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 81; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }

    private List<Point2DDataset> largeDataset() {
        double[][] initialData = new double[5000][100];
        for (int i = 0; i < 5000; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 5000; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }

    @Override
    public BufferedImage draw(LineGraph2DRenderer renderer) {
        Point2DDataset data = Point2DTestDatasets.sharpPeakData();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), data);
        return image;
    }
    private static Point2DDataset largeDataset;

    @BeforeClass
    public static void setUpClass() throws Exception {
        double[] waveform = Point2DTestDatasets.randomDataset();
        largeDataset = org.diirt.graphene.Point2DDatasets.lineData(waveform);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }

    @Test
    public void oneDataPointLinear() throws Exception {
        Point2DDataset data = Point2DTestDatasets.oneValueDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.oneDataPoint", image);
    }

    @Test
    public void twoDataPointsLinear() throws Exception {
        Point2DDataset data = Point2DTestDatasets.twoValueDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.twoDataPoints", image);
    }

    @Test
    public void multipleDataPointsLinear() throws Exception {
        Point2DDataset data = Point2DTestDatasets.sharpPeakData();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.multiplePoints", image);
    }

    @Test
    public void negativePoints() throws Exception {
        Point2DDataset data = Point2DTestDatasets.negativeDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.negativePoints", image);
    }

    @Test
    public void endsNaNLinear() throws Exception {;
        Point2DDataset data = endNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.ends", image);
    }

    @Test
    public void consecutiveNaNLinear() throws Exception {
        Point2DDataset data = Point2DTestDatasets.consecNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.consecutive", image);
    }

    @Test
    public void twoNaNLinear() throws Exception {
        Point2DDataset data = Point2DTestDatasets.twoSpacedNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.NaN.2", image);
    }

    @Test
    public void oneNaNNearestNeighbor() throws Exception {
        Point2DDataset data = Point2DTestDatasets.oneNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighbor.NaN.1", image);
    }

    @Test
    public void twoNaNNearestNeighbor() throws Exception {
        Point2DDataset data = Point2DTestDatasets.consecNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighbor.NaN.2", image);
    }

    @Test
    public void oneValueCubic() throws Exception {
        Point2DDataset data = Point2DTestDatasets.oneValueDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.oneValue", image);
    }

    @Test
    public void startNanEndValueCubic() throws Exception {
        Point2DDataset data = startNaNEndValueDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startNaNEndValue", image);
    }

    @Test
    public void twoValuesCubic() throws Exception {
        Point2DDataset data = Point2DTestDatasets.twoValueDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.twoValues", image);
    }

    @Test
    public void startValueEndNaNCubic() throws Exception {
        Point2DDataset data = startValueEndNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startValueEndNaN", image);
    }

    @Test
    public void startThreeNaNEndValueCubic() throws Exception {
        Point2DDataset data = startThreeNaNEndValDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startThreeNaNEndValue", image);
    }

    @Test
    public void startValueEndThreeNaNCubic() throws Exception {
        Point2DDataset data = startValEndThreeNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.startValueEndThreeNaN", image);
    }

    @Test
    public void middleNaNCubic() throws Exception {
        Point2DDataset data = peakMiddleNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.middle", image);
    }

    @Test
    public void middleOfCurveNaNCubic() throws Exception {
        Point2DDataset data = middleCurveNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.middleOfCurve", image);
    }

    @Test
    public void firstMiddleLastNaNCubic() throws Exception {
        Point2DDataset data = firstMiddleLastNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.firstMiddleLast", image);
    }

    @Test
    public void oneNaNCubic() throws Exception {
        Point2DDataset data = oneNaNCubicDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.1", image);
    }

    @Test
    public void twoNaNCubic() throws Exception {
        Point2DDataset data = Point2DTestDatasets.twoSpacedNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.2", image);
    }

    @Test
    public void consecutiveNaNCubic() throws Exception {
        Point2DDataset data = Point2DTestDatasets.consecNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.consecutive", image);
    }

    @Test
    public void threeNaNCubic() throws Exception {
        Point2DDataset data = threeNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.3", image);
    }

    @Test
    public void regularDataCubic() throws Exception {
        Point2DDataset data = squaredDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.regularData", image);
    }

    @Test
    public void endsNaNCubic() throws Exception {
        Point2DDataset data = endNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.ends", image);
    }

    @Test
    public void linearInterpolation() throws Exception {
        Point2DDataset data = squaredDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linearInterpolation", image);
    }

    @Test
    public void nearestNeighborInterpolation() throws Exception {
        Point2DDataset data = Point2DTestDatasets.oneNaNDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.nearestNeighborInterpolation", image);
    }

    public void linearInterpolationOrderedDataset() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.linear.orderedDataset", image);
    }

    @Test
    public void cubicInterpolationOrderedDataset() throws Exception {
        Point2DDataset data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineGraph2D.cubic.orderedDataset", image);
    }

    @Test
    public void highlightFocusValue() throws Exception {
        Point2DDataset data = scatteredPerfectSquaresDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR)
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
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR)
                .xAxisRange(AxisRanges.fixed(10, 20)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        ImageAssert.compareImages("lineGraph2D.linear.xAxisRange", image);
    }

    //here begin the tests of multiline
    @Test
    public void multipleCosineColorScheme() throws Exception {
        List<Point2DDataset> data = cosineDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().yAxisRange(AxisRanges.data())
                .interpolation(InterpolationScheme.LINEAR).valueColorScheme(NumberColorMaps.JET));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.multipleCosineColorScheme", image);
    }

    @Test
    public void singleValueSingleLine() throws Exception {
        List<Point2DDataset> data = linearDataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.singleValueSingleLine", image);
    }

    @Test
    public void singleValueMultipleLines() throws Exception {
        List<Point2DDataset> data = linear10Dataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR)
                .xAxisRange(AxisRanges.auto(0.0))
                .yAxisRange(AxisRanges.auto(0.0)));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.singleValueMultipleLines", image);
    }

    @Test
    public void multipleCosine() throws Exception {
        List<Point2DDataset> data = cosineDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().yAxisRange(AxisRanges.data())
                .interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.multipleCosine", image);
    }

    @Test
    public void linesEqualPixels() throws Exception {
        List<Point2DDataset> data = pixelEqualLineDataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR)
                .xAxisRange(AxisRanges.auto(0.0))
                .yAxisRange(AxisRanges.auto(0.0)));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.linesEqualsPixels", image);
    }

    @Test
    public void manyLinesStress() throws Exception {
        List<Point2DDataset> data = largeDataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR)
                .xAxisRange(AxisRanges.auto(0.0))
                .yAxisRange(AxisRanges.auto(0.0)));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.manyLinesStress", image);
    }

    @Test
    @Ignore
    public void drawGraphBuffer()throws Exception{

        Point2DDataset data = Point2DTestDatasets.twoValueDataset();
        GraphBuffer buffer = new GraphBuffer(300, 200);
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(300, 200);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.CUBIC));
        renderer.draw(buffer, data);
        ImageAssert.compareImages("lineGraph2D.cubic.twoValues", buffer.getImage());
    }

    @Test
    public void updateInterpolation() throws Exception {
        List<Point2DDataset> data = cosineDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().yAxisRange(AxisRanges.data())
                .interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("lineGraph2D.updateInterpolation", image);
    }
}
