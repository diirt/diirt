/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 * Renderer for a line graph with multiple y axes.
 *
 * @author carcassi, sjdallst
 */
public class MultiYAxisGraph2DRenderer extends Graph2DRenderer<MultiYAxisGraph2DRendererUpdate> {

    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE);
    
    @Override
    public MultiYAxisGraph2DRendererUpdate newUpdate() {
        return new MultiYAxisGraph2DRendererUpdate();
    }

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;
    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
    // Pixel focus
    private Integer focusPixelX;
    
    private boolean highlightFocusValue = false;

    private List<ListDouble> yReferenceCoords;
    private List<ListDouble> yReferenceValues;
    private List<List<String>> yReferenceLabels;
    private Range emptyRange;
    private AxisRange xAxisRange = AxisRanges.integrated();
    private AxisRange yAxisRange = AxisRanges.integrated();
    private ValueScale xValueScale = ValueScales.linearScale();
    private ValueScale yValueScale = ValueScales.linearScale();
    private Range xAggregatedRange;
    private List<Range> yAggregatedRange;
    private Range xPlotRange;
    private List<Range> yPlotRange;
    private HashMap<Integer, Range> indexToRangeMap = new HashMap<Integer,Range>();
    private int focusValueIndex = -1;
    private int numGraphs;
    private int spaceForYAxes;
    private int minimumGraphWidth = 200;
    private int yLabelMaxWidth;
    private int xLabelMaxHeight;
    private ColorScheme lineScheme = ColorScheme.JET;
    private ValueColorScheme lineValueScheme;
    
    private double xPlotValueStart;
    private List<Double> yPlotValueStart;
    private double xPlotValueEnd;
    private List<Double> yPlotValueEnd;
    /**
     * Creates a new line graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */
    public MultiYAxisGraph2DRenderer(int imageWidth, int imageHeight) {
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
    public void update(MultiYAxisGraph2DRendererUpdate update) {
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
    public void draw(Graphics2D g, List<Point2DDataset> data) {
        this.g = g;
        
        List<Range> dataRangesX = new ArrayList<Range>();
        for(int i = 0; i < data.size(); i++){
            dataRangesX.add(data.get(i).getXStatistics());
        }
        List<Range> dataRangesY = new ArrayList<Range>();
        for(int i = 0; i < data.size(); i++){
            dataRangesY.add(data.get(i).getYStatistics());
        }
        getNumGraphs(data);
        Range datasetRange = RangeUtil.range(0,numGraphs);
        lineValueScheme = ValueColorSchemes.schemeFor(lineScheme, datasetRange);
        calculateRanges(dataRangesX, dataRangesY, numGraphs);
        calculateLabels();
        calculateGraphArea();        
        drawBackground();
        drawGraphArea();

    }
    
    private void getNumGraphs(List<Point2DDataset> data){
            numGraphs = data.size();
            while((double)getImageWidth() - spaceForYAxes < minimumGraphWidth){
                numGraphs-=1;
            }
    }
    
    protected void calculateRanges(List<Range> xDataRange, List<Range> yDataRange, int length) {
        for(int i = 0; i < length; i++){
            xAggregatedRange = aggregateRange(xDataRange.get(i), xAggregatedRange);
            xPlotRange = xAxisRange.axisRange(xDataRange.get(i), xAggregatedRange);
        }  
        if(yAggregatedRange == null || yDataRange.size() != yAggregatedRange.size()){
            yAggregatedRange = new ArrayList<Range>();
            yPlotRange = new ArrayList<Range>();
            for(int i = 0; i < length; i++){
                if(indexToRangeMap.isEmpty() || !indexToRangeMap.containsKey(i)){
                    yAggregatedRange.add(aggregateRange(yDataRange.get(i), emptyRange));
                    yPlotRange.add(yAxisRange.axisRange(yDataRange.get(i), yAggregatedRange.get(i)));
                }
                else{
                    if(indexToRangeMap.containsKey(i)){
                        yAggregatedRange.add(aggregateRange(yDataRange.get(i), emptyRange));
                        yPlotRange.add(indexToRangeMap.get(i));
                    }
                }
            }
        }
        else{
            for(int i = 0; i < length; i++){
                if(indexToRangeMap.isEmpty() || !indexToRangeMap.containsKey(i)){
                    yAggregatedRange.set(i,aggregateRange(yDataRange.get(i), yAggregatedRange.get(i)));
                    yPlotRange.set(i,yAxisRange.axisRange(yDataRange.get(i), yAggregatedRange.get(i)));
                }
                else{
                    if(indexToRangeMap.containsKey(i)){
                        yPlotRange.set(i,indexToRangeMap.get(i));
                    }
                }
            }
        }
    }
    
    @Override
    protected void calculateLabels() {
        // Calculate horizontal axis references. If range is zero, use special logic
        if (!xPlotRange.getMinimum().equals(xPlotRange.getMaximum())) {
            ValueAxis xAxis = xValueScale.references(xPlotRange, 2, Math.max(2, getImageWidth() / 60));
            xReferenceLabels = Arrays.asList(xAxis.getTickLabels());
            xReferenceValues = new ArrayDouble(xAxis.getTickValues());            
        } else {
            // TODO: use something better to format the number
            xReferenceLabels = Collections.singletonList(xPlotRange.getMinimum().toString());
            xReferenceValues = new ArrayDouble(xPlotRange.getMinimum().doubleValue());            
        }      
        
        // Calculate vertical axis references. If range is zero, use special logic
        if(yReferenceLabels == null || yReferenceLabels.size() != numGraphs){
            yReferenceLabels = new ArrayList<List<String>>();
            yReferenceValues = new ArrayList<ListDouble>();
            for(int i = 0; i < yPlotRange.size(); i++){
                if (!yPlotRange.get(i).getMinimum().equals(yPlotRange.get(i).getMaximum())) {
                    ValueAxis yAxis = yValueScale.references(yPlotRange.get(i), 2, Math.max(2, getImageHeight() / 60));
                    yReferenceLabels.add(Arrays.asList(yAxis.getTickLabels()));
                    yReferenceValues.add(new ArrayDouble(yAxis.getTickValues()));            
                } else {
                    // TODO: use something better to format the number
                    yReferenceLabels.add(Collections.singletonList(yPlotRange.get(i).getMinimum().toString()));
                    yReferenceValues.add(new ArrayDouble(yPlotRange.get(i).getMinimum().doubleValue()));            
                }
            }
        }
        else{
            for(int i = 0; i < yPlotRange.size(); i++){
                if (!yPlotRange.get(i).getMinimum().equals(yPlotRange.get(i).getMaximum())) {
                    ValueAxis yAxis = yValueScale.references(yPlotRange.get(i), 2, Math.max(2, getImageHeight() / 60));
                    yReferenceLabels.set(i,Arrays.asList(yAxis.getTickLabels()));
                    yReferenceValues.set(i,new ArrayDouble(yAxis.getTickValues()));            
                } else {
                    // TODO: use something better to format the number
                    yReferenceLabels.set(i,Collections.singletonList(yPlotRange.get(i).getMinimum().toString()));
                    yReferenceValues.set(i,new ArrayDouble(yPlotRange.get(i).getMinimum().doubleValue()));            
                }
            }
        }
        
        
        // Compute y axis spacing
        int yLabelWidth = 0;
        yLabelMaxWidth = 0;
        for (int a = 0; a < yReferenceLabels.size(); a++) {
            for(int b = 0; b < yReferenceLabels.get(a).size(); b++){
                yLabelWidth = labelFontMetrics.stringWidth(yReferenceLabels.get(a).get(b));
                yLabelMaxWidth = Math.max(yLabelMaxWidth, yLabelWidth);
            }
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
    protected void calculateGraphArea() {
        int areaFromBottom = bottomMargin + xLabelMaxHeight + xLabelMargin;
        int areaFromLeft = (leftMargin + yLabelMaxWidth + yLabelMargin*2 + 1)*(numGraphs-(numGraphs/2));
        int areaFromRight;
        if(numGraphs > 1){
            areaFromRight = (rightMargin + yLabelMaxWidth + yLabelMargin*2 + 1)*(numGraphs/2);
        }
        else{
            areaFromRight = rightMargin;
        }
        
        xPlotValueStart = getXPlotRange().getMinimum().doubleValue();
        xPlotValueEnd = getXPlotRange().getMaximum().doubleValue();
        if (xPlotValueStart == xPlotValueEnd) {
            // If range is zero, fake a range
            xPlotValueStart -= 1.0;
            xPlotValueEnd += 1.0;
        }
        xAreaCoordStart = areaFromLeft;
        xAreaCoordEnd = getImageWidth() - areaFromRight;
        xPlotCoordStart = xAreaCoordStart + leftAreaMargin + xPointMargin;
        xPlotCoordEnd = xAreaCoordEnd - rightAreaMargin - xPointMargin;
        xPlotCoordWidth = xPlotCoordEnd - xPlotCoordStart;
        
       //set the start and end of each plot in terms of values.
        if(yPlotValueStart == null || yPlotValueStart.size() != yPlotRange.size()){
            yPlotValueStart = new ArrayList<Double>();
            yPlotValueEnd = new ArrayList<Double>();
            for(int i = 0; i < yPlotRange.size(); i++){
                yPlotValueStart.add(yPlotRange.get(i).getMinimum().doubleValue());
                yPlotValueEnd.add(yPlotRange.get(i).getMaximum().doubleValue());
            }
        }
        else{
            for(int i = 0; i < yPlotRange.size(); i++){
                yPlotValueStart.set(i, yPlotRange.get(i).getMinimum().doubleValue());
                yPlotValueEnd.set(i, yPlotRange.get(i).getMaximum().doubleValue());
            }
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
        
        yReferenceCoords = new ArrayList<ListDouble>();
        if (yReferenceValues != null) {
            for(int a = 0; a < yReferenceValues.size(); a++){
                double[] yRefCoords = new double[yReferenceValues.get(a).size()];
                for (int b = 0; b < yRefCoords.length; b++) {
                    yRefCoords[b] = scaledY(yReferenceValues.get(a).getDouble(b),a);
                }
                yReferenceCoords.add(new ArrayDouble(yRefCoords));
            }
        }
    }
    
    private final double scaledY(double value,  int index) {
        return yValueScale.scaleValue(value, yPlotValueStart.get(index), yPlotValueEnd.get(index), yPlotCoordEnd, yPlotCoordStart);
    }
    
    @Override
    protected void drawGraphArea() {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // When drawing the reference line, align them to the pixel
        drawVerticalReferenceLines();
        drawHorizontalReferenceLines();
        
        drawYLabels();
        drawXLabels();
    }
    
    @Override
    protected void drawVerticalReferenceLines() {
        g.setColor(referenceLineColor);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        ListNumber xTicks = xReferenceCoords;
        for (int i = 0; i < xTicks.size(); i++) {
            Shape line = new Line2D.Double(xTicks.getDouble(i), yAreaCoordStart, xTicks.getDouble(i), yAreaCoordEnd - 1);
            g.draw(line);
        }
        int count = 0;
        for(int i = 0; i < numGraphs; i+=2){
            g.setColor(new Color(lineValueScheme.colorFor(i)));
            Shape line = new Line2D.Double((xAreaCoordStart - (count+1)*(yLabelMargin - 1) - count*(rightMargin + yLabelMaxWidth + yLabelMargin)), yAreaCoordStart, (xAreaCoordStart - (count+1)*(yLabelMargin - 1) - count*(rightMargin + yLabelMaxWidth + yLabelMargin)), yAreaCoordEnd - 1);
            g.draw(line);
            count++;
        }
        count = 0;
        for(int i = 1; i < numGraphs; i+=2){
            g.setColor(new Color(lineValueScheme.colorFor(i)));
            Shape line = new Line2D.Double((xAreaCoordEnd + (count+1)*(yLabelMargin + 1) + count*(rightMargin + yLabelMaxWidth + yLabelMargin)), yAreaCoordStart, (xAreaCoordEnd + (count+1)*(yLabelMargin + 1) + count*(rightMargin + yLabelMaxWidth + yLabelMargin)), yAreaCoordEnd - 1);
            g.draw(line);
            count++;
        }
    }
    
    @Override
    protected void drawHorizontalReferenceLines() {
        g.setColor(referenceLineColor);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            ListNumber yTicks = yReferenceCoords.get(0);
            for (int b = 0; b < yTicks.size(); b++) {
                Shape line = new Line2D.Double(xAreaCoordStart, yTicks.getDouble(b), xAreaCoordEnd - 1, yTicks.getDouble(b));
                g.draw(line);
            }
    }
    
    @Override
    protected void drawYLabels() {
        // Draw Y labels
        for(int a = 0; a < numGraphs; a++){
            ListNumber yTicks = yReferenceCoords.get(a);
            if (yReferenceLabels.get(a) != null && !yReferenceLabels.get(a).isEmpty()) {
                //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                g.setColor(labelColor);
                g.setFont(labelFont);
                FontMetrics metrics = g.getFontMetrics();

                // Draw first and last label
                int[] drawRange = new int[] {yAreaCoordStart, yAreaCoordEnd - 1};
                int xRightLabel = (int) (xAreaCoordStart - yLabelMargin - 1);
                drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(a).get(0), (int) Math.floor(yTicks.getDouble(0)),
                    drawRange, xRightLabel, true, false);
                drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(a).get(yReferenceLabels.get(a).size() - 1), (int) Math.floor(yTicks.getDouble(yReferenceLabels.get(a).size() - 1)),
                    drawRange, xRightLabel, false, false);

                for (int b = 1; b < yReferenceLabels.get(a).size() - 1; b++) {
                    drawHorizontalReferencesLabel(g, metrics, yReferenceLabels.get(a).get(b), (int) Math.floor(yTicks.getDouble(b)),
                        drawRange, xRightLabel, true, false);
                }
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
}
