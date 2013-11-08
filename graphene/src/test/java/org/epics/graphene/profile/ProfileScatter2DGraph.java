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
public class ProfileScatter2DGraph extends ProfileGraph2D<ScatterGraph2DRenderer, Point2DDataset>{

    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DData(1000);
    }

    @Override
    protected ScatterGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new ScatterGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(ScatterGraph2DRenderer renderer, Point2DDataset data) {
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        renderer.draw(graphics, data);
    }
    
    
    public static void main(String[] args){
        ProfileScatter2DGraph profiler = new ProfileScatter2DGraph();
        profiler.profile();
        profiler.printStatistics();
    }    
}
