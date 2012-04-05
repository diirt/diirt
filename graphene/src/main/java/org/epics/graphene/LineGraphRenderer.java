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
public class LineGraphRenderer {
    private int width = 300;
    private int height = 200;

    public LineGraphRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public LineGraphRenderer() {
        this(300,200);
    }
    
    public void draw(Graphics2D g, OrderedDataset2D data) {
        int dataCount = data.getCount();
        double startX = data.getXMinValue();
        double startY = data.getYMinValue();
        double endX = data.getXMaxValue();
        double endY = data.getYMaxValue();
        int margin = 3;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        
        // Compute axis
        ValueAxis xAxis = ValueAxis.createAutoAxis(data.getXMinValue(), data.getXMaxValue(), Math.max(2, width / 60));
        ValueAxis yAxis = ValueAxis.createAutoAxis(data.getYMinValue(), data.getYMaxValue(), Math.max(2, height / 60));
        HorizontalAxisRenderer xAxisRenderer = new HorizontalAxisRenderer(xAxis, margin, g);
        VerticalAxisRenderer yAxisRenderer = new VerticalAxisRenderer(yAxis, margin, g);
        
        // Compute graph area
        int xStartGraph = yAxisRenderer.getAxisWidth();
        int xEndGraph = width - margin;
        int yStartGraph = margin;
        int yEndGraph = height - xAxisRenderer.getAxisHeight();
        int plotWidth = xEndGraph - xStartGraph;
        int plotHeight = yEndGraph - yStartGraph;
        
        // Draw axis
        xAxisRenderer.draw(g, 0, xStartGraph, xEndGraph, width, yEndGraph);
        yAxisRenderer.draw(g, 0, yStartGraph, yEndGraph, height, xStartGraph);
        
        
        double rangeX = endX - startX;
        double rangeY = endY - startY;
        
        // Scale data
        double[] scaledX = new double[dataCount];
        double[] scaledY = new double[dataCount];
        for (int i = 0; i < scaledY.length; i++) {
            scaledX[i] = xStartGraph + NumberUtil.scale(data.getXValue(i), startX, endX, plotWidth);
            scaledY[i] = height - xAxisRenderer.getAxisHeight() - NumberUtil.scale(data.getYValue(i), startY, endY, plotHeight);
        }
        Path2D path = cubicInterpolation(scaledX, scaledY);
        Path2D line = linearInterpolation(scaledX, scaledY);
        Path2D nearest = nearestNeighbour(scaledX, scaledY);

        //g.drawLine(0, 0, 30, 30);
        //g.draw(path);
        //g.draw(line);
        g.draw(nearest);
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
