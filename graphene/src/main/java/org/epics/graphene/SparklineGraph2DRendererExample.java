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
 * @author asbarber, jkfeng, sjdallst
 */
public class SparklineGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            //Creates sample data
            double[] initialDataX = new double[100];
            for(int i = 20; i< 120; i+=1)
            {
                initialDataX[i-20]= Math.cos(Math.PI * 6 * i/100.0);
            }

            //Converts data array into dataset type
            Point2DDataset data = Point2DDatasets.lineData(initialDataX);
            
            //Image and Graphics initialization
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            
            //Graph initialization
            SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100,100, true);
            
            //Start time
            double startTime = System.currentTimeMillis();
            
            //Render
            renderer.draw(g, data);     
            
            //End time
            double finishTime = System.currentTimeMillis();
            
            //Debugs time spent on rendering
            System.out.println(finishTime-startTime);
            
            //Outputs image to file
            ImageIO.write(image, "png", new File("SparklineGraph.png"));
        }    
}
