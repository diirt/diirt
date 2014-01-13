/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.epics.graphene.Graph2DRenderer;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.Point2DDatasets;
import org.epics.graphene.ShowResizableGraph;

/**
 *
 * @author asbarber
 */
public class MultiLevelProfiler<T extends Graph2DRenderer, S> {
    private ProfileGraph2D<T, S> profiler;
    
    private List<Resolution> resolutions;
    private List<Integer> datasetSizes;
        
    private Map<Resolution, Map<Integer, Statistics>> results;
      
    /**
     * Creates a object to profile a <code>Graph2DRenderer</code>
     * using a <code>ProfileGraph2D</code> and analyzing performance
     * through various sets of renderings at specified intervals
     * of data sizes and image resolution sizes.
     * 
     * @param profiler graph renderer with given profile settings to
     * do extensive testing on
     */
    public MultiLevelProfiler(ProfileGraph2D<T, S> profiler){
        this.profiler = profiler;
        this.results = new HashMap<>();
    }
    
    //Profile Running
    
    /**
     * Runs through the list of dataset sizes paired with every element
     * from the list of image resolutions to profile a
     * <code>ProfileGraph2D</code> to analyze the performance of the graph
     * renderer and how the performance is effected by data size and
     * image size.
     * <br>
     * Precondition: the set of dataset sizes and the set of resolutions to
     * test are both not null and not empty.
     * <br>
     * A mapping of results is created to link dataset size, resolution,
     * and statistics.
     * This is done by creating a map, <i>map_A</i>, for a link between
     * the statistics and the dataset size, and mapping <i>map_A</i>
     * to a second map for every resolution.
     * 
     * @see #getResults() 
     */
    public void run(){
        if (datasetSizes == null || datasetSizes.isEmpty()){
            throw new NullPointerException("Use the setter to list dataset sizes.");
        }
        if (resolutions == null || resolutions.isEmpty()){
            throw new NullPointerException("Use the setter to list resolutions.");
        }
        
        //Shows estimated time
        int estimatedTime = datasetSizes.size() * resolutions.size() * profiler.getTestTime();
        JOptionPane.showMessageDialog(null, "The estimated run time is " + estimatedTime + " seconds.");
        
        //Loop through combinations of settings
        for (int r = 0; r < resolutions.size(); r++){
            
            HashMap<Integer, Statistics> map = new HashMap<>();
            
                for (int s = 0; s < datasetSizes.size(); s++){

                    //Apply settings
                    profiler.setNumDataPoints(datasetSizes.get(s));
                    profiler.setImageWidth(resolutions.get(r).getWidth());
                    profiler.setImageHeight(resolutions.get(r).getHeight());

                    //Profiler
                    profiler.profile();

                    //Track results (dataset size and statistics)
                    map.put(datasetSizes.get(s), profiler.getStatistics());
                }
            
            //Put dataSize & statistics map into resolution set
            results.put(resolutions.get(r), map);
        }
    }
    
    
    //Post-Run Options
    
    /**
     * A mapping that gets the results of profiler that
     * maps a statistics result to every dataset size that is
     * mapped to every resolution.
     * <p>
     * The keys to the outer map are the set of resolutions.
     * The keys to the inner map are the set of dataset sizes.
     * The value of the outer map is the inner map.
     * The value of the inner map is the statistics of the profiling.
     * 
     * Precondition: <code>#run()</code> has been called.
     * 
     * @return a mapping that gets the results of running the profiler
     * associated resolution sizes to dataset sizes to statistical timing
     * results
     */
    public Map<Resolution, Map<Integer, Statistics>> getResults(){
        if (results == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        
        return results;
    }
    
    /**
     * Forms a set of point 2D data for each resolution profiled
     * where the point is composed of the number of data points profiled
     * and the average profile time.
     * 
     * @return lines for each resolution composed of average run time and
     * dataset size points
     */
    public List<Point2DDataset> getStatisticLineData(){
        List<Point2DDataset> allLines = new ArrayList<>();
        
        
        //Resolution[] keys for resolutions
        Object[] resolutionKeys = results.keySet().toArray();
        
        
        //Resolution Map
        for (int i = 0; i < results.size(); i++){
            Resolution resolution = (Resolution) resolutionKeys[i];
            Map<Integer, Statistics> map = results.get(resolution);
            
            //Integer[] keys for dataset sizes
            Object[] sizeKeys = map.keySet().toArray();
            double[] sizes = new double[map.size()];
            double[] times = new double[map.size()];
            
                //Dataset Size Map
                for (int j = 0; j < map.size(); j++){
                    Integer size = (Integer) sizeKeys[j];
                    Statistics stats = map.get(size);
                    
                    sizes[j] = size;
                    times[j] = stats.getAverageTime();
                }
                
            Point2DDataset data = Point2DDatasets.lineData(sizes, times);
            allLines.add(data);
        }
        

        return allLines;
    }
    
    /**
     * Displays a separate <code>frame</code> for every profile operation
     * performed.
     * 
     * Precondition: <code>#run()</code> has been called.
     * 
     */
    public void graphStatistics(){
        if (results == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        
        List<Point2DDataset> lines = getStatisticLineData();
        
        for (Point2DDataset data: lines){
            ShowResizableGraph.showLineGraph(data);            
        }
    }
    
    /**
     * Creates a table (.CSV format) to display the results,
     * with the dataset sizes as the row heading and the 
     * resolution as the column heading.
     * <p>
     * Saves the CSV file to the same directory as general profile results,
     * with the specific profile graph type as well as the <b>date</b>.
     * 
     * Precondition: results is non-null and not empty, meaning profiling 
     * has been run.
     */
    public void saveStatistics(){
       if (results == null){
           throw new NullPointerException("Profiling has not been run.");
       }
       //Should not occur since resolutions must be non-empty
       if (results.isEmpty()){
           return;
       }
       
       //Assumption: the set of keys for dataset sizes are the same
       //for each line (the Map<Integer,Statistics> keys are equivalent for
       //each resolutioin)
       Resolution[] resKeys = results.keySet().toArray(new Resolution[0]);
       Integer[] sizeKeys = results.get(resKeys[0]).keySet().toArray(new Integer[0]);
       String[] rows = new String[sizeKeys.length];
       String header = ",";
       String delim = ",";      
       
       Calendar today = Calendar.getInstance();       
       String date = today.get(Calendar.YEAR) + "-" +               
                     (today.get(Calendar.MONTH) + 1) + "-" +
                     today.get(Calendar.DAY_OF_MONTH) + " " +
                     today.get(Calendar.HOUR) + "." +
                     today.get(Calendar.MINUTE);

       //Sorts keys so table has sorted columns/rows
       Arrays.sort(resKeys);
       Arrays.sort(sizeKeys);
       
       //Makes row header
       for (Resolution res: resKeys){
           header += res.toString() + delim;
       }
       
       //For all dataset sizes
       for (int i = 0; i < sizeKeys.length; i++){
           //Include the dataset size in the row
           rows[i] = "";
           rows[i] += sizeKeys[i] + delim;
           
           //Finds the timings for the dataset size for all resolutions
           for (int j = 0; j < resKeys.length; j++){
               //Profile Time
               double avgTime = results.get(resKeys[j]).get(sizeKeys[i]).getAverageTime();
               
               //Adds profile time formatted to the row
               rows[i] +=  String.format("%.3f", avgTime) + delim;
           }
       }
       
       //Creates file
       File outputFile = new File(ProfileGraph2D.LOG_FILEPATH + 
                                  profiler.getGraphTitle() +
                                  " - Table" + 
                                  "(" +
                                  date +
                                  ")" +
                                  ".csv");
       
        try {
            outputFile.createNewFile();
            
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
            
            //Prints header
            out.println(header);
            
            //Prints rows
            for (String row: rows){
                out.println(row);
            }
            
            out.close();
        } catch (IOException ex) {
            System.err.println("Output errors exist.");
        }
    }
    
    
    //Test Paramter Setters
    
    /**
     * Sets the resolution sizes to be profiled.
     * Precondition: resolution set is not null and not empty
     * @param resolutions set of resolutions to profile
     * 
     * @see #defaultResolutions() 
     * @see #run() 
     * @see org.epics.graphene.profile.ProfileGraph2D#setImageWidth(int)       
     * @see org.epics.graphene.profile.ProfileGraph2D#setImageHeight(int) 
     */
    public void setImageSizes(List<Resolution> resolutions){
        if (resolutions == null){
            throw new IllegalArgumentException("The list of image resolutions must be non-null.");
        }
        if (resolutions.isEmpty()){
            throw new IllegalArgumentException("The list of image resolutions must be non-empty.");
        }        
        
        this.resolutions = resolutions;
    }

    /**
     * Sets the dataset sizes to be profiled.
     * The <code>ProfileGraph2D</code> field of this object will profile
     * repeatedly updating the number of data points in rendering.
     * @param nPoints 
     * 
     * @see #defaultDatasetSizes() 
     * @see #run() 
     * @see org.epics.graphene.profile.ProfileGraph2D#setNumDataPoints(int) 
     */
    public void setDatasetSizes(List<Integer> nPoints){
        if (nPoints == null){
            throw new IllegalArgumentException("The list of dataset sizes must be non-null.");
        }
        if (nPoints.isEmpty()){
            throw new IllegalArgumentException("The list of dataset sizes must be non-empty.");
        }        
        
        this.datasetSizes = nPoints;
    }
    
    
    //Defaults
    
    /**
     * Default set of dataset sizes to test profiling on, 
     * on a logarithmic scale.
     * The values are 10^1, 10^2, ... , 10^6.
     * 
     * @return a list with values of 10^n for n = 1 to n = 6
     * (a logarithmic scale from 10 to 1,000,000)
     */
    public static List<Integer> defaultDatasetSizes(){
        int n = 6;
        int base = 10;
        List<Integer> sizes = new ArrayList<>(n);
        
        for (int power = 1; power <= n; power++){
            sizes.add((int) Math.pow(base, power));
        }
        
        return sizes;
    }

    /**
     * Default set of resolutions (image width and height set) to test
     * profiling on, based on standard computer resolutions.
     * @return  a list with standard computer screen resolutions
     * (160x120, 320x240, ... 1600x1200)
     */
    public static List<Resolution> defaultResolutions(){
        List<Resolution> r = new ArrayList<>();
        
        r.add(new Resolution(160, 120));
        r.add(new Resolution(320, 240));
        r.add(new Resolution(640, 480));
        r.add(new Resolution(800, 600));
        r.add(new Resolution(1024, 768));
        r.add(new Resolution(1440, 1080));
        r.add(new Resolution(1600, 1200));
        return r;
    } 
    
    
    //Example use
    
    public static void main(String[] args){
        ProfileSparklineGraph2D profile = new ProfileSparklineGraph2D();
        profile.setTestTime(1);
        
        MultiLevelProfiler layer = new MultiLevelProfiler(profile);
        layer.setImageSizes(defaultResolutions());
        layer.setDatasetSizes(defaultDatasetSizes());
        
        layer.run();
        
        layer.saveStatistics();        
    }
}
