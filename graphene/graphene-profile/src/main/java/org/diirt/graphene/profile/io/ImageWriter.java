/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.diirt.graphene.profile.ProfileGraph2D;

/**
 * Handles image input and output, such as saving an image.
 *
 * @author asbarber
 */
public final class ImageWriter {

    /**
     * Prevents instantiation.
     */
    private ImageWriter(){}

    /**
     * Saves the image as a png with the given name in the specified path
     * by <code>ProfileGraph2D</code>.
     * @param name name of the image (no path, no extension)
     * @param image image to save
     */
    public static void saveImage(String name, BufferedImage image){
        saveImage(ProfileGraph2D.LOG_FILEPATH, name, image);
    }

    /**
     * Saves the image as a png with the gaiven name in the specified path.
     * @param path file path to save the image to
     * @param name file name of the image (no path, no extension)
     * @param image image to save
     */
    public static void saveImage(String path, String name, BufferedImage image){
        try {
            if (image == null){
                return;
            }

            String fileName = path + name + ".png";

            ImageIO.write(image, "png", new File(fileName));
        } catch (IOException ex) {
            System.out.println("Could not save image.");
        }
    }

    /**
     * Gets a unique filename by appending .# after the name to prevent
     * a file overwrite.
     * @param original original name of the file to check uniqueness
     *                 (includes path but not extension)
     * @param ext extension of original file
     * @return filename that does not correspond to an existing file
     * by appending .# to the original if necessary
     */
    public static String getUniqueName(String original, String ext){
        File outputFile = new File(original + ext);

        //Prevent File Overwrite
        int tmp = 1;
        while (outputFile.exists()){
            outputFile = new File(original + ext + "." + tmp);
            tmp++;
        }

        return  outputFile.getName();
    }
}
