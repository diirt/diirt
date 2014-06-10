/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.vtype.ndarray;

import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class InvertListProfile {
    public static void main(String[] args) {
        ListInt sizes = new ArrayInt(100,100,10);
        boolean[] invert = new boolean[] {false, true, false};
        int nTries = 10000;
        
        int nSamples = 1;
        for (int i = 0; i < sizes.size(); i++) {
            nSamples *= sizes.getInt(i);
        }
        Random rand = new Random(0);
        double[] data = new double[nSamples];
        for (int i = 0; i < data.length; i++) {
            data[i] = rand.nextGaussian();
        }
        ListDouble list = new ArrayDouble(data);
        
        long direct = benchmark(list, nTries);
        System.out.println(direct / 1000000000.0);
        long wrapped = benchmark(new InvertListNumber.Double(list, sizes, invert), nTries);
        System.out.println(wrapped / 1000000000.0);
        
        System.out.println("Ratio " + (wrapped/direct));
    }
    
    public static long benchmark(ListNumber data, int nTries) {
        long start = System.nanoTime();
        for (int i = 0; i < nTries; i++) {
            average(data);
        }
        long end = System.nanoTime();
        return end - start;
    }
    
    public static void average(ListNumber data) {
        double avg = 0;
        for (int i = 0; i < data.size(); i++) {
            avg += data.getDouble(i);
        }
        avg /= data.size();
        if (avg == 0) {
            System.out.println("Force calculation");
        }
    }
}
