/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    public void profile(){        
        //Timing
        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(testTimeSec));        
        stopWatch = new StopWatch(maxTries);
        
        nTries = 0;
        
        //Data and Render Objects (Implemented in subclasses)
        S data = getDataset();
        T renderer = getRenderer(imageWidth, imageHeight);
        
        //Creates the image buffer if parameter says to set it ouside of render loop
        BufferedImage image = null;
        Graphics2D graphics = null;
        if (!bufferInLoop){
            image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics = image.createGraphics();
        }
        
        //Trials
        while (end.compareTo(Timestamp.now()) >= 0) {
            nTries++;
            stopWatch.start();
            
                //Create Image if necessary
                if (bufferInLoop){
                    image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);                    
                    graphics = image.createGraphics();
                }
                
                //Subclass render
                render(graphics, renderer, data);
            
            stopWatch.stop();
            
            //Buffer clears
            if (image != null && image.getRGB(0, 0) == 0){
                System.out.println("Black");
            }
        }
    }    
    
    
    public static final String LOG_FILEPATH = "ProfileResults\\";
    
    //Profile Parameters (Customizable)
    private int maxTries    = 1000000,
                testTimeSec = 20;
    
    private int nPoints = 1000;
    private boolean bufferInLoop = false;
    
    //Pofile Parameters (Uncustomizable)
    private int imageWidth  = 600,
                imageHeight = 400;
    
    //Statistics
    private int         nTries = 0;
    private StopWatch   stopWatch;    
    
    //Save Parameters
    private String datasetMessage = "",
                   saveMessage = "";
    
      
    //During-Profile Sections
    protected abstract S getDataset();
    protected abstract T getRenderer(int imageWidth, int imageHeight);
    protected abstract void render(Graphics2D graphics, T renderer, S data);
    
    //Post-Profile Options
    public Statistics getStatistics(){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        return new Statistics(nTries, stopWatch.getAverageMs(), stopWatch.getTotalMs());
    }
    public void printStatistics(){
        System.out.println(getGraphTitle());
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
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        //Format output string:
        //"graphType","date","average time","total time","number of tries","numDataPoints","datasetComment","message",
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        String quote = "\"";
        String delim = " ";
        String results = quote + getGraphTitle() + quote + delim +
                         quote + dateFormat.format(new Date()) + quote + delim +
                                 stopWatch.getAverageMs() + delim +
                                 stopWatch.getTotalMs() + delim +
                                 nTries + delim +
                                 getNumDataPoints() + delim +
                         quote + getDatasetMessage() + quote + delim +
                         quote + getSaveMessage() + quote + delim;
        
        //Ensures file is created
        File outputFile = new File(LOG_FILEPATH + getLogFileName());
        if (!outputFile.exists()){
            createLog();
        }
        
        //Write to file
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)))) {
                out.println(results);
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Output errors exist.");
        }
    }
    private void createLog(){
        File outputFile = new File(LOG_FILEPATH + getLogFileName());
        try {
            //Creates file
            outputFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ProfileGraph2D.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Header
        String quote = "\"";
        String delim = " ";
        String header = quote + "Graph Type" + quote + delim +
                        quote + "Date" + quote + delim +
                        quote + "Average Time (ms)" + quote + delim +
                        quote + "Total Time (ms)" + quote + delim +
                        quote + "Number of Tries" + quote + delim +
                        quote + "Number of Data Points" + quote + delim +
                        quote + "Datatset Comments" + quote + delim +
                        quote + "General Message" + quote + delim;
        
        //Write to file
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)))) {
                out.println(header);
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Output errors exist.");
        }    
    }
    
    //Save Parameter Getters
    public abstract String getGraphTitle();
    public String getLogFileName(){
        return getGraphTitle() + ".csv";
    }
    public String getDatasetMessage(){
        return datasetMessage;
    }
    public String getSaveMessage(){
        return saveMessage;
    }
    
    //Save Parameter Setters
    public void setDatasetMessage(String message){
        this.datasetMessage = message;
    }
    public void setSaveMessage(String message){
        this.saveMessage = message;
    }
    
    //Test Parameter Getters
    public int getNumDataPoints(){
        return nPoints;
    }    
    public int getImageWidth(){
        return imageWidth;
    }
    public int getImageHeight(){
        return imageHeight;
    }
    public int getMaxTries(){
        return maxTries;
    }
    public int getTestTime(){
        return testTimeSec;
    }
    public boolean getBufferInLoop(){
        return this.bufferInLoop;
    }
    
    //Test Parameter Setters
    public void setNumDataPoints(int nPoints){
        this.nPoints = nPoints;
    }
    public void setImageWidth(int imageWidth){
        this.imageWidth = imageWidth;
    }   
    public void setImageHeight(int imageHeight){
        this.imageHeight = imageHeight;
    }
    public void setMaxTries(int maxTries){
        this.maxTries = maxTries;
    }
    public void setTestTime(int testTimeSec){
        this.testTimeSec = testTimeSec;
    }
    public void setBufferInLoop(boolean bufferInLoop){
        this.bufferInLoop = bufferInLoop;
    }
    
    //Dataset Generators
    public static Point1DDataset makePoint1DGaussianRandomData(int nSamples){        
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
    public static Point2DDataset makePoint2DGaussianRandomData(int nSamples){
        double[] waveform = new double[nSamples];
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);        
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }
        
        return org.epics.graphene.Point2DDatasets.lineData(waveform);
    }
    public static Cell2DDataset makeCell2DGaussianRandomData(int xSamples, int ySamples){
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
    public static Histogram1D makeHistogram1DGaussianRandomData(int nSamples){
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
