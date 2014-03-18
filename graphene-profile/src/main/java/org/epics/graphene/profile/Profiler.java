/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.io.File;
import java.util.List;
import org.epics.graphene.Histogram1D;
import org.epics.graphene.Histograms;
import org.epics.graphene.Point1DCircularBuffer;
import org.epics.graphene.Point1DDatasetUpdate;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.profile.image.ShowResizableGraph;
import org.epics.graphene.profile.io.CSVWriter;
import org.epics.graphene.profile.settings.ProfileSettings;
import org.epics.graphene.profile.utils.Statistics;
import org.epics.graphene.profile.utils.StopWatch;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListMath;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

public abstract class Profiler {
    protected int         nTries = 0;
    protected StopWatch   stopWatch;
    
    private ProfileSettings profileSettings;

    
    public Profiler(){
        this.profileSettings = new ProfileSettings();
    }
    
    
    public void profile(){
        preLoopAction();
        
        stopWatch = new StopWatch(profileSettings.getMaxTries());
        stopWatch.setTimeType(profileSettings.getTimeType());
                
        nTries = 0;
        
        //System Time
        Timestamp start = Timestamp.now();
        Timestamp end = start.plus(TimeDuration.ofSeconds(profileSettings.getTestTime()));   
                
        //Trials
        while (end.compareTo(Timestamp.now()) >= 0 &&           //not over max time
               !Thread.currentThread().isInterrupted() &&       //not interrupted
               nTries < profileSettings.getMaxTries()) {        //not over max tries
            
                    nTries++;
                    stopWatch.start();

                    iterationAction();
                    
                    stopWatch.stop();

                    postIterationAction();
        }        
    }
    
    
    //During-Profile Actions
    
    protected void preLoopAction(){}
    
    protected abstract void iterationAction();
    
    protected void postIterationAction(){}
    
    
    //Post-Profile Helpers
    
    public abstract String getProfileTitle();
    
    public void saveStatistics(String fileName, List header, List row){
        //Verifies non-interrupted
        if (Thread.currentThread().isInterrupted()){
            return;
        }
        
        //Ensures profile() was called
        if (stopWatch == null || nTries == 0){
            throw new NullPointerException("Has not been profiled.");
        }        
        
        //Data
        File output;
        
        //Creates if necessary
        output = new File(fileName + ".csv");
        if (!output.exists()){
            output = CSVWriter.createNewFile(fileName);

            CSVWriter.writeHeader(output, header);
        }

        
        //Adds data
        CSVWriter.writeRow(output, row);      
    }
    
    
    //Post-Profile Actions
    
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
    
    public void printStatistics(){
        Statistics stats = getStatistics();
        
        if (stats != null){
            System.out.println(getProfileTitle());            
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
    
    
    
    public ProfileSettings getProfileSettings(){
        return this.profileSettings;
    }
}
