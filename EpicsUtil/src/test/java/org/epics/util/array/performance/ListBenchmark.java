/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.util.array.performance;

import org.epics.util.array.ListDouble;

/**
 *
 * @author carcassi
 */
public class ListBenchmark {
    
    public static void profileListDouble(ListDouble list, int nIterations) {
        long startTime = System.nanoTime();
        for (int i = 0; i < nIterations; i++) {
            double sum = 0;
            for (int n = 0; n < list.size(); n++) {
                sum += list.getDouble(n);
            }
            if (sum == 0) {
                System.out.println("Unexpected value " + sum);
            }
        }
        long stopTime = System.nanoTime();

        System.out.println("Iteration on ListDouble: " + (stopTime - startTime) / nIterations +" ns/iter - " +
                (stopTime - startTime) / nIterations / list.size() + " ns/sample");
    }
    
}
