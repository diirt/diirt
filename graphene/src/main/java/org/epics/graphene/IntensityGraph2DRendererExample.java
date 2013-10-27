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
import org.epics.util.array.*;

/**
 *
 * @author nitishp
 */
public class IntensityGraph2DRendererExample {
    public static void main(String[] args) throws IOException
        {
            /*Cell2DDataset data = Cell2DDatasets.linearRange(new Cell2DDatasets.Function2D() {
                
                @Override
                public double getValue(double x, double y) {
                    return x+y;
                }
            }, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            ImageIO.write(image, "png", new File("IntensityGraph.png"));*/
            double listOfData [] = new double[280*280];
            for(int i = 0; i < (140*140); i++){
                listOfData[i] = Math.random()*200;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 280), 280, RangeUtil.range(0, 280), 280);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            double startTime = System.currentTimeMillis();
            renderer.draw(g, data);
            double endTime = System.currentTimeMillis();
            System.out.println((endTime-startTime));
            ImageIO.write(image, "png", new File("IntensityGraph.png"));
        }
    }
