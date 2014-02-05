/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import static org.epics.graphene.ColorScheme.*;
import static org.epics.graphene.Graph2DRenderer.aggregateRange;
import org.epics.util.array.*;

/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRenderer extends Graph2DRenderer{
    public NLineGraphs2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth,imageHeight);
    }
    
    private AxisRange xAxisRange = AxisRanges.integrated();
    private AxisRange yAxisRange = AxisRanges.integrated();
    private ValueScale xValueScale = ValueScales.linearScale();
    private ValueScale yValueScale = ValueScales.linearScale();
    private Range xAggregatedRange;
    private List<Range> yAggregatedRange;
    private Range xPlotRange;
    private List<Range> yPlotRange;
    private ArrayList<Double> graphBoundaries;
    private ArrayList<Double> graphBoundaryRatios;
    private HashMap<Integer, Range> indexToRangeMap = new HashMap<Integer,Range>();
    private HashMap<Integer, Boolean> indexToForceMap = new HashMap<Integer,Boolean>();
    private int numGraphs;
    private List<ListDouble> yReferenceCoords;
    private List<ListDouble> yReferenceValues;
    private List<List<String>> yReferenceLabels;
    private int xLabelMaxHeight;
    private int yLabelMaxWidth;
    private Range emptyRange;
    
    //TODO: EVERYTHING. LISTS. YEAH!
    private double xPlotValueStart;
    private List<Double> yPlotValueStart;
    private double xPlotValueEnd;
    private List<Double> yPlotValueEnd;

    // The pixel coordinates for the area
    private int xAreaCoordStart;
    private List<Integer> yAreaCoordStart;
    private List<Integer> yAreaCoordEnd;
    private int xAreaCoordEnd;

    // The pixel coordinates for the ranges
    // These match the xPlotValueXxx
    private double xPlotCoordStart;
    private List<Double> yPlotCoordStart;
    private List<Double> yPlotCoordEnd;
    private double xPlotCoordEnd;

    // The pixel size of the range (not of the plot area)
    private List<Double> yPlotCoordHeight;
    private double xPlotCoordWidth;
    
    public void update(NLineGraphs2DRendererUpdate update) {
        super.update(update);
        if(update.getImageHeight() != null){
            for(int i = 0; i < graphBoundaries.size(); i++){
                graphBoundaries.set(i, getImageHeight() * graphBoundaryRatios.get(i));
            }
        }
        if(update.getGraphBoundaries() != null){
            graphBoundaries = update.getGraphBoundaries();
            graphBoundaryRatios = new ArrayList<Double>();
            for(int i = 0; i < graphBoundaryRatios.size(); i++){
                graphBoundaryRatios.add(graphBoundaries.get(i)/ getImageHeight());
            }
        }
        if(update.getGraphBoundaryRatios() != null){
            graphBoundaryRatios = update.getGraphBoundaryRatios();
            graphBoundaries = new ArrayList<Double>();
            for(int i = 0; i < graphBoundaries.size(); i++){
                graphBoundaries.add(getImageHeight() * graphBoundaryRatios.get(i));
            }
        }
        if(update.getIndexToRange() != null){
            indexToRangeMap = update.getIndexToRange();
        }
        if(update.getIndexToForce() != null){
            indexToForceMap = update.getIndexToForce();
        }
    }
    
    public void draw( Graphics2D g, List<Point2DDataset> data){
        if(g == null){
            throw new IllegalArgumentException("Graphics can't be null.");
        }
        if(data == null){
            throw new IllegalArgumentException("data can't be null.");
        }
        this.g = g;
        List<Range> dataRangesX = new ArrayList<Range>();
        for(int i = 0; i < data.size(); i++){
            dataRangesX.add(data.get(i).getXStatistics());
        }
        List<Range> dataRangesY = new ArrayList<Range>();
        for(int i = 0; i < data.size(); i++){
            dataRangesY.add(data.get(i).getYStatistics());
        }
        calculateRanges(dataRangesX,dataRangesY,data.size());
        addGraphs(data);
        drawGraphs(data);
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new NLineGraphs2DRendererUpdate();
    }
    

    private void addGraphs(List<Point2DDataset> data){
        if(this.graphBoundaries == null || this.graphBoundaries.size() != numGraphs+1){
            numGraphs = data.size();
            while((double)getImageHeight()/numGraphs < 40){
                numGraphs-=1;
            }
            graphBoundaries = new ArrayList<Double>();
            for(double i = 0; i <= numGraphs; i++){
                graphBoundaries.add(i/numGraphs*(getImageHeight()));
            }
            graphBoundaryRatios = new ArrayList<Double>();
            for(double i = 0; i <= numGraphs; i++){
                graphBoundaryRatios.add(i/numGraphs);
            }
        }
        
    }
    
    private void drawGraphs(List<Point2DDataset> data){
        
    }
    
    protected void calculateRanges(List<Range> xDataRange, List<Range> yDataRange, int length) {
        for(int i = 0; i < length; i++){
            xAggregatedRange = aggregateRange(xDataRange.get(i), xAggregatedRange);
            xPlotRange = xAxisRange.axisRange(xDataRange.get(i), xAggregatedRange);
        }  
        if(yAggregatedRange.size() == 0 || yDataRange.size() != yAggregatedRange.size()){
            yAggregatedRange = new ArrayList<Range>();
            yPlotRange = new ArrayList<Range>();
            for(int i = 0; i < length; i++){
                yAggregatedRange.add(aggregateRange(yDataRange.get(i), emptyRange));
                yPlotRange.add(yAxisRange.axisRange(yDataRange.get(i), emptyRange));
            }
        }
        else{
            for(int i = 0; i < length; i++){
                yAggregatedRange.set(i,aggregateRange(yDataRange.get(i), yAggregatedRange.get(i)));
                yPlotRange.set(i,yAxisRange.axisRange(yDataRange.get(i), yAggregatedRange.get(i)));
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
        labelFontMetrics = g.getFontMetrics(labelFont);
        
        // Compute x axis spacing
        xLabelMaxHeight = labelFontMetrics.getHeight() - labelFontMetrics.getLeading();
        
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
    protected void calculateGraphArea() {
        int areaFromBottom = bottomMargin + xLabelMaxHeight + xLabelMargin;
        int areaFromLeft = leftMargin + yLabelMaxWidth + yLabelMargin;

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
        
        //set the start and end of each plot in terms of values.
        if(yPlotValueStart.size() == 0 || yPlotValueStart.size() != yPlotRange.size()){
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
        
        //range faking
        for(int i = 0; i < yPlotRange.size(); i++){
            if (yPlotValueStart.get(i) == yPlotValueEnd.get(i)) {
                // If range is zero, fake a range
                yPlotValueStart.set(i, yPlotValueStart.get(i)-1.0);
                yPlotValueEnd.set(i, yPlotValueEnd.get(i)+1.0);
            }
        }
        
        //TODO: FINISH THIS PART OF THE METHOD. ACCOUNT FOR BEGINNING AND END GRAPHS, ACCOUNT FOR EXISTING LISTS
        for(int i = 0; i < numGraphs; i++){
            yAreaCoordStart = new ArrayList<Integer>();
            yAreaCoordEnd = new ArrayList<Integer>();
            yPlotCoordStart = new ArrayList<Double>();
            yPlotCoordEnd = new ArrayList<Double>();
            yPlotCoordHeight = new ArrayList<Double>();
            yAreaCoordStart.add(topMargin + graphBoundaries.get(i).intValue());
            yAreaCoordEnd.add(graphBoundaries.get(i+1).intValue()-areaFromBottom);
            yPlotCoordStart.add(yAreaCoordStart.get(i).intValue() + topAreaMargin + yPointMargin);
            yPlotCoordEnd.add(yAreaCoordEnd.get(i).intValue() - bottomAreaMargin - yPointMargin);
            yPlotCoordHeight.add(yPlotCoordEnd.get(i)-yPlotCoordStart.get(i));
        }
        
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
                //yRefCoords[i] = scaledY(yReferenceValues.getDouble(i));
            }
           //yReferenceCoords = new ArrayDouble(yRefCoords);
        }
    }
}
