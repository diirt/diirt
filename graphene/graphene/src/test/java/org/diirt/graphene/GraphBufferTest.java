/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.graphene.ValueScales;
import org.diirt.graphene.FontUtil;
import org.diirt.graphene.GraphBuffer;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.stats.Ranges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class GraphBufferTest {
    
    @Test
    public void xScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setXScaleAsCell(Ranges.range(0, 100), 0, 3, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(0));
        assertThat(graph.xValueToPixel(24.9999), equalTo(0));
        assertThat(graph.xValueToPixel(25), equalTo(1));
        assertThat(graph.xValueToPixel(49.9999), equalTo(1));
        assertThat(graph.xValueToPixel(50), equalTo(2));
        assertThat(graph.xValueToPixel(74.9999), equalTo(2));
        assertThat(graph.xValueToPixel(75), equalTo(3));
        assertThat(graph.xValueToPixel(99.9999), equalTo(3));
        assertThat(graph.xValueToPixel(100), equalTo(4));
        assertThat(graph.xPixelLeftToValue(0), equalTo(0.0));
        assertThat(graph.xPixelCenterToValue(0), equalTo(12.5));
        assertThat(graph.xPixelRightToValue(0), equalTo(25.0));
        assertThat(graph.xPixelLeftToValue(1), equalTo(25.0));
        assertThat(graph.xPixelCenterToValue(1), equalTo(37.5));
        assertThat(graph.xPixelRightToValue(1), equalTo(50.0));
        assertThat(graph.xPixelLeftToValue(2), equalTo(50.0));
        assertThat(graph.xPixelCenterToValue(2), equalTo(62.5));
        assertThat(graph.xPixelRightToValue(2), equalTo(75.0));
        assertThat(graph.xPixelLeftToValue(3), equalTo(75.0));
        assertThat(graph.xPixelCenterToValue(3), equalTo(87.5));
        assertThat(graph.xPixelRightToValue(3), equalTo(100.0));
    }
    
    @Test
    public void xScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setXScaleAsPoint(Ranges.range(0, 30), 0, 3, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(0));
        assertThat(graph.xValueToPixel(4.9999), equalTo(0));
        assertThat(graph.xValueToPixel(5), equalTo(1));
        assertThat(graph.xValueToPixel(10), equalTo(1));
        assertThat(graph.xValueToPixel(14.9999), equalTo(1));
        assertThat(graph.xValueToPixel(15), equalTo(2));
        assertThat(graph.xValueToPixel(20), equalTo(2));
        assertThat(graph.xValueToPixel(24.9999), equalTo(2));
        assertThat(graph.xValueToPixel(25), equalTo(3));
        assertThat(graph.xValueToPixel(30), equalTo(3));
        assertThat(graph.xPixelLeftToValue(0), equalTo(-5.0));
        assertThat(graph.xPixelCenterToValue(0), equalTo(0.0));
        assertThat(graph.xPixelRightToValue(0), equalTo(5.0));
        assertThat(graph.xPixelLeftToValue(1), equalTo(5.0));
        assertThat(graph.xPixelCenterToValue(1), equalTo(10.0));
        assertThat(graph.xPixelRightToValue(1), equalTo(15.0));
        assertThat(graph.xPixelLeftToValue(2), equalTo(15.0));
        assertThat(graph.xPixelCenterToValue(2), equalTo(20.0));
        assertThat(graph.xPixelRightToValue(2), equalTo(25.0));
        assertThat(graph.xPixelLeftToValue(3), equalTo(25.0));
        assertThat(graph.xPixelCenterToValue(3), equalTo(30.0));
        assertThat(graph.xPixelRightToValue(3), equalTo(35.0));
    }
    
    @Test
    public void yScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsCell(Ranges.range(0, 100), 3, 0, ValueScales.linearScale());
        assertThat(graph.yValueToPixel(0), equalTo(3));
        assertThat(graph.yValueToPixel(24.9999), equalTo(3));
        assertThat(graph.yValueToPixel(25), equalTo(2));
        assertThat(graph.yValueToPixel(49.9999), equalTo(2));
        assertThat(graph.yValueToPixel(50), equalTo(1));
        assertThat(graph.yValueToPixel(74.9999), equalTo(1));
        assertThat(graph.yValueToPixel(75), equalTo(0));
        assertThat(graph.yValueToPixel(99.9999), equalTo(0));
        assertThat(graph.yValueToPixel(100), equalTo(-1));
        assertThat(graph.yPixelBottomToValue(3), equalTo(0.0));
        assertThat(graph.yPixelCenterToValue(3), equalTo(12.5));
        assertThat(graph.yPixelTopToValue(3), equalTo(25.0));
        assertThat(graph.yPixelBottomToValue(2), equalTo(25.0));
        assertThat(graph.yPixelCenterToValue(2), equalTo(37.5));
        assertThat(graph.yPixelTopToValue(2), equalTo(50.0));
        assertThat(graph.yPixelBottomToValue(1), equalTo(50.0));
        assertThat(graph.yPixelCenterToValue(1), equalTo(62.5));
        assertThat(graph.yPixelTopToValue(1), equalTo(75.0));
        assertThat(graph.yPixelBottomToValue(0), equalTo(75.0));
        assertThat(graph.yPixelCenterToValue(0), equalTo(87.5));
        assertThat(graph.yPixelTopToValue(0), equalTo(100.0));
    }
    
    @Test
    public void yScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsPoint(Ranges.range(0, 30), 3, 0, ValueScales.linearScale());
        assertThat(graph.yValueToPixel(0), equalTo(3));
        assertThat(graph.yValueToPixel(4.9999), equalTo(3));
        assertThat(graph.yValueToPixel(5), equalTo(2));
        assertThat(graph.yValueToPixel(10), equalTo(2));
        assertThat(graph.yValueToPixel(14.9999), equalTo(2));
        assertThat(graph.yValueToPixel(15), equalTo(1));
        assertThat(graph.yValueToPixel(20), equalTo(1));
        assertThat(graph.yValueToPixel(24.9999), equalTo(1));
        assertThat(graph.yValueToPixel(25), equalTo(0));
        assertThat(graph.yValueToPixel(30), equalTo(0));
    }

    @Test
    public void drawBottomLabels() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        List<String> labels = Arrays.asList("0", "50", "100");
        ArrayInt positions = new ArrayInt(10, 150, 289);
        graphBuffer.drawBottomLabels(labels, positions, Color.BLACK, FontUtil.getLiberationSansRegular(),
                10, 289, 190);
        ImageAssert.compareImages("graphBuffer.drawBottomLabels", graphBuffer.getImage());
    }

    @Test
    public void drawLeftLabels() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        List<String> labels = Arrays.asList("0", "50", "100");
        ArrayInt positions = new ArrayInt(189, 100, 10);
        graphBuffer.drawLeftLabels(labels, positions, Color.BLACK, FontUtil.getLiberationSansRegular(),
                189, 10, 35);
        ImageAssert.compareImages("graphBuffer.drawLeftLabels", graphBuffer.getImage());
    }

    @Test
    public void drawHorizontalReferenceLines() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        ArrayInt positions = new ArrayInt(189, 100, 10);
        graphBuffer.drawHorizontalReferenceLines(positions, Color.BLACK, 5, 294);
        ImageAssert.compareImages("graphBuffer.drawHorizontalReferenceLines", graphBuffer.getImage());
    }

    @Test
    public void drawVerticalReferenceLines() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        ArrayInt positions = new ArrayInt(10, 150, 289);
        graphBuffer.drawVerticalReferenceLines(positions, Color.BLACK, 5, 194);
        ImageAssert.compareImages("graphBuffer.drawVerticalReferenceLines", graphBuffer.getImage());
    }
}
