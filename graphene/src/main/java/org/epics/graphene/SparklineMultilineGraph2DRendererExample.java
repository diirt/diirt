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
public class SparklineMultilineGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            //Creates sample data
            double[][] initialDataX = new double[10][100];

            for(int x = 0; x < 100; x++){
                initialDataX[0][x] = 50*Math.cos(6.0*(x/100.0)*Math.PI); 
            }
            
            for(int x = 0; x < 100; x++){
                initialDataX[1][x] = 45*Math.cos(6.0*(x/100.0)*Math.PI-Math.PI/3); 
            }
            for(int x = 0; x < 100; x++){
                initialDataX[2][x] = 55*Math.cos(6.0*(x/100.0)*Math.PI-Math.PI/3*2);
            }
            
            /*for(int a = 0; a < initialDataX.length; a++){
                for(int b = 0; b < initialDataX[0].length; b++){
                    initialDataX[a][b] = a;
                }
            }*/
            
            
            Point2DDataset [] finalData = new Point2DDataset[initialDataX.length];
            //Converts data array into dataset type
            
            for(int a = 0; a < initialDataX.length; a++){
                finalData[a] = Point2DDatasets.lineData(initialDataX[a]);
            }
             
            //Image and Graphics initialization
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            
            //Graph initialization
            SparklineMultilineGraph2DRenderer renderer = new SparklineMultilineGraph2DRenderer(100,100);
            
            //Start time
            double startTime = System.currentTimeMillis();
            
            //Render
            renderer.draw(g, finalData);     
            
            //End time
            double finishTime = System.currentTimeMillis();
            
            //Debugs time spent on rendering
            System.out.println(finishTime-startTime);
            
            //Outputs image to file
            ImageIO.write(image, "png", new File("SparklineMultilineGraph.png"));
        }    
    
}
