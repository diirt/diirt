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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.graphene.*;

/**
 *
 * @author carcassi
 */
public class ProfileParallelHistogram1D {

    public static void main(String[] args) {
        final int nSamples = 1000;
        final int nTries = 20000;
        final int imageWidth = 600;
        final int imageHeight = 400;
        
        int nThreads = 4;
        
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
        executor.execute(new Runnable() {

            @Override
            public void run() {

                final Point1DCircularBuffer dataset = new Point1DCircularBuffer(nSamples);
                Random rand = new Random();
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

                    if (image.getRGB(0, 0) == 0) {
                        System.out.println("Black");
                    }
                    stopWatch.stop();
                }

                System.out.println("average " + stopWatch.getAverageMs() + " ms");
            }
        });
        }
//        Dataset1D timings = new Dataset1DArray(nTries);
//        timings.update().addData(Arrays.copyOfRange(stopWatch.getData(), 1, nTries)).commit();
//        Histogram1D hist = Histograms.createHistogram(timings);
//        hist.update(new Histogram1DUpdate().imageWidth(800).imageHeight(600));
//        ShowResizableImage.showHistogram(hist);
        executor.shutdown();
    }
}
