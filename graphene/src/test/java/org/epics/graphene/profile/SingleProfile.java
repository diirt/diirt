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
public class SingleProfile {
    public static void main(String[] args){
        SingleProfile profiler = new SingleProfile();
        
        profiler.largeDataset2DPoint();
        
        //profiler.invokeAll()
    }
    
    
    public void invokeAll(){
        Method[] allMethods = SingleProfile.class.getMethods();
        SingleProfile profiler = new SingleProfile();
                
        for (Method method: allMethods){
            //Ensures one of the test methods
            boolean notInherited = method.getDeclaringClass().equals(SingleProfile.class);
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
                    Logger.getLogger(SingleProfile.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        }        
    }
    
    public void largeDataset1D(){        
        ArrayList<ProfileGraph2D> graphs = new ArrayList<>();
        
        graphs.add(new ProfileHistogram1D());
        //Add more 1D dataset types here
        
        while(!graphs.isEmpty()){
            ProfileGraph2D graph = graphs.get(0);
            
            //Apply Settings
            graph.setNumDataPoints( (int)Math.pow(10, 6) );

            graph.setImageWidth(600);
            graph.setImageHeight(400);

            graph.setSaveMessage("Max Dataset Size Test");
            graph.setAuthorMessage("asbarber");

            graph.setTestTime(20);

            //Run
            graph.profile();
            graph.saveStatistics();
            
            //Free up memory
            graphs.remove(0);
        }        
    }
    
    public void largeDataset2DPoint(){
        ArrayList<ProfileGraph2D> graphs = new ArrayList<>();
        
        graphs.add(new ProfileLineGraph2D());
        graphs.add(new ProfileScatterGraph2D());
        graphs.add(new ProfileSparklineGraph2D());
        //Add more 2D point dataset types here
        
        while(!graphs.isEmpty()){
            ProfileGraph2D graph = graphs.get(0);
            
            //Apply Settings
            graph.setNumDataPoints( (int)Math.pow(10, 6) );

            graph.setImageWidth(600);
            graph.setImageHeight(400);

            graph.setSaveMessage("Max Dataset Size Test");
            graph.setAuthorMessage("asbarber");

            graph.setTestTime(20);

            //Run
            graph.profile();
            graph.saveStatistics();
            
            //Free up memory
            graphs.remove(0);
        }
    }
    
    public void largeDataset2DCell(){
        ProfileIntensityGraph2D graph = new ProfileIntensityGraph2D();
        
        //Apply Settings
        graph.setNumXDataPoints( 200000 );
        graph.setNumYDataPoints( 200000 );
        
        graph.setImageWidth(600);
        graph.setImageHeight(400);
        
        graph.setDatasetMessage("20000x200000");
        graph.setSaveMessage("Max Dataset Size Test");
        graph.setAuthorMessage("asbarber");
        
        graph.setTestTime(20);

        //Run
        graph.profile();
        graph.saveStatistics();
    }
}
