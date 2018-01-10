/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.Test;
import org.diirt.util.array.*;
import org.diirt.util.stats.Ranges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author asbarber, jkfeng, sjdallst
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
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 10), 10, Ranges.range(0, 10), 10);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        return graphBuffer.getImage();

    }

    private static Cell2DDataset randomDataset() {
        Random rand = new Random(1);
        int nSamples = 1000 * 1000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        ArrayDouble data = new ArrayDouble(waveform);
        Cell2DDataset datum = Cell2DDatasets.linearRange(data, Ranges.range(0, 1000), 1000, Ranges.range(0, 1000), 1000);
        return datum;
    }

    private Cell2DDataset randomXYDataset() {
        double listOfData[] = new double[640 * 480];
        Random rand = new Random(0);
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = rand.nextDouble();
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 480), 480);
        return data;
    }

    public static Cell2DDataset rectangleDataset() {
        double listOfData[] = new double[10 * 10];
        for (int i = 0; i < (10 * 10); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 10), 10, Ranges.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset rectangleSolidDataset() {
        double listOfData[] = new double[640 * 10];
        for (int i = 0; i < (640 * 10); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset smallXDataset() {
        double listOfData[] = new double[640 * 10];
        for (int i = 0; i < (640 * 10); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 10), 10);
        return data;
    }

    private Cell2DDataset smallXSolidDataset() {
        double listOfData[] = new double[10 * 480];
        for (int i = 0; i < (10 * 480); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 10), 10, Ranges.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallYDataset() {
        double listOfData[] = new double[10 * 480];
        for (int i = 0; i < (10 * 480); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 10), 10, Ranges.range(0, 480), 480);
        return data;

    }

    private Cell2DDataset smallYSolidDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = 1;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallXAndYDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = i;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 480), 480);
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
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset smallXSingleYValueDataset() {
        double listOfData[] = new double[640 * 480];
        for (int i = 0; i < (640 * 480); i++) {
            listOfData[i] = 10000000;
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        Cell2DDataset data = Cell2DDatasets.linearRange(dataList, Ranges.range(0, 640), 640, Ranges.range(0, 480), 480);
        return data;
    }

    private Cell2DDataset nonlinearBoundaries() {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        return data;
    }

    private Cell2DDataset largeAndSmallCellsDataset() {
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

    @Test
    public void smallXAndYJet() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYJet", graphBuffer.getImage());

    }

    @Test
    public void smallXAndYSingleValue() throws Exception {
        Cell2DDataset data = smallXSingleYValueDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.smallXAndYSingleValue", graphBuffer.getImage());

    }

    @Test
    public void largeAndSmallCells() throws Exception {
        Cell2DDataset data = largeAndSmallCellsDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(100, 100);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.largeAndSmallCells", graphBuffer.getImage());
    }

    @Test
    public void smallCellsZoomInAndOut() throws Exception {
        Cell2DDataset data = ellipticParaboloid(200, Ranges.range(0, 100), 200, Ranges.range(0, 100));
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.1", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.fixed(20, 80))
                .yAxisRange(AxisRanges.fixed(20, 80)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.2", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.fixed(0, 100))
                .yAxisRange(AxisRanges.fixed(0, 100)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.1", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.fixed(-20, 120))
                .yAxisRange(AxisRanges.fixed(-20, 120)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.3", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.fixed(-20, 120))
                .yAxisRange(AxisRanges.fixed(40, 60)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.4", graphBuffer.getImage());

        renderer.update(renderer.newUpdate().xAxisRange(AxisRanges.fixed(20, 40))
                .yAxisRange(AxisRanges.fixed(-40, 120)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.smallCellsZoomInAndOut.5", graphBuffer.getImage());
    }

    @Test
    public void nonlinearBoundariesZoomInAndZoomOut() throws Exception {
        Cell2DDataset data = nonlinearBoundaries();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.nonlinearBoundariesZoomInAndZoomOut.1", graphBuffer.getImage());

        renderer.update(renderer.newUpdate()
                .xAxisRange(AxisRanges.fixed(20, 80))
                .yAxisRange(AxisRanges.fixed(20, 80)));
        renderer.draw(graphBuffer, data);
        ImageAssert.compareImages("intensityGraph2D.nonlinearBoundariesZoomInAndZoomOut.2", graphBuffer.getImage());

        renderer.update(renderer.newUpdate()
                .xAxisRange(AxisRanges.fixed(-20, 120))
                .yAxisRange(AxisRanges.fixed(-20, 120)));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.nonlinearBoundariesZoomInAndZoomOut.3", graphBuffer.getImage());
    }

    @Test
    public void rightMarginWithLegend() throws Exception {
        Cell2DDataset data = randomXYDataset();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.update(renderer.newUpdate().drawLegend(true).rightMargin(20));
        renderer.draw(graphBuffer, data);

        ImageAssert.compareImages("intensityGraph2D.rightMarginWithLegend", graphBuffer.getImage());

    }

    private Cell2DDataset createRandomDataset(int width, int height) {
        double listOfData[] = new double[width * height];
        Random rand = new Random(0);
        for (int i = 0; i < (width * height); i++) {
            listOfData[i] = rand.nextDouble();
        }
        ArrayDouble dataList = new ArrayDouble(listOfData);
        return Cell2DDatasets.linearRange(dataList, Ranges.range(0, width), width, Ranges.range(0, height), height);
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
    public void selectedRegion() throws Exception {
        Cell2DDataset data = ellipticParaboloid(200, Ranges.range(0, 100), 200, Ranges.range(0, 100));
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().pixelSelectionRange(100, 200, 150, 250));
        GraphBuffer graphBuffer = new GraphBuffer(renderer);
        renderer.draw(graphBuffer, data);
        assertThat(renderer.getXPixelSelectionRange().getMinimum(), equalTo(100.0));
        assertThat(renderer.getXPixelSelectionRange().getMaximum(), equalTo(200.0));
        assertThat(renderer.getYPixelSelectionRange().getMinimum(), equalTo(150.0));
        assertThat(renderer.getYPixelSelectionRange().getMaximum(), equalTo(250.0));
        assertThat(renderer.getXValueSelectionRange().getMinimum(), closeTo(12.5203, 0.0001));
        assertThat(renderer.getXValueSelectionRange().getMaximum(), closeTo(28.9430, 0.0001));
        assertThat(renderer.getYValueSelectionRange().getMinimum(), closeTo(45.9869, 0.0001));
        assertThat(renderer.getYValueSelectionRange().getMaximum(), closeTo(67.8958, 0.0001));
        assertThat(renderer.getXIndexSelectionRange().getMinimum(), equalTo(25.0));
        assertThat(renderer.getXIndexSelectionRange().getMaximum(), equalTo(57.0));
        assertThat(renderer.getYIndexSelectionRange().getMinimum(), equalTo(92.0));
        assertThat(renderer.getYIndexSelectionRange().getMaximum(), equalTo(135.0));
        ImageAssert.compareImages("intensityGraph2D.selectedRegion.1", graphBuffer.getImage());
    }

}
