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
    
    private double minValueRange;
    private double maxValueRange;
    private int minCountRange;
    private int maxCountRange;
    private double[] binValueBoundary;
    private int[] binCount;
    
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
    public int getMaxCountRange() {
        return maxCountRange;
    }

    public void setMaxCountRange(int maxCountRange) {
        this.maxCountRange = maxCountRange;
    }

    @Override
    public int getMinCountRange() {
        return minCountRange;
    }

    public void setMinCountRange(int minCountRange) {
        this.minCountRange = minCountRange;
    }

    @Override
    public int getNBins() {
        return binCount.length;
    }

    @Override
    public double getBinValueBoundary(int index) {
        return binValueBoundary[index];
    }

    @Override
    public int getBinCount(int index) {
        return binCount[index];
    }

    public void setBinCount(int[] binCount) {
        this.binCount = binCount;
    }

    public void setBinValueBoundary(double[] binValueBoundary) {
        this.binValueBoundary = binValueBoundary;
    }

    @Override
    public void update(Histogram1DUpdate update) {
        throw new UnsupportedOperationException();
    }
    
}
