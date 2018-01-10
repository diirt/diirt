/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class ImageAssert {
    public static void compareImages(String imageName, BufferedImage image) throws Exception {
        boolean done = false;
        try {
            BufferedImage expected = ImageIO.read(ImageAssert.class.getResource(imageName + ".png"));

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
                ImageIO.write(image, "png", new File("src/test/resources/org/diirt/graphene/" + imageName + ".failed.png"));
            } else {
                File file = new File("src/test/resources/org/diirt/graphene/" + imageName + ".failed.png");
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }
}
