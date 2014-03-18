/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.MultilineGraph2DRenderer;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.DatasetFactory;

public class ProfileMultilineGraph2D extends ProfileGraph2D<MultilineGraph2DRenderer, List<Point2DDataset>>{
    private int numGraphs = 3;
    
    public int getNumGraphs(){
        return this.numGraphs;
    }
    
    public final void setNumGraphs(int numGraphs){
        if (numGraphs <= 0){
            throw new IllegalArgumentException("Invalid number of graphs");
        }
        
        this.numGraphs = numGraphs;
        
        this.createDatasetMessage();
    }
    
    @Override
    public void setNumDataPoints(int numData){
        super.setNumDataPoints(numData);
        
        this.createDatasetMessage();
    }
    
    public final void createDatasetMessage(){
        super.getSaveSettings().setDatasetMessage(getNumDataPoints() + "," + numGraphs + "graphs");
    }
    
    
    @Override
    protected List<Point2DDataset> getDataset() {
        List<Point2DDataset> data = new ArrayList<>();
        
        for (int i = 0; i < numGraphs; ++i){
            data.add(DatasetFactory.makePoint2DGaussianRandomData(super.getNumDataPoints()));
        }
        
        return data;
    }

    @Override
    protected MultilineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new MultilineGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, MultilineGraph2DRenderer renderer, List<Point2DDataset> data) {
        renderer.draw(graphics, data);
    }

    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();
        
        map.put("None", null);

        return map;
    }

    @Override
    public String getGraphTitle() {
        return "MultilineGraph2D";
    }
    
}
