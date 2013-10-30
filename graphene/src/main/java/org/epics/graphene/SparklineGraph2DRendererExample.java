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
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class SparklineGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            //Creates sample data
            double[] initialDataX = new double[100];

            for(int x = 0; x < 33; x++){
                initialDataX[x] = 1;
            }
            for (int x = 33; x < 67; x++){
                initialDataX[x] = -1;
            }
            for (int x = 67; x < 100; x++){
                initialDataX[x] = 0;
            }
            
            //Converts data array into dataset type
            Point2DDataset data = Point2DDatasets.lineData(initialDataX);
            
            //Image and Graphics initialization
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            
            //Graph initialization
            SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(100, 100);
            
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
