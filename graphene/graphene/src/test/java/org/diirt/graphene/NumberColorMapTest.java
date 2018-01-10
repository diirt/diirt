/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import  javafx.scene.paint.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.diirt.util.array.ArrayDouble;
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
    private int getRGB(Color c){
            int red = (int)(255*c.getRed());
            int green = (int)(255*c.getGreen());
            int blue = (int)(255*c.getBlue());
            return (255 << 24) | (red<< 16) | (green<< 8) | blue;
    }
    @Test
    public void jetScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.JET.createInstance(range);
        // TODO: we should better test the optimized version as well.
        assertThat(colorScheme.colorFor(Double.NaN), equalTo(getRGB(Color.rgb(0,0,0))));
        assertThat(colorScheme.colorFor(-0.1), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(0.0), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(1.0), equalTo(getRGB(Color.rgb(138,0,0))));
        assertThat(colorScheme.colorFor(1.1), equalTo(getRGB(Color.rgb(138,0,0))));
    }

    @Test
    public void jetSchemeOptimized(){
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.JET.createInstance(range);
        //ensure that the optimized colormap handles nan
        colorScheme = NumberColorMaps.optimize(colorScheme, range);
        assertThat(colorScheme.colorFor(Double.NaN), equalTo(getRGB(Color.rgb(0,0,0))));
        assertThat(colorScheme.colorFor(-0.1), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(0.0), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(1.0), equalTo(getRGB(Color.rgb(138,0,0))));
        assertThat(colorScheme.colorFor(1.1), equalTo(getRGB(Color.rgb(138,0,0))));
    }

    @Test
    public void optimizedScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.optimize(NumberColorMaps.JET.createInstance(range), range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(0.0), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(0.601), equalTo(getRGB(Color.rgb(255,254,0))));
        assertThat(colorScheme.colorFor(1.0), equalTo(getRGB(Color.rgb(138,0,0))));
        assertThat(colorScheme.colorFor(1.1), equalTo(getRGB(Color.rgb(138,0,0))));
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
        NumberColorMapGradient colorMap = (NumberColorMapGradient) NumberColorMaps.load(file);
        List<Color> colors = colorMap.getColors();
        assertThat(colors.get(0),equalTo(Color.rgb(95,0,0)));
        assertThat(colors.get(1),equalTo(Color.rgb(195,9,9,1)));
        assertThat(colors.get(2), equalTo(Color.rgb(201,201,242)));
        assertThat(colors.get(3), equalTo(Color.rgb(13,13,198)));
        assertThat(colors.get(4),equalTo(Color.rgb(0,0,95)));

    }
    @Test
    public void loadColorMapXML2 () throws Exception {

        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap2.xml");
        NumberColorMapGradient colorMap = (NumberColorMapGradient) NumberColorMaps.load(file);
        List<Color> colors = colorMap.getColors();
        assertThat(colors.get(0), equalTo(Color.rgb(95, 0, 0)));
        assertThat(colors.get(1), equalTo(Color.rgb(135, 2, 2)));
        assertThat(colors.get(2), equalTo(Color.rgb(195, 9, 9)));
        assertThat(colors.get(3), equalTo(Color.rgb(201, 201, 242)));
        assertThat(colors.get(4), equalTo(Color.rgb(13, 13, 198)));
        assertThat(colors.get(5), equalTo(Color.rgb(9, 9, 137)));
        assertThat(colors.get(6), equalTo(Color.rgb(0, 0, 95)));
    }

    @Test
    public void loadColorMapCMAP () throws Exception{
        //summer.cmap: exported from matlab. comma separated value of RGB colors
        File file = new File ("src/test/resources/org/diirt/graphene/summer.cmap");
        NumberColorMapGradient colormap = (NumberColorMapGradient) NumberColorMaps.load(file);
        List<Color> colors = colormap.getColors();
        assertThat(colors.get(0), equalTo(Color.rgb(0,128,102)));
        assertThat(colors.get(20),equalTo(Color.rgb(81,168,102)));
        assertThat(colors.get(40), equalTo(Color.rgb(162,208,102)));
        assertThat(colors.get(60), equalTo(Color.rgb(243,249,102)));
        assertThat(colors.get(63), equalTo(Color.rgb(255,255,102)));

    }

    @Test(expected = RuntimeException.class)
    public void expectExceptionPercentageRange() throws Exception{
        //percentage value should not be negative
        NumberColorMap colorMap = NumberColorMaps.relative(
                Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN),
                new ArrayDouble(-1.0, 0, 1.0),
                Color.BLACK, "TEST");
    }

    @Test(expected = RuntimeException.class)
    public void expectExceptionIncreasingOrder()throws Exception{
        //position values should always be in increasing order
          NumberColorMap colorMap = NumberColorMaps.absolute(
                Arrays.asList(Color.RED,Color.DARKGRAY,Color.BLACK, Color.YELLOW, Color.GREEN),
                new ArrayDouble(-1.0, 0, -0.5, 1.0, 0.5),
                Color.BLACK, "TEST");
    }
    @Test(expected = RuntimeException.class)
    public void expectExceptionIncreasingOrder2() throws Exception {
        NumberColorMap colorMap = NumberColorMaps.relative(
                Arrays.asList(Color.RED,Color.DARKGRAY,Color.BLACK, Color.YELLOW, Color.GREEN),
                new ArrayDouble(0.0, 0.1, 0.2, 0.5,0.3),
                Color.BLACK, "TEST");
    }


    @Test
    public void absoluteScheme()throws Exception {
        NumberColorMap colorMap = NumberColorMaps.absolute(
                Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN),
                new ArrayDouble(-1.0, 0, 1.0),
                Color.BLACK, "TEST");
        NumberColorMapInstance instance = colorMap.createInstance(Ranges.range(0.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(getRGB(Color.RED)));
        assertThat(instance.colorFor(0.0), equalTo(getRGB(Color.YELLOW)));
        assertThat(instance.colorFor(1.0), equalTo(getRGB(Color.GREEN)));
        instance = colorMap.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(getRGB(Color.RED)));
        assertThat(instance.colorFor(0.0), equalTo(getRGB(Color.YELLOW)));
        assertThat(instance.colorFor(1.0), equalTo(getRGB(Color.GREEN)));
    }

    @Test
    public void absoluteScheme2() throws Exception{
        File file = new File("src/test/resources/org/diirt/graphene/customizedColorMap2.xml");
        NumberColorMap customized = NumberColorMaps.load(file);
        NumberColorMapInstance instance = customized.createInstance(Ranges.range(0.0, 1.0));
        assertThat(instance.colorFor(-1.0),equalTo(getRGB(Color.rgb(95,0,0))));
        assertThat(instance.colorFor(0.0),equalTo(getRGB(Color.rgb(95,0,0))));
        assertThat(instance.colorFor(0.1),equalTo(getRGB(Color.rgb(135,2,2))));
        assertThat(instance.colorFor(0.25),equalTo(getRGB(Color.rgb(195,9,9))));
        assertThat(instance.colorFor(0.5),equalTo(getRGB(Color.rgb(201,201,242))));
        assertThat(instance.colorFor(0.75),equalTo(getRGB(Color.rgb(13,13,198))));
        assertThat(instance.colorFor(0.9),equalTo(getRGB(Color.rgb(9,9,137))));
        assertThat(instance.colorFor(1.0),equalTo(getRGB(Color.rgb(0,0,95))));
        assertThat(instance.colorFor(1.1),equalTo(getRGB(Color.rgb(0,0,95))));

        instance = customized.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(getRGB(Color.rgb(95, 0, 0))));
        assertThat(instance.colorFor(0.0), equalTo(getRGB(Color.rgb(95, 0, 0))));
        assertThat(instance.colorFor(0.1), equalTo(getRGB(Color.rgb(135, 2, 2))));
        assertThat(instance.colorFor(0.25), equalTo(getRGB(Color.rgb(195, 9, 9))));
        assertThat(instance.colorFor(0.5), equalTo(getRGB(Color.rgb(201, 201, 242))));
        assertThat(instance.colorFor(0.75), equalTo(getRGB(Color.rgb(13, 13, 198))));
        assertThat(instance.colorFor(0.9), equalTo(getRGB(Color.rgb(9, 9, 137))));
        assertThat(instance.colorFor(1.0), equalTo(getRGB(Color.rgb(0, 0, 95))));
        assertThat(instance.colorFor(1.1), equalTo(getRGB(Color.rgb(0, 0, 95))));
    }

    @Test
    public void relativeScheme() throws Exception {
        NumberColorMap colorMap = NumberColorMaps.relative(
                Arrays.asList(Color.RED, Color.YELLOW, Color.GREEN),
                new ArrayDouble(0.0, 0.5, 1.0),
                Color.BLACK, "TEST");
        NumberColorMapInstance instance = colorMap.createInstance(Ranges.range(0.0, 1.0));
        assertThat(instance.colorFor(0.0), equalTo(getRGB(Color.RED)));
        assertThat(instance.colorFor(0.5), equalTo(getRGB(Color.YELLOW)));
        assertThat(instance.colorFor(1.0), equalTo(getRGB(Color.GREEN)));
        instance = colorMap.createInstance(Ranges.range(-1.0, 1.0));
        assertThat(instance.colorFor(-1.0), equalTo(getRGB(Color.RED)));
        assertThat(instance.colorFor(0.0), equalTo(getRGB(Color.YELLOW)));
        assertThat(instance.colorFor(1.0), equalTo(getRGB(Color.GREEN)));
    }

    @Test

    public void getRegisteredColorSchemeJET() throws Exception{

       NumberColorMap jet = NumberColorMaps.getRegisteredColorSchemes().get("JET");
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = jet.createInstance(range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(0.0), equalTo(getRGB(Color.rgb(0,0,138))));
        assertThat(colorScheme.colorFor(1.0), equalTo(getRGB(Color.rgb(138,0,0))));
        assertThat(colorScheme.colorFor(1.1), equalTo(getRGB(Color.rgb(138,0,0))));
        assertThat(colorScheme.colorFor(Double.NaN), equalTo(getRGB(Color.BLACK)));
    }
    @Test

    public void getRegisterColorSchemeBONE() throws Exception{
        NumberColorMap bone = NumberColorMaps.getRegisteredColorSchemes().get("BONE");
        Range range = Ranges.range(0.0,1.0);
        NumberColorMapInstance colorScheme = bone.createInstance(range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(getRGB(Color.BLACK)));
        assertThat(colorScheme.colorFor(0.0), equalTo(getRGB(Color.BLACK)));
        assertThat(colorScheme.colorFor(0.25), equalTo(getRGB(Color.rgb(57,57,86))));
        assertThat(colorScheme.colorFor(0.5), equalTo(getRGB(Color.rgb(107,115,140))));
        assertThat(colorScheme.colorFor(Double.NaN), equalTo(getRGB(Color.RED)));
    }


}
