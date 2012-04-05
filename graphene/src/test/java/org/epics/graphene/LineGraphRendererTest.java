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
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class LineGraphRendererTest {
    
    public LineGraphRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test1() throws Exception {
        OrderedDataset2D data = new OrderedDataset2DT1();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineGraphRenderer renderer = new LineGraphRenderer();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        compareImages("lineGraph.1", image);
    }
    
    public static void compareImages(String imageName, BufferedImage image) throws Exception {
        boolean done = false;
        try {
            BufferedImage expected = ImageIO.read(LineGraphRendererTest.class.getResource(imageName + ".png"));
            
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
