/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 *
 * @author carcassi
 */
public class Graph2DAreaRendererTest {
    
    public Graph2DAreaRendererTest() {
    }

    @Test
    public void render1() throws Exception {
        // Draws only the reference lines Only check the drawing area, with no labels
        Graph2DArea data = mock(Graph2DArea.class);
        when(data.getBackgroundColor()).thenReturn(Color.WHITE);
        when(data.getWidth()).thenReturn(300);
        when(data.getHeight()).thenReturn(200);
        
        when(data.getStartX()).thenReturn(4.5);
        when(data.getEndX()).thenReturn(295.5);
        when(data.getStartY()).thenReturn(195.5);
        when(data.getEndY()).thenReturn(4.5);
        
        when(data.getXReferences()).thenReturn(new ArrayDouble(4.5, 150, 295.5));
        when(data.getYReferences()).thenReturn(new ArrayDouble(195.5, 100, 4.5));
        when(data.getYReferenceLabels()).thenReturn(Collections.<String>emptyList());
        when(data.getYReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getYReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graph2DAreaRenderer renderer = new Graph2DAreaRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("graph2DArea.1", image);
    }

    @Test
    public void render2() throws Exception {
        Graph2DArea data = mock(Graph2DArea.class);
        when(data.getBackgroundColor()).thenReturn(Color.WHITE);
        when(data.getWidth()).thenReturn(300);
        when(data.getHeight()).thenReturn(200);
        
        when(data.getStartX()).thenReturn(24.5);
        when(data.getEndX()).thenReturn(295.5);
        when(data.getStartY()).thenReturn(195.5);
        when(data.getEndY()).thenReturn(4.5);
        
        when(data.getXReferences()).thenReturn(new ArrayDouble(24.5, 150, 295.5));
        when(data.getYReferences()).thenReturn(new ArrayDouble(195.5, 147.75, 100, 47.25, 4.5));
        when(data.getYReferenceLabels()).thenReturn(Arrays.asList("0", "50", "100", "150", "200"));
        when(data.getYReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getYReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        when(data.getYReferenceLabelMargin()).thenReturn(2);
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graph2DAreaRenderer renderer = new Graph2DAreaRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("graph2DArea.2", image);
    }

    @Test
    public void render3() throws Exception {
        Graph2DArea data = mock(Graph2DArea.class);
        when(data.getBackgroundColor()).thenReturn(Color.WHITE);
        when(data.getWidth()).thenReturn(300);
        when(data.getHeight()).thenReturn(200);
        
        when(data.getStartX()).thenReturn(24.5);
        when(data.getEndX()).thenReturn(295.5);
        when(data.getStartY()).thenReturn(195.5);
        when(data.getEndY()).thenReturn(4.5);
        
        when(data.getXReferences()).thenReturn(new ArrayDouble(50, 100, 150, 200, 250));
        when(data.getXReferenceLabels()).thenReturn(null);
        when(data.getXReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getXReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        when(data.getXReferenceLabelMargin()).thenReturn(1);
        when(data.getYReferences()).thenReturn(new ArrayDouble(160, 120, 80, 40));
        when(data.getYReferenceLabels()).thenReturn(Arrays.asList("0", "50", "100", "150"));
        when(data.getYReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getYReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        when(data.getYReferenceLabelMargin()).thenReturn(1);
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graph2DAreaRenderer renderer = new Graph2DAreaRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("graph2DArea.3", image);
    }

    @Test
    public void render4() throws Exception {
        Graph2DArea data = mock(Graph2DArea.class);
        when(data.getBackgroundColor()).thenReturn(Color.WHITE);
        when(data.getWidth()).thenReturn(300);
        when(data.getHeight()).thenReturn(200);
        
        when(data.getStartX()).thenReturn(4.5);
        when(data.getEndX()).thenReturn(295.5);
        when(data.getStartY()).thenReturn(185.5);
        when(data.getEndY()).thenReturn(4.5);
        
        when(data.getXReferences()).thenReturn(new ArrayDouble(50, 100, 150, 200, 250));
        when(data.getXReferenceLabels()).thenReturn(Arrays.asList("0", "50", "100", "150", "200"));
        when(data.getXReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getXReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        when(data.getXReferenceLabelMargin()).thenReturn(1);
        when(data.getYReferences()).thenReturn(new ArrayDouble(160, 120, 80, 40));
        when(data.getYReferenceLabelColor()).thenReturn(Color.BLACK);
        when(data.getYReferenceLabelFont()).thenReturn(FontUtil.getLiberationSansRegular());
        when(data.getYReferenceLabelMargin()).thenReturn(1);
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graph2DAreaRenderer renderer = new Graph2DAreaRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("graph2DArea.4", image);
    }
}
