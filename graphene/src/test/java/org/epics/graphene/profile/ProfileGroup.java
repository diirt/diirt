/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asbarber
 */
public class ProfileGroup {
    public ProfileGroup(List<ProfileGraph2D> profilers){
        this.profilers = profilers;
    }

    
    private List<ProfileGraph2D> profilers;

    private boolean graphStats = false,
                    printStats = false,
                    saveStats = true;
    
    private int     maxTries = 100000,
                    testTimeSec = 10;
    private int     nPoints = 1000;
    private int     imageWidth = 500,
                    imageHeight = 500;
    private boolean bufferInLoop = false;
                
    
    public void setGraphStats(boolean b){
        graphStats = b;
    }
    public void setPrintStats(boolean b){
        printStats = b;
    }
    public void setSaveStats(boolean b){
        saveStats = b;
    }
    
    private void applyParameters(ProfileGraph2D profiler){
        profiler.setMaxTries(maxTries);
        profiler.setTestTime(testTimeSec);
        profiler.setNumDataPoints(nPoints);
        profiler.setImageWidth(imageWidth);
        profiler.setImageHeight(imageHeight);
        profiler.setBufferInLoop(bufferInLoop);
    }
    
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
    
    
    public static void main(String[] args){
        ProfileGroup group = new ProfileGroup(defaultProfileSet());
        
        group.run();
    }
    
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
