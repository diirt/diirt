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
import org.epics.graphene.MultiAxisLineGraph2DRenderer;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>MultiAxisLineGraph2DRenderer</code> when drawing split graphs.
 * Takes a <code>Point2DDataset</code> dataset and repeatedly renders 
 * through a <code>Point2DDataset</code>.
 * 
 * @author asbarber
 */
public class ProfileNLineGraphs2D extends ProfileGraph2D<MultiAxisLineGraph2DRenderer, List<Point2DDataset>>{
    private int numGraphs = 3;

    
    //Dataset Sepcific
    //--------------------------------------------------------------------------
        
    /**
     * Gets the number of graphs for the renderer.
     * @return number of graphs being rendered
     */    
    public int getNumGraphs(){
        return this.numGraphs;
    }
    
    /**
     * Sets the number of graphs for the renderer.
     * Must be a value greater than zero.
     * @param numGraphs number of graphs being rendered
     */   
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
    
    /**
     * Creates a dataset message to output the number of graphs.
     */    
    public final void createDatasetMessage(){
        super.getSaveSettings().setDatasetMessage(getNumDataPoints() + " & " + numGraphs + "graphs");
    }
    
    //--------------------------------------------------------------------------
    
    
    //Superclass
    //--------------------------------------------------------------------------  
    
    @Override
    protected List<Point2DDataset> getDataset() {
        List<Point2DDataset> data = new ArrayList<>();
        
        for (int i = 0; i < numGraphs; ++i){
            data.add(DatasetFactory.makePoint2DGaussianRandomData(super.getNumDataPoints()));
        }
        
        return data;
    }

    @Override
    protected MultiAxisLineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new MultiAxisLineGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, MultiAxisLineGraph2DRenderer renderer, List<Point2DDataset> data) {
        //To draw in the same way that NLine did, split must be set true.
        renderer.update(renderer.newUpdate().split(true));
        renderer.draw(graphics, data);
    }

    /**
     * Gets the updates associated with the renderer in a map, linking a 
     * description of the update to the update object.
     * @return map with description of update paired with an update
     */    
    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();
        
        map.put("None", null);

        return map;
    }

    @Override
    public String getGraphTitle() {
        return "NLineGraphs2D";
    }
    
    //--------------------------------------------------------------------------    
}
