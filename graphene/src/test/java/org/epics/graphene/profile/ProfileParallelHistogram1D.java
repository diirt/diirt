/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.graphene.*;

/**
 * 
 * @author asbarber
 */
public class ProfileParallelHistogram1D extends ProfileHistogram1D{
    public ProfileParallelHistogram1D(){
        initDatasets();
    }
    private void initDatasets(){
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);                
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        datasetBuffer.update(update);
        
        dataset = Histograms.createHistogram(datasetBuffer);              
    }
    
    //Amount of Threads
    private static int nThreads = 4;
    
    //Dataset of each profiler
    private final int nSamples = 1000;
    private final Point1DCircularBuffer datasetBuffer = new Point1DCircularBuffer(nSamples);
    private Histogram1D dataset;    
    
    
    @Override
    protected final Histogram1D getDataset() {
      return dataset;
    }

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
