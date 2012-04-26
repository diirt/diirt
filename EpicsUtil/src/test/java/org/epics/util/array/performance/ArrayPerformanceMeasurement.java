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
        System.out.println(System.getProperty("java.version"));
        
        int arraySize = 100000;
        int nIterations = 10000;

        double[] doubleArray = new double[arraySize];
        float[] floatArray = new float[arraySize];
        byte[] byteArray = new byte[arraySize];
        Random rand = new Random();
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = rand.nextGaussian();
            floatArray[i] = (float) rand.nextGaussian();
        }
        rand.nextBytes(byteArray);

        profileArrayThroughCollectionNumber(doubleArray, nIterations);
        profileArrayThroughCollectionNumber(floatArray, nIterations);
        profileArrayThroughCollectionNumber(byteArray, nIterations);
        profileArrayThroughCollectionNumber(doubleArray, nIterations);

        profileJavaArray(nIterations, doubleArray);
        profileJavaArray(nIterations, byteArray);

        profileArray(doubleArray, nIterations);
        profileArray(byteArray, nIterations);

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

    private static void profileArrayThroughCollectionNumber(double[] doubleArray, int nIterations) {
        long startTime = System.nanoTime();
        CollectionNumber list = new ArrayDouble(doubleArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = ArrayOperation.sum.compute(list);
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using double[] through abstract class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArrayThroughCollectionNumber(float[] floatArray, int nIterations) {
        long startTime = System.nanoTime();
        CollectionNumber list = new ArrayFloat(floatArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = ArrayOperation.average.compute(list);
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using float[] through abstract class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArrayThroughCollectionNumber(byte[] byteArray, int nIterations) {
        long startTime = System.nanoTime();
        CollectionNumber list = new ArrayByte(byteArray);
        for (int i = 0; i < nIterations; i++) {
            double sum = ArrayOperation.average.compute(list);
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using byte[] through abstract class: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArray(double[] doubleArray, int nIterations) {
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

        System.out.println("Iteration using ArrayDouble: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileArray(byte[] byteArray, int nIterations) {
        long startTime = System.nanoTime();
        ArrayByte coll = new ArrayByte(byteArray);
        for (int i = 0; i < nIterations; i++) {
            int sum = 0;
            for (int j = 0; j < coll.size(); j++) {
                sum += coll.getByte(j);
            }
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using ArrayByte: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileJavaArray(int nIterations, double[] doubleArray) {
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

        System.out.println("Iteration using double[]: ns " + (stopTime - startTime) / nIterations);
    }

    private static void profileJavaArray(int nIterations, byte[] byteArray) {
        long startTime = System.nanoTime();
        for (int i = 0; i < nIterations; i++) {
            int sum = 0;
            for (int j = 0; j < byteArray.length; j++) {
                sum += byteArray[j];
            }
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration using byte[]: ns " + (stopTime - startTime) / nIterations);
    }
}
