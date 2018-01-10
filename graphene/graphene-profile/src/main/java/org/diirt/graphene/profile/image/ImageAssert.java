/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Handles image comparisons.
 *
 * @author asbarber
 */
public class ImageAssert {

    /**
     * Determines whether the images specified by the file paths are identical.
     *
     * @param a file path of one image
     * @param b file path of another image
     * @return true if the same, otherwise false
     */
    public static boolean sameImages(String a, String b){
        try {
            BufferedImage aImage = ImageIO.read(new File(a));
            BufferedImage bImage = ImageIO.read(new File(b));
            return sameImages(aImage, bImage);
        } catch (IOException ex) {
            System.out.println("Error reading the images.");
            Logger.getLogger(ImageAssert.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Determines whether the images are identical by checking every pixel.
     *
     * @param a one image
     * @param b another image
     * @return true if the same, otherwise false
     */
    public static boolean sameImages(BufferedImage a, BufferedImage b){
        int x = 0;
        int y = 0;
        try{
            if (a.getHeight() != b.getHeight()){
                throw new IllegalArgumentException("Images are not the same height.");
            }

            if (a.getWidth() != b.getWidth()){
                throw new IllegalArgumentException("Images are not the same width.");
            }

            for (x = 0; x < b.getWidth(); x++) {
                for (y = 0; y < b.getHeight(); y++) {
                    if (a.getRGB(x, y) != b.getRGB(x, y)){
                        throw new IllegalArgumentException("Images are not the same.");

                    }
                }
            }

            return true;
        }
        catch (Exception e){
            System.out.println(x + "," + y);
            return false;
        }
    }
}
