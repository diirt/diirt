/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.epics.graphene.Graph2DRenderer;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.Point2DDatasets;
import org.epics.graphene.ShowResizableGraph;


/**
 *
 * @author Aaron
 */
public class MultiLevelProfiler<T extends Graph2DRenderer, S> {
    private ProfileGraph2D<T, S> profiler;
    
    private List<Resolution> resolutions;
    private List<Integer> datasetSizes;
        
    private Map<Resolution, Map<Integer, Statistics>> results;
       
    public MultiLevelProfiler(ProfileGraph2D<T, S> profiler){
        this.profiler = profiler;
        this.results = new HashMap<>();
    }
    
    //Profile Running
    
    public void run(){
        if (datasetSizes == null){
            throw new NullPointerException("Use the setter to list dataset sizes.");
        }
        if (resolutions == null){
            throw new NullPointerException("Use the setter to list resolutions.");
        }
        
        //Loop through combinations of settings
        for (int r = 0; r < resolutions.size(); r++){
            
            HashMap<Integer, Statistics> map = new HashMap<>();
            
                for (int s = 0; s < datasetSizes.size(); s++){

                    //Apply settings
                    profiler.setNumDataPoints(datasetSizes.get(s));
                    profiler.setImageWidth(resolutions.get(r).getWidth());
                    profiler.setImageHeight(resolutions.get(r).getHeight());

                    //Profiler
                    profiler.profile();

                    //Track results (dataset size and statistics)
                    map.put(datasetSizes.get(s), profiler.getStatistics());
                }
            
            //Put dataSize & statistics map into resolution set
            results.put(resolutions.get(r), map);
        }
    }
    
    
    //Post-Run Options
    
    public Map<Resolution, Map<Integer, Statistics>> getResults(){
        if (results == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        
        return results;
    }
    
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
    
    public void graphStatistics(){
        if (results == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        
        List<Point2DDataset> lines = getStatisticLineData();
        
        for (Point2DDataset data: lines){
            ShowResizableGraph.showLineGraph(data);            
        }
    }
    
    public void saveStatistics(){
        if (results == null){
            throw new NullPointerException("Profiling has not been run.");
        }
        
        List<Point2DDataset> allLines = getStatisticLineData();
        
        //TODO: finish
    }
    
    //Test Paramter Setters
    
    public void setImageSizes(List<Resolution> resolutions){
        if (resolutions == null){
            throw new IllegalArgumentException("The list of image resolutions must be non-null.");
        }
        
        this.resolutions = resolutions;
    }

    public void setDatasetSizes(List<Integer> nPoints){
        if (nPoints == null){
            throw new IllegalArgumentException("The list of dataset sizes must be non-null.");
        }
        
        this.datasetSizes = nPoints;
    }
    
    
    //Defaults
    
    public static List<Integer> defaultDatasetSizes(){
        int n = 6;
        int base = 10;
        List<Integer> sizes = new ArrayList<>(n);
        
        for (int power = 0; power < n; power++){
            sizes.add((int) Math.pow(base, power));
        }
        
        return sizes;
    }

    public static List<Resolution> defaultResolutions(){
        List<Resolution> r = new ArrayList<>();
        
        r.add(new Resolution(160, 120));
        r.add(new Resolution(320, 240));
        r.add(new Resolution(640, 480));
        r.add(new Resolution(800, 600));
        r.add(new Resolution(1024, 768));
        r.add(new Resolution(1440, 1080));
        r.add(new Resolution(1600, 1200));
        return r;
    } 
    
    public static void main(String[] args){
        ProfileSparklineGraph2D profile = new ProfileSparklineGraph2D();
        profile.setTestTime(1);
        
        MultiLevelProfiler layer = new MultiLevelProfiler(profile);
        layer.setImageSizes(defaultResolutions());
        layer.setDatasetSizes(defaultDatasetSizes());
        
        layer.run();
        
        layer.graphStatistics();
        
        System.out.println( layer.getStatisticLineData().size() );
    }
}
