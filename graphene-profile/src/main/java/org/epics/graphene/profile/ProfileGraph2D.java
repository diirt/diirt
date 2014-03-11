/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import org.epics.graphene.profile.utils.SaveSettings;
import org.epics.graphene.profile.utils.Statistics;
import org.epics.graphene.profile.utils.StopWatch;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.graphene.Cell2DDataset;
import org.epics.graphene.Cell2DDatasets;
import org.epics.graphene.Graph2DRenderer;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.Histogram1D;
import org.epics.graphene.Histograms;
import org.epics.graphene.Point1DCircularBuffer;
import org.epics.graphene.Point1DDataset;
import org.epics.graphene.Point1DDatasetUpdate;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.RangeUtil;
import org.epics.graphene.profile.image.ShowResizableGraph;
import org.epics.graphene.profile.io.CSVWriter;
import org.epics.graphene.profile.io.DateUtils;
import org.epics.graphene.profile.io.DateUtils;
import org.epics.graphene.profile.utils.ProfileSettings;
import org.epics.graphene.profile.utils.Resolution;
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
        saveSettings = new SaveSettings();
        profileSettings = new ProfileSettings(this);
    }
    
    /**
     * Creates a graph profiler with specified properties.
     * @param testTimeSec the maximum time spent in the render loop
     */
    public ProfileGraph2D(int testTimeSec){
        this();
        this.profileSettings.setTestTime(testTimeSec);
    }
    
    /**
     * Default file path for all CSV log files of statistics.
     */
    public static final String LOG_FILEPATH = "ProfileResults\\";
    
    
    //Parameters
    private Resolution resolution = new Resolution(600, 400);
    private int        nPoints = 1000;
    
    //Statistics
    private int         nTries = 0;
    private StopWatch   stopWatch;    
    
    //Settings
    private ProfileSettings profileSettings;    
    private SaveSettings    saveSettings;
    
    
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
        stopWatch = new StopWatch(profileSettings.getMaxTries());
        stopWatch.setTimeType(profileSettings.getTimeType());
        
        //Data and Render Objects (Implemented in subclasses)
        S data = getDataset();
        T renderer = getRenderer(resolution.getWidth(), resolution.getHeight());
        
        if (profileSettings.getUpdate() != null){ renderer.update(profileSettings.getUpdate()); }
        
        //Creates the image buffer if parameter says to set it ouside of render loop
        BufferedImage image = null;
        Graphics2D graphics = null;
        if (!profileSettings.getBufferInLoop()){
            image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics = image.createGraphics();
        }
        
        nTries = 0;
        
        //System Time
        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(profileSettings.getTestTime()));   
                
        //Trials
        while (end.compareTo(Timestamp.now()) >= 0 && 
               !Thread.currentThread().isInterrupted() && 
               nTries < profileSettings.getMaxTries()) {
            
                    nTries++;
                    stopWatch.start();

                        //Create Image if necessary
                        if (profileSettings.getBufferInLoop()){
                            image = new BufferedImage(renderer.getImageWidth(), 
                                                      renderer.getImageHeight(), 
                                                      BufferedImage.TYPE_3BYTE_BGR);                    
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
    
    public abstract LinkedHashMap<String, Graph2DRendererUpdate> getVariations();
        
    
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
     *      <li>Timing Type</li>
     *      <li>Update Applied</li>
     *      <li>Comment about the data set rendered (useful for multi-dimensional or unusual data sets)</li>
     *      <li>Comment about the author performing the profile</li>
     *      <li>General comment about rendering</li>
     * </ol>     
     */
    public void saveStatistics(){
        //Verifies non-interrupted
        if (Thread.currentThread().isInterrupted()){
            return;
        }
        
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }
        
        
        //Data
        String fileName = LOG_FILEPATH + getLogFileName();
        File output;
        
        //Creates if necessary
        output = new File(fileName);
        if (!output.exists()){
            output = CSVWriter.createNewFile(fileName);

            CSVWriter.writeHeader(output, Arrays.asList(new Object[]{
                "Graph Type",
                "Date",
                stopWatch.getTitle(),
                "Number of Tries",
                "Number of Data Points",
                resolution.getTitle(),
                profileSettings.getTitle(),
                saveSettings.getTitle()
            }));
        }

        //Adds data
        CSVWriter.writeRow(output, Arrays.asList(new Object[]{
            getGraphTitle(),
            DateUtils.getDate(DateUtils.DateFormat.DELIMITED),
            stopWatch.getOutput(),
            nTries,
            getNumDataPoints(),
            resolution.getOutput(),
            profileSettings.getOutput(),
            saveSettings.getOutput()
        }));
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
        return getGraphTitle();
    }
    
    /**
     * Gets the settings to be saved to the output file
     * for a profile.
     * Some settings include:
     * <ul>
     *      <li>Author</li>
     *      <li>Dataset message</li>
     *      <li>Save message</li>
     * </ul>
     * @return the messages about settings information to
     *         be saved to an output file
     */
    public SaveSettings getSaveSettings(){
        return this.saveSettings;
    }
    
    public ProfileSettings getProfileSettings(){
        return this.profileSettings;
    }

    public Resolution getResolution(){
        return this.resolution;
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
}
