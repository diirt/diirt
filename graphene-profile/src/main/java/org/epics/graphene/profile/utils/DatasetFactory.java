/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene.profile.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.epics.graphene.Cell2DDataset;
import org.epics.graphene.Cell2DDatasets;
import org.epics.graphene.Histogram1D;
import org.epics.graphene.Histograms;
import org.epics.graphene.Point1DCircularBuffer;
import org.epics.graphene.Point1DDataset;
import org.epics.graphene.Point1DDatasetUpdate;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.RangeUtil;
import org.epics.util.array.ArrayDouble;

public class DatasetFactory {
    private DatasetFactory(){}
   
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
    
    
    /**
     * Generates Point1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random data</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Point1DDataset makePoint1DGaussianRandomData(int nSamples){        
        Point1DCircularBuffer dataset = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        dataset.update(update);   
        
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
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */
    public static Point2DDataset makePoint2DGaussianRandomData(int nSamples){
        double[] waveform = new double[nSamples];
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);        
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }
        
        return org.epics.graphene.Point2DDatasets.lineData(waveform);
    }
    
    /**
     * Generates Cell2D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data is xSamples * ySamples<li>
     *      <li>Random cell data</li>
     *      <li>Gaussian distribution of values from 0 to 1</li>
     * @param xSamples number of x-cells in data
     * @param ySamples number of y-cells in data
     * @return a set of data to be drawn
     */    
    public static Cell2DDataset makeCell2DGaussianRandomData(int xSamples, int ySamples){
        int nSamples = xSamples * ySamples;
        double[] waveform = new double[nSamples];
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);        
        for (int i = 0; i < nSamples; i++){
            waveform[i] = rand.nextGaussian();
        }
        
        return Cell2DDatasets.linearRange(new ArrayDouble(waveform), RangeUtil.range(0, xSamples), xSamples, RangeUtil.range(0, ySamples), ySamples);
    }
    
    /**
     * Generates Histogram1D data that can be used in rendering.
     * The data set has the following properties:
     * <ol>
     *      <li>Size of data (number of points) is nSamples<li>
     *      <li>Random values</li>
     *      <li>Gaussian distribution from 0 to 1</li>
     * @param nSamples number of points in data
     * @return a set of data to be drawn
     */    
    public static Histogram1D makeHistogram1DGaussianRandomData(int nSamples){
        Point1DCircularBuffer dataset = new Point1DCircularBuffer(nSamples);
        Point1DDatasetUpdate update = new Point1DDatasetUpdate();
        int maxValue = 1;
        
        //Creates data
        Random rand = new Random(maxValue);                
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        dataset.update(update);
        
        return Histograms.createHistogram(dataset);        
    }    
}
