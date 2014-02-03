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
    private List<Range> xAggregatedRange;
    private List<Range> yAggregatedRange;
    private List<Range> xPlotRange;
    private List<Range> yPlotRange;
    private ArrayList<Double> graphBoundaries;
    private ArrayList<Double> graphBoundaryRatios;
    private HashMap<Integer, Range> IndexToRangeMap = new HashMap<Integer,Range>();
    private HashMap<Integer, Boolean> IndexToForceMap = new HashMap<Integer,Boolean>();
    private int num_Graphs;
    private List<ListDouble> yReferenceCoords;
    private List<ListDouble> yReferenceValues;
    private List<List<String>> yReferenceLabels;
    private int xLabelMaxHeight;
    private int yLabelMaxWidth;
    private Range emptyRange;
    
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
            IndexToRangeMap = update.getIndexToRange();
        }
        if(update.getIndexToForce() != null){
            IndexToForceMap = update.getIndexToForce();
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
        if(this.graphBoundaries == null || this.graphBoundaries.size() != num_Graphs+1){
            num_Graphs = data.size();
            while((double)getImageHeight()/num_Graphs < 40){
                num_Graphs-=1;
            }
            graphBoundaries = new ArrayList<Double>();
            for(double i = 0; i <= num_Graphs; i++){
                graphBoundaries.add(i/num_Graphs*(getImageHeight()));
            }
            graphBoundaryRatios = new ArrayList<Double>();
            for(double i = 0; i <= num_Graphs; i++){
                graphBoundaryRatios.add(i/num_Graphs);
            }
        }
        
    }
    
    private void drawGraphs(List<Point2DDataset> data){
        
    }
    
    protected void calculateRanges(List<Range> xDataRange, List<Range> yDataRange, int length) {
        if(xAggregatedRange.size() == 0 || xDataRange.size() != xAggregatedRange.size()){
            xAggregatedRange = new ArrayList<Range>();
            xPlotRange = new ArrayList<Range>();
            for(int i = 0; i < length; i++){
              xAggregatedRange.add(aggregateRange(xDataRange.get(i), emptyRange));
              xPlotRange.add(xAxisRange.axisRange(xDataRange.get(i), emptyRange));
            }
        }
        else{
            for(int i = 0; i < length; i++){
                xAggregatedRange.set(i,aggregateRange(xDataRange.get(i), xAggregatedRange.get(i)));
                xPlotRange.set(i,xAxisRange.axisRange(xDataRange.get(i), xAggregatedRange.get(i)));
            }
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
}
