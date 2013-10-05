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
import java.math.*;

/**
 *
 * @author Aaron
 */
public class SparklineGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            double[] initialDataX = new double[100];
            for(double i = 0; i< 1; i+=.01)
            {
                initialDataX[(int)(i*100)]= Math.cos(Math.PI*i*2);
            }

            Point2DDataset data = Point2DDatasets.lineData(initialDataX);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(300,100, new String("Pounds"));
            double startTime = System.currentTimeMillis();
            renderer.draw(g, data);     
            double finishTime = System.currentTimeMillis();
            System.out.println(finishTime-startTime);
            ImageIO.write(image, "png", new File("SparklineGraph.png"));
        }    
}
