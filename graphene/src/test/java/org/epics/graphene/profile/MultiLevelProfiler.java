package org.epics.graphene.profile;

import java.util.ArrayList;
import java.util.List;
import org.epics.graphene.Graph2DRenderer;

/**
 *
 * @author Aaron
 */
public class MultiLevelProfiler<T extends Graph2DRenderer, S> {
    private ProfileGraph2D<T, S> profiler;
    
    private List<Resolution> resolutions;
    private int[] nPoints;
        
    private List<Statistics> results;
    
    public MultiLevelProfiler(ProfileGraph2D<T, S> profiler){
        this.profiler = profiler;
        
    }
    
    //Profile Running
    
    public void run(){
        if (nPoints == null){
            
        }
        if (resolutions == null){
            throw new NullPointerException("Use the setter to list resolutions");
        }
        
    }
    
    //Post-Run Options
    
    public List<Statistics> getStatistics(){
        if (results == null){
            throw new NullPointerException("Has not been run.");
        }
        
        return results;
    }
    
    
    //Test Paramter Setters
    
    public void setImageSizes(List<Resolution> resolutions){
        if (resolutions == null){
            throw new IllegalArgumentException("The list of image resolutions must be non-null");
        }
        
        this.resolutions = resolutions;
    }

    public void setNumDataPoints(int[] nPoints){
        this.nPoints = nPoints;
    }
    
    
    //Defaults
    
    public static int[] defaultDataPointSizeSet(){
        int n = 6;
        int base = 10;
        int[] sizes = new int[n];
        
        for (int power = 0; power < n; power++){
            sizes[power] = (int) Math.pow(base, power);
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
}
