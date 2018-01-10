/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.utils;

import org.diirt.graphene.profile.settings.Settings;
import java.util.ArrayList;
import java.util.List;

/**
 * A screen resolution that has a width and a height.
 *
 * @author asbarber
 */
public class Resolution implements Comparable, Settings{

    /**
     * Common screen resolution.
     */
    public static Resolution RESOLUTION_160x120 = new Resolution(160, 120),
                             RESOLUTION_320x240 = new Resolution(320, 240),
                             RESOLUTION_640x480 = new Resolution(640, 480),
                             RESOLUTION_1024x768 = new Resolution(1024, 768),
                             RESOLUTION_1440x1080 = new Resolution(1440, 1080),
                             RESOLUTION_1600x1200 = new Resolution(1600, 1200);

    private int width, height;

    /**
     * Creates a <code>Resolution</code> that stores a width to
     * height resolution size.
     * @param width image width in pixels
     * @param height image height in pixels
     */
    public Resolution (int width, int height){
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the image width of the resolution in pixels.
     * @param width image width in pixels
     */
    public final void setWidth(int width){
        if (width <= 0){
            throw new IllegalArgumentException("Width must be a postive non-zero integer.");
        }

        this.width = width;
    }

    /**
     * Sets the image height of the resolution in pixels.
     * @param height image height in pixels
     */
    public final void setHeight(int height){
        if (height <= 0){
            throw new IllegalArgumentException("Height must be a postive non-zero integer.");
        }

        this.height = height;
    }

    /**
     * Returns the image width of the resolution in pixels.
     * @return image width of the resolution
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Returns the image height of the resolution in pixels.
     * @return image height of the resolution
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * Returns the amount of pixels based on this resolution.
     * @return image width times image height
     */
    public int getPixels(){
        return this.width * this.height;
    }

    /**
     * Returns a string representation of the resolution.
     * Formats the resolution as WxH with W as width and H as height.
     * @return WxH for the width, W, and the height, H
     */
    @Override
    public String toString(){
        return width + "x" + height;
    }

    /**
     * Compares which resolution has more pixels.
     * @param o other resolution to compare to
     * @return 1 if this resolution has more pixels,
     *         0 if the same amount of pixels OR the object is not a resolution,
     *        -1 if this resolution has less pixels
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Resolution){
            Resolution other = ((Resolution) o);
            return Integer.compare(this.getPixels(), other.getPixels());
        }
        else{
            return 0;
        }
    }

    /**
     * Default set of resolutions (image width and height set) to test
     * profiling on, based on standard computer resolutions.
     * @return  a list with standard computer screen resolutions
     * (160x120, 320x240, ... 1600x1200)
     */
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

    @Override
    /**
     * Gets the list of headers to output this as settings.
     * @return width and height title
     */
    public Object[] getTitle() {
        return new Object[]{
            "Image Width",
            "Image Height"
        };
    }

    /**
     * Gets the list of data to output this as settings.
     * @return width and height data
     */
    @Override
    public Object[] getOutput() {
        return new Object[]{
            getWidth(),
            getHeight()
        };
    }
}
