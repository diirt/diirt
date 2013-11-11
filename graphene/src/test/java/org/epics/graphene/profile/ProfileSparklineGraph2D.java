/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.SparklineGraph2DRenderer;
import org.epics.graphene.SparklineGraph2DRendererUpdate;

/**
 *
 * @author asbarber
 */
public class ProfileSparklineGraph2D extends ProfileGraph2D<SparklineGraph2DRenderer, Point2DDataset> {
    
    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DData(getNumDataPoints());
    }
    
    @Override
    protected SparklineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new SparklineGraph2DRendererUpdate());
        
        return renderer;
    }
    @Override
    
    protected void render(SparklineGraph2DRenderer renderer, Point2DDataset data) {
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        renderer.draw(graphics, data);    
    }

    @Override
    public String getGraphTitle() {
        return "SparklineGraph2D";
    }    

    public static void main(String[] args){
        ProfileSparklineGraph2D profiler = new ProfileSparklineGraph2D();
        profiler.profile();
        profiler.printStatistics();
        profiler.saveStatistics();
    }    
}
