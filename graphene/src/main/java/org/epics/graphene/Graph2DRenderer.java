/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.List;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public abstract class Graph2DRenderer<T extends Graph2DRendererUpdate> {
    protected double endXPlot;
    protected double endYPlot;
    protected int plotHeight;
    protected int plotWidth;
    protected double startXPlot;
    protected double startYPlot;
    protected int xEndGraph;
    protected int xStartGraph;
    protected int yEndGraph;
    protected int yStartGraph;
    private ListInt verticalTickPositions;

    public Graph2DRenderer(int imageWidth, int imageHeight) {
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
    private Range xAggregatedRange;
    private Range yAggregatedRange;
    private Range xPlotRange;
    private Range yPlotRange;

    public AxisRange getXAxisRange() {
        return xAxisRange;
    }

    public AxisRange getYAxisRange() {
        return yAxisRange;
    }

    public Range getXAggregatedRange() {
        return xAggregatedRange;
    }

    public Range getYAggregatedRange() {
        return yAggregatedRange;
    }

    public Range getXPlotRange() {
        return xPlotRange;
    }

    public Range getYPlotRange() {
        return yPlotRange;
    }

    public void update(T update) {
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
    
    static Range aggregateRange(Range dataRange, Range aggregatedRange) {
        if (aggregatedRange == null) {
            return dataRange;
        } else {
            return RangeUtil.sum(dataRange, aggregatedRange);
        }
    }
    
    public abstract T newUpdate();
    
    protected void calculateRanges(Range xDataRange, Range yDataRange) {
        xAggregatedRange = aggregateRange(xDataRange, xAggregatedRange);
        yAggregatedRange = aggregateRange(yDataRange, yAggregatedRange);
        xPlotRange = xAxisRange.axisRange(xDataRange, xAggregatedRange);
        yPlotRange = yAxisRange.axisRange(yDataRange, yAggregatedRange);
    }

    protected void drawAxis(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        // Determine range of the plot.
        // If no range is set, use the one from the dataset
        startXPlot = getXPlotRange().getMinimum().doubleValue();
        startYPlot = getYPlotRange().getMinimum().doubleValue();
        endXPlot = getXPlotRange().getMaximum().doubleValue();
        endYPlot = getYPlotRange().getMaximum().doubleValue();
        int margin = 3;
        // Compute axis
        ValueAxis xAxis = ValueAxis.createAutoAxis(startXPlot, endXPlot, Math.max(2, getImageWidth() / 60));
        ValueAxis yAxis = ValueAxis.createAutoAxis(startYPlot, endYPlot, Math.max(2, getImageHeight() / 60));
        HorizontalAxisRenderer xAxisRenderer = new HorizontalAxisRenderer(xAxis, margin, g);
        yAxisRenderer = new VerticalAxisRenderer(yAxis, margin, g);
        // Compute graph area
        xStartGraph = yAxisRenderer.getAxisWidth();
        xEndGraph = getImageWidth() - margin;
        yStartGraph = margin;
        yEndGraph = getImageHeight() - xAxisRenderer.getAxisHeight();
        plotWidth = xEndGraph - xStartGraph;
        plotHeight = yEndGraph - yStartGraph;
        // Draw axis
        xAxisRenderer.draw(g, 0, xStartGraph, xEndGraph, getImageWidth(), yEndGraph);
        yAxisRenderer.draw(g, 0, yStartGraph, yEndGraph, getImageHeight(), xStartGraph);
        // Draw reference lines
        g.setColor(new Color(240, 240, 240));
        int[] xTicks = xAxisRenderer.horizontalTickPositions();
        for (int xTick : xTicks) {
            g.drawLine(xTick, yStartGraph, xTick, yEndGraph);
        }
        int[] yTicks = yAxisRenderer.verticalTickPositions();
        for (int yTick : yTicks) {
            g.drawLine(xStartGraph, getImageHeight() - yTick, xEndGraph, getImageHeight() - yTick);
        }
    }
    
    private VerticalAxisRenderer yAxisRenderer;
    
    protected void drawHorizontalReferenceLines(Graphics2D g) {
        int[] yTicks = yAxisRenderer.verticalTickPositions();
        for (int yTick : yTicks) {
            g.drawLine(xStartGraph, getImageHeight() - yTick, xEndGraph, getImageHeight() - yTick);
        }
    }
    
    protected Color backgroundColor = Color.WHITE;
    protected Range xCoordRange;
    protected Range yCoordRange;
    protected Color labelColor = Color.BLACK;
    protected Font labelFont = FontUtil.getLiberationSansRegular();
    protected double xLabelMargin = 3;
    protected double yLabelMargin = 3;
    protected ListDouble xReferenceCoords;
    protected ListDouble xReferenceValues;
    protected List<String> xReferenceLabels;
    protected ListDouble yReferenceCoords;
    protected ListDouble yReferenceValues;
    protected List<String> yReferenceLabels;
    protected Graphics2D g;
    
    protected void drawGraphArea() {
        // Draw background
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getImageWidth(), getImageHeight());
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw reference lines
        // When drawing the reference line, align them to the pixel
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setColor(new Color(240, 240, 240));
        ListNumber xTicks = xReferenceCoords;
        for (int i = 0; i < xTicks.size(); i++) {
            Shape line = new Line2D.Double(xTicks.getDouble(i), yCoordRange.getMinimum().doubleValue(), xTicks.getDouble(i), yCoordRange.getMaximum().doubleValue());
            g.draw(line);
        }
        ListNumber yTicks = yReferenceCoords;
        for (int i = 0; i < yTicks.size(); i++) {
            Shape line = new Line2D.Double(xCoordRange.getMinimum().doubleValue(), yTicks.getDouble(i), xCoordRange.getMaximum().doubleValue(), yTicks.getDouble(i));
            g.draw(line);
        }

        // Draw Y labels
        if (yReferenceLabels != null && !yReferenceLabels.isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(labelColor);
            g.setFont(labelFont);
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {(int) yCoordRange.getMinimum().intValue(), (int) yCoordRange.getMaximum().intValue()};
            int xRightLabel = (int) (xCoordRange.getMinimum().doubleValue() - yLabelMargin - 1);
            drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(0), (int) Math.floor(yTicks.getDouble(0)),
                drawRange, xRightLabel, true, false);
            drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(yReferenceLabels.size() - 1), (int) Math.floor(yTicks.getDouble(yReferenceLabels.size() - 1)),
                drawRange, xRightLabel, false, false);
            
            for (int i = 1; i < yReferenceLabels.size() - 1; i++) {
                drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(i), (int) Math.floor(yTicks.getDouble(i)),
                    drawRange, xRightLabel, true, false);
            }
        }
        
        // Draw X labels
        if (xReferenceLabels != null && !xReferenceLabels.isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(labelColor);
            g.setFont(labelFont);
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {(int) xCoordRange.getMinimum().intValue(), (int) xCoordRange.getMaximum().intValue()};
            int yTop = (int) (yCoordRange.getMinimum().doubleValue() + xLabelMargin + 1);
            drawVerticalReferenceLabel(g, metrics, xReferenceLabels.get(0), (int) Math.floor(xTicks.getDouble(0)),
                drawRange, yTop, true, false);
            drawVerticalReferenceLabel(g, metrics, xReferenceLabels.get(xReferenceLabels.size() - 1), (int) Math.floor(xTicks.getDouble(xReferenceLabels.size() - 1)),
                drawRange, yTop, false, false);
            
            for (int i = 1; i < xReferenceLabels.size() - 1; i++) {
                drawVerticalReferenceLabel(g, metrics, xReferenceLabels.get(i), (int) Math.floor(xTicks.getDouble(i)),
                    drawRange, yTop, true, false);
            }
        }
    }
    
    private static final int MIN = 0;
    private static final int MAX = 1;
    
    private static void drawHorizontalReferencesLabel(Graphics2D graphics, FontMetrics metrics, String text, int yCenter, int[] drawRange, int xRight, boolean updateMin, boolean centeredOnly) {
        // If the center is not in the range, don't draw anything
        if (drawRange[MAX] < yCenter || drawRange[MIN] > yCenter)
            return;
        
        // If there is no space, don't draw anything
        if (drawRange[MAX] - drawRange[MIN] < metrics.getHeight())
            return;
        
        Java2DStringUtilities.Alignment alignment = Java2DStringUtilities.Alignment.RIGHT;
        int targetY = yCenter;
        int halfHeight = metrics.getAscent() / 2;
        if (yCenter < drawRange[MIN] + halfHeight) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.TOP_RIGHT;
            targetY = drawRange[MIN];
        } else if (yCenter > drawRange[MAX] - halfHeight) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;
            targetY = drawRange[MAX];
        }

        Java2DStringUtilities.drawString(graphics, alignment, xRight, targetY, text);
        
        if (updateMin) {
            drawRange[MAX] = targetY - metrics.getHeight();
        } else {
            drawRange[MIN] = targetY + metrics.getHeight();
        }
    }
    
    private static void drawVerticalReferenceLabel(Graphics2D graphics, FontMetrics metrics, String text, int xCenter, int[] drawRange, int yTop, boolean updateMin, boolean centeredOnly) {
        // If the center is not in the range, don't draw anything
        if (drawRange[MAX] < xCenter || drawRange[MIN] > xCenter)
            return;
        
        // If there is no space, don't draw anything
        if (drawRange[MAX] - drawRange[MIN] < metrics.getHeight())
            return;
        
        Java2DStringUtilities.Alignment alignment = Java2DStringUtilities.Alignment.TOP;
        int targetX = xCenter;
        int halfWidth = metrics.stringWidth(text) / 2;
        if (xCenter < drawRange[MIN] + halfWidth) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.TOP_LEFT;
            targetX = drawRange[MIN];
        } else if (xCenter > drawRange[MAX] - halfWidth) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;
            targetX = drawRange[MAX];
        }

        Java2DStringUtilities.drawString(graphics, alignment, targetX, yTop, text);
        
        if (updateMin) {
            drawRange[MIN] = targetX + metrics.getHeight();
        } else {
            drawRange[MAX] = targetX - metrics.getHeight();
        }
    }
    

    protected void drawBackground(Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getImageWidth(), getImageHeight());
    }

    protected final double scaledX(double value) {
        return xStartGraph + NumberUtil.scale(value, startXPlot, endXPlot, plotWidth);
    }

    protected final double scaledY(double value) {
        return yEndGraph - NumberUtil.scale(value, startYPlot, endYPlot, plotHeight);
    }
    
    protected void setClip(Graphics2D g) {
        // Make sure that the line does not go ouside the chart
        g.setClip(xStartGraph - 1, yStartGraph - 1, plotWidth + 2, plotHeight + 2);
    }

}
