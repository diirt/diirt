/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.settings;

import org.diirt.graphene.profile.utils.StopWatch;

/**
 * The settings of a profile object that deal with the actual process of
 * profiling.
 *
 * @author asbarber
 */
public class ProfileSettings implements Settings{

    //Data Members
    //--------------------------------------------------------------------------

    private StopWatch.TimeType timeType = StopWatch.TimeType.System;

    private int maxTries    = 100000, //10^6
                testTimeSec = 20;

    //--------------------------------------------------------------------------


    //Setters
    //--------------------------------------------------------------------------

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
     * Sets the type of time to be measured.
     *
     * @param timeType type of time
     */
    public void setTimeType(StopWatch.TimeType timeType){
        this.timeType = timeType;
    }

    //--------------------------------------------------------------------------


    //Getters
    //--------------------------------------------------------------------------

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
     * Gets the type of time measured in profiling
     * @return type of time
     */
    public StopWatch.TimeType getTimeType(){
        return this.timeType;
    }

    //--------------------------------------------------------------------------



    //FORMATS FOR OUTPUT FILES
    //--------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------

}
