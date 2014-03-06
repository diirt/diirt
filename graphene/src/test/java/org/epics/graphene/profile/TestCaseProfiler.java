/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.epics.graphene.IntensityGraph2DRenderer;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;


public final class TestCaseProfiler {
    private TestCaseProfiler(){}
    
    
    //--------------------------------------------------------------------------
    //Usage
    
    /**
     * Starting point to running the test methods.
     * Provides the options to run all test methods.
     * @param args 
     */
    public static void main(String[] args){
        /**
         * Change this to:
         *      0:      invokes all methods
         *      1:      invokes methods without memory requirements
         *      2:      invokes methods with memory requirements
         */
        int invokeType = -1;
        
        
        switch(invokeType){
            case 0:     invokeAll();                        break;
            case 1:     invokeNoRequirements();             break;
            case 2:     invokeWithRequirements();           break;
            default:    //Invoke specific tests
                        TestCaseProfiler.setPixelVsDrawRect();
        }
    }    

    
    //--------------------------------------------------------------------------
    //Invoking methods 
    
    public static void invokeNoRequirements(){
        Method[] allMethods = TestCaseProfiler.class.getMethods();
        TestCaseProfiler profiler = new TestCaseProfiler();
        
        //All methods
        for (Method method: allMethods){
            boolean noRequirements;
            
            //Gets Requires annotation
            NoRequires req = method.getAnnotation(NoRequires.class);
            if (req != null){
                noRequirements = true;
            }
            else{
                noRequirements = false;
            }
            
            //If should run the method
            if (!ignoreMethod(method) && noRequirements){
                try{
                    //Console message
                    System.out.println("Invoking " + method.getName() + "...");
                    
                    //Attempts to run the method
                    method.invoke(profiler);  

                    System.out.println("success");                    
                }
                //Could not invoke
                catch (IllegalAccessException | InvocationTargetException ex) {
                    //Method invoke failure
                    System.err.println("Error invoking method: " + method.getName());
                    Logger.getLogger(TestCaseProfiler.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        }         
    }
    
    public static void invokeWithRequirements(){
        Method[] allMethods = TestCaseProfiler.class.getMethods();
        TestCaseProfiler profiler = new TestCaseProfiler();
           
        double memoryAvailable  = Runtime.getRuntime().maxMemory();

        //All methods
        for (Method method: allMethods){
            boolean hasRequirements;
            double memoryRequired = -1;
            Unit unit = null;
            
            //Gets Requires annotation
            Requires req = method.getAnnotation(Requires.class);
            if (req != null){
                memoryRequired = req.memory();
                unit = req.unit();
                hasRequirements = true;
            }
            else{
                hasRequirements = false;
            }
            
            //If should run the method
            if (!ignoreMethod(method) && hasRequirements && unit != null){
                try{
                    //Not enough memory
                    if (memoryRequired > Unit.convert(memoryAvailable, unit)){
                        throw new IllegalArgumentException();
                    }
                    
                    //Console message
                    System.out.print("Invoking " + method.getName() + 
                                       " (Requirements: " + String.format("%.3f", memoryRequired) + unit.toString()  +
                                       ")...");
                    
                    //Attempts to run the method
                    method.invoke(profiler); 
                    
                    System.out.println("success");
                }
                //Not enough memory
                catch (IllegalArgumentException ex){
                    System.err.println("Error invoking method: " + method.getName() + ", " +
                                       "Memory Required: " + String.format("%.3f", memoryRequired) + unit.toString() + ", " +
                                       "Memory Available: " + String.format("%.3f", Unit.convert(memoryAvailable, unit)) + unit.toString());
                    
                }
                //Could not invoke
                catch (IllegalAccessException | InvocationTargetException ex) {
                    //Method invoke failure
                    System.err.println("Error invoking method: " + method.getName());
                    Logger.getLogger(TestCaseProfiler.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        }         
    }
    
    /**
     * Runs every test method in <code>TestCaseProfile</code>
     * <p>
     * Prints to the system console for each test being run.
     * <p>
     * Methods excluded:
     * <ol>
     *      <li>Inherited Methods</li>
     *      <li>main</li>
     *      <li>invokeAll (this)</li>
     * </ol>
     */
    public static void invokeAll(){
        Method[] allMethods = TestCaseProfiler.class.getMethods();
        TestCaseProfiler profiler = new TestCaseProfiler();
           
        double memoryAvailable  = Runtime.getRuntime().maxMemory();

        //All methods
        for (Method method: allMethods){
            boolean hasRequirements;
            double memoryRequired = -1;
            Unit unit = null;
            
            //Gets Requires annotation
            Requires req = method.getAnnotation(Requires.class);
            if (req != null){
                memoryRequired = req.memory();
                unit = req.unit();
                hasRequirements = true;
            }
            else{
                hasRequirements = false;
            }
            
            //If should run the method
            if (!ignoreMethod(method)){
                try{
                    //Requires memory and Not enough memory
                    if (hasRequirements && unit != null && memoryRequired > Unit.convert(memoryAvailable, unit)){
                        throw new IllegalArgumentException();
                    }
                    
                    //Console message
                    System.out.print("Invoking " + method.getName() + "...");
                    
                    //Attempts to run the method
                    method.invoke(profiler); 
                    
                    System.out.println("success");
                }
                //Not enough memory
                catch (IllegalArgumentException ex){
                    if (unit != null){
                        System.err.println("Error invoking method: " + method.getName() + ", " +
                                           "Memory Required: " + String.format("%.3f", memoryRequired) + unit.toString() + ", " +
                                           "Memory Available: " + String.format("%.3f", Unit.convert(memoryAvailable, unit)) + unit.toString() +
                                           "...failed");
                    }
                    
                }
                //Could not invoke
                catch (IllegalAccessException | InvocationTargetException ex) {
                    //Method invoke failure
                    System.err.println("Error invoking method: " + method.getName());
                    Logger.getLogger(TestCaseProfiler.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        }         
    }
    
    private static boolean ignoreMethod(Method method){
        List<String> names = Arrays.asList(new String[]{"main",
                                                        "TestCaseProfiler",
                                                        "ignoreMethod",
                                                       });
        
            boolean isInherited = !method.getDeclaringClass().equals(TestCaseProfiler.class);
            boolean isInvocation = method.getName().contains("invoke");

            boolean isInIgnoreList = names.contains(method.getName());
            
            //True if this method should be ignored
            return isInherited ||
                   isInvocation ||
                   isInIgnoreList;
    }
    
    
    //--------------------------------------------------------------------------
    //Annotations for invoke() methods
    
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Requires{
        String xmx() default "-Xmx4g";
        double memory() default 512;   
        Unit unit() default Unit.MEGABYTE;
    }
    
    @Retention(RetentionPolicy.RUNTIME)    
    private @interface NoRequires{}    

    private enum Unit {
        BYTE, KILOBYTE, MEGABYTE, GIGABYTE;
        
        @Override
        public String toString(){
            if (this.equals(BYTE)){
                return "byte";
            }
            else if (this.equals(KILOBYTE)){
                return "kb";
            }
            else if (this.equals(MEGABYTE)){
                return "mb";
            }
            else if (this.equals(GIGABYTE)){
                return "gb";
            }
            else{
                return "";
            }
        }
        
        public double toValue(){
            if (this.equals(BYTE)){
                return 1;
            }
            else if (this.equals(KILOBYTE)){
                return 1024;
            }
            else if (this.equals(MEGABYTE)){
                return 1048576;
            }
            else if (this.equals(GIGABYTE)){
                return 1073741824;
            }
            else{
                return 1;
            }            
        }
        public static double convert(double value, Unit unit){
            return value / unit.toValue();
        }
    }
    
    //--------------------------------------------------------------------------
    //Test Methods (not requiring more memory) 
    
    /**
     * Test method for the maximum dataset size used on
     * every 1D renderer (ie - Histogram1D).
     * Saves the output to a <code>ProfileGraph2D</code>
     * 1D Table .CSV file.
     * <p>
     * Settings:
     * <ul>
     *      <li>Dataset Size: 10^6</li>
     *      <li>Image Width: 600</li>
     *      <li>Image Height: 400</li>
     *      <li>Test Time: 20s</li>
     * </ul>
     */
    @NoRequires
    public static void maxDataset1D(){  
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
    
    /**
     * Test method for the maximum dataset size used on
     * every 2D Point data renderer (ie - LineGraph).
     * Saves the output to a <code>ProfileGraph2D</code>
     * 1D Table .CSV file.
     * <p>
     * Settings:
     * <ul>
     *      <li>Renderers: {LineGraph, ScatterGraph, SparklineGraph}</li>
     *      <li>Dataset Size: {10^6, 10^3, 10^3}</li>
     *      <li>Image Width: 600</li>
     *      <li>Image Height: 400</li>
     *      <li>Test Time: 20s</li>
     * </ul>
     */    
    @NoRequires
    public static void maxDataset2DPoint(){
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

    @NoRequires
    public static void setPixelVsDrawRect(){
        SetPixelVsDrawRect profiler = new SetPixelVsDrawRect();

        //Uses the Set Pixel method in drawing the image
        profiler.profileSetPixel();
        profiler.printStatistics();

        System.out.println();

        //Uses the Draw Rect method in drawing the image
        profiler.profileDrawRect();
        profiler.printStatistics();
    }

    
    //--------------------------------------------------------------------------
    //Test Methods (requiring more memory)
    
    /**
     * Test method for the different <code>IntensityGraph2D</code> renderers.
     * Saves the output to a <code>ProfileGraph2D</code>
     * 1D Table .CSV file.
     * <p>
     * Settings:
     * <ul>
     *      <li>Strategies: Using Linear Boundaries, Not Using Linear Boundaries</li>
     *      <li>Dataset Size: {10^6, 10^3, 10^3}</li>
     *      <li>Image Width: 600</li>
     *      <li>Image Height: 400</li>
     *      <li>Test Time: 20s</li>
     * </ul>
     */
    @Requires (xmx = "-Xmx4g", memory = 3.50, unit = Unit.GIGABYTE)
    public static void intensityGraphStrategies(){
        //Index 0:  Linear Boundaries
        //Index 1:  Non Linear Boundaries
        ProfileIntensityGraph2D profilers[] = new ProfileIntensityGraph2D[2];
        
        //Setup Profilers
        profilers[0] = new ProfileIntensityGraph2D(){
            @Override
            protected IntensityGraph2DRenderer getRenderer(int imageWidth, int imageHeight){
                IntensityGraph2DRenderer renderer = super.getRenderer(imageWidth, imageHeight);
                
                renderer.setLinearBoundaries(true);
                
                return renderer;
            }
        };
        profilers[0].getSaveSettings().setSaveMessage("Using Linear Boundaries");
        
        profilers[1] = new ProfileIntensityGraph2D(){
            @Override
            protected IntensityGraph2DRenderer getRenderer(int imageWidth, int imageHeight){
                IntensityGraph2DRenderer renderer = super.getRenderer(imageWidth, imageHeight);
                
                renderer.setLinearBoundaries(false);
                
                return renderer;
            }
        };  
        profilers[1].getSaveSettings().setSaveMessage("Not Using Linear Boundaries");
        
        //Apply To Both, One Time
        for (ProfileIntensityGraph2D profile : profilers){
            profile.getSaveSettings().setAuthorMessage("asbarber");
        }
        
        
        //Test Parameters
        int datasetSizes[] = {1000, 10000};
        Resolution resolutions[] = {Resolution.RESOLUTION_320x240, Resolution.RESOLUTION_1024x768};
        int testTime = 20;
        
        //Multiple Test Runs        
        for (Resolution resolution: resolutions){
            for (int datasetSize : datasetSizes){
                
                for (ProfileIntensityGraph2D profile : profilers){
                    //Updates
                    profile.setNumXDataPoints(datasetSize);
                    profile.setNumYDataPoints(datasetSize);
                    
                    profile.setImageWidth(resolution.getWidth());
                    profile.setImageHeight(resolution.getHeight());
                    
                    profile.setTestTime(testTime);
                    
                    profile.profile();
                    profile.saveStatistics();
                }
                
            }
        }
    }
    
    /**
     * Test method for the maximum dataset size used on
     * every 2D Cell data renderer (ie - IntensityGraph).
     * Saves the output to a <code>ProfileGraph2D</code>
     * 1D Table .CSV file.
     * <p>
     * Settings:
     * <ul>
     *      <li>Renderers: {IntensityGraph}</li>
     *      <li>Dataset Size: {10^6, 10^3, 10^3}</li>
     *      <li>Image Width: 600</li>
     *      <li>Image Height: 400</li>
     *      <li>Test Time: 20s</li>
     * </ul>
     */ 
    @Requires (xmx = "-Xmx4g", memory = 3.50, unit = Unit.GIGABYTE)
    public static void maxDataset2DCell(){
        ProfileIntensityGraph2D graph = new ProfileIntensityGraph2D();
        
        //Apply SaveSettings
        graph.setNumXDataPoints( 10000 );
        graph.setNumYDataPoints( 10000 );
        
        graph.setImageWidth(600);
        graph.setImageHeight(400);
        
        graph.getSaveSettings().setDatasetMessage("10000x10000");
        graph.getSaveSettings().setSaveMessage("Max Dataset Size Test");
        graph.getSaveSettings().setAuthorMessage("asbarber");
        
        graph.setTestTime(20);

        //Run
        graph.profile();
        graph.saveStatistics();
    }
    
    
    //--------------------------------------------------------------------------
    //Helper
    
    /**
     * Profiles for two different methods for drawing an image to see which
     * is the more efficient method.
     * 
     * <p>
     * One method is "Set Pixel", which individually sets each pixel value of an image.
     * One method is "Draw Rect", which creates the image by drawing a rectangle
     * for every single pixel of the image.
     * 
     * <p>
     * The results are that the "Set Pixel" method is 3 times as fast as
     * the "Draw Rect" method.
     * 
     * @author carcassi
     * @author asbarber
     */
    private static class SetPixelVsDrawRect{
        private boolean     profileSetPixel = true;

        private int         width = 1024, 
                            height = 768;

        private StopWatch   stopWatch;

        private int         maxTries = 1000000,
                            testTimeSec = 20,
                            nTries = 0;

        private BufferedImage image;
        private Graphics2D graphics;

        /**
         * Creates a profiler to test image render strategies.
         * Default to use the "Set Pixel" method.
         */
        public SetPixelVsDrawRect(){
        }

        /**
         * Creates a profiler to test image render strategies.
         * Default to use the "Set Pixel" method.
         * @param width width in pixels of image
         * @param height height in pixels of image
         */
        public SetPixelVsDrawRect(int width, int height){
            this.width = width;
            this.height = height;
        }

        //Profile

        /**
         * Profiles creating the image by setting the value of each pixel.
         */
        public void profileSetPixel(){
            this.profileSetPixel = true;
            profile();
        }

        /**
         * Profiles creating the image by drawing a rectangle at each pixel.
         */
        public void profileDrawRect(){
            this.profileSetPixel = false;
            profile();
        }

        /**
         * Creates a loop where an image is repeatedly drawn using the 
         * appropriate method being tested.
         */
        private void profile(){
            //Timing
            Timestamp start = Timestamp.now();
            Timestamp end = start.plus(TimeDuration.ofSeconds(testTimeSec));        
            stopWatch = new StopWatch(maxTries);

            nTries = 0;

            //Creates the image buffer
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            graphics = image.createGraphics();

            //Makes Data
            double[] data = makeData();

            //Trials
            while (end.compareTo(Timestamp.now()) >= 0 && nTries < maxTries) {
                nTries++;
                stopWatch.start();

                    //Profile action
                    if (profileSetPixel){
                        doSetPixel(data);
                    }
                    else{
                        doDrawRect(data);
                    }

                stopWatch.stop();
            }
        }


        //Profile Helpers

        /**
         * Set of locations representing each pixel of the image.
         * @return data for pixel locations and pixel values
         */
        private double[] makeData(){
            Random rand  = new Random(0);
            double[] values = new double[width*height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++){
                    values[y*width + x] = rand.nextDouble();
                }
            }        

            return values;
        }

        /**
         * Creates an image by setting the pixel value for every pixel.
         * @param values where to set the pixel
         */
        private void doSetPixel(double[] values){
                for (int y = 0; y < height; y++){
                    for (int x = 0; x < width; x++){
                        image.setRGB(x, y, (int) (255*values[y*width + x]));                    
                    }
                }      
        }

        /**
         * Creates an image by drawing a rectangle at every pixel.
         * @param values where to draw rectangles
         */
        private void doDrawRect(double[] values){
                for (int y = 0; y < height; y++){
                    for (int x = 0; x < width; x++){
                        Color color = new Color((int)(255*values[y*width + x]));
                        graphics.setColor(color);
                        graphics.fillRect(x, y, 1, 1);                     
                        image.setRGB(x, y, (int) (255*values[y*width + x]));                    
                    }
                }
        }


        //Post-Profile Options

        /**
         * Saves the image created in profiling.
         * Does nothing if <code>profile</code> has not been run.
         * @throws IOException could not save image
         */
        public void saveImage() throws IOException{
            if (image == null){
                return;
            }

            String fileName = ProfileGraph2D.LOG_FILEPATH + "SetPixelVsDrawRect.png";

            ImageIO.write(image, "png", new File(fileName));
        }

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

        /**
         * Prints the class title and then the statistics.
         * Does not print anything if statistics do not exist.
         */
        public void printStatistics(){
            Statistics stats = getStatistics();

            if (stats != null){
                System.out.println(this.getClass().getName());      
                System.out.println("Profiled: " + getTypeProfiled());
                stats.printStatistics();
            }
        }    

        /**
         * Returns a string representing whether "Set Pixel" process was used or
         * whether "Draw Rect" process was used.
         * @return process that was tested in profiling
         */
        public String getTypeProfiled(){
            if (profileSetPixel){
                return "Set Pixel";
            }
            else{
                return "Draw Rect";
            }
        }
    }   
}
