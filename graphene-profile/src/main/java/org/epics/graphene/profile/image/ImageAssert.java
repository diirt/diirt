/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static org.junit.Assert.*;

/**
 *
 * @author asbarber
 */
public class ImageAssert {
    
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
    
    public static boolean sameImages(BufferedImage a, BufferedImage b){
        int x = 0;
        int y = 0;
        try{
    
            assertEquals("Images are not the same height", a.getHeight(), b.getHeight());
            assertEquals("Images are not the same width", a.getWidth(), b.getWidth());

            for (x = 0; x < b.getWidth(); x++) {
                for (y = 0; y < b.getHeight(); y++) {
                    assertEquals(a.getRGB(x, y), b.getRGB(x, y));
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
