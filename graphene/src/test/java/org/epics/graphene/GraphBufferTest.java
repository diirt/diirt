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
    public void xScaling() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setGraphArea(4, 4, 295, 195);
        graph.setXScaleAsCell(RangeUtil.range(0, 100), 4, 295, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(4));
        assertThat(graph.xValueToPixel(100), equalTo(296));
        assertThat(graph.xValueToPixel(50), equalTo(150));
        assertThat(graph.xValueToPixel(49.99999), equalTo(149));
    }

}
