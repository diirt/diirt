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
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRenderer extends Graph2DRenderer{
    public NLineGraphs2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth,imageHeight);
    }
    
    private ArrayList<NLineGraph2DRenderer> graphList;
    private ArrayList<Integer> roundingIndices;
    private LineGraph2DRenderer lastGraph;
    
    public void draw( Graphics2D g, List<Point2DDataset> data){
        if(g == null){
            throw new IllegalArgumentException("Graphics can't be null.");
        }
        if(data == null){
            throw new IllegalArgumentException("data can't be null.");
        }
        this.g = g;
        graphList = new ArrayList<NLineGraph2DRenderer>();
        roundingIndices = new ArrayList<Integer>();
        addGraphs(data);
        drawGraphs(data);
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new NLineGraphs2DRendererUpdate();
    }
    public void update(NLineGraphs2DRendererUpdate update) {
        super.update(update);
    }
    private void addGraphs(List<Point2DDataset> data){
        double roundingError = 0;
        for(int i = 0; i < data.size()-1;i++){
            NLineGraph2DRenderer added = null;
            roundingError+=((double)this.getImageHeight()/data.size())-(int)(this.getImageHeight()/data.size());
            if(roundingError < 1){
                added = new NLineGraph2DRenderer(this.getImageWidth(),this.getImageHeight()/data.size());
            }
            if(roundingError >= 1){
                added = new NLineGraph2DRenderer(this.getImageWidth(),this.getImageHeight()/data.size()+1);
                roundingError-=1;
                if(i < data.size()-1){
                    roundingIndices.add(Integer.valueOf(i+1));
                }
            }
            graphList.add(added);
        }  
        LineGraph2DRenderer added = null;
        roundingError+=((double)this.getImageHeight()/data.size())-(int)(this.getImageHeight()/data.size());
        if(roundingError < 1){
            added = new LineGraph2DRenderer(this.getImageWidth(),this.getImageHeight()/data.size());
        }
        if(roundingError >= 1){
            added = new LineGraph2DRenderer(this.getImageWidth(),this.getImageHeight()/data.size()+1);
            roundingError-=1;
        }
        lastGraph = added;
    }
    private void drawGraphs(List<Point2DDataset> data){
        double roundingError = 0;
        for(int i = 0; i < graphList.size(); i++){
            Graphics2D gtemp = (Graphics2D)g.create();
            if(roundingIndices.contains(i)){
                roundingError = (1*(roundingIndices.indexOf(i)+1));
            }
            gtemp.translate(0,this.getImageHeight()/data.size()*i+roundingError);
            graphList.get(i).draw(gtemp, data.get(i));
        }
        Graphics2D gtemp = (Graphics2D)g.create();
        gtemp.translate(0,this.getImageHeight()/data.size()*graphList.size()+roundingError);
        lastGraph.draw(gtemp, data.get(graphList.size()));
    }
}
