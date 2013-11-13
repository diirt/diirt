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
 * @author asbarber
 */
public class ProfileLineGraph2D extends ProfileGraph2D<LineGraph2DRenderer, Point2DDataset>{
    // Using array, 3.855865984000003, 100 samples, 15000 tries, 600x400
    // Using ArrayDouble, 3.9436457209999842 ms, 100 samples, 10000 tries, 600x400
    // Using array, 19.336473031333334 ms, 1000 samples, 1500 tries, 600x400
    // Using ArrayDouble, 17.84245149399999 ms, 1000 samples, 1500 tries, 600x400 18.67990659599997
    // Adding sorting and new impl, 12.17332916666666 ms, 1000 samples, 1500 tries, 600x400
    // Adding sorting and new impl, 2.679641561333325 ms, 100 samples, 15000 tries, 600x400

    // Before large array optimization, 16686.272965666667 ms, 1000000 samples, 3 tries, 600x400 LINEAR
    // Before large array optimization, 423.63001873999997 ms, 100000 samples, 100 tries, 600x400 LINEAR
    // Before large array optimization, 38.32643352900002 ms, 10000 samples, 100 tries, 600x400 LINEAR

    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DData(getNumDataPoints());
    }

    @Override
    protected LineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        
        return renderer;
    }

    @Override
    protected void render(LineGraph2DRenderer renderer, Point2DDataset data) {
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
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
