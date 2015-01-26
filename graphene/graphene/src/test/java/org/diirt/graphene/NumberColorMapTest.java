/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.graphene.NumberColorMapInstance;
import org.diirt.graphene.NumberColorMaps;
import org.diirt.util.stats.Range;
import java.awt.Color;
import java.io.File;
import org.diirt.util.stats.Ranges;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class NumberColorMapTest {
    
    public NumberColorMapTest() {
    }

    @Test
    public void jetScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.JET.createInstance(range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.0), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.6), equalTo(Color.YELLOW.getRGB()));
        assertThat(colorScheme.colorFor(1.0), equalTo(new Color(138,0,0).getRGB()));
        assertThat(colorScheme.colorFor(1.1), equalTo(new Color(138,0,0).getRGB()));
    }
    
    @Test
    public void optimizedScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.optimize(NumberColorMaps.JET.createInstance(range), range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.0), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.601), equalTo(new Color(255,254,0).getRGB()));
        assertThat(colorScheme.colorFor(1.0), equalTo(new Color(138,0,0).getRGB()));
        assertThat(colorScheme.colorFor(1.1), equalTo(new Color(138,0,0).getRGB()));
    }
    
    @Test
    public void customizedColorMapDraw() throws Exception{ 
        //0% = dark red 25% = red 50% =light lilac 75% = blue 100% =dark blue
        Cell2DDataset data = IntensityGraph2DRendererTest.rectangleDataset();  
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        File file = new File("/Users/YifengYang/Desktop/UROP/graphene_new/diirt/graphene/graphene/src/test/resources/org/diirt/graphene/ColorTest.xml"); 
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.loadColorMap(file, true)));
        GraphBuffer graphbuffer= new GraphBuffer(renderer); 
        renderer.draw(graphbuffer, data);
        // TODO: Change the name of the file
        ImageAssert.compareImages("intensityGraph2D.customizedColorMap", graphbuffer.getImage());
    }
    
}
