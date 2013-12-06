/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

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
public class SetPixelVsDrawRect{
    private boolean     profileSetPixel = true;
    
    private int         width = 600, 
                        height = 600;
    
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
        int[] data = makeData();
        
        //Trials
        while (end.compareTo(Timestamp.now()) >= 0) {
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
    private int[] makeData(){
        Random rand  = new Random(0);
        int[] values = new int[100000];
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextInt(height);
        }        
        
        return values;
    }
    
    /**
     * Creates an image by setting the pixel value for every pixel.
     * @param values where to set the pixel
     */
    private void doSetPixel(int[] values){
            for (int j = 0; j < values.length; j++) {
                int x = values[j++];
                int y = values[j];
                int rgb = values[j];
                image.setRGB(x, y, rgb);
            }        
    }
    
    /**
     * Creates an image by drawing a rectangle at every pixel.
     * @param values where to draw rectangles
     */
    private void doDrawRect(int[] values){
            for (int j = 0; j < values.length; j++) {
                int x = values[j++];
                int y = values[j];
                int rgb = values[j];
                Color color = new Color(rgb);
                graphics.setColor(color);
                graphics.drawLine(x, y, x, y);
                graphics.fillRect(x, y, 1, 1);   
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
    
    
    /**
     * Tests both methods (creating image by setting every pixel and by drawing 
     * a rectangle at every pixel) and prints the results.
     * @param args console arguments -- no effect
     */
    public static void main(String[] args){
        SetPixelVsDrawRect profiler = new SetPixelVsDrawRect();
        
        //Uses the Set Pixel method in drawing the image
        profiler.profileSetPixel();
        profiler.printStatistics();
        
        System.out.println();
        
        //Uses the Draw Rect method in drawing the image
        profiler.profileDrawRect();
        profiler.printStatistics();
    }
}
