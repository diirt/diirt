/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.epics.util.array.*;
import org.junit.Ignore;

/**
 *
 * @authors asbarber, jkfeng, sjdallst
 */
@Ignore
public class IntensityGraph2DRendererTest extends BaseGraphTest<IntensityGraph2DRendererUpdate, IntensityGraph2DRenderer> {

    public IntensityGraph2DRendererTest() {
        super("intensityGraph2D");
    }

    @Override
    public IntensityGraph2DRenderer createRenderer() {
        return new IntensityGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(IntensityGraph2DRenderer renderer) {
        double listOfData[] = new double[10 * 10];
        for (int i = 0; i < (10 * 10); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 10), 10);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        return graphBuffer.getImage();

    }
    private static Cell2DDataset largeDataset;

    private static Cell2DDataset randomDataset() {
        Random rand = new Random(1);
        int nSamples = 1000 * 1000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        ArrayDouble data = new ArrayDouble(waveform);
        Cell2DDataset datum = Cell2DDatasets.linearRange(data, RangeUtil.range(0, 1000), 1000, RangeUtil.range(0, 1000), 1000);
        return datum;
    }

    private Cell2DDataset randomXYDataset() {
        double listOfData[] = new double[640 * 480];
        Random rand = new Random(0);
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = rand.nextDouble();
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset rectangleDataset() {
        double listOfData[] = new double[10 * 10];
        for (int i = 0; i < (10 * 10); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset rectangleSolidDataset() {
        double listOfData[] = new double[640 * 10];
        for (int i = 0; i < (640 * 10); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset smallXDataset() {
        double listOfData[] = new double[640 * 10];
        for (int i = 0; i < (640 * 10); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset smallXSolidDataset() {
        double listOfData[] = new double[10 * 480];
        for (int i = 0; i < (10 * 480); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallYDataset() {
        double listOfData[] = new double[10 * 480];
        for (int i = 0; i < (10 * 480); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
        return data;

    }

    private Cell2DDataset smallYSolidDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallXAndYDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset singlePixelDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            if (i % 1240 == 0) {
                listOfData[i] = 0;
            } else {
                listOfData[i] = 1;
            }
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallXSingleYValueDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = 10000000;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset customBoundaryDataset() {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        return data;
    }

    private Cell2DDataset boundaryFunctionDataset() {
        int size = 100;
        double[] boundaries = new double[size];
        boundaries[0] = 0;
        for (int i = 1; i < boundaries.length; i++) {
            boundaries[i] = 90.0 + i * 10.0 / size;
        }
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(boundaries), new ArrayDouble(boundaries));
        return data;
    }
    
    private Cell2DDataset ellipticParaboloid(int xPoints, Range xRange, int yPoints, Range yRange) {
        return Cell2DDatasets.linearRange(new Cell2DDatasets.Function2D() {

            @Override
            public double getValue(double x, double y) {
                return x*x + y*y;
            }
        }, xRange, xPoints, yRange, yPoints);
    }

    /**
     * Sets up the large dataset used in the tests
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        largeDataset = randomDataset();
    }

    /**
     * Empties the memory used in the large dataset
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }

    /**
     * Tests case of:
     * <ul>
     * <li>Min Value = Last Value</li>
     * <li>There exists more than one min value</li>
     * </ul>
     *
     * @throws Exception Test fails
     */
    @Test
    public void rectangles() throws Exception {
        Cell2DDataset data = rectangleDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.rectangles", graphBuffer.getImage());

    }

    @Test
    public void rectanglesSolid() throws Exception {
        Cell2DDataset data = rectangleSolidDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.rectanglesSolid", graphBuffer.getImage());

    }

    @Test
    public void smallX() throws Exception {
        Cell2DDataset data = smallXDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallX", graphBuffer.getImage());

    }

    @Test
    public void smallXSolid() throws Exception {
        Cell2DDataset data = smallXSolidDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXSolid", graphBuffer.getImage());

    }

    @Test
    public void smallY() throws Exception {
        Cell2DDataset data = smallYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallY", graphBuffer.getImage());

    }

    @Test
    public void smallYSolid() throws Exception {
        Cell2DDataset data = smallYSolidDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallYSolid", graphBuffer.getImage());

    }

    @Test
    public void smallXAndY() throws Exception {
        Cell2DDataset data = smallXAndYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndY", graphBuffer.getImage());

    }

    @Test
    public void smallXAndYWithLegend() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallXAndYWithLegend", graphBuffer.getImage());

    }

    @Test
    public void smallXandYSinglePixels() throws Exception {
        Cell2DDataset data = singlePixelDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYSinglePixels", graphBuffer.getImage());

    }

    //Tests ColorScheme with JET colors.
    @Test
    public void smallXAndYJet() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYJet", graphBuffer.getImage());

    }

    //Single-value test.
    @Test
    public void smallXAndYSingleValue() throws Exception {
        Cell2DDataset data = smallXSingleYValueDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYSingleValue", graphBuffer.getImage());

    }

    @Test
    public void customBoundaries() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.optimizeColorScheme = true;
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.drawArray(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.customBoundaries", graphBuffer.getImage());
    }

    @Test
    public void test13() throws Exception {
        Cell2DDataset data = boundaryFunctionDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(100, 100);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.13", graphBuffer.getImage());
    }

    @Test
    public void ZoomInTest() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).xAxisRange(AxisRanges.absolute(20, 80)).yAxisRange(AxisRanges.absolute(20, 80)));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.ZoomIn", graphBuffer.getImage());
    }

    @Test
    @Ignore("FIXME: zoom in with linear boundaries does not seem to work")
    public void linearBoundariesZoomIn() throws Exception {
        Cell2DDataset data = ellipticParaboloid(200, RangeUtil.range(0, 100), 200, RangeUtil.range(0, 100));
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.linearBoundaries.zoomIn.1", graphBuffer.getImage());
        
        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.absolute(20, 80)).yAxisRange(AxisRanges.absolute(20, 80)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.linearBoundaries.zoomIn.2", graphBuffer.getImage());
    }

    @Test
    public void ZoomOutTest() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).xAxisRange(AxisRanges.absolute(-20, 120)).yAxisRange(AxisRanges.absolute(-20, 120)));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.ZoomOut", graphBuffer.getImage());
    }

    @Test
    public void UpdateRightMarginWLegendTest() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.colorMap(NumberColorMaps.JET);
        update.rightMargin(20);
        renderer.update(update);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).rightMargin(20));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.UpdateRightMarginWLegend", graphBuffer.getImage());

    }

    private Cell2DDataset createRandomDataset(int width, int height) {
        double listOfData[] = new double[width * height];
        Random rand = new Random(0);
        for (int i = 0; i < (width * height); i++) {
            listOfData[i] = rand.nextDouble();
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        return Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, width), width, RangeUtil.range(0, height), height);
    }

    @Test
    public void addRemoveLegend() throws Exception {
        Cell2DDataset data = createRandomDataset(640, 480);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.1", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().drawLegend(true));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.2", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().drawLegend(false));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.1", graphBuffer.getImage());
    }

    @Test
    public void UpdateRightMarginTest() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.JET).rightMargin(20));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.UpdateRightMargin", graphBuffer.getImage());

    }
}
