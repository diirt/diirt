/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Aaron
 */
public class SparklineGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            double[] initialDataX = new double[100];
            for(int i = 0; i< 100; i++)
            {
                initialDataX[i] = i;
            }
            double[] initialDataY = new double[100];
            for(int i = 0; i< 100; i++)
            {
                initialDataY[i] = i;
            }
            Point2DDataset data = Point2DDatasets.lineData(initialDataX);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(640,100, new String("Pounds"));
            renderer.draw(g, data);                                          //640,480
            ImageIO.write(image, "png", new File("SparklineGraph.png"));
        }    
}
