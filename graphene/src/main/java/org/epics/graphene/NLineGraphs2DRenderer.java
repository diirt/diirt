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
    private ListNumber graphBoundaries;
    private ListNumber graphBoundaryRatios;
    
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
                graphBoundaries.setDouble(i, getImageHeight() * graphBoundaryRatios.getDouble(i));
            }
        }
        
    }

    private void addGraphs(List<Point2DDataset> data){
        if(this.graphBoundaries == null || this.graphBoundaries.size() != data.size()+1){
            graphBoundaries = ListNumbers.linearListFromRange(0,this.getImageHeight(),data.size()+1);
            graphBoundaryRatios = ListNumbers.linearListFromRange(0,1,data.size()+1);
        }
        for(int i = 0; i < data.size()-1;i++){
            NLineGraph2DRenderer added = null;
            added = new NLineGraph2DRenderer(this.getImageWidth(),graphBoundaries.getInt(i+1)-
                    graphBoundaries.getInt(i));
            graphList.add(added);
        }  
        LineGraph2DRenderer added = null;
        added = new LineGraph2DRenderer(this.getImageWidth(),graphBoundaries.getInt(data.size())-
                    graphBoundaries.getInt(data.size()-1));
        lastGraph = added;
    }
    
    private void drawGraphs(List<Point2DDataset> data){
        for(int i = 0; i < graphList.size(); i++){
            Graphics2D gtemp = (Graphics2D)g.create();
            gtemp.translate(0,graphBoundaries.getInt(i));
            graphList.get(i).draw(gtemp, data.get(data.size()-1-i));
        }
        Graphics2D gtemp = (Graphics2D)g.create();
        gtemp.translate(0,graphBoundaries.getInt(graphList.size()));
        lastGraph.draw(gtemp, data.get(0));
    }
}
