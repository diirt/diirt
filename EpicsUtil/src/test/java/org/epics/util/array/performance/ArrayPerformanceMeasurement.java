/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array.performance;

import java.util.Random;
import org.epics.util.array.*;

/**
 *
 * @author carcassi
 */
public class ArrayPerformanceMeasurement {

    public static void main(String[] args) {
        int arraySize = 100000;
        int nIterations = 10000;

        double[] doubleArray = new double[arraySize];
        float[] floatArray = new float[arraySize];
        Random rand = new Random();
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = rand.nextGaussian();
            floatArray[i] = (float) rand.nextGaussian();
        }

        profileArrayDoubleThroughCollectionNumber(doubleArray, nIterations);
        profileArrayFloatThroughCollectionNumber(floatArray, nIterations);
        profileArrayDoubleThroughCollectionNumber(doubleArray, nIterations);

        profileArray(nIterations, doubleArray);

        profileArrayDouble(doubleArray, nIterations);

//        {
//            // TODO
//            long startTime = System.nanoTime();
//            ArrayDouble coll = new ArrayDouble(doubleArray);
//            for (int i = 0; i < nIterations; i++) {
//                double sum = 0;
//                for (int j = 0; j < coll.size(); j++) {
//                    sum += coll.getDouble(j);
//                }
//                if (sum == 0) {
//                    System.out.println("Unexpected value " + sum);
//                }
//            }
//            long stopTime = System.nanoTime();
//
//            System.out.println("Iteration using abstract class converted: ns " + (stopTime - startTime) / nIterations);
//        }

    }

    private static void profileArrayDoubleThroughCollectionNumber(double[] doubleArray, int nIterations) {
        long startTime = System.nanoTime();
        CollectionNumber list = new ArrayDouble(doubleArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = ArrayOperation.sum.compute(list);
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using abstract class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArrayFloatThroughCollectionNumber(float[] floatArray, int nIterations) {
        long startTime = System.nanoTime();
        CollectionNumber list = new ArrayFloat(floatArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = ArrayOperation.average.compute(list);
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using abstract class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArrayDouble(double[] doubleArray, int nIterations) {
        long startTime = System.nanoTime();
        ArrayDouble coll = new ArrayDouble(doubleArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = 0;
            for (int j = 0; j < coll.size(); j++) {
                sum += coll.getDouble(j);
            }
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using final class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArray(int nIterations, double[] doubleArray) {
        long startTime = System.nanoTime();
        for (int i = 0; i < nIterations; i++) {
            double sum = 0;
            for (int j = 0; j < doubleArray.length; j++) {
                sum += doubleArray[j];
            }
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using array: ns " + (stopTime - startTime) / nIterations);
    }
}
