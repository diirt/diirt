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
import java.util.Arrays;
import java.util.List;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.diirt.util.stats.Ranges;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

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
        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap.xml"); 
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.load(file)));
        GraphBuffer graphbuffer= new GraphBuffer(renderer); 
        renderer.draw(graphbuffer, data);
        ImageAssert.compareImages("numberColorMap.cutomizedColorMapDraw", graphbuffer.getImage());
    }
    
    @Test 
    public void customizedColorMapDraw2() throws Exception{
        
        Cell2DDataset data = IntensityGraph2DRendererTest.rectangleDataset(); 
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);     
        File file = new File ("src/test/resources/org/diirt/graphene/summer.cmap"); 
        renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.load(file))); 
        GraphBuffer graphbuffer = new GraphBuffer(renderer); 
        renderer.draw(graphbuffer,data); 
        ImageAssert.compareImages("numberColorMap.cutomizedColorMapDraw2", graphbuffer.getImage());
    }
    
    @Test 
    public void loadColorMapXML() throws Exception {
        //testing with relative colormap 
        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap.xml"); 
        NumberColorMapGradient colorMap = (NumberColorMapGradient)NumberColorMaps.load(file); 
        List<Color> colors = colorMap.getColors(); 
        assertThat(colors.get(0),equalTo(new Color(95,0,0))); 
        assertThat(colors.get(1),equalTo(new Color(195,9,9))); 
        assertThat(colors.get(2), equalTo(new Color(201,201,242)));
        assertThat(colors.get(3), equalTo(new Color(13,13,198)));
        assertThat(colors.get(4),equalTo(new Color(0,0,95))); 

    }
    @Test 
    public void loadColorMapXML2 () throws Exception {
     
        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap2.xml");
        NumberColorMapGradient colorMap = (NumberColorMapGradient)NumberColorMaps.load(file);
        List<Color> colors = colorMap.getColors();
        assertThat(colors.get(0), equalTo(new Color(95, 0, 0)));
        assertThat(colors.get(1), equalTo(new Color(135, 2, 2)));
        assertThat(colors.get(2), equalTo(new Color(195, 9, 9)));
        assertThat(colors.get(3), equalTo(new Color(201, 201, 242)));
        assertThat(colors.get(4), equalTo(new Color(13, 13, 198)));
        assertThat(colors.get(5), equalTo(new Color(9, 9, 137)));
        assertThat(colors.get(6), equalTo(new Color(0, 0, 95)));
    }
    
    @Test 
    public void loadColorMapCMAP () throws Exception{
        //summer.cmap: exported from matlab. comma separated value of RGB colors
        File file = new File ("src/test/resources/org/diirt/graphene/summer.cmap"); 
        NumberColorMapGradient colormap = (NumberColorMapGradient)NumberColorMaps.load(file); 
        List<Color> colors = colormap.getColors(); 
        assertThat(colors.get(0), equalTo(new Color(0,128,102))); 
        assertThat(colors.get(20),equalTo(new Color(81,168,102))); 
        assertThat(colors.get(40), equalTo(new Color(162,208,102))); 
        assertThat(colors.get(60), equalTo(new Color(243,249,102))); 
        assertThat(colors.get(63), equalTo(new Color(255,255,102))); 
        
    }

    
    @Test
    public void absoluteScheme()throws Exception {
        NumberColorMap colorMap = NumberColorMaps.absolute(
                Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN),
                new ArrayDouble(-1.0, 0, 1.0),
                Color.BLACK, "TEST");
        NumberColorMapInstance instance = colorMap.createInstance(Ranges.range(0.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(Color.RED.getRGB()));
        assertThat(instance.colorFor(0.0), equalTo(Color.YELLOW.getRGB()));
        assertThat(instance.colorFor(1.0), equalTo(Color.GREEN.getRGB()));
        instance = colorMap.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(Color.RED.getRGB()));
        assertThat(instance.colorFor(0.0), equalTo(Color.YELLOW.getRGB()));
        assertThat(instance.colorFor(1.0), equalTo(Color.GREEN.getRGB()));
    }
    
    @Test 
    public void absoluteScheme2() throws Exception{
        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap2.xml");
        NumberColorMap customized = NumberColorMaps.load(file); 
        NumberColorMapInstance instance = customized.createInstance(Ranges.range(0.0, 1.0)); 
        assertThat(instance.colorFor(-1.0),equalTo(new Color(95,0,0).getRGB())); 
        assertThat(instance.colorFor(0.0),equalTo(new Color(95,0,0).getRGB())); 
        assertThat(instance.colorFor(0.1),equalTo(new Color(135,2,2).getRGB())); 
        assertThat(instance.colorFor(0.25),equalTo(new Color(195,9,9).getRGB())); 
        assertThat(instance.colorFor(0.5),equalTo(new Color(201,201,242).getRGB())); 
        assertThat(instance.colorFor(0.75),equalTo(new Color(13,13,198).getRGB())); 
        assertThat(instance.colorFor(0.9),equalTo(new Color(9,9,137).getRGB())); 
        assertThat(instance.colorFor(1.0),equalTo(new Color(0,0,95).getRGB())); 
        assertThat(instance.colorFor(1.1),equalTo(new Color(0,0,95).getRGB()));
        
        instance = customized.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(new Color(95, 0, 0).getRGB()));
        assertThat(instance.colorFor(0.0), equalTo(new Color(95, 0, 0).getRGB()));
        assertThat(instance.colorFor(0.1), equalTo(new Color(135, 2, 2).getRGB()));
        assertThat(instance.colorFor(0.25), equalTo(new Color(195, 9, 9).getRGB()));
        assertThat(instance.colorFor(0.5), equalTo(new Color(201, 201, 242).getRGB()));
        assertThat(instance.colorFor(0.75), equalTo(new Color(13, 13, 198).getRGB()));
        assertThat(instance.colorFor(0.9), equalTo(new Color(9, 9, 137).getRGB()));
        assertThat(instance.colorFor(1.0), equalTo(new Color(0, 0, 95).getRGB()));
        assertThat(instance.colorFor(1.1), equalTo(new Color(0, 0, 95).getRGB()));
    }

    @Test
    public void relativeScheme() throws Exception {
        NumberColorMap colorMap = NumberColorMaps.relative(
                Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN),
                new ArrayDouble(0.0, 0.5, 1.0),
                Color.BLACK, "TEST");
        NumberColorMapInstance instance = colorMap.createInstance(Ranges.range(0.0, 1.0));
        assertThat(instance.colorFor(0.0), equalTo(Color.RED.getRGB()));
        assertThat(instance.colorFor(0.5), equalTo(Color.YELLOW.getRGB()));
        assertThat(instance.colorFor(1.0), equalTo(Color.GREEN.getRGB()));
        instance = colorMap.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(Color.RED.getRGB()));
        assertThat(instance.colorFor(0.0), equalTo(Color.YELLOW.getRGB()));
        assertThat(instance.colorFor(1.0), equalTo(Color.GREEN.getRGB()));
    }
}
