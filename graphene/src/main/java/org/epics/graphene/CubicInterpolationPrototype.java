/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author carcassi
 */
public class CubicInterpolationPrototype {
    public static void main(String[] args) throws Exception {
        // Given data
        double[] dataY = new double[] {0.1,0.15,0.6,0.3,0.7,0.5,0.55};
        double[] dataX = new double[] {1,2,3,4,7,8,9};
        //double[] dataY = new double[] {0.1,0.6,0.7,0.3};
        //double[] dataX = new double[] {1,3,7,9};
        double startX = 0;
        double startY = 0;
        double endX = 10;
        double endY = 1;
        int width = 600;
        int height = 400;

        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 600, 400);
        g.setColor(Color.BLACK);
        
        double rangeX = endX - startX;
        double rangeY = endY - startY;
        
        // Scale data
        double[] scaledX = new double[dataX.length];
        double[] scaledY = new double[dataY.length];
        for (int i = 0; i < scaledY.length; i++) {
            scaledX[i] = dataX[i] * width / rangeX;
            scaledY[i] = height - dataY[i] * height / rangeY;
        }
        Path2D path = cubicInterpolation(scaledX, scaledY);
        Path2D line = linearInterpolation(scaledX, scaledY);
        Path2D nearest = nearestNeighbour(scaledX, scaledY);
        
        //g.drawLine(0, 0, 30, 30);
        g.draw(path);
        //g.draw(line);
        //g.draw(nearest);
        ImageIO.write(image, "png", new File("test.png"));
    }

    private static Double nearestNeighbour(double[] scaledX, double[] scaledY) {
        Path2D.Double line = new Path2D.Double();
        line.moveTo(scaledX[0], scaledY[0]);
        for (int i = 1; i < scaledY.length; i++) {
            double halfX = scaledX[i-1] + (scaledX[i] - scaledX[i-1]) / 2;
            line.lineTo(halfX, scaledY[i - 1]);
            line.lineTo(halfX, scaledY[i]);
        }
        line.lineTo(scaledX[scaledX.length - 1], scaledY[scaledY.length - 1]);
        return line;
    }

    private static Double linearInterpolation(double[] scaledX, double[] scaledY) {
        Path2D.Double line = new Path2D.Double();
        line.moveTo(scaledX[0], scaledY[0]);
        for (int i = 1; i < scaledY.length; i++) {
            line.lineTo(scaledX[i], scaledY[i]);
        }
        return line;
    }

    private static Double cubicInterpolation(double[] scaledX, double[] scaledY) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(scaledX[0], scaledY[0]);
        for (int i = 1; i < scaledY.length; i++) {
            // Extract 4 points (take care of boundaries)
            double y1 = scaledY[i - 1];
            double y2 = scaledY[i];
            double x1 = scaledX[i - 1];
            double x2 = scaledX[i];
            double y0;
            double x0;
            if (i > 1) {
                y0 = scaledY[i - 2];
                x0 = scaledX[i - 2];
            } else {
                y0 = y1 - (y2 - y1) / 2;
                x0 = x1 - (x2 - x1);
            }
            double y3;
            double x3;
            if (i < scaledY.length - 1) {
                y3 = scaledY[i + 1];
                x3 = scaledX[i + 1];
            } else {
                y3 = y2 + (y2 - y1) / 2;
                x3 = x2 + (x2 - x1) / 2;
            }
            
            // Convert to Bezier
            double bx0 = x1;
            double by0 = y1;
            double bx3 = x2;
            double by3 = y2;
            double bdy0 = (y2 - y0) / (x2 - x0);
            double bdy3 = (y3 - y1) / (x3 - x1);
            double bx1 = bx0 + (x2 - x0) / 6.0;
            double by1 = (bx1 - bx0) * bdy0 + by0;
            double bx2 = bx3 - (x3 - x1) / 6.0;
            double by2 = (bx2 - bx3) * bdy3 + by3;
            
            path.curveTo(bx1, by1, bx2, by2, bx3, by3);
        }
        return path;
    }
}
