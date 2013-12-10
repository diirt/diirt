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
 * Handles the profiling for testing rendering (specifically the draw) of a <code>Graph2DRenderer</code>.
 * The base class for all graph profilers.
 * Has parameter T that is a graph renderer.
 * Has parameter S that is the dataset associated with T.
 * 
 * A profiler creates a loop in which a Graph2DRenderer perform multiple render operations.
 * Various options are provided to handle the profile statistics.
 * 
 * @param <T> type of graph render being profiled that is subclass of <code>Graph2DRenderer</code>
 * @param <S> dataset type that is associated with <T> the graph renderer
 * 
 * @author asbarber
 */
public abstract class ProfileGraph2D<T extends Graph2DRenderer, S> {
    
    /**
     * Creates a graph profiler.
     */
    public ProfileGraph2D(){    
    }
    
    /**
     * Creates a graph profiler with specified properties.
     * @param maxTries the maximum attempts for rendering
     * @param testTimeSec the maximum time spent in the render loop
     */
    public ProfileGraph2D(int maxTries, int testTimeSec){
        this.maxTries = maxTries;
        this.testTimeSec = testTimeSec;
    }
    
    /**
     * Default file path for all CSV log files of statistics.
     */
    public static final String LOG_FILEPATH = "ProfileResults\\";
    
    //Profile Parameters (Customizable)
    private int maxTries    = 1000000,
                testTimeSec = 20;
    private int nPoints     = 1000;
    
    private boolean bufferInLoop = false;
    
    private int imageWidth  = 600,
                imageHeight = 400;
    
    //Statistics
    private int         nTries = 0;
    private StopWatch   stopWatch;    
    
    //Save Parameters
    private String datasetMessage = "",
                   saveMessage = "";
    
    /**
     * Performs the necessary operation to 'profile' a graph renderer.
     * Gathers statistics about the rendering time of a Graph2DRenderer.
     * 
     * <ol>
     *      <li>Makes the data</li>
     *      <li>Makes the renderer</li>
     *      <li>Repeatedly renders</li>
     * </ol>
     */
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

    
    //During-Profile Sections
    
    /**
     * The data that to be rendered in the render loop.
     * Precondition: getRenderer() is capable of rendering getDataset().
     *               Thus type T is capable of rendering type S.
     * 
     * A useful helper method is getNumDataPoints.
     * The size of the returned data set should match the size specified by getNumDataPoints.
     * @return a set of data associated with T
     */
    protected abstract S getDataset();
    
    /**
     * The renderer used in the render loop.
     * Precondition: <code>getRenderer()</code> is capable of rendering <code>getDataset()</code>.
     *               Thus type <code>T</code> is capable of rendering type <code>S</code>.
     * @param imageWidth pixel width of rendered image
     * @param imageHeight pixel height of rendered image
     * @return a <code>Graph2DRenderer</code> associated with data S
     */
    protected abstract T getRenderer(int imageWidth, int imageHeight);
    
    /**
     * The primary method in the profiling render loop.
     * Override this method to test the draw method of 'renderer'.
     * 
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data what is drawn
     */
    protected abstract void render(Graphics2D graphics, T renderer, S data);
    
    
    //Post-Profile Options
    
    /**
     * Gets profile statistics. 
     * Returns null if the profile method has not been called and no statistics exist.
     * 
     * @return statistical information about profiling
     */
    public Statistics getStatistics(){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        return new Statistics(nTries, stopWatch.getAverageMs(), stopWatch.getTotalMs());
    }
    
    /**
     * Prints the graph title and then the statistics.
     * Does not print anything if statistics do not exist.
     */
    public void printStatistics(){
        Statistics stats = getStatistics();
        
        if (stats != null){
            System.out.println(getGraphTitle());            
            stats.printStatistics();
        }
    }
    
    /**
     * Graphs the profile statistics in a histogram, line graph, and averaged line graph.
     */
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
    
    /**
     * Writes the profile statistics to a CSV file designated to the profile graph.
     * Appends the statistics to the end of the CSV file.
     * 
     * The file name is designated by getLogFileName(). 
     * Each subclass should thus have its own CSV file.
     * If a CSV file does not exist, the file is created.
     * 
     * If statistics do no exist, no operations are performed.
     * If errors occur in IO, a console message is printed.
     * 
     * The format for the appended record is:
     * "graphType","date","timeAverage","timeTotal","numberAttempts","numDataPoints","imageWidth","imageHeight","datasetComment","generalMessage"
     * 
     * The delimiting is:
     * <ul>
     *      <li>Non-numeric components enclosed in quotes</li>
     *      <li>Numeric components not enclosed
     *      <li>Components separated by commas (no spaces)</li>
     * </ul>
     * 
     * The components are:
     * <ol>
     *      <li>Graph title (type of graph)</li>
     *      <li>Date</li>
     *      <li>Average time rendering</li>
     *      <li>Total time rendering</li>
     *      <li>Number of render attempts</li>
     *      <li>Number of data points</li>
     *      <li>Width of image rendered</li>
     *      <li>Height of image rendered</li>
     *      <li>Comment about the data set rendered (useful for multi-dimensional or unusual data sets)</li>
     *      <li>General comment about rendering</li>
     * </ol>     
     * 
     * It is important that this method has a parallel format to createLog so that
     * the file writes properly and can be read/open to make sense.
     */
    public void saveStatistics(){
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        //Format output string:
        //  "graphType","date","average time","total time","number of tries","numDataPoints","Image Width","Image Height","datasetComment","message",
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        String quote = "\"";
        String delim = ",";
        String results = quote + getGraphTitle() + quote + delim +
                         quote + dateFormat.format(new Date()) + quote + delim +
                                 stopWatch.getAverageMs() + delim +
                                 stopWatch.getTotalMs() + delim +
                                 nTries + delim +
                                 getNumDataPoints() + delim +
                                 getImageWidth() + delim +
                                 getImageHeight() + delim +
                         quote + getDatasetMessage() + quote + delim +
                         quote + getSaveMessage() + quote;
        
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
    
    /**
     * Creates a CSV file designated to the profile graph.
     * Makes the file and appends the header.
     * 
     * Precondition: The designated file does not exist.
     * 
     * The file name is designated by getLogFileName(). 
     * Each subclass should thus have its own CSV file.
     * 
     * If errors occur in creating the file, the exception is thrown and is logged.
     * If errors occur in IO, a console message is printed.
     * 
     * The header delimiting is:
     * <ul>
     *      <li>Components are enclosed in quotes</li>
     *      <li>Components separated by commas (no spaces)</li>
     * </ul>
     * 
     * The header components are:
     * <ol>
     *      <li>Graph Type</li>
     *      <li>Date</li>
     *      <li>Average Time (ms)</li>
     *      <li>Total Time (ms)</li>
     *      <li>Number of tries</li>
     *      <li>Number of Data Points</li>
     *      <li>Image Width</li>
     *      <li>Image Height</li>
     *      <li>Dataset Comments</li>
     *      <li>General Message</li>
     * </ol>     
     */
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
        String delim = ",";
        String header =quote + "Graph Type" + quote + delim +
                       quote + "Date" + quote + delim +
                       quote + "Average Time (ms)" + quote + delim +
                       quote + "Total Time (ms)" + quote + delim +
                       quote + "Number of Tries" + quote + delim +
                       quote + "Number of Data Points" + quote + delim +
                       quote + "Image Width" + quote + delim +
                       quote + "Image Height" + quote + delim +
                       quote + "Dataset Comments" + quote + delim +
                       quote + "General Message" + quote;
        
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
    
    /**
     * Gets the type of graph renderer.
     * Used in printing and saving statistics.
     * Example: "LineGraph2D"
     * @return the title of the graph renderer being profiled
     */
    public abstract String getGraphTitle();
    
    /**
     * Gets the name of the CSV file to save statistics to.
     * Derived from <code>getGraphTitle()</code>.
     * @return the file name (not file path) of the CSV log file
     */
    public String getLogFileName(){
        return getGraphTitle() + ".csv";
    }
    
    /**
     * Gets the comment associated with the data set.
     * This comment will be written to the CSV log file when saving the statistics.
     * 
     * This is appropriate for discussing the distribution of the data, dimensions of the data, etc.
     * @return message about the data set
     */
    public String getDatasetMessage(){
        return datasetMessage;
    }
    
    /**
     * Gets the general comment associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     * 
     * This is appropriate for discussing the parameters of the renderer, etc.
     * @return general message about the profiling results
     */
    public String getSaveMessage(){
        return saveMessage;
    }
    
    
    //Save Parameter Setters
    
    /**
     * Set the comment associated with the data set.
     * This comment will be written to the CSV log file when saving the statistics.
     * 
     * This is appropriate for discussing the distribution of the data, dimensions of the data, etc.
     * 
     * @param message comment about the data
     */
    public void setDatasetMessage(String message){
        this.datasetMessage = message;
    }
    
    /**
     * Set the general comment associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     * 
     * This is appropriate for discussing the parameters of the renderer, etc.
     * 
     * @param message general comment about the profiling
     */    
    public void setSaveMessage(String message){
        this.saveMessage = message;
    }
    
    
    //Test Parameter Getters
    
    /**
     * Gets the size of the data set.
     * Useful for creating the data set of the appropriate size.
     * Used in saving statistics to the CSV log file.
     * 
     * @return size of the data set in rendering
     */
    public int getNumDataPoints(){
        return nPoints;
    }    
    
    /**
     * Gets the width of the image rendered.
     * Useful for creating the graph renderer.
     * Used in saving statistics to the CSV log file.
     * 
     * @return image width in pixels 
     */
    public int getImageWidth(){
        return imageWidth;
    }
    
    /**
     * Gets the height of the image rendered.
     * Useful for creating the graph renderer.
     * Used in saving statistics to the CSV log file.
     * 
     * @return image height in pixels 
     */    
    public int getImageHeight(){
        return imageHeight;
    }
    
    /**
     * Gets the number of times the profiler will try to render.
     * Used in saving statistics to the CSV log file.
     * 
     * @return max tries the render loop will be run
     */
    public int getMaxTries(){
        return maxTries;
    }
    
    /**
     * Gets the time limit (seconds) for how long the profiler will try to render.
     * Used in saving statistics to the CSV log file.
     * 
     * @return max time the render loop will be run (in seconds)
     */
    public int getTestTime(){
        return testTimeSec;
    }
    
    /**
     * Gets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @return whether the image buffer is created in the render loop
     */
    public boolean getBufferInLoop(){
        return this.bufferInLoop;
    }
    
    
    //Test Parameter Setters
    
    /**
     * Sets the size of the data set.
     * Useful for creating the data set of the appropriate size.
     * Used in saving statistics to the CSV log file.
     * 
     * @param nPoints size of the data set in rendering
     */
    public void setNumDataPoints(int nPoints){
        this.nPoints = nPoints;
    }
    
    /**
     * Sets the width of the image rendered.
     * Useful for creating the graph renderer.
     * Used in saving statistics to the CSV log file.
     * 
     * @param imageWidth image width in pixels 
     */    
    public void setImageWidth(int imageWidth){
        this.imageWidth = imageWidth;
    }   
    
    /**
     * Sets the height of the image rendered.
     * Useful for creating the graph renderer.
     * Used in saving statistics to the CSV log file.
     * 
     * @param imageHeight image height in pixels 
     */      
    public void setImageHeight(int imageHeight){
        this.imageHeight = imageHeight;
    }
    
    /**
     * Sets the number of times the profiler will try to render.
     * Used in saving statistics to the CSV log file.
     * 
     * @param maxTries max tries the render loop will be run in
     */    
    public void setMaxTries(int maxTries){
        this.maxTries = maxTries;
    }
    
    /**
     * Sets the time limit (seconds) for how long the profiler will try to render.
     * Used in saving statistics to the CSV log file.
     * 
     * @param testTimeSec max time the render loop will be run in seconds
     */    
    public void setTestTime(int testTimeSec){
        this.testTimeSec = testTimeSec;
    }
    
    /**
     * Sets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @param bufferInLoop whether the image buffer is created in the render loop
     */    
    public void setBufferInLoop(boolean bufferInLoop){
        this.bufferInLoop = bufferInLoop;
    }
    
    
    //Dataset Generators
    
    /**
     * Generates Point1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random data</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
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
    
    /**
     * Generates Point2D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random y-values</li>
     *      <li>y-values are sorted ascending and plotted against sorted index (sorted index is x-value)</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
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
    
    /**
     * Generates Cell2D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data is xSamples * ySamples<li>
     *      <li>Random cell data</li>
     *      <li>Gaussian distribution of values from 0 to 1</li>
     * @param xSamples number of x-cells in data
     * @param ySamples number of y-cells in data
     * @return a set of data to be drawn
     */    
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
    
    /**
     * Generates Histogram1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random values</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */    
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
