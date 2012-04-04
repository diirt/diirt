/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class MockOrderedDataset2D implements OrderedDataset2D {
    
    private double[] xValues;
    private double[] yValues;
    private double[] xMinMax;
    private double[] yMinMax;

    public MockOrderedDataset2D(double[] xValues, double[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        xMinMax = NumberUtil.minMax(xValues);
        yMinMax = NumberUtil.minMax(yValues);
    }

    @Override
    public double getXValue(int index) {
        return xValues[index];
    }

    @Override
    public double getYValue(int index) {
        return yValues[index];
    }

    @Override
    public double getXMinValue() {
        return xMinMax[0];
    }

    @Override
    public double getXMaxValue() {
        return xMinMax[1];
    }

    @Override
    public double getYMinValue() {
        return yMinMax[0];
    }

    @Override
    public double getYMaxValue() {
        return yMinMax[1];
    }

    @Override
    public int getCount() {
        return xValues.length;
    }
    
}
