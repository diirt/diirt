/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.epics.graphene.Cell2DDataset;
import org.epics.graphene.Cell2DDatasets;
import org.epics.graphene.Graph2DRenderer;
import org.epics.graphene.Histogram1D;
import org.epics.graphene.Histograms;
import org.epics.graphene.Point1DCircularBuffer;
import org.epics.graphene.Point1DDataset;
import org.epics.graphene.Point1DDatasetUpdate;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.RangeUtil;
import org.epics.graphene.ShowResizableGraph;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListMath;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

/**
 *
 * @author asbarber
 */
public abstract class ProfileGraph2D<T extends Graph2DRenderer, S> {
    public ProfileGraph2D(){
        
    }
    public ProfileGraph2D(int maxTries, int testTimeSec){
        this.maxTries = maxTries;
        this.testTimeSec = testTimeSec;
    }
    
    
    public static final String LOG_FILENAME = "src\\test\\resources\\org\\epics\\graphene\\log.csv";
    
    //Profile Parameters (Customizable)
    private int maxTries    = 1000000,
                testTimeSec = 20;
    
    //Pofile Parameters (Uncustomizable)
    private int imageWidth  = 600,
                imageHeight = 400;
    
    //Statistics
    private int         nTries = 0;
    private StopWatch   stopWatch;    
    
    public void profile(){        
        //Timing
        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(testTimeSec));        
        stopWatch = new StopWatch(maxTries);
        
        nTries = 0;
        
        //Data and Render Objects (Implemented in subclasses)
        S data = getDataset();
        T renderer = getRenderer(imageWidth, imageHeight);

        //Trials
        while (end.compareTo(Timestamp.now()) >= 0) {
            nTries++;
            stopWatch.start();
            render(renderer, data);
            stopWatch.stop();
        }
    }
      
    protected abstract S getDataset();
    protected abstract T getRenderer(int imageWidth, int imageHeight);
    protected abstract void render(T renderer, S data);
    
    public Statistics getStatistics(){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        return new Statistics(nTries, stopWatch.getAverageMs(), stopWatch.getTotalMs());
    }
    public void printStatistics(){
        getStatistics().printStatistics();
    }
    public void graphStatistics(){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
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
    public void saveStatistics(){
        saveStatistics("");
    }
    public void saveStatistics(String message){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        //Format output string:
        //"graphType","date","average time","total time","number of tries","numDataPoints","dataComment","message",
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        String quote = "\"";
        String results = quote + getGraphTitle() + quote + "," +
                         quote + dateFormat.format(new Date()) + quote + "," +
                         quote + stopWatch.getAverageMs() + " ms" + quote + "," +
                         quote + stopWatch.getTotalMs() + " ms" + quote + "," +
                         quote + nTries + quote + "," +
                         quote + getNumDataPoints() + quote + "," +
                         quote + getDataPointMessage() + quote + "," +
                         quote + message + quote + ",";
        
        //Write to file
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILENAME, true)))) {
                out.println(results);
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Output errors exist.");
        }
    }
    
    public int getNumDataPoints(){
        return 1000;
    }
    public abstract String getGraphTitle();
    public String getDataPointMessage(){
        return "";
    }
    
    public static Point1DDataset makePoint1DData(int nSamples){        
        Point1DCircularBuffer dataset = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        dataset.update(update);   
        
        return dataset;
    }
    public static Point2DDataset makePoint2DData(int nSamples){
        double[] waveform = new double[nSamples];
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);        
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }
        
        return org.epics.graphene.Point2DDatasets.lineData(waveform);
    }
    public static Cell2DDataset makeCell2DData(int xSamples, int ySamples){
        int nSamples = xSamples * ySamples;
        double[] waveform = new double[nSamples];
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);        
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }
        
        return Cell2DDatasets.linearRange(new ArrayDouble(waveform), RangeUtil.range(0, xSamples), xSamples, RangeUtil.range(0, ySamples), ySamples);
    }
    public static Histogram1D makeHistogram1DData(int nSamples){
        Point1DCircularBuffer dataset = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);                
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        dataset.update(update);
        
        return Histograms.createHistogram(dataset);        
    }
}
