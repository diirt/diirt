/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author carcassi
 */
public class SetPixelVsDrawRect {
    public static void main(String[] args) throws IOException {
        // Checking the difference in performance in using Image.setRGB
        // and Graphics2D.fillRect. It's about twice and fast.
        int width = 600;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        
        Random rand  = new Random(0);
        int[] values = new int[100000];
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextInt(height);
        }
        
        Graphics2D g = image.createGraphics();
        int nAttempts = 1000;
        StopWatch watch = new StopWatch(nAttempts);
        for (int i = 0; i < nAttempts; i++) {
            watch.start();
            for (int j = 0; j < values.length; j++) {
                int x = values[j++];
                int y = values[j];
                int rgb = values[j];
//                Color color = new Color(rgb);
//                g.setColor(color);
//                g.drawLine(x, y, x, y);
//                g.fillRect(x, y, 1, 1);
                image.setRGB(x, y, rgb);
            }
            watch.stop();
        }
        
        ImageIO.write(image, "png", new File("test.png"));
        System.out.println(watch.getAverageMs() + " ms");
        
        
    }
}
