/*
 * Copyright 2011 Brookhaven National Laboratory
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
        Histogram1D hist = new Hist1DT1();
        BufferedImage image = new BufferedImage(hist.getImageWidth(), hist.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Histogram1DRenderer renderer = new Histogram1DRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, hist);
        compareImages("hist1D.1", image);
    }
    
    public static void compareImages(String imageName, BufferedImage image) throws Exception {
        BufferedImage expected = ImageIO.read(Histogram1DRendererTest.class.getResource(imageName + ".png"));
        boolean done = false;
        try {
            assertEquals("Images are not the same height", expected.getHeight(), image.getHeight());
            assertEquals("Images are not the same width", expected.getWidth(), image.getWidth());

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    assertEquals(expected.getRGB(x, y), image.getRGB(x, y));
                }
            }
            done = true;
        } finally {
            if (!done) {
                ImageIO.write(image, "png", new File("src/test/resources/org/epics/graphene/" + imageName + ".failed.png"));
            }
        }
    }
}
