/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.diirt.graphene.Cell1DDataset;
import org.diirt.graphene.Cell1DDatasets;
import org.diirt.graphene.Point1DDataset;
import org.diirt.graphene.Point1DDatasets;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.profile.image.ShowResizableGraph;
import org.diirt.graphene.profile.io.CSVWriter;
import org.diirt.graphene.profile.settings.ProfileSettings;
import org.diirt.graphene.profile.utils.Statistics;
import org.diirt.graphene.profile.utils.StopWatch;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListMath;

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
        Instant start = Instant.now();
        Instant end = start.plus(Duration.ofSeconds(profileSettings.getTestTime()));

        //Trials
        while (end.compareTo(Instant.now()) >= 0 &&           //not over max time
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

    protected void saveStatistics(String fileName, List header, List row){
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

            CSVWriter.writeRow(output, header);
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

        Point1DDataset timings = Point1DDatasets.of(timingsExcludeFirst);
        Cell1DDataset hist = Cell1DDatasets.createHistogram(timings);
        Point2DDataset line = org.diirt.graphene.Point2DDatasets.lineData(timingsExcludeFirst);
        Point2DDataset averagedLine = org.diirt.graphene.Point2DDatasets.lineData(averages);
        ShowResizableGraph.showHistogram(hist);
        ShowResizableGraph.showLineGraph(line);
        ShowResizableGraph.showLineGraph(averagedLine);
    }



    public ProfileSettings getProfileSettings(){
        return this.profileSettings;
    }
}
