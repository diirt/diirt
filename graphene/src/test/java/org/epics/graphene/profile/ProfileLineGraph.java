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
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListLong;
import org.epics.util.array.ListMath;

/**
 *
 * @author carcassi
 */
public class ProfileLineGraph {

    public static void main(String[] args) {
        // Using array, 3.855865984000003, 100 samples, 15000 tries, 600x400
        // Using ArrayDouble, 3.9436457209999842 ms, 100 samples, 10000 tries, 600x400
        // Using array, 19.336473031333334 ms, 1000 samples, 1500 tries, 600x400
        // Using ArrayDouble, 17.84245149399999 ms, 1000 samples, 1500 tries, 600x400
        int nSamples = 100;
        int nTries = 5000;
        int imageWidth = 600;
        int imageHeight = 400;
        Random rand = new Random();

        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        //OrderedDataset2D dataset = org.epics.graphene.Arrays.lineData(waveform);
        OrderedDataset2D dataset = org.epics.graphene.Arrays.lineData(new ArrayDouble(waveform));
        
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
        System.out.println("total " + stopWatch.getTotalMs() + " ms");
        
        ListDouble timingsExcludeFirst = ListMath.rescale(ListMath.limit(stopWatch.getNanoTimings(), 1, stopWatch.getNanoTimings().size()), 0.000001, 0.0);
        ListDouble averages = ListMath.rescale(stopWatch.getNanoAverages(1), 0.000001, 0.0);
        
        Dataset1D timings = new Dataset1DArray(nTries);
        timings.update(new Dataset1DUpdate().addData(timingsExcludeFirst));
        Histogram1D hist = Histograms.createHistogram(timings);
        OrderedDataset2D line = org.epics.graphene.Arrays.lineData(timingsExcludeFirst);
        OrderedDataset2D averagedLine = org.epics.graphene.Arrays.lineData(averages);
        ShowResizableGraph.showHistogram(hist);
        ShowResizableGraph.showLineGraph(line);
        ShowResizableGraph.showLineGraph(averagedLine);
    }
}
