/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.graphene.*;

/**
 * Handles profiling for <code>Histogram1D</code> while synchronized.
 * Takes a <code>Histogram1D</code> dataset and repeatedly renders with several synchronized <code>AreaGraph2DRenderer</code> objects.
 * 
 * @author carcassi
 * @author asbarber
 */
public class ProfileLockHistogram1D extends ProfileHistogram1D{
    
    /**
     * Creates a tool to profile <code>Histogram1D</code> where multiple renders are performed with synchronization.
     */
    public ProfileLockHistogram1D(){
        initDatasets();
    }
    
    /**
     * Creates Gaussian random histogram data of the appropriate size.
     * Updates the circular point data buffer with the histogram data.
     */
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
    
    
    /**
     * Number of threads running histogram profilers.
     */
    private static int nThreads = 4;
    
    //Dataset of each profiler
    private final int nSamples = getNumDataPoints();
    private final Point1DCircularBuffer datasetBuffer = new Point1DCircularBuffer(nSamples);
    private Histogram1D dataset;    
    
    
    /**
     * Gets the histogram data used for each profiler.
     * @see <code>initDatasets()</code>
     * @return the histogram data to be drawn
     */
    @Override
    protected final Histogram1D getDataset() {
      return dataset;
    }
    
    /**
     * Draws the histogram data in multiple histogram profilers synchronized through the dataset buffer.
     * Primary method in the render loop.
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data the histogram data being drawn
     */  
    @Override
    public void render(Graphics2D graphics, AreaGraph2DRenderer renderer, Histogram1D data){
        synchronized(datasetBuffer){
            super.render(graphics, renderer, data);
        }
    }
    
    /**
     * Returns the name of the graph being profiled.
     * @return <code>Histogram1D</code> lock profile title
     */     
    @Override
    public String getGraphTitle() {
        return "LockHistogram1D";
    } 
    
    /**
     * Profiles for <code>Histogram1D</code> with lock synchronization, 
     * prints the statistics to the console and saves the statistics.
     * @param args console arguments -- no impact
     */  
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        
        for (int i = 0; i < nThreads; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    ProfileLockHistogram1D profiler = new ProfileLockHistogram1D();
                    profiler.profile();
                    System.out.println(profiler.getStatistics());
                }   
                
            });
        }
        
        executor.shutdown();        
    }        
}    
