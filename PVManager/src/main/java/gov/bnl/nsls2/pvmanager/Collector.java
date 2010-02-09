/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collects the data at the CA rate and allows a client to get all values
 * since last check. The class needs to be thread safe, and it function
 * as a queue where the CA threads post data, and a timer based thread
 * collects the data.
 *
 * @author carcassi
 */
public class Collector {

    // @GuardedBy(this)
    private List<Double> buffer;
    
    public Collector() {
        synchronized(this) {
            buffer = new ArrayList<Double>();
        }
    }

    /**
     * Adds a value in the queue.
     * @param value the new value
     */
    public synchronized void post(double value) {
        buffer.add(value);
    }

    /**
     * Returns all values since last check and removes values from the queue.
     * @return a new array with the value; never null
     */
    public synchronized double[] getData() {
        double[] data = new double[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            data[i] = buffer.get(i);
        }
        buffer.clear();
        return data;
    }
}