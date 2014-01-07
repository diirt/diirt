/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.SparklineGraph2DRenderer;
import org.epics.graphene.SparklineGraph2DRendererUpdate;

/**
 * Handles profiling for <code>SparklineGraph2DRenderer</code>.
 * Takes a <code>Point2DDataset</code> dataset and repeatedly renders through a <code>SparklineGraph2DRenderer</code>.
 * 
 * @author asbarber
 */
public class ProfileSparklineGraph2D extends ProfileGraph2D<SparklineGraph2DRenderer, Point2DDataset> {
    
    /**
     * Gets a set of random Gaussian 2D point data.
     * @return the appropriate <code>SparklineGraph2DRenderer</code> data
     */      
    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DGaussianRandomData(getNumDataPoints());
    }
    
    /**
     * Returns the renderer used in the render loop.
     * The 2D point is rendered by a <code>SparklineGraph2DRenderer</code>.
     * @param imageWidth width of rendered image in pixels
     * @param imageHeight height of rendered image in pixels
     * @return a sparkline graph to draw the data
     */     
    @Override
    protected SparklineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new SparklineGraph2DRendererUpdate());
        
        return renderer;
    }
    
    /**
     * Draws the 2D point data in a sparkline graph.
     * Primary method in the render loop.
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data the 2D point data being drawn
     */        
    @Override
    protected void render(Graphics2D graphics, SparklineGraph2DRenderer renderer, Point2DDataset data) {
        renderer.draw(graphics, data);    
    }

    /**
     * Returns the name of the graph being profiled.
     * @return <code>SparklineGraph2DRenderer</code> title
     */          
    @Override
    public String getGraphTitle() {
        return "SparklineGraph2D";
    }    

    /**
     * Profiles for <code>SparklineGraph2DRenderer</code>, 
     * prints the statistics to the console and saves the statistics.
     * @param args console arguments -- no impact
     */      
    public static void main(String[] args){
        ProfileSparklineGraph2D profiler = new ProfileSparklineGraph2D();
        profiler.profile();
        profiler.printStatistics();
        profiler.saveStatistics();
    }    
}
