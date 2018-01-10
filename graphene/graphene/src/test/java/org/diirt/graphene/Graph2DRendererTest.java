/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.stats.Ranges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class Graph2DRendererTest {

    public Graph2DRendererTest() {
    }

    @Test
    public void graphArea1() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            {
                this.xAreaCoordStart = 4;
                this.xAreaCoordEnd = 296;
                this.yAreaCoordStart = 4;
                this.yAreaCoordEnd = 196;
                this.xReferenceCoords = new ArrayDouble(4.5, 150, 295.5);
                this.yReferenceCoords = new ArrayDouble(195.5, 100, 4.5);
                this.yReferenceLabels = Collections.<String>emptyList();
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.1", image);
    }

    @Test
    public void graphArea2() throws Exception {

        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            {
                this.xAreaCoordStart = 24;
                this.xAreaCoordEnd = 296;
                this.yAreaCoordStart = 4;
                this.yAreaCoordEnd = 196;
                this.xReferenceCoords = new ArrayDouble(24.5, 150, 295.5);
                this.yReferenceCoords = new ArrayDouble(195.5, 147.75, 100, 47.25, 4.5);
                this.yReferenceLabels = Arrays.asList("0", "50", "100", "150", "200");
                this.yLabelMargin = 2;
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.2", image);
    }

    @Test
    public void graphArea3() throws Exception {

        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            {
                this.xAreaCoordStart = 24;
                this.xAreaCoordEnd = 296;
                this.yAreaCoordStart = 4;
                this.yAreaCoordEnd = 196;
                this.xReferenceCoords = new ArrayDouble(50, 100, 150, 200, 250);
                this.yReferenceCoords = new ArrayDouble(160, 120, 80, 40);
                this.yReferenceLabels = Arrays.asList("0", "50", "100", "150");
                this.yLabelMargin = 1;
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.3", image);
    }

    @Test
    public void graphArea4() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            {
                this.xAreaCoordStart = 4;
                this.xAreaCoordEnd = 296;
                this.yAreaCoordStart = 4;
                this.yAreaCoordEnd = 186;
                this.xReferenceCoords = new ArrayDouble(50, 100, 150, 200, 250);
                this.xReferenceLabels = Arrays.asList("0", "50", "100", "150", "200");
                this.xLabelMargin = 1;
                this.yReferenceCoords = new ArrayDouble(160, 120, 80, 40);
                this.yLabelMargin = 1;
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.4", image);
    }

    @Test
    public void graphArea5() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(Ranges.range(0, 10), Ranges.range(0, 10), Ranges.range(0, 10), Ranges.range(0, 10));
        renderer.calculateLabels();
        renderer.calculateGraphArea();
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.5", image);
    }

    @Test
    public void graphArea6() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(Ranges.range(0, 10), Ranges.range(0, 10), Ranges.range(3, 3), Ranges.range(3, 3));
        renderer.calculateLabels();
        renderer.calculateGraphArea();
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.6", image);
    }

    @Test
    public void graphArea7() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(Ranges.range(3, 3), Ranges.range(3, 3), Ranges.range(0, 10), Ranges.range(0, 10));
        renderer.calculateLabels();
        renderer.calculateGraphArea();
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.7", image);
    }

    @Test
    public void graphArea8() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.xPointMargin = 2.5;
        renderer.yPointMargin = 2.5;
        renderer.calculateRanges(Ranges.range(0, 10), Ranges.range(0, 10), Ranges.range(0, 10), Ranges.range(0, 10));
        renderer.calculateLabels();
        renderer.calculateGraphArea();
        renderer.drawBackground();
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.8", image);
    }

    @Test
    public void inheritance1() throws Exception {
        AreaGraph2DRenderer renderer = new AreaGraph2DRenderer(300, 200);
        changeSize(renderer);
        assertThat(renderer.getImageWidth(), equalTo(200));
        assertThat(renderer.getImageHeight(), equalTo(100));
    }

    @Test
    public void inheritance2() throws Exception {
        ScatterGraph2DRenderer renderer = new ScatterGraph2DRenderer(300, 200);
        changeSize(renderer);
        assertThat(renderer.getImageWidth(), equalTo(200));
        assertThat(renderer.getImageHeight(), equalTo(100));
    }

    @Test
    public void inheritance3() throws Exception {
        Graph2DRenderer<?> renderer = new ScatterGraph2DRenderer(300, 200);
        changeSize(renderer);
        assertThat(renderer.getImageWidth(), equalTo(200));
        assertThat(renderer.getImageHeight(), equalTo(100));
    }

    public static <T extends Graph2DRendererUpdate<T>> void changeSize(Graph2DRenderer<T> renderer) {
        renderer.update(renderer.newUpdate().imageHeight(100).imageWidth(200));
    }

    @Test
    public void processValue1() throws Exception {
        final Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1,2,3,4,5), new ArrayDouble(1,3,5,7,9));
        Graph2DRenderer<?> renderer = new Graph2DRenderer<Graph2DRendererUpdate>(300, 200) {
            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }

            @Override
            protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
                assertThat(data.getXValues().getDouble(index), equalTo(valueX));
                assertThat(data.getYValues().getDouble(index), equalTo(valueY));
            }

        };
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(data.getXStatistics().getRange(), data.getXDisplayRange(), data.getYStatistics().getRange(), data.getYDisplayRange());
        renderer.calculateLabels();
        renderer.drawValueExplicitLine(data.getXValues(), data.getYValues(), InterpolationScheme.LINEAR, ReductionScheme.NONE);
    }

    @Test
    public void processValue2() throws Exception {
        final Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1,2,3,4,5), new ArrayDouble(1,3,5,7,9));
        Graph2DRenderer<?> renderer = new Graph2DRenderer<Graph2DRendererUpdate>(300, 200) {
            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }

            @Override
            protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
                assertThat(data.getXValues().getDouble(index), equalTo(valueX));
                assertThat(data.getYValues().getDouble(index), equalTo(valueY));
            }

        };
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(data.getXStatistics().getRange(), data.getXDisplayRange(), data.getYStatistics().getRange(), data.getYDisplayRange());
        renderer.calculateLabels();
        renderer.drawValueExplicitLine(data.getXValues(), data.getYValues(), InterpolationScheme.LINEAR, ReductionScheme.FIRST_MAX_MIN_LAST);
    }

    @Test
    public void processValue3() throws Exception {
        final Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1,2,3,4,5), new ArrayDouble(1,3,5,7,9));
        Graph2DRenderer<?> renderer = new Graph2DRenderer<Graph2DRendererUpdate>(300, 200) {
            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }

            @Override
            protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
                assertThat(data.getXValues().getDouble(index), equalTo(valueX));
                assertThat(data.getYValues().getDouble(index), equalTo(valueY));
            }

        };
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(Ranges.range(2.5, 4.5), null, data.getYStatistics().getRange(), data.getYDisplayRange());
        renderer.calculateLabels();
        renderer.drawValueExplicitLine(data.getXValues(), data.getYValues(), InterpolationScheme.LINEAR, ReductionScheme.NONE);
    }

    @Test
    public void processValue4() throws Exception {
        final Point2DDataset data = Point2DDatasets.lineData(new ArrayDouble(1,2,3,4,5), new ArrayDouble(1,3,5,7,9));
        Graph2DRenderer<?> renderer = new Graph2DRenderer<Graph2DRendererUpdate>(300, 200) {
            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }

            @Override
            protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
                assertThat(data.getXValues().getDouble(index), equalTo(valueX));
                assertThat(data.getYValues().getDouble(index), equalTo(valueY));
            }

        };
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.calculateRanges(Ranges.range(2.5, 4.5), null, data.getYStatistics().getRange(), data.getYDisplayRange());
        renderer.calculateLabels();
        renderer.drawValueExplicitLine(data.getXValues(), data.getYValues(), InterpolationScheme.LINEAR, ReductionScheme.FIRST_MAX_MIN_LAST);
    }

}
