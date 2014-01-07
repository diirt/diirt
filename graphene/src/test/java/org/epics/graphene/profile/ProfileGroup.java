/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * A tool to analyze a set of graph profilers.
 * Provides options for handling multiple profile operations.
 * 
 * @author asbarber
 */
public class ProfileGroup {
    
    /**
     * Creates a profile grouping that can handle mass profile operations.
     * @param profilers relevant set of graph profilers to perform operations on
     */
    public ProfileGroup(List<ProfileGraph2D> profilers){
        this.profilers = profilers;
    }

    //Set of profilers to perform operations on
    private List<ProfileGraph2D> profilers;

    //Group Profiling Parameters
    private boolean graphStats = false,
                    printStats = false,
                    saveStats = true;
    
    //Profile Parameters
    private int     maxTries = 100000,
                    testTimeSec = 10;
    private int     nPoints = 1000;
    private int     imageWidth = 500,
                    imageHeight = 500;
    private boolean bufferInLoop = false;
             
    
    //Parameter Setters
    
    /**
     * Sets whether a graph of statistics will be displayed after each profile.
     * @param graphStats if graphs of profile statistics should be displayed
     */
    public void setGraphStats(boolean graphStats){
        this.graphStats = graphStats;
    }
    
    /**
     * Sets whether statistics will be printed to the console after each profile.
     * @param printStats if profile statistics should be printed to console
     */    
    public void setPrintStats(boolean printStats){
        this.printStats = printStats;
    }
    
    /**
     * Sets whether statistics will be saved to a CSV log after each profile.
     * @param saveStats if profile statistics should be saved to log files
     */     
    public void setSaveStats(boolean saveStats){
        this.saveStats = saveStats;
    }
    
    
    //Settings Apply
    
    /**
     * Applies all profile parameters to a profiler.
     * The parameters are taken from this ProfileGroup.
     * @param profiler profiler object to apply parameter updates to
     */
    private void applyParameters(ProfileGraph2D profiler){
        profiler.setMaxTries(maxTries);
        profiler.setTestTime(testTimeSec);
        profiler.setNumDataPoints(nPoints);
        profiler.setImageWidth(imageWidth);
        profiler.setImageHeight(imageHeight);
        profiler.setBufferInLoop(bufferInLoop);
    }
    
    
    //Profiling for Group
    
    /**
     * Profiles for every <code>ProfileGraph2D</code> in the group.
     * The profile group settings are applied to each profiler in the group.
     * If indicated, will graph / print / save the statistics of each profiler.
     */
    public void run(){
        for (ProfileGraph2D profiler: profilers){
            //Parameter Options
            this.applyParameters(profiler);
            
            //Profiling
            profiler.profile();
            
            //Output Options
            if (printStats){
                profiler.printStatistics();
            }
            if (graphStats){
                profiler.graphStatistics();
            }
            if (saveStats){
                profiler.saveStatistics();
            }
        }
    }
    
    /**
     * Profiles for every <code>ProfileGraph2D</code> in the group
     * while iterating through different sized data and images for each profile.
     * 
     * Warning: time consuming process if the list of profilers is large and arrays are large.
     * 
     * Each profiler is profiled for each data size for each width/height pair.
     * 
     * @param numPoints set of data sizes to be traversed
     * @param widths set of widths to be traversed (paired with heights)
     * @param heights set of heights to be traversed (paired with widths)
     */
    public void run(int[] numPoints, int[] widths, int[] heights){
        //Traverse different data point sizes
        for (int n: numPoints){
            this.nPoints = n;
            
            //Traverse different widths/heights
            for (int index = 0; index < widths.length && index < heights.length; index++){
                this.imageWidth = widths[index];
                this.imageHeight = heights[index];
                
                run();
            }
        }
    }
    
    
    //Defaults
    
    /**
     * Runs a simple group profile for every <code>ProfileGraph2D</code> in <code>defaultProfileSet</code>.
     * @param args console arguments -- no impact
     */
    public static void main(String[] args){
        ProfileGroup group = new ProfileGroup(defaultProfileSet());
        
        group.run();
    }
    
    /**
     * The default set of all relevant <code>ProfileGraph2D</code> subclasses to perform profiling on.
     * @return default set of profilers
     */
    public static List<ProfileGraph2D> defaultProfileSet(){
        List<ProfileGraph2D> set = new ArrayList<>();
        
        set.add(new ProfileHistogram1D());
        set.add(new ProfileIntensityGraph2D());
        set.add(new ProfileLineGraph2D());
        set.add(new ProfileScatterGraph2D());
        set.add(new ProfileSparklineGraph2D());
        
        return set;
    }
}
