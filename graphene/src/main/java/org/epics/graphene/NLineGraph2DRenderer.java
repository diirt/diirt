/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 * Renderer for a line graph.
 *
 * @author carcassi
 */
public class NLineGraph2DRenderer extends Graph2DRenderer<NLineGraph2DRendererUpdate> {

    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE);
    
    @Override
    public NLineGraph2DRendererUpdate newUpdate() {
        return new NLineGraph2DRendererUpdate();
    }

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;
    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
    // Pixel focus
    private Integer focusPixelX;
    
    private boolean highlightFocusValue = false;

    private int focusValueIndex = -1;
    
    /**
     * Creates a new line graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */
    public NLineGraph2DRenderer(int imageWidth, int imageHeight) {
        super(imageWidth, imageHeight);
    }

    /**
     * The current interpolation used for the line.
     * 
     * @return the current interpolation
     */
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }
    
    /**
     *Current state of highlightFocusValue.
     * <ul>
     *  <li>True - highlight and show the value the mouse is on.</li>
     *  <li>False - Avoid calculation involved with finding the highlighted value/ do not highlight the value.</li>
     * </ul>
     * @return true or false
     */
    public boolean isHighlightFocusValue() {
        return highlightFocusValue;
    }
    
    /**
     *Current index of the value that the mouse is focused on.
     * @return focused index (in the dataset).
     */
    public int getFocusValueIndex() {
        return focusValueIndex;
    }
    
    /**
     *Current x-position(pixel) of the value that the mouse is focused on.
     * @return the x position that the mouse is focused on in the graph (pixel).
     */
    public Integer getFocusPixelX() {
        return focusPixelX;
    }
    
    @Override
    public void update(NLineGraph2DRendererUpdate update) {
        super.update(update);
        if (update.getInterpolation() != null) {
            interpolation = update.getInterpolation();
        }
        if (update.getDataReduction() != null) {
            reduction = update.getDataReduction();
        }
        if (update.getFocusPixelX()!= null) {
            focusPixelX = update.getFocusPixelX();
        }
        if (update.getHighlightFocusValue()!= null) {
            highlightFocusValue = update.getHighlightFocusValue();
        }
    }

    /**
     * Draws the graph on the given graphics context.
     * 
     * @param g the graphics on which to display the data
     * @param data the data to display
     */
    public void draw(Graphics2D g, Point2DDataset data) {
        this.g = g;
        
        calculateRanges(data.getXStatistics(), data.getYStatistics());
        calculateLabels();
        calculateGraphArea();
        yAreaCoordStart = topMargin;
        yAreaCoordEnd = this.getImageHeight() -bottomMargin;
        yPlotCoordStart = yAreaCoordStart + topAreaMargin + yPointMargin;
        yPlotCoordEnd = yAreaCoordEnd - bottomAreaMargin - yPointMargin;
        yPlotCoordHeight = yPlotCoordEnd - yPlotCoordStart;
        drawBackground();
        drawGraphArea();
        
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());

        setClip(g);
        g.setColor(Color.BLACK);

        currentIndex = 0;
        currentScaledDiff = getImageWidth();
        drawValueExplicitLine(xValues, yValues, interpolation, reduction);
        if (focusPixelX != null) {
            focusValueIndex = xValues.getIndexes().getInt(currentIndex);
            if (highlightFocusValue) {
                g.setColor(new Color(0, 0, 0, 128));
                int x = (int) scaledX(xValues.getDouble(currentIndex));
                g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
                g.drawLine(x, yAreaCoordStart, x, yAreaCoordEnd - 1);
            }
        } else {
            focusValueIndex = -1;
        }
    }

    @Override
    protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
        if (focusPixelX != null) {
            double scaledDiff = Math.abs(scaledX - focusPixelX);
            if (scaledDiff < currentScaledDiff) {
                currentIndex = index;
                currentScaledDiff = scaledDiff;
            }
        }
    }
    
    private int currentIndex;
    private double currentScaledDiff;
    
    @Override
    protected void drawGraphArea() {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // When drawing the reference line, align them to the pixel
        drawVerticalReferenceLines();
        drawHorizontalReferenceLines();
        
        drawYLabels();
    }
    @Override
    protected void calculateGraphArea() {
        int yLabelMaxWidthThis;
        int[] yLabelWidths = new int[yReferenceLabels.size()];
        yLabelMaxWidthThis = 0;
        for (int i = 0; i < yLabelWidths.length; i++) {
            yLabelWidths[i] = labelFontMetrics.stringWidth(yReferenceLabels.get(i));
            yLabelMaxWidthThis = Math.max(yLabelMaxWidthThis, yLabelWidths[i]);
        }
        int areaFromBottom = bottomMargin;
        int areaFromLeft = leftMargin + yLabelMaxWidthThis + yLabelMargin;

        xPlotValueStart = getXPlotRange().getMinimum().doubleValue();
        xPlotValueEnd = getXPlotRange().getMaximum().doubleValue();
        if (xPlotValueStart == xPlotValueEnd) {
            // If range is zero, fake a range
            xPlotValueStart -= 1.0;
            xPlotValueEnd += 1.0;
        }
        xAreaCoordStart = areaFromLeft;
        xAreaCoordEnd = getImageWidth() - rightMargin;
        xPlotCoordStart = xAreaCoordStart + leftAreaMargin + xPointMargin;
        xPlotCoordEnd = xAreaCoordEnd - rightAreaMargin - xPointMargin;
        xPlotCoordWidth = xPlotCoordEnd - xPlotCoordStart;
        
        yPlotValueStart = getYPlotRange().getMinimum().doubleValue();
        yPlotValueEnd = getYPlotRange().getMaximum().doubleValue();
        if (yPlotValueStart == yPlotValueEnd) {
            // If range is zero, fake a range
            yPlotValueStart -= 1.0;
            yPlotValueEnd += 1.0;
        }
        yAreaCoordStart = topMargin;
        yAreaCoordEnd = getImageHeight() - areaFromBottom;
        yPlotCoordStart = yAreaCoordStart + topAreaMargin + yPointMargin;
        yPlotCoordEnd = yAreaCoordEnd - bottomAreaMargin - yPointMargin;
        yPlotCoordHeight = yPlotCoordEnd - yPlotCoordStart;
        
        //Only calculates reference coordinates if calculateLabels() was called
        if (xReferenceValues != null) {
            double[] xRefCoords = new double[xReferenceValues.size()];
            for (int i = 0; i < xRefCoords.length; i++) {
                xRefCoords[i] = scaledX(xReferenceValues.getDouble(i));
            }
            xReferenceCoords = new ArrayDouble(xRefCoords);
        }
        
        if (yReferenceValues != null) {
            double[] yRefCoords = new double[yReferenceValues.size()];
            for (int i = 0; i < yRefCoords.length; i++) {
                yRefCoords[i] = scaledY(yReferenceValues.getDouble(i));
            }
            yReferenceCoords = new ArrayDouble(yRefCoords);
        }
    }
}
