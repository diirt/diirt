/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import org.diirt.graphene.Cell1DDatasets;
import org.diirt.graphene.Cell1DDataset;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumbers;

/**
 * Handles profiling for <code>Histogram1D</code> in parallel (by threading).
 * Takes a <code>Histogram1D</code> dataset and repeatedly renders with several threads of <code>AreaGraph2DRenderer</code> objects.
 *
 * @author carcassi
 * @author asbarber
 */
public class ProfileParallelHistogram1D extends ProfileHistogram1D{

    /**
     * Creates a tool to profile <code>Histogram1D</code> where multiple renders are performed by several threads of histogram profilers.
     */
    public ProfileParallelHistogram1D(){
        initDatasets();
    }

    /**
     * Creates Gaussian random histogram data of the appropriate size.
     * Updates the circular point data buffer with the histogram data.
     */
    private void initDatasets(){
        //Creates data
        Random rand = new Random(1);
        double[] data = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            data[i] = rand.nextGaussian();
        }

        dataset = Cell1DDatasets.datasetFrom(new ArrayDouble(data), ListNumbers.linearList(0, 1, nSamples));
    }

    /**
     * Number of threads running histogram profilers.
     */
    private static int nThreads = 4;

    //Dataset of each profiler
    private final int nSamples = getNumDataPoints();
    private Cell1DDataset dataset;

    /**
     * Gets the histogram data used for each thread.
     * @see <code>initDatasets()</code>
     * @return the histogram data to be drawn
     */
    @Override
    protected final Cell1DDataset getDataset() {
      return dataset;
    }

    /**
     * Returns the name of the graph being profiled.
     * @return <code>Histogram1D</code> parallel profile title
     */
    @Override
    public String getGraphTitle() {
        return "ParallelHistogram1D";
    }

    /**
     * Profiles for <code>Histogram1D</code> with parallel threads,
     * prints the statistics to the console and saves the statistics.
     * @param args console arguments -- no impact
     */
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < nThreads; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    ProfileParallelHistogram1D profiler = new ProfileParallelHistogram1D();
                    profiler.profile();
                    System.out.println(profiler.getStatistics().toString());
                }

            });
        }

        executor.shutdown();
    }
}
