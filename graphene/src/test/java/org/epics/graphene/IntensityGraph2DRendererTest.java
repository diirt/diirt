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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);
        return image;

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
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.rectangles", image);

    }

    @Test
    public void rectanglesSolid() throws Exception {
        Cell2DDataset data = rectangleSolidDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.rectanglesSolid", image);

    }

    @Test
    public void smallX() throws Exception {
        Cell2DDataset data = smallXDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallX", image);

    }

    @Test
    public void smallXSolid() throws Exception {
        Cell2DDataset data = smallXSolidDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXSolid", image);

    }

    @Test
    public void smallY() throws Exception {
        Cell2DDataset data = smallYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallY", image);

    }

    @Test
    public void smallYSolid() throws Exception {
        Cell2DDataset data = smallYSolidDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallYSolid", image);

    }

    @Test
    public void smallXAndY() throws Exception {
        Cell2DDataset data = smallXAndYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndY", image);

    }

    @Test
    public void smallXAndYWithLegend() throws Exception {
        Cell2DDataset data = randomXYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallXAndYWithLegend", image);

    }

    @Test
    public void smallXandYSinglePixels() throws Exception {
        Cell2DDataset data = singlePixelDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.GRAY));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYSinglePixels", image);

    }

    //Tests ColorScheme with JET colors.
    @Test
    public void smallXAndYJet() throws Exception {
        Cell2DDataset data = randomXYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYJet", image);

    }

    //Single-value test.
    @Test
    public void smallXAndYSingleValue() throws Exception {
        Cell2DDataset data = smallXSingleYValueDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYSingleValue", image);

    }

    @Test
    public void customBoundaries() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.optimizeColorScheme = true;
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.drawArray(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.customBoundaries", image);
    }

    @Test
    @Ignore("This dataset does not draw correctly")
    public void test13() throws Exception {
        Cell2DDataset data = boundaryFunctionDataset();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(100, 100);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET));
        renderer.draw(graphBuffer, data);
        renderer.drawArray(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.13", image);
    }

    @Test
    public void ZoomInTest() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).xAxisRange(AxisRanges.absolute(20, 80)).yAxisRange(AxisRanges.absolute(20, 80)));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.ZoomIn", image);
    }

    @Test
    public void ZoomOutTest() throws Exception {
        Cell2DDataset data = customBoundaryDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).xAxisRange(AxisRanges.absolute(-20, 120)).yAxisRange(AxisRanges.absolute(-20, 120)));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.ZoomOut", image);
    }

    @Test
    public void UpdateRightMarginWLegendTest() throws Exception {
        Cell2DDataset data = randomXYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.colorMap(NumberColorMaps.JET);
        update.rightMargin(20);
        renderer.update(update);
        renderer.update(renderer.newUpdate().drawLegend(true).colorMap(NumberColorMaps.JET).rightMargin(20));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.UpdateRightMarginWLegend", image);

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
    @Ignore("TODO: adding and removing the legend leaves a space")
    public void addRemoveLegend() throws Exception {
        Cell2DDataset data = createRandomDataset(640, 480);
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.1", image);

        renderer.update(renderer.newUpdate().drawLegend(true));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.2", image);

        renderer.update(renderer.newUpdate().drawLegend(false));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.addRemoveLegend.1", image);
    }

    @Test
    public void UpdateRightMarginTest() throws Exception {
        Cell2DDataset data = randomXYDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.JET).rightMargin(20));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.UpdateRightMargin", image);

    }
}
