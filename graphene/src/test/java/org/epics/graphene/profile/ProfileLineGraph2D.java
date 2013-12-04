/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import org.epics.graphene.*;

/**
 *
 * @author asbarber
 */
public class ProfileLineGraph2D extends ProfileGraph2D<LineGraph2DRenderer, Point2DDataset>{
    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DGaussianRandomData(getNumDataPoints());
    }

    @Override
    protected LineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        
        return renderer;
    }

    @Override
    protected void render(Graphics2D graphics, LineGraph2DRenderer renderer, Point2DDataset data) {
        renderer.draw(graphics, data);  
    }
    
    @Override
    public String getGraphTitle() {
        return "LineGraph2D";
    }
    
    public static void main(String[] args){
        ProfileLineGraph2D profiler = new ProfileLineGraph2D();
        profiler.profile();
        profiler.printStatistics();
        profiler.saveStatistics();        
    }     
}
