/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.epics.graphene.profile.ProfileGraph2D;

public class ImageWriter {
    public static void saveImage(String name, BufferedImage image){
        saveImage(ProfileGraph2D.LOG_FILEPATH, name, image);   
    }    
    
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
