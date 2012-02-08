/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class MockHistogram1D implements Histogram1D {
    
    private int imageHeight;
    private int imageWidth;
    private double minValueRange;
    private double maxValueRange;
    private int minCountRange;
    private int maxCountRange;
    

    @Override
    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public double getMinValueRange() {
        return minValueRange;
    }

    @Override
    public double getMaxValueRange() {
        return maxValueRange;
    }

    public void setMaxValueRange(double maxValueRange) {
        this.maxValueRange = maxValueRange;
    }

    public void setMinValueRange(double minValueRange) {
        this.minValueRange = minValueRange;
    }
    
    @Override
    public void setImageHeight(int height) {
        this.imageHeight = height;
    }

    @Override
    public void setImageWidth(int width) {
        this.imageWidth = width;
    }

    @Override
    public int getMaxCountRange() {
        return maxCountRange;
    }

    @Override
    public void setMaxCountRange(int maxCountRange) {
        this.maxCountRange = maxCountRange;
    }

    @Override
    public int getMinCountRange() {
        return minCountRange;
    }

    @Override
    public void setMinCountRange(int minCountRange) {
        this.minCountRange = minCountRange;
    }
    
    
    
}
