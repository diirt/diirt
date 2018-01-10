/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile;

import java.awt.image.BufferedImage;
import java.io.File;
import org.diirt.graphene.profile.settings.SaveSettings;
import org.diirt.graphene.profile.utils.Statistics;
import org.diirt.graphene.profile.utils.Resolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.Point2DDatasets;
import org.diirt.graphene.profile.image.ShowResizableGraph;
import org.diirt.graphene.profile.io.CSVWriter;
import org.diirt.graphene.profile.io.DateUtils;
import org.diirt.graphene.profile.io.ImageWriter;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles the profiling for testing rendering (specifically the draw) of a
 * <code>Graph2DRenderer</code> through sets of testing settings.
 * Has a <code>ProfileGraph2D</code> with the type of the
 * <code>Graph2DRenderer</code> being profiled.
 * <p>
 * Enables the <i>profile object</i> to be profile at various image resolutions
 * and dataset sizes.  These statistics may then be graphed or saved.
 *
 * @author asbarber
 */
public class MultiLevelProfiler{
    private ProfileGraph2D profiler;

    private List<Resolution> resolutions;
    private List<Integer> datasetSizes;

    private Map<Resolution, Map<Integer, Statistics>> results;
    private Map<Resolution, Map<Integer, BufferedImage>> images;

    /**
     * Creates a object to profile a <code>Graph2DRenderer</code>
     * using a <code>ProfileGraph2D</code> and analyzing performance
     * through various sets of renderings at specified intervals
     * of data sizes and image resolution sizes.
     *
     * @param profiler graph renderer with given profile settings to
     * do extensive testing on
     */
    public MultiLevelProfiler(ProfileGraph2D profiler){
        this.profiler = profiler;
        this.results = new HashMap<>();
        this.images = new HashMap<>();
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
    public void profile(){
        if (datasetSizes == null || datasetSizes.isEmpty()){
            throw new NullPointerException("Use the setter to list dataset sizes.");
        }
        if (resolutions == null || resolutions.isEmpty()){
            throw new NullPointerException("Use the setter to list resolutions.");
        }

        //Can give warning about how long this method will profile
        this.processTimeWarning(datasetSizes.size() * resolutions.size() * profiler.getProfileSettings().getTestTime());

        //Loop through combinations of settings
        for (int r = 0; r < resolutions.size() && !Thread.currentThread().isInterrupted(); r++){

            HashMap<Integer, Statistics> map = new HashMap<>();
            HashMap<Integer, BufferedImage> imageMap = new HashMap<>();

                for (int s = 0; s < datasetSizes.size() && !Thread.currentThread().isInterrupted(); s++){

                    //Use this to process before the result, such as to print to console
                    this.processPreResult(resolutions.get(r), datasetSizes.get(s));

                    //Apply settings
                    profiler.setNumDataPoints(datasetSizes.get(s));
                    profiler.getResolution().setWidth(resolutions.get(r).getWidth());
                    profiler.getResolution().setHeight(resolutions.get(r).getHeight());

                    //Profiler
                    profiler.profile();

                    //Track results (dataset size and statistics)
                    map.put(datasetSizes.get(s), profiler.getStatistics());
                    imageMap.put(datasetSizes.get(s), profiler.getSaveSettings().getSaveImage());

                    //Use to process the result, such as print to console
                    this.processResult(resolutions.get(r), datasetSizes.get(s), profiler.getStatistics());
                }

            //Put dataSize & statistics map into resolution set
            results.put(resolutions.get(r), map);
            images.put(resolutions.get(r), imageMap);
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
     * Precondition: <code>#profile()</code> has been called.
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
     * Each point has the form (size, time).
     *
     * @return lines for each resolution composed of average profile time and
     * dataset size points, with the point have the form (size, time)
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
     * Plots the sizes on the x-axis and the times on the y-axis.
     *
     * Precondition: <code>#profile()</code> has been called.
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
     * has been completed.
     */
    public void saveStatistics(){
        if (Thread.currentThread().isInterrupted()){
            return;
        }

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

        List<List> data = new ArrayList();

        //Sorts keys so table has sorted columns/rows
        Arrays.sort(resKeys);
        Arrays.sort(sizeKeys);

        //For all dataset sizes
        for (int i = 0; i < sizeKeys.length; i++){
            ArrayList row = new ArrayList();

            //Dataset column
            row.add(sizeKeys[i]);

            //Finds the timings for the dataset size for all resolutions
            for (int j = 0; j < resKeys.length; j++){
                row.add(
                    String.format("%.3f",
                    results.get(resKeys[j]).get(sizeKeys[i]).getAverageTime()
                ));
            }

            data.add(row);
        }

        String filename =
                        ProfileGraph2D.LOG_FILEPATH +
                        DateUtils.getDate(DateUtils.DateFormat.NONDELIMITED) +
                        "-" +
                        profiler.getGraphTitle() +
                        "-" +
                        "Table2D";

        File output = CSVWriter.createFile(filename);
        CSVWriter.writeRow(output, CSVWriter.arrayCombine("", resKeys));
        CSVWriter.writeData(output, data);

        saveAdditionalInfo(filename);
    }

    /**
     * Creates a table (.out.CSV format) to display the save settings,
     * such as author, dataset message, save message,
     * as well as information about the physical computer profiling.
     * <p>
     * Saves the CSV file to the same directory as general profile results,
     * with the specific profile graph type as well as the <b>date</b>.
     */
    private void saveAdditionalInfo(String filename){
        File output = CSVWriter.createFile(filename + ".out");

        CSVWriter.writeRow(output, CSVWriter.arrayCombine(
            "Graph Type",
            "Date",
            profiler.getProfileSettings().getTitle(),
            profiler.getRenderSettings().getTitle(),
            profiler.getSaveSettings().getTitle()
        ));

        CSVWriter.writeRow(output, CSVWriter.arrayCombine(
            profiler.getGraphTitle(),
            DateUtils.getDate(DateUtils.DateFormat.DELIMITED),
            profiler.getProfileSettings().getOutput(),
            profiler.getRenderSettings().getOutput(),
            profiler.getSaveSettings().getOutput()
        ));
    }

    /**
     * Saves the images generated by the profiling.
     * Saves the image files to a directory with the specific profile graph
     * type as well as the <b>date</b>.
     *
     *
     * Precondition: images is non-null and not empty, meaning profiling
     * has been completed.
     */
    public void saveImages(){
        if (Thread.currentThread().isInterrupted()){
            return;
        }

        if (images == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        //Should not occur since resolutions must be non-empty
        if (images.isEmpty()){
            return;
        }


        //Makes directory
        //--------------------------------------------------------------
        String path =
            ProfileGraph2D.LOG_FILEPATH +
            DateUtils.getDate(DateUtils.DateFormat.NONDELIMITED) +
            "-" +
            profiler.getGraphTitle() +
            "-" +
            "Table2D";

        boolean success = (new File(path)).mkdir();

        //Failed to make directory
        if (!success){
            return;
        }
        //--------------------------------------------------------------


        //Assumption: the set of keys for dataset sizes are the same
        //for each line (the Map<Integer,Statistics> keys are equivalent for
        //each resolutioin)
        Resolution[] resKeys = images.keySet().toArray(new Resolution[0]);
        Integer[] sizeKeys = images.get(resKeys[0]).keySet().toArray(new Integer[0]);

        //Sorts keys so table has sorted columns/rows
        Arrays.sort(resKeys);
        Arrays.sort(sizeKeys);

        //For all dataset sizes
        for (int i = 0; i < sizeKeys.length; i++){
            //For all resolutions
            for (int j = 0; j < resKeys.length; j++){
                BufferedImage img = images.get(resKeys[j]).get(sizeKeys[i]);
                String name = resKeys[j].toString() +
                              "." +
                              sizeKeys[i].toString();

                ImageWriter.saveImage(path + "/", name, img);
            }
        }
    }

    //During Profile

    /**
     * Performed after the call to <code>profile</code> but prior to actual
     * image rendering.
     * <p>
     * Default behavior will display the estimated time based on the
     * number of resolutions, number of dataset sizes, and the test time.
     *
     * @param estimatedTime  estimated time to profile
     *
     * @see #profile()
     */
    public void processTimeWarning(int estimatedTime){
        System.out.println("The estimated run time is " + estimatedTime + " seconds.");
    }

    /**
     * Performed every iteration of profiling and is the action undertaken
     * before knowing the statistics about the profile.
     * <p>
     * Default behavior is to print the resolution and dataset
     * size to the console.
     * <p>
     * Override this to provide custom behaviors.
     *
     * @param resolution resolution about to be profiled (image width, height)
     * @param datasetSize size of data about to be profiled
     */
    public void processPreResult(Resolution resolution, int datasetSize){
        System.out.print(resolution + ": " + datasetSize + ": " );
    }

    /**
     * Performed every iteration of profiling and is the action undertaken
     * after knowing the statistics about the profile.
     * <p>
     * Default behavior is to print the average time to the console.
     * <p>
     * Override this to provide custom behaviors.
     *
     * @param resolution resolution just profiled (image width, height)
     * @param datasetSize size of data just profiled
     * @param stats results of the profiling
     */
    public void processResult(Resolution resolution, int datasetSize, Statistics stats){
        System.out.println(stats.getAverageTime() + "ms");
    }


    //Test Parameter Setters

    /**
     * Sets the resolution sizes to be profiled.
     * Precondition: resolution set is not null and not empty
     * @param resolutions set of resolutions to profile
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


    //Save Parameters

    /**
     * Gets the settings to be saved to the output file
     * for a profile.
     * Some settings include:
     * <ul>
     *      <li>Author</li>
     *      <li>Dataset message</li>
     *      <li>Save message</li>
     * </ul>
     * @return the messages about settings information to
     *         be saved to an output file
     */
    public SaveSettings getSaveSettings(){
        return this.profiler.getSaveSettings();
    }


    //Defaults

    /**
     * Sample multi-level profiling for a given renderer.
     * Uses the default resolutions and default datasets as data.
     * <p>
     * Graphs and saves the data.
     *
     * @param profiler renderer to profile at multiple levels
     */
    public static void defaultProfile(ProfileGraph2D profiler){
        MultiLevelProfiler layer = new MultiLevelProfiler(profiler);
        layer.setImageSizes(Resolution.defaultResolutions());
        layer.setDatasetSizes(DatasetFactory.defaultDatasetSizes());

        layer.profile();

        layer.graphStatistics();
        layer.saveStatistics();
    }
}