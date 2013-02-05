/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
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
public class Graph2DRendererTest {
    
    public Graph2DRendererTest() {
    }
    
    @Test
    public void graphArea1() throws Exception {
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {
            
            {
                this.xCoordRange = RangeUtil.range(4.5, 295.5);
                this.yCoordRange = RangeUtil.range(4.5, 195.5);
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
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.1", image);
    }
    
    @Test
    public void graphArea2() throws Exception {
        
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {
            
            {
                this.xCoordRange = RangeUtil.range(24.5, 295.5);
                this.yCoordRange = RangeUtil.range(4.5, 195.5);
                this.xReferenceCoords = new ArrayDouble(24.5, 150, 295.5);
                this.yReferenceCoords = new ArrayDouble(195.5, 147.75, 100, 47.25, 4.5);
                this.yReferenceLabels = Arrays.asList("0", "50", "100", "150", "200");
                this.yLabelMargin = 2.0;
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.2", image);
    }
    
    @Test
    public void graphArea3() throws Exception {
        
        Graph2DRenderer renderer = new Graph2DRenderer(300, 200) {
            
            {
                this.xCoordRange = RangeUtil.range(24.5, 295.5);
                this.yCoordRange = RangeUtil.range(4.5, 195.5);
                this.xReferenceCoords = new ArrayDouble(50, 100, 150, 200, 250);
                this.yReferenceCoords = new ArrayDouble(160, 120, 80, 40);
                this.yReferenceLabels = Arrays.asList("0", "50", "100", "150");
                this.yLabelMargin = 1.0;
            }

            @Override
            public Graph2DRendererUpdate newUpdate() {
                return new Graph2DRendererUpdate();
            }
        };
        
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        renderer.drawGraphArea();
        ImageAssert.compareImages("graph2DArea.3", image);
    }
    
    @Test
    public void inheritance1() throws Exception {
        Bar1DChartRenderer renderer = new Bar1DChartRenderer(300, 200);
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

}
