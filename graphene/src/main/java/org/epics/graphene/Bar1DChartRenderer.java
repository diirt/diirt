/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static org.epics.graphene.NumberUtil.normalize;

/**
 *
 * @author carcassi
 */
public class Bar1DChartRenderer {

    public Bar1DChartRenderer(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    private int imageWidth;
    private int imageHeight;

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }
    
    private AxisRange xAxisRange = AxisRanges.integrated();
    private AxisRange yAxisRange = AxisRanges.integrated();

    public void update(Bar1DChartRendererUpdate update) {
        if (update.getImageHeight() != null) {
            imageHeight = update.getImageHeight();
        }
        if (update.getImageWidth() != null) {
            imageWidth = update.getImageWidth();
        }
        if (update.getXAxisRange() != null) {
            xAxisRange = update.getXAxisRange();
        }
        if (update.getYAxisRange() != null) {
            yAxisRange = update.getYAxisRange();
        }
    }

    public void draw(Graphics2D graphics, Cell1DDataset dataset) {
        int imageWidth = this.getImageWidth();
        int imageHeight = this.getImageHeight();
        
        Color backgroundColor = Color.WHITE;
        Color axisTextColor = Color.BLACK;
        Color axisColor = Color.BLACK;
        Color dividerColor = new Color(196, 196, 196);
        Color lineColor = new Color(140, 140, 140);
        Color histogramColor = new Color(175, 175, 175);
        
        int margin = 3; // 3 px of margin all around
        int axisMargin = 3; // 3 px of margin all around
        int xAxisTickSize = 3;
        
        Range xRange = xAxisRange.axisRange(dataset.getXRange());
        Range yRange = yAxisRange.axisRange(dataset.getStatistics());
                
        double xValueMin = xRange.getMinimum().doubleValue();
        double xValueMax = xRange.getMaximum().doubleValue();
        ValueAxis xAxis = ValueAxis.createAutoAxis(xValueMin, xValueMax, imageWidth / 60);
        HorizontalAxisRenderer xAxisRenderer = new HorizontalAxisRenderer(xAxis, margin, graphics);
        
        double yValueMin = yRange.getMinimum().doubleValue();
        double yValueMax = yRange.getMaximum().doubleValue();
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
        ValueAxis yAxis = ValueAxis.createAutoAxis(yValueMin, yValueMax, Math.max(4, nYTicks), 1.0);
        VerticalAxisRenderer yAxisRenderer = new VerticalAxisRenderer(yAxis, margin, graphics);
        
        // Compute plot size
        
        int plotWidth = imageWidth - yAxisRenderer.getAxisWidth() - margin;
        int plotHeight = imageHeight - xAxisRenderer.getAxisHeight() - margin;
        
        // Compute bin limits
        int[] binLimitsPx = new int[dataset.getXCount() + 1];
        int[] binHeightsPx = new int[dataset.getXCount()];
        
        for (int i = 0; i < dataset.getXCount(); i++) {
            binLimitsPx[i] = yAxisRenderer.getAxisWidth() + (int) (normalize(dataset.getXBoundaries().getDouble(i), xValueMin, xValueMax) * plotWidth);
            binHeightsPx[i] = (int) (normalize(dataset.getValue(i), yValueMin, yValueMax) * plotHeight);
        }
        binLimitsPx[dataset.getXCount()] = yAxisRenderer.getAxisWidth() + (int) (normalize(dataset.getXBoundaries().getDouble(dataset.getXCount()), xValueMin, xValueMax) * plotWidth);

        // Draw background
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw axis
        xAxisRenderer.draw(graphics, 0, yAxisRenderer.getAxisWidth(), imageWidth - margin, imageWidth, imageHeight - xAxisRenderer.getAxisHeight());
        yAxisRenderer.draw(graphics, 0, margin, imageHeight - xAxisRenderer.getAxisHeight(), imageHeight, yAxisRenderer.getAxisWidth());
        int[] yTicks = yAxisRenderer.verticalTickPositions();
        
        // Draw histogram area
        for (int i = 0; i < binHeightsPx.length; i++) {
            graphics.setColor(histogramColor);
            graphics.fillRect(binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - binHeightsPx[i], binLimitsPx[i+1] - binLimitsPx[i], binHeightsPx[i]);
            graphics.setColor(dividerColor);
            // Draw the divider only if the vertical size is more than 0
            if (binHeightsPx[i] > 0)
                graphics.drawLine(binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - binHeightsPx[i], binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - 1);
        }
        
        // Draw horizontal reference lines
        for (int i = 0; i < yTicks.length; i++) {
            if (yTicks[i] != xAxisRenderer.getAxisHeight()) {
                graphics.setColor(backgroundColor);
                graphics.drawLine(yAxisRenderer.getAxisWidth(), imageHeight - yTicks[i], yAxisRenderer.getAxisWidth() + plotWidth, imageHeight - yTicks[i]);
            }
        }
        
        // Draw histogram contour
        int previousHeight = 0;
        for (int i = 0; i < binHeightsPx.length; i++) {
            graphics.setColor(lineColor);
            // Draw the countour only when the height is not 0
            if (binHeightsPx[i] > 0 || previousHeight > 0) {
                graphics.drawLine(binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - Math.max(previousHeight, 1), binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - Math.max(binHeightsPx[i], 1));
            }
            if (binHeightsPx[i] > 0) {
                graphics.drawLine(binLimitsPx[i], imageHeight - xAxisRenderer.getAxisHeight() - binHeightsPx[i], binLimitsPx[i+1], imageHeight - xAxisRenderer.getAxisHeight() - binHeightsPx[i]);
            }
            previousHeight = binHeightsPx[i];
        }
        if (previousHeight > 0)
            graphics.drawLine(binLimitsPx[binLimitsPx.length - 1], imageHeight - xAxisRenderer.getAxisHeight() - previousHeight, binLimitsPx[binLimitsPx.length - 1], imageHeight - xAxisRenderer.getAxisHeight() - 1);
        
        
        
    }
    
    private static final int MIN = 0;
    private static final int MAX = 1;
    private static final int marginBetweenXLabels = 4;
    private static void drawCenteredText(Graphics2D graphics, FontMetrics metrics, String text, int center, int[] drawRange, int y, boolean updateMin, boolean centeredOnly) {
        // If the center is not in the range, don't draw anything
        if (drawRange[MAX] < center || drawRange[MIN] > center)
            return;
        
        double width = metrics.getStringBounds(text, graphics).getWidth();
        // If there is no space, don't draw anything
        if (drawRange[MAX] - drawRange[MIN] < width)
            return;
        
        int targetX = center - (int) ((width / 2));
        if (targetX < drawRange[MIN]) {
            if (centeredOnly)
                return;
            targetX = drawRange[MIN];
        } else if (targetX + width > drawRange[MAX]) {
            if (centeredOnly)
                return;
            targetX = drawRange[MAX] - (int) width;
        }
        
        graphics.drawString(text, targetX, y);
        
        if (updateMin) {
            drawRange[MIN] = targetX + (int) width + marginBetweenXLabels;
        } else {
            drawRange[MAX] = targetX - marginBetweenXLabels;
        }
    }
}
