/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.utils;

/**
 * Stores useful values that can be obtained through profiling.
 * These values are statistical values that can be used to analyze
 * the performance of the Graph with profiling.
 *
 * @author asbarber
 */
public class Statistics {

    /**
     * Constructs and initializes the specified set of statistics.
     * @param nTries the number of iterations in the render loop of profiling
     * @param averageTime the average time in ms per iteration in the render loop
     * @param totalTime the total time in ms spent in the render loop
     */
    public Statistics(int nTries, double averageTime, double totalTime){
        this.nTries = nTries;
        this.averageTime = averageTime;
        this.totalTime = totalTime;
    }


    private int nTries;
    private double averageTime, //ms
                   totalTime;   //ms


    /**
     * Returns the number of iterations in the render loop of profiling.
     * @return number of iterations in render loop
     */
    public int getAttempts(){
        return nTries;
    }

    /**
     * Returns the average time in ms per iteration in the render loop
     * @return average render time in ms
     */
    public double getAverageTime(){
        return averageTime;
    }

    /**
     * Returns the total time spent in the render loop.
     * @return total time in render loop in ms
     */
    public double getTotalTime(){
        return totalTime;
    }

    /**
     * Displays the statistics in the console.
     * Each statistical value is displayed in a new line.
     * There is no new line displayed before or after.
     */
    public void printStatistics(){
        String[] components = this.toString().split(", ");

        for (String part: components){
            System.out.println(part);
        }
    }

    /**
     * Returns a string representation of the statistics.
     * Each statistical value is comma separated with no new lines.
     * @return statistical values comma separated
     */
    @Override
    public String toString(){
        return "nTries: " + nTries + ", " +
                "average: " + averageTime + " ms, " +
                "total: " + totalTime + " ms";
    }
}
