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

            double acceptableRange = 1.0;
            if(!System.getProperty("java.version").startsWith("11")) {
                acceptableRange = 5.0;
            }
            assertTrue("The percentage image difference is : " + getDifferencePercent(expected, image), getDifferencePercent(expected, image) <= acceptableRange);
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

    private static double getDifferencePercent(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
        }
 
        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;
 
        return 100.0 * diff / maxDiff;
    }
 
    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
}
