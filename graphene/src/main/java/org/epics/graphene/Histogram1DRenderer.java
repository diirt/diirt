/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author carcassi
 */
public class Histogram1DRenderer {
    
    private double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public void draw(Graphics2D graphics, Histogram1D hist) {
        int imageWidth = hist.getImageWidth();
        int imageHeight = hist.getImageHeight();
        
        Color backgroundColor = Color.WHITE;
        Color axisTextColor = Color.BLACK;
        Color axisColor = Color.BLACK;
        Color dividerColor = new Color(196, 196, 196);
        Color lineColor = new Color(140, 140, 140);
        Color histogramColor = new Color(175, 175, 175);

        double xValueMin = hist.getMinValueRange();
        double xValueMax = hist.getMaxValueRange();
        double[] xValueTicks = RangeUtil.ticksForRange(xValueMin, xValueMax, imageWidth / 60);
        
        int yValueMin = hist.getMinCountRange();
        int yValueMax = hist.getMaxCountRange();
        // In bigger plots, too many horizonal lines make it too confusing,
        // so distance between each vertical ticks is higher at smaller heights
        // and smaller at higher heights.
        int nYTicks = 0;
        if (imageHeight < 80) {
            nYTicks = 2;
        } else if (imageHeight < 360) {
            nYTicks = 6;
        } else {
            nYTicks = imageHeight / 60;
        }
        double[] yValueTicks = RangeUtil.ticksForRange(yValueMin, yValueMax, Math.max(4, nYTicks), 1.0);

        // Create labels
        String[] xLabels = new String[xValueTicks.length];
        for (int i = 0; i < xLabels.length; i++) {
            xLabels[i] = Double.toString(xValueTicks[i]);
        }
        String[] yLabels = new String[yValueTicks.length];
        for (int i = 0; i < yLabels.length; i++) {
            yLabels[i] = Integer.toString((int) yValueTicks[i]);
        }
        Font axisFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        graphics.setFont(axisFont);
        
        double[] binLimits = new double[] {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0,
            1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
        int[] binValues = new int[] {30, 14, 150, 160, 180, 230, 220, 350, 400, 450, 500,
            350, 230, 180, 220, 170, 130, 80, 30, 40};
        
        // Compute axis size
        
        int margin = 3; // 3 px of margin all around
        int axisMargin = 3; // 3 px of margin all around
        int xAxisTickSize = 3;
        FontMetrics metrics = graphics.getFontMetrics();
        
        // Compute x axis spacing
        int[] xLabelWidths = new int[xLabels.length];
        for (int i = 0; i < xLabelWidths.length; i++) {
            xLabelWidths[i] = metrics.stringWidth(xLabels[i]);
        }
        int xAxisFromBottom = margin + metrics.getHeight() - metrics.getLeading() + axisMargin;
        
        // Compute y axis spacing
        int[] yLabelWidths = new int[yLabels.length];
        int yLargestLabel = 0;
        for (int i = 0; i < yLabelWidths.length; i++) {
            yLabelWidths[i] = metrics.stringWidth(yLabels[i]);
            yLargestLabel = Math.max(yLargestLabel, yLabelWidths[i]);
        }
        int yAxisFromLeft = margin + yLargestLabel + axisMargin;
        
        // Compute plot size
        
        int plotWidth = imageWidth - yAxisFromLeft - margin;
        int plotHeight = imageHeight - xAxisFromBottom - margin;
        
        // Compute ticks
        int[] xTicks = new int[xLabels.length];
        for (int i = 0; i < xTicks.length; i++) {
            xTicks[i] = yAxisFromLeft + (int) (normalize(xValueTicks[i], xValueMin, xValueMax) * plotWidth);
        }
        int[] yTicks = new int[yLabels.length];
        for (int i = 0; i < yTicks.length; i++) {
            yTicks[i] = xAxisFromBottom + (int) (normalize(yValueTicks[i], yValueMin, yValueMax) * plotHeight);
        }
        
        // Compute bin limits
        int[] binLimitsPx = new int[binLimits.length];
        int[] binHeightsPx = new int[binValues.length];
        
        for (int i = 0; i < binValues.length; i++) {
            binLimitsPx[i] = yAxisFromLeft + (int) (normalize(binLimits[i], xValueMin, xValueMax) * plotWidth);
            binHeightsPx[i] = (int) (normalize(binValues[i], yValueMin, yValueMax) * plotHeight);
        }
        binLimitsPx[binLimits.length - 1] = yAxisFromLeft + (int) (normalize(binLimits[binLimits.length - 1], xValueMin, xValueMax) * plotWidth);

        // Draw background
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, imageWidth, imageHeight);

        // Draw x-axis
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(axisColor);
        graphics.drawLine(yAxisFromLeft, imageHeight - xAxisFromBottom, yAxisFromLeft + plotWidth, imageHeight - xAxisFromBottom);
        for (int i = 0; i < xLabels.length; i++) {
            Rectangle2D bounds = metrics.getStringBounds(xLabels[i], graphics);
            graphics.setColor(axisTextColor);
            graphics.drawString(xLabels[i], xTicks[i] - ((int) ((bounds.getWidth() / 2))), imageHeight - margin);
            graphics.setColor(axisColor);
            graphics.drawLine(xTicks[i], imageHeight - xAxisFromBottom, xTicks[i], imageHeight - xAxisFromBottom + xAxisTickSize);
        }

        // Draw y-axis
        graphics.setColor(axisTextColor);
        for (int i = 0; i < yLabels.length; i++) {
            int halfHeight = (metrics.getAscent()) / 2 - 1;
            graphics.drawString(yLabels[i], yAxisFromLeft - yLabelWidths[i] - axisMargin, imageHeight - yTicks[i] + halfHeight);
        }
        
        // Draw histogram area
        for (int i = 0; i < binValues.length; i++) {
            graphics.setColor(histogramColor);
            graphics.fillRect(binLimitsPx[i], imageHeight - xAxisFromBottom - binHeightsPx[i], binLimitsPx[i+1] - binLimitsPx[i], binHeightsPx[i]);
            graphics.setColor(dividerColor);
            graphics.drawLine(binLimitsPx[i], imageHeight - xAxisFromBottom - binHeightsPx[i], binLimitsPx[i], imageHeight - xAxisFromBottom - 1);
        }
        
        for (int i = 0; i < yTicks.length; i++) {
            if (yTicks[i] != xAxisFromBottom) {
                graphics.setColor(backgroundColor);
                graphics.drawLine(yAxisFromLeft, imageHeight - yTicks[i], yAxisFromLeft + plotWidth, imageHeight - yTicks[i]);
            }
        }
        
        int previousHeight = 0;
        for (int i = 0; i < binValues.length; i++) {
            graphics.setColor(lineColor);
            graphics.drawLine(binLimitsPx[i], imageHeight - xAxisFromBottom - previousHeight, binLimitsPx[i], imageHeight - xAxisFromBottom - binHeightsPx[i]);
            graphics.drawLine(binLimitsPx[i], imageHeight - xAxisFromBottom - binHeightsPx[i], binLimitsPx[i+1], imageHeight - xAxisFromBottom - binHeightsPx[i]);
            previousHeight = binHeightsPx[i];
        }
        graphics.drawLine(binLimitsPx[binLimitsPx.length - 1], imageHeight - xAxisFromBottom - previousHeight, binLimitsPx[binLimitsPx.length - 1], imageHeight - xAxisFromBottom);
        
        
        
    }
}
