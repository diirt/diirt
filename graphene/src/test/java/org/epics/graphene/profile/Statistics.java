/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

/**
 *
 * @author asbarber
 */
public class Statistics {
    public Statistics(int nTries, double averageTime, double totalTime){
        this.nTries = nTries;
        this.averageTime = averageTime;
        this.totalTime = totalTime;
    }
    
    private int nTries;
    private double averageTime, //ms
                   totalTime;   //ms
    
    public int getAttempts(){
        return nTries;
    }
    public double getAverageTime(){
        return averageTime;
    }
    public double getTotalTime(){
        return totalTime;
    }
    
    public void printStatistics(){
        String[] components = this.toString().split(", ");
        
        for (String part: components){
            System.out.println(part);
        }
    }
    
    @Override
    public String toString(){
        return "nTries: " + nTries + ", " +
                "average: " + averageTime + " ms, " +
                "total: " + totalTime + " ms";
    }
}
