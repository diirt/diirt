/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asbarber
 */
public class TestCaseProfiler {
    public static void main(String[] args){
        TestCaseProfiler profiler = new TestCaseProfiler();
        
        profiler.maxDataset2DCell();
        
        //profiler.invokeAll()
    }
    
    
    public void invokeAll(){
        Method[] allMethods = TestCaseProfiler.class.getMethods();
        TestCaseProfiler profiler = new TestCaseProfiler();
                
        for (Method method: allMethods){
            //Ensures one of the test methods
            boolean notInherited = method.getDeclaringClass().equals(TestCaseProfiler.class);
            boolean notMain = !method.getName().equals("main");
            boolean notThis = !method.getName().equals("invokeAll");
            
            if (notInherited && notMain && notThis){
                try{
                    System.out.println("Invoking " + method.getName() + "...");
                    //Attempts to run the method
                    method.invoke(profiler);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    //Method invoke failure
                    System.err.println("Error invoking method: " + method.getName());
                    Logger.getLogger(TestCaseProfiler.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        }        
    }
    
    public void maxDataset1D(){        
        ArrayList<ProfileGraph2D> graphs = new ArrayList<>();
        
        graphs.add(new ProfileHistogram1D());
        //Add more 1D dataset types here
        
        while(!graphs.isEmpty()){
            ProfileGraph2D graph = graphs.get(0);
            
            //Apply SaveSettings
            graph.setNumDataPoints( (int)Math.pow(10, 6) );

            graph.setImageWidth(600);
            graph.setImageHeight(400);

            graph.getSaveSettings().setSaveMessage("Max Dataset Size Test");
            graph.getSaveSettings().setAuthorMessage("asbarber");

            graph.setTestTime(20);

            //Run
            graph.profile();
            graph.saveStatistics();
            
            //Free up memory
            graphs.remove(0);
        }        
    }
    
    public void maxDataset2DPoint(){
        ArrayList<ProfileGraph2D> graphs = new ArrayList<>();
        ArrayList<Integer> size = new ArrayList<>();
        
        graphs.add(new ProfileLineGraph2D());
        graphs.add(new ProfileScatterGraph2D());
        graphs.add(new ProfileSparklineGraph2D());
        //Add more 2D point dataset types here
        
        size.add( (int)Math.pow(10, 6) );
        size.add( (int)Math.pow(10,3) );
        size.add( (int)Math.pow(10,3) );
        //Add here to add dataset sizes
        
        while(!graphs.isEmpty() && !size.isEmpty()){
            ProfileGraph2D graph = graphs.get(0);
            
            //Apply SaveSettings
            graph.setNumDataPoints( size.get(0) );

            graph.setImageWidth(600);
            graph.setImageHeight(400);

            graph.getSaveSettings().setSaveMessage("Max Dataset Size Test");
            graph.getSaveSettings().setAuthorMessage("asbarber");

            graph.setTestTime(20);

            //Run
            graph.profile();
            graph.saveStatistics();
            
            //Free up memory
            graphs.remove(0);
            size.remove(0);
        }
    }
    
    public void maxDataset2DCell(){
        ProfileIntensityGraph2D graph = new ProfileIntensityGraph2D();
        
        //Apply SaveSettings
        graph.setNumXDataPoints( 20000 );
        graph.setNumYDataPoints( 10000 );
        
        graph.setImageWidth(600);
        graph.setImageHeight(400);
        
        graph.getSaveSettings().setDatasetMessage("20000x200000");
        graph.getSaveSettings().setSaveMessage("Max Dataset Size Test");
        graph.getSaveSettings().setAuthorMessage("asbarber");
        
        graph.setTestTime(5);

        //Run
        graph.profile();
        graph.saveStatistics();
    }
    
    public void intensityGraphStrategies(){
        ProfileIntensityGraph2D profiler = new ProfileIntensityGraph2D();
        
    }
}
