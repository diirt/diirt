/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.settings;

import org.epics.graphene.profile.utils.StopWatch;

public class ProfileSettings implements Settings{
    private StopWatch.TimeType timeType = StopWatch.TimeType.System;
    
    private int maxTries    = 100000, //10^6
                testTimeSec = 20;

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
    
    public void setTimeType(StopWatch.TimeType timeType){
        this.timeType = timeType;
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
    
    public StopWatch.TimeType getTimeType(){
        return this.timeType;
    }    

    
    //FORMATS FOR OUTPUT FILES
    
    @Override
    public Object[] getTitle() {
        return new Object[]{
            "Timing Type",
        };
    }

    @Override
    public Object[] getOutput() {
        return new Object[]{
            getTimeType(),
        };
    }
}
