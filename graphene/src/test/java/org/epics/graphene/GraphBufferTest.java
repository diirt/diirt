/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import org.epics.util.array.ArrayDouble;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class GraphBufferTest {
    
    @Test
    public void yScalingAsCell1() throws Exception {
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
    public void xScalingAsCell2() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setGraphArea(4, 4, 295, 195);
        graph.setXScaleAsCell(RangeUtil.range(0, 100), 4, 295, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(4));
        assertThat(graph.xValueToPixel(100), equalTo(296));
        assertThat(graph.xValueToPixel(99.99999), equalTo(295));
        assertThat(graph.xValueToPixel(50), equalTo(150));
        assertThat(graph.xValueToPixel(49.99999), equalTo(149));
    }
    
    @Test
    public void xScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setGraphArea(4, 4, 295, 195);
        graph.setXScaleAsPoint(RangeUtil.range(0, 100), 4, 295, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(4));
        assertThat(graph.xValueToPixel(100), equalTo(295));
        assertThat(graph.xValueToPixel(99.99999), equalTo(295));
        assertThat(graph.xValueToPixel(50), equalTo(150));
        assertThat(graph.xValueToPixel(49.99999), equalTo(149));
    }
    
    @Test
    public void yScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setGraphArea(4, 4, 295, 195);
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

}
