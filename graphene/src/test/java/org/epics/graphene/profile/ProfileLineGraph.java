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
public class ProfileLineGraph {

    public static void main(String[] args) {
        int nSamples = 100;
        int nTries = 5000;
        int imageWidth = 600;
        int imageHeight = 400;
        Random rand = new Random();

        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        OrderedDataset2D dataset = org.epics.graphene.Arrays.lineData(waveform);
        
        LineGraphRenderer renderer = new LineGraphRenderer(imageWidth, imageHeight);
        renderer.update(new LineGraphRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        
        StopWatch stopWatch = new StopWatch(nTries);
        
        for (int i = 0; i < nTries; i++) {
            stopWatch.start();
            BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = image.createGraphics();
            renderer.draw(graphics, dataset);
            stopWatch.stop();
            
            if (image.getRGB(0, 0) == 0) {
                System.out.println("Black");
            }
        }
        
        System.out.println("average " + stopWatch.getAverageMs() + " ms");
        Dataset1D timings = new Dataset1DArray(nTries);
        timings.update(new Dataset1DUpdate().addData(Arrays.copyOfRange(stopWatch.getData(), 1, nTries)));
        Histogram1D hist = Histograms.createHistogram(timings);
        ShowResizableImage.showHistogram(hist);
    }
}
