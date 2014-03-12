/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.utils;

import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.StopWatch.TimeType;

public class ProfileSettings implements Settings{
    
    private Graph2DRendererUpdate update;
    private String updateDescription;
    
    private boolean     bufferInLoop = false;
    private TimeType    timeType     = StopWatch.TimeType.System;
    
    private int maxTries    = 100000, //10^6
                testTimeSec = 20;
    
    private ProfileGraph2D profiler;
    
    
    public ProfileSettings(ProfileGraph2D profiler){
        if (profiler == null){
            throw new IllegalArgumentException("Use a non-null profiler");
        }
        
        this.profiler = profiler;
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
    
    public void setTimeType(TimeType timeType){
        this.timeType = timeType;
    }    

    public void setUpdate(String updateDescription, Graph2DRendererUpdate update){
        this.update = update;
        this.updateDescription = updateDescription;
    }
    
    public void setUpdate(String updateDescription){
        setUpdate(updateDescription, (Graph2DRendererUpdate) profiler.getVariations().get(updateDescription));
    }    
    
    //Getters
    
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
    
    public TimeType getTimeType(){
        return this.timeType;
    }
    
    public Graph2DRendererUpdate getUpdate(){
        return update;
    }
    
    public String getUpdateDescription(){
        if (updateDescription == null) return "";
        
        return this.updateDescription;
    }    

    
    //FORMATS FOR OUTPUT FILES
    
    @Override
    public Object[] getTitle() {
        return new Object[]{
            "Timing Type",
            "Update Applied"
        };
    }

    @Override
    public Object[] getOutput() {
        return new Object[]{
            getTimeType(),
            getUpdateDescription()
        };
    }
}
