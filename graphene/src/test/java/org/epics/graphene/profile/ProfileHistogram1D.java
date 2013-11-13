/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.epics.graphene.*;

/**
 *
 * @author asbarber
 */
public class ProfileHistogram1D extends ProfileGraph2D<AreaGraph2DRenderer, Histogram1D>{
    // With 1000000 samples and 1000 tries, 26 ms
    // With 1000 samples and 100000 tries, 0.86 ms

    // After using CollectionNumber
    // With 1000000 samples and 1000 tries, 25.4 ms
    // With 1000 samples and 100000 tries, 0.73 ms

    // After refactoring to Cell1DDataset and AreaGraph2DRenderer
    // With 1000000 samples and 1000 tries, 13.5 ms
    // with 1000 samples and 100000 tries, 0.57 ms
    
    private Point1DCircularBuffer datasetBuffer;
    
    
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

    @Override
    protected AreaGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new AreaGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, AreaGraph2DRenderer renderer, Histogram1D data) {
        data.update(new Histogram1DUpdate().recalculateFrom(datasetBuffer));
        renderer.draw(graphics, data);            
    }
    
    @Override
    public String getGraphTitle() {
        return "Histogram1D";
    }
    
    
    public static void main(String[] args) {
        ProfileHistogram1D profiler = new ProfileHistogram1D();
        profiler.profile();
        profiler.printStatistics();
        profiler.saveStatistics();
    }


    
}
