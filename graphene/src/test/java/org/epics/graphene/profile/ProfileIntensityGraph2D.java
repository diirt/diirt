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
    /* Summary
     * Using array,         3.86 ms,   100 samples,    15000 tries,    600x400
     * Using ArrayDouble,   3.94 ms,   100 samples,    10000 tries,    600x400
     * Using array,         19.34 ms,  1000 samples,   1500 tries,     600x400
     * Using ArrayDouble,   17.84 ms,  1000 samples,   1500 tries,     600x400
     *
     * Adding sorting and new impl, 12.17 ms,  1000 samples,   1500 tries,     600x400
     * Adding sorting and new impl, 2.68 ms,   100 samples,    15000 tries,    600x400
     *
     * Before large array optimization, 16686.27 ms,    1000000 samples,    3 tries,    600x400 LINEAR
     * Before large array optimization, 423.63 ms,      100000 samples,     100 tries,  600x400 LINEAR
     * Before large array optimization, 38.33 ms,       10000 samples,      100 tries,  600x400 LINEAR
    */

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
    
    public String getSaveMessage(){
        return getNumXDataPoints() + "x" + getNumYDataPoints();
    }
    
    @Override
    protected Cell2DDataset getDataset() {
        return ProfileGraph2D.makeCell2DData(getNumXDataPoints(), getNumYDataPoints());
    }

    @Override
    protected IntensityGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new IntensityGraph2DRendererUpdate());
        
        return renderer;
    }

    @Override
    protected void render(IntensityGraph2DRenderer renderer, Cell2DDataset data) {
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
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
        profiler.saveStatistics(profiler.getSaveMessage());
    }    
}