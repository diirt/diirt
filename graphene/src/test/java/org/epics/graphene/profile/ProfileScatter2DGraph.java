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
public class ProfileScatter2DGraph {

    public static void main(String[] args) {
        int nSamples = 1000;
        int nTries = 3000;
        int imageWidth = 600;
        int imageHeight = 400;
        Random rand = new Random();

        double[] x = new double[nSamples];
        double[] y = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            x[i] = rand.nextGaussian();
            y[i] = rand.nextGaussian();
        }
        Point2DDataset dataset = org.epics.graphene.Point2DDatasets.lineData(x,y);
        //OrderedDataset2D dataset = org.epics.graphene.Arrays.lineData(new ArrayDouble(waveform));
        
        Scatter2DGraphRenderer renderer = new Scatter2DGraphRenderer(imageWidth, imageHeight);
        
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
