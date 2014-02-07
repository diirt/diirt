/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Line2D;
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
    private int marginBetweenGraphs = 0;
    protected List<String> xReferenceLabels;
    
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
        calculateLabels();
        calculateGraphArea();        
        drawBackground();
        drawGraphArea();
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
        if(yAggregatedRange == null || yDataRange.size() != yAggregatedRange.size()){
            yAggregatedRange = new ArrayList<Range>();
            yPlotRange = new ArrayList<Range>();
            for(int i = 0; i < length; i++){
                yAggregatedRange.add(aggregateRange(yDataRange.get(i), emptyRange));
                yPlotRange.add(yAxisRange.axisRange(yDataRange.get(i), yAggregatedRange.get(i)));
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

        xPlotValueStart = xPlotRange.getMinimum().doubleValue();
        xPlotValueEnd = xPlotRange.getMaximum().doubleValue();
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
        
        //range faking
        for(int i = 0; i < yPlotRange.size(); i++){
            if (yPlotValueStart.get(i).doubleValue() == yPlotValueEnd.get(i).doubleValue()) {
                // If range is zero, fake a range
                yPlotValueStart.set(i, yPlotValueStart.get(i)-1.0);
                yPlotValueEnd.set(i, yPlotValueEnd.get(i)+1.0);
            }
        }
        
        //TODO: FINISH THIS PART OF THE METHOD. ACCOUNT FOR BEGINNING AND END GRAPHS, ACCOUNT FOR EXISTING LISTS
        if(yAreaCoordStart == null || yAreaCoordStart.size() != numGraphs){
            yAreaCoordStart = new ArrayList<Integer>();
            yAreaCoordEnd = new ArrayList<Integer>();
            yPlotCoordStart = new ArrayList<Double>();
            yPlotCoordEnd = new ArrayList<Double>();
            yPlotCoordHeight = new ArrayList<Double>();

            yAreaCoordStart.add(topMargin + graphBoundaries.get(0).intValue());
            yAreaCoordEnd.add(graphBoundaries.get(1).intValue()-areaFromBottom - marginBetweenGraphs);
            yPlotCoordStart.add(yAreaCoordStart.get(0) + topAreaMargin + yPointMargin);
            yPlotCoordEnd.add(yAreaCoordEnd.get(0) - bottomAreaMargin - yPointMargin);
            yPlotCoordHeight.add(yPlotCoordEnd.get(0)-yPlotCoordStart.get(0));
            for(int i = 1; i < numGraphs-1; i++){
                yAreaCoordStart.add(topMargin + graphBoundaries.get(i).intValue() + marginBetweenGraphs);
                yAreaCoordEnd.add(graphBoundaries.get(i+1).intValue()-areaFromBottom - marginBetweenGraphs);
                yPlotCoordStart.add(yAreaCoordStart.get(i) + topAreaMargin + yPointMargin);
                yPlotCoordEnd.add(yAreaCoordEnd.get(i) - bottomAreaMargin - yPointMargin);
                yPlotCoordHeight.add(yPlotCoordEnd.get(i)-yPlotCoordStart.get(i));
            }
            yAreaCoordStart.add(topMargin + graphBoundaries.get(numGraphs-1).intValue());
            yAreaCoordEnd.add(graphBoundaries.get(numGraphs).intValue()-areaFromBottom - marginBetweenGraphs);
            yPlotCoordStart.add(yAreaCoordStart.get(numGraphs-1) + topAreaMargin + yPointMargin);
            yPlotCoordEnd.add(yAreaCoordEnd.get(numGraphs-1) - bottomAreaMargin - yPointMargin);
            yPlotCoordHeight.add(yPlotCoordEnd.get(numGraphs-1)-yPlotCoordStart.get(numGraphs-1));
        }
        else{
            yAreaCoordStart.set(0,topMargin + graphBoundaries.get(0).intValue());
            yAreaCoordEnd.set(0,graphBoundaries.get(1).intValue()-areaFromBottom - marginBetweenGraphs);
            yPlotCoordStart.set(0,yAreaCoordStart.get(0) + topAreaMargin + yPointMargin);
            yPlotCoordEnd.set(0,yAreaCoordEnd.get(0) - bottomAreaMargin - yPointMargin);
            yPlotCoordHeight.set(0,yPlotCoordEnd.get(0)-yPlotCoordStart.get(0));
            for(int i = 1; i < numGraphs-1; i++){
                yAreaCoordStart.set(i,topMargin + graphBoundaries.get(i).intValue() + marginBetweenGraphs);
                yAreaCoordEnd.set(i,graphBoundaries.get(i+1).intValue()-areaFromBottom - marginBetweenGraphs);
                yPlotCoordStart.set(i,yAreaCoordStart.get(i) + topAreaMargin + yPointMargin);
                yPlotCoordEnd.set(i,yAreaCoordEnd.get(i) - bottomAreaMargin - yPointMargin);
                yPlotCoordHeight.set(i,yPlotCoordEnd.get(i)-yPlotCoordStart.get(i));
            }
            yAreaCoordStart.set(numGraphs-1,topMargin + graphBoundaries.get(numGraphs-1).intValue());
            yAreaCoordEnd.set(numGraphs-1,graphBoundaries.get(numGraphs).intValue()-areaFromBottom - marginBetweenGraphs);
            yPlotCoordStart.set(numGraphs-1,yAreaCoordStart.get(numGraphs-1) + topAreaMargin + yPointMargin);
            yPlotCoordEnd.set(numGraphs-1,yAreaCoordEnd.get(numGraphs-1) - bottomAreaMargin - yPointMargin);
            yPlotCoordHeight.set(numGraphs-1,yPlotCoordEnd.get(numGraphs-1)-yPlotCoordStart.get(numGraphs-1));
        }
        
        //Only calculates reference coordinates if calculateLabels() was called
        if (xReferenceValues != null) {
            double[] xRefCoords = new double[xReferenceValues.size()];
            for (int i = 0; i < xRefCoords.length; i++) {
                xRefCoords[i] = scaledX1(xReferenceValues.getDouble(i));
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
        for(int a = 0; a < numGraphs; a++){
            for (int b = 0; b < xTicks.size(); b++) {
                Shape line = new Line2D.Double(xTicks.getDouble(b), yAreaCoordStart.get(a), xTicks.getDouble(b), yAreaCoordEnd.get(a) - 1);
                g.draw(line);
            }
        }
    }
    
    @Override
    protected void drawHorizontalReferenceLines() {
        g.setColor(referenceLineColor);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        for(int a = 0; a < numGraphs-1; a++){
            ListNumber yTicks = yReferenceCoords.get(a);
            for (int b = 0; b < yTicks.size(); b++) {
                Shape line = new Line2D.Double(xAreaCoordStart, yTicks.getDouble(b), xAreaCoordEnd - 1, yTicks.getDouble(b));
                g.draw(line);
            }
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
                int[] drawRange = new int[] {yAreaCoordStart.get(a), yAreaCoordEnd.get(a) - 1};
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
    
    @Override
    protected void drawXLabels() {
        // Draw X labels
        ListNumber xTicks = xReferenceCoords;
        if (xReferenceLabels != null && !xReferenceLabels.isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(labelColor);
            g.setFont(labelFont);
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {xAreaCoordStart, xAreaCoordEnd - 1};
            int yTop = (int) (yAreaCoordEnd.get(numGraphs-1) + xLabelMargin);
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
            alignment = Java2DStringUtilities.Alignment.TOP_RIGHT;
            targetX = drawRange[MAX];
        }

        Java2DStringUtilities.drawString(graphics, alignment, targetX, yTop, text);
        
        if (updateMin) {
            drawRange[MIN] = targetX + metrics.getHeight();
        } else {
            drawRange[MAX] = targetX - metrics.getHeight();
        }
    }
    
    private double scaledX1(double value) {
        return xValueScale.scaleValue(value, xPlotValueStart, xPlotValueEnd, xPlotCoordStart, xPlotCoordEnd);
    }
    
    private final double scaledY(double value,  int index) {
        return yValueScale.scaleValue(value, yPlotValueStart.get(index), yPlotValueEnd.get(index), yPlotCoordEnd.get(index), yPlotCoordStart.get(index));
    }
}
