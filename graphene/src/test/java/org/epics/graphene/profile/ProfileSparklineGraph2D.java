/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import org.epics.graphene.*;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListMath;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

/**
 *
 * @authors asbarber, jkfeng, sjdallst
 */
public class ProfileSparklineGraph2D {

    public static void main(String[] args) throws IOException {
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
        
        //Profile Parameters
        int nSamples = 500000;
        int imageWidth = 600;
        int imageHeight = 400;
        long testTimeSec = 20;
        int maxTries = 1000000;
        Random rand = new Random(1);

        //Creates data
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        Point2DDataset dataset = org.epics.graphene.Point2DDatasets.lineData(waveform);
        
        //Creates renderer
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new SparklineGraph2DRendererUpdate());

        //Time Init
        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(testTimeSec));
        int nTries = 0;
        StopWatch stopWatch = new StopWatch(maxTries);
        
        //Renders repeatedly
        BufferedImage image;
        while (end.compareTo(Timestamp.now()) >= 0) {
            nTries++;
            stopWatch.start();
            
            //Render
            image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = image.createGraphics();
            renderer.draw(graphics, dataset);
            
            stopWatch.stop();
            
            //Bad render
            if (image.getRGB(0, 0) == 0) {
                System.out.println("Black");
            }
        }
        
        //Displays Statistics
        System.out.println("nTries " + nTries + " ");
        System.out.println("average " + stopWatch.getAverageMs() + " ms");
        System.out.println("total " + stopWatch.getTotalMs() + " ms");
        
        //Displays Graph of times
        ListDouble timingsExcludeFirst = ListMath.rescale(ListMath.limit(stopWatch.getNanoTimings(), 1, stopWatch.getNanoTimings().size()), 0.000001, 0.0);
        ListDouble averages = ListMath.rescale(stopWatch.getNanoAverages(1), 0.000001, 0.0);
        
        Point1DCircularBuffer timings = new Point1DCircularBuffer(nTries);
        timings.update(new Point1DDatasetUpdate().addData(timingsExcludeFirst));
        Histogram1D hist = Histograms.createHistogram(timings);
        Point2DDataset line = org.epics.graphene.Point2DDatasets.lineData(timingsExcludeFirst);
        Point2DDataset averagedLine = org.epics.graphene.Point2DDatasets.lineData(averages);
        ShowResizableGraph.showHistogram(hist);
        ShowResizableGraph.showLineGraph(line);
        ShowResizableGraph.showLineGraph(averagedLine);
    }
}
