/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.BeforeClass;
import static org.epics.graphene.ImageAssert.*;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class AreaGraph2DRendererTest {
    
    public AreaGraph2DRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test1() throws Exception {
        Cell1DDataset dataset = Cell1DDatasets.linearRange(new ArrayDouble(30, 14, 150, 160, 180, 230, 220, 350, 400, 450, 500,
                                        350, 230, 180, 220, 170, 130, 80, 30, 40), 0, 2);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        AreaGraph2DRenderer renderer = new AreaGraph2DRenderer(300, 200);
        renderer.update(new AreaGraph2DRendererUpdate().yAxisRange(AxisRanges.absolute(0, 550)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        compareImages("bar1DChart.1", image);
    }
    
    @Test
    public void test2() throws Exception {
        Cell1DDataset dataset = Cell1DDatasets.linearRange(new ArrayDouble(0,5,10,5,0,5,10,0,5,10), 0, 10);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        AreaGraph2DRenderer renderer = new AreaGraph2DRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        compareImages("bar1DChart.2", image);
        dataset = Cell1DDatasets.linearRange(new ArrayDouble(0,2,3,0,4,5,0,6,7,0), 0, 10);
        renderer.draw(graphics, dataset);
        compareImages("bar1DChart.3", image);
    }
    
    @Test
    public void test4() throws Exception {
        Cell1DDataset dataset = Cell1DDatasets.linearRange(new ArrayDouble(0,1,2,3,4,5,6,7,8,9,10), 0, 10);
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        AreaGraph2DRenderer renderer = new AreaGraph2DRenderer(640, 480);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        compareImages("bar1DChart.4", image);
    }

}
