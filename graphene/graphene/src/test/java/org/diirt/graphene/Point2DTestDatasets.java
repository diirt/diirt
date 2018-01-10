/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.util.Random;
import org.diirt.util.array.ArrayDouble;

/**
 *
 * @author carcassi
 */
public class Point2DTestDatasets {

    public static Point2DDataset sineDataset(int nSamples, int wavelengthInSamples, double initialAngleInRad, double amplitude, double average, Range xRange) {
        double[] data = new double[nSamples];
        for (int j = 0; j < nSamples; j++) {
            data[j] = amplitude * Math.sin(j * 2.0 * Math.PI / wavelengthInSamples + initialAngleInRad) + average;
        }

        return Point2DDatasets.lineData(xRange, new ArrayDouble(data));
    }

    public static double[] randomDataset() {
        Random rand = new Random(1);
        int nSamples = 100000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        return waveform;
    }

    public static Point2DDataset sharpPeakData() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 50; i++) {
            dataSet[i] = i;
        }
        for (int i = 50; i < 100; i++) {
            dataSet[i] = 100 - i;
        }
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset oneValueDataset() {
        double[] dataSet = {1.5};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset twoValueDataset() {
        double[] dataSet = {10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset negativeDataset() {
        double[] dataSet = new double[100];
        for (int i = 0; i < 100; i++) {
            dataSet[i] = (i * -.5) / Math.pow(i, 2);
        }

        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset consecNaNDataset() {
        double[] dataSet = {Double.NaN, Double.NaN, 2, 5, 9, 15};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset oneNaNDataset() {
        double[] dataSet = {1, Double.NaN, 10, 20};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }

    public static Point2DDataset twoSpacedNaNDataset() {
        double[] dataSet = {1, 8, 27, Double.NaN, 125, Double.NaN, 349};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);
        return data;
    }
}
