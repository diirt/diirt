/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.epics.graphene.*;
import org.epics.graphene.profile.ProfileGraph2D;

/**
 * Handles profiling for <code>Histogram1D</code>.
 * Takes a <code>Histogram1D</code> dataset and repeatedly renders through a <code>AreaGraph2DRenderer</code>.
 * 
 * @author asbarber
 */
public class ProfileHistogram1D extends ProfileGraph2D<AreaGraph2DRenderer, Histogram1D>{
    
    private Point1DCircularBuffer datasetBuffer;
    
    /**
     * Generates <code>Histogram1D</code> data that can be used in rendering.
     * The data is Gaussian and random between 0 and 1.
     * @return data as a histogram
     */
    @Override
    protected Histogram1D getDataset() {
        int nSamples = getNumDataPoints();
        
        datasetBuffer = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);                
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        datasetBuffer.update(update);
        
        return Histograms.createHistogram(datasetBuffer);            
    }

    /**
     * Returns the renderer used in the render loop.
     * The histogram data is rendered by a <code>AreaGraph2DRenderer</code>.
     * @param imageWidth width of rendered image in pixels
     * @param imageHeight height of rendered image in pixels
     * @return a <code>AreaGraph2DRenderer</code> associated with <code>Histogram1D</code> data
     */
    @Override
    protected AreaGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new AreaGraph2DRenderer(imageWidth, imageHeight);
    }

    /**
     * Draws the histogram in an area graph.
     * Primary method in the render loop.
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data the histogram being drawn
     */
    @Override
    protected void render(Graphics2D graphics, AreaGraph2DRenderer renderer, Histogram1D data) {
        data.update(new Histogram1DUpdate().recalculateFrom(datasetBuffer));
        renderer.draw(graphics, data);            
    }
    
    /**
     * Returns the name of the graph being profiled.
     * @return <code>Histogram1D</code> title
     */
    @Override
    public String getGraphTitle() {
        return "Histogram1D";
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
}
