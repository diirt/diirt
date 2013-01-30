/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

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
