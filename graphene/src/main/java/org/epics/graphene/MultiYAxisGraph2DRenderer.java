/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    
}
