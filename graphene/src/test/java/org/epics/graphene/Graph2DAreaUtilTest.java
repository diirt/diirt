/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class Graph2DAreaUtilTest {
    
    public Graph2DAreaUtilTest() {
    }

    @Test
    public void autoArea1() {
        BufferedImage image = new BufferedImage(320, 240, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        Graph2DArea area = Graph2DAreaUtil.autoArea(graphics, 0.0, 10.0, 0.0, 10.0, 320, 240);
        assertThat(area.getWidth(), equalTo(320));
        assertThat(area.getHeight(), equalTo(240));
        assertThat(area.getStartXValue(), equalTo(0.0));
        assertThat(area.getEndXValue(), equalTo(10.0));
        assertThat(area.getStartX(), equalTo(3.5));
        assertThat(area.getEndX(), equalTo(316.5));
    }
}
