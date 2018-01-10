/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.diirt.graphene.Cell1DDataset;
import org.diirt.graphene.Cell1DDatasets;
import org.diirt.graphene.Cell2DDataset;
import org.diirt.graphene.Cell2DDatasets;
import org.diirt.graphene.Point1DDataset;
import org.diirt.graphene.Point1DDatasets;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.Point3DWithLabelDataset;
import org.diirt.graphene.Point3DWithLabelDatasets;
import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.stats.Ranges;

/**
 * Factory object to create datasets.
 *
 * @author asbarber
 */
public final class DatasetFactory {


    /**
     * Prevents instantiation.
     */
    private DatasetFactory(){}


    //Dataset Size Generators
    //--------------------------------------------------------------------------

    /**
     * Default set of dataset sizes to test profiling on,
     * on a logarithmic scale.
     * The values are 10^1, 10^2, ... , 10^6.
     *
     * @return a list with values of 10^n for n = 1 to n = 6
     * (a logarithmic scale from 10 to 1,000,000)
     */
    public static List<Integer> defaultDatasetSizes(){
        return logarathmicDatasetSizes(1, 6, 10);
    }

    /**
     * Generates a set of dataset sizes to test profiling on,
     * on a logarithmic scale.
     * The values are base^min, base^(min+1), ... , base^max.
     *
     * @param min minimum power to raise the base to
     * @param max maximum power to raise the base to
     * @param base raise this to the power of n
     * @return a list with values of base^n for n = min to n = max
     */
    public static List<Integer> logarathmicDatasetSizes(int min, int max, int base){
        List<Integer> sizes = new ArrayList<>(max);

        for (int power = min; power <= max; power++){
            sizes.add((int) Math.pow(base, power));
        }

        return sizes;
    }

    //--------------------------------------------------------------------------


    //Dataset Generators
    //--------------------------------------------------------------------------

    /**
     * Generates Point1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random data</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * </ol>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Point1DDataset makePoint1DGaussianRandomData(int nSamples){
        //Creates data
        int seed = 1;
        Random rand = new Random(seed);
        double[] data = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            data[i] = rand.nextGaussian();
        }
        Point1DDataset dataset = Point1DDatasets.of(new ArrayDouble(data));

        return dataset;
    }

    /**
     * Generates Point2D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random y-values</li>
     *      <li>y-values are sorted ascending and plotted against sorted index (sorted index is x-value)</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * </ol>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Point2DDataset makePoint2DGaussianRandomData(int nSamples){
        double[] waveform = new double[nSamples];
        int seed = 1;

        //Creates data
        Random rand = new Random(seed);
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }

        return org.diirt.graphene.Point2DDatasets.lineData(waveform);
    }

    /**
     * Generates Point3D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random y-values</li>
     *      <li>y-values are sorted ascending and plotted against sorted index (sorted index is x-value)</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * </ol>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Point3DWithLabelDataset makePoint3DWithLabelGaussianRandomData(int nSamples){
        ArrayDouble x = new ArrayDouble(new double[nSamples], false);
        ArrayDouble y = new ArrayDouble(new double[nSamples], false);
        ArrayDouble z = new ArrayDouble(new double[nSamples], false);

        int seed = 1;

        List<String> labels = new ArrayList<>(nSamples);
        String[] labelSet = new String[] {"First", "Second", "Third", "Fourth", "Fifth"};

        //Creates data
        Random rand = new Random(seed);
        for (int i = 0; i < nSamples; ++i){
            x.setDouble(i, rand.nextGaussian());
            y.setDouble(i, rand.nextGaussian());
            z.setDouble(i, rand.nextGaussian());
            labels.add(labelSet[rand.nextInt(labelSet.length)]);
        }

        return Point3DWithLabelDatasets.build(x, y, z, labels);
    }

    /**
     * Generates Cell1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data is xSamples * ySamples<li>
     *      <li>Random cell data</li>
     *      <li>Gaussian distribution of values from 0 to 1</li>
     * </ol>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Cell1DDataset makeCell1DGaussianRandomData(int nSamples){
        double[] waveform = new double[nSamples];
        int seed = 1;

        //Creates data
        Random rand = new Random(seed);
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }

        org.diirt.util.stats.Statistics stats = StatisticsUtil.statisticsOf(new ArrayDouble(waveform));
        return Cell1DDatasets.linearRange(new ArrayDouble(waveform), stats.getRange().getMinimum(), stats.getRange().getMaximum());
    }

    /**
     * Generates Cell2D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data is xSamples * ySamples<li>
     *      <li>Random cell data</li>
     *      <li>Gaussian distribution of values from 0 to 1</li>
     * </ol>
     * @param xSamples number of x-cells in data
     * @param ySamples number of y-cells in data
     * @return a set of data to be drawn
     */
    public static Cell2DDataset makeCell2DGaussianRandomData(int xSamples, int ySamples){
        int nSamples = xSamples * ySamples;
        double[] waveform = new double[nSamples];
        int seed = 1;

        //Creates data
        Random rand = new Random(seed);
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }

        return Cell2DDatasets.linearRange(new ArrayDouble(waveform), Ranges.range(0, xSamples), xSamples, Ranges.range(0, ySamples), ySamples);
    }

    //--------------------------------------------------------------------------
}
