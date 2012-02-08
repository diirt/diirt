/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class Histogram1DRendererTest {
    
    public Histogram1DRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test1() throws Exception {
        BufferedImage expected = ImageIO.read(getClass().getResource("hist1D.1.png"));
        Histogram1D hist = new Hist1DT1();
        BufferedImage image = new BufferedImage(hist.getImageWidth(), hist.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Histogram1DRenderer renderer = new Histogram1DRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, hist);
        compareImages(expected, image);
    }
    
    public static void compareImages(BufferedImage expected, BufferedImage image) {
        assertEquals("Images are not the same height", expected.getHeight(), image.getHeight());
        assertEquals("Images are not the same width", expected.getWidth(), image.getWidth());
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), image.getRGB(x, y));
            }
        }
    }
}
