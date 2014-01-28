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
import static org.epics.graphene.ColorScheme.*;
import org.epics.util.array.*;

/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRenderer extends Graph2DRenderer{
    public NLineGraphs2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth,imageHeight);
    }
    
    private ArrayList<NLineGraph2DRenderer> graphList;
    private LineGraph2DRenderer lastGraph;
    private ArrayList<Double> graphBoundaries;
    private ArrayList<Double> graphBoundaryRatios;
    private int num_Graphs;
    
    public void draw( Graphics2D g, List<Point2DDataset> data){
        if(g == null){
            throw new IllegalArgumentException("Graphics can't be null.");
        }
        if(data == null){
            throw new IllegalArgumentException("data can't be null.");
        }
        this.g = g;
        graphList = new ArrayList<NLineGraph2DRenderer>();
        addGraphs(data);
        drawGraphs(data);
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new NLineGraphs2DRendererUpdate();
    }
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
        for(int i = 0; i < num_Graphs-1;i++){
            NLineGraph2DRenderer added = null;
            added = new NLineGraph2DRenderer(this.getImageWidth(),(int)(graphBoundaries.get(i+1)-0)-
                    (int)(graphBoundaries.get(i)-0));
            graphList.add(added);
        }  
        LineGraph2DRenderer added = null;
        added = new LineGraph2DRenderer(this.getImageWidth(),(int)(graphBoundaries.get(num_Graphs)-0)-
                    (int)(graphBoundaries.get(num_Graphs-1)-0));
        lastGraph = added;
    }
    
    private void drawGraphs(List<Point2DDataset> data){
        for(int i = 0; i < graphList.size(); i++){
            Graphics2D gtemp = (Graphics2D)g.create();
            gtemp.translate(0,(int)(graphBoundaries.get(i)-0));
            graphList.get(i).draw(gtemp, data.get(num_Graphs-1-i));
        }
        Graphics2D gtemp = (Graphics2D)g.create();
        gtemp.translate(0,(int)(graphBoundaries.get(graphList.size())-0));
        lastGraph.draw(gtemp, data.get(0));
    }
}
