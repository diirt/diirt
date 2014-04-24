/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayInt;
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
        graph.setGraphArea(4, 4, 295, 195);
        graph.setXScaleAsCell(RangeUtil.range(0, 100), 0, 3, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(0));
        assertThat(graph.xValueToPixel(24.9999), equalTo(0));
        assertThat(graph.xValueToPixel(25), equalTo(1));
        assertThat(graph.xValueToPixel(49.9999), equalTo(1));
        assertThat(graph.xValueToPixel(50), equalTo(2));
        assertThat(graph.xValueToPixel(74.9999), equalTo(2));
        assertThat(graph.xValueToPixel(75), equalTo(3));
        assertThat(graph.xValueToPixel(99.9999), equalTo(3));
        assertThat(graph.xValueToPixel(100), equalTo(4));
    }
    
    @Test
    public void xScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setXScaleAsPoint(RangeUtil.range(0, 30), 0, 3, ValueScales.linearScale());
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
    }
    
    @Test
    public void yScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsCell(RangeUtil.range(0, 100), 3, 0, ValueScales.linearScale());
        assertThat(graph.yValueToPixel(0), equalTo(3));
        assertThat(graph.yValueToPixel(24.9999), equalTo(3));
        assertThat(graph.yValueToPixel(25), equalTo(2));
        assertThat(graph.yValueToPixel(49.9999), equalTo(2));
        assertThat(graph.yValueToPixel(50), equalTo(1));
        assertThat(graph.yValueToPixel(74.9999), equalTo(1));
        assertThat(graph.yValueToPixel(75), equalTo(0));
        assertThat(graph.yValueToPixel(99.9999), equalTo(0));
        assertThat(graph.yValueToPixel(100), equalTo(-1));
    }
    
    @Test
    public void yScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsPoint(RangeUtil.range(0, 30), 3, 0, ValueScales.linearScale());
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
        ArrayInt positions = new ArrayInt(10, 100, 189);
        graphBuffer.drawHorizontalReferenceLines(positions, Color.BLACK, 5, 294);
        ImageAssert.compareImages("graphBuffer.drawHorizontalReferenceLines", graphBuffer.getImage());
    }
}
