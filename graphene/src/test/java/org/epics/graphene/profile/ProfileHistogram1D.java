/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.epics.graphene.*;

/**
 *
 * @author carcassi
 */
public class ProfileHistogram1D {

    public static void main(String[] args) {
        // With 1000000 samples and 1000 tries, 26 ms
        // With 1000 samples and 100000 tries, 0.86 ms
        
        // After using CollectionNumber
        // With 1000000 samples and 1000 tries, 25.4 ms
        // With 1000 samples and 100000 tries, 0.73 ms
        
        int nSamples = 1000;
        int nTries = 10000;
        int imageWidth = 600;
        int imageHeight = 400;
        Random rand = new Random();
                
        Point1DDataset dataset = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        dataset.update(update);
        
        Histogram1D histogram = Histograms.createHistogram(dataset);
        Histogram1DRenderer renderer = new Histogram1DRenderer(imageWidth, imageHeight);
        
        StopWatch stopWatch = new StopWatch(nTries);
        
        for (int i = 0; i < nTries; i++) {
            stopWatch.start();
            histogram.update(new Histogram1DUpdate().recalculateFrom(dataset));
            BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = image.createGraphics();
            renderer.draw(graphics, histogram);
            stopWatch.stop();
            
            if (image.getRGB(0, 0) == 0) {
                System.out.println("Black");
            }
        }
        
        System.out.println("average " + stopWatch.getAverageMs() + " ms");
        Point1DDataset timings = new Point1DCircularBuffer(nTries);
        timings.update(new Point1DDatasetUpdate().addData(Arrays.copyOfRange(stopWatch.getData(), 1, nTries)));
        Histogram1D hist = Histograms.createHistogram(timings);
        ShowResizableImage.showHistogram(hist);
    }
}
