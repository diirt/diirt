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
public class Bar1DChartRendererTest {
    
    public Bar1DChartRendererTest() {
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
        Bar1DChartRenderer renderer = new Bar1DChartRenderer(300, 200);
        renderer.update(new Bar1DChartRendererUpdate().yAxisRange(AxisRanges.absolute(0, 550)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, dataset);
        compareImages("bar1DChart.1", image);
    }
//    
//    @Test
//    public void test2() throws Exception {
//        Histogram1D hist = new Hist1DT3();
//        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
//        Histogram1DRenderer renderer = new Histogram1DRenderer(300, 200);
//        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        renderer.draw(graphics, hist);
//        compareImages("hist1D.3", image);
//    }
//    
//    @Test
//    public void test4() throws Exception {
//        Histogram1D hist = new Hist1DT4();
//        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
//        Histogram1DRenderer renderer = new Histogram1DRenderer(300, 200);
//        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        renderer.draw(graphics, hist);
//        compareImages("hist1D.4", image);
//    }
//    
//    @Test
//    public void test5() throws Exception {
//        Histogram1D hist = new Hist1DT5();
//        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
//        Histogram1DRenderer renderer = new Histogram1DRenderer(300, 200);
//        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        renderer.draw(graphics, hist);
//        compareImages("hist1D.5", image);
//    }
//    
//    @Test
//    public void test6() throws Exception {
//        Histogram1D hist = new Hist1DT6();
//        BufferedImage image = new BufferedImage(600, 200, BufferedImage.TYPE_3BYTE_BGR);
//        Histogram1DRenderer renderer = new Histogram1DRenderer(600, 200);
//        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        renderer.draw(graphics, hist);
//        compareImages("hist1D.6", image);
//    }
//    

}
