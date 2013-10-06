/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import org.epics.graphene.*;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListLong;
import org.epics.util.array.ListMath;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class ProfileSparklineGraph2D {

    public static void main(String[] args) throws IOException {
        // Using array, 3.855865984000003, 100 samples, 15000 tries, 600x400
        // Using ArrayDouble, 3.9436457209999842 ms, 100 samples, 10000 tries, 600x400
        // Using array, 19.336473031333334 ms, 1000 samples, 1500 tries, 600x400
        // Using ArrayDouble, 17.84245149399999 ms, 1000 samples, 1500 tries, 600x400 18.67990659599997
        // Adding sorting and new impl, 12.17332916666666 ms, 1000 samples, 1500 tries, 600x400
        // Adding sorting and new impl, 2.679641561333325 ms, 100 samples, 15000 tries, 600x400
        
        // Before large array optimization, 16686.272965666667 ms, 1000000 samples, 3 tries, 600x400 LINEAR
        // Before large array optimization, 423.63001873999997 ms, 100000 samples, 100 tries, 600x400 LINEAR
        // Before large array optimization, 38.32643352900002 ms, 10000 samples, 100 tries, 600x400 LINEAR
        int nSamples = 500000;
        int imageWidth = 600;
        int imageHeight = 400;
        long testTimeSec = 20;
        int maxTries = 1000000;
        Random rand = new Random(1);

        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        Point2DDataset dataset = org.epics.graphene.Point2DDatasets.lineData(waveform);
        //OrderedDataset2D dataset = org.epics.graphene.Arrays.lineData(new ArrayDouble(waveform));
        
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(imageWidth, imageHeight, "Stuff");
        renderer.update(new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));

        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(testTimeSec));
        int nTries = 0;
        StopWatch stopWatch = new StopWatch(maxTries);
        
        BufferedImage image = null;
        while (end.compareTo(Timestamp.now()) >= 0) {
            nTries++;
            stopWatch.start();
            image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = image.createGraphics();
            renderer.draw(graphics, dataset);
            stopWatch.stop();
            
            if (image.getRGB(0, 0) == 0) {
                System.out.println("Black");
            }
        }
        
        ImageIO.write(image, "png", new File("result.png"));
        
        System.out.println("nTries " + nTries + " ");
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
