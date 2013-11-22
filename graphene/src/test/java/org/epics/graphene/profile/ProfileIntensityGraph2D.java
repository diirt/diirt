/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.epics.graphene.*;

/**
 *
 * @author asbarber,
 * @author jkfeng,
 * @author sjdallst
 */
public class ProfileIntensityGraph2D extends ProfileGraph2D<IntensityGraph2DRenderer, Cell2DDataset>{
    @Override
    public int getNumDataPoints(){
        return getNumXDataPoints() * getNumYDataPoints();
    }
    
    public int getNumXDataPoints(){
        return 100;
    }
    public int getNumYDataPoints(){
        return 100;
    }
    
    public void setDatasetMessage(){
        super.setDatasetMessage(getNumXDataPoints() + "x" + getNumYDataPoints());
    }
    
    @Override
    protected Cell2DDataset getDataset() {
        return ProfileGraph2D.makeCell2DGaussianRandomData(getNumXDataPoints(), getNumYDataPoints());
    }

    @Override
    protected IntensityGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new IntensityGraph2DRendererUpdate());
        
        return renderer;
    }

    @Override
    protected void render(Graphics2D graphics, IntensityGraph2DRenderer renderer, Cell2DDataset data) {
        renderer.draw(graphics, data);        
    }
    
    @Override
    public String getGraphTitle() {
        return "IntensityGraph2D";
    }
    
    
    public static void main(String[] args){
        ProfileIntensityGraph2D profiler = new ProfileIntensityGraph2D();
        profiler.profile();
        profiler.printStatistics();  
        profiler.setDatasetMessage();
        profiler.saveStatistics();
    }    
}