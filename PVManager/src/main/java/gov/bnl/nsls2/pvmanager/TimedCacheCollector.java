/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.aps.jca.dbr.TimeStamp;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class TimedCacheCollector<T> extends Collector<T> {

    private final Deque<T> buffer = new ArrayDeque<T>();
    private final PVFunction<T> function;
    private final int cachedPeriodInMs;
    
    TimedCacheCollector(PVFunction<T> function, int cachedPeriodInMs) {
        this.function = function;
        this.cachedPeriodInMs = cachedPeriodInMs;
    }
    /**
     * Calculates the next value and puts it in the queue.
     */
    @Override
    synchronized void collect() {
        // Calculation may take time, and is locked by this
        T newValue = function.getValue();

        // Buffer is locked and updated
        if (newValue != null) {
            synchronized(buffer) {
                buffer.add(newValue);
            }
        }
    }

    /**
     * Returns all values since last check and removes values from the queue.
     * @return a new array with the value; never null
     */
    @Override
    List<T> getData() {
        synchronized(buffer) {
            // last allowed time = now - msCache / 1000
            BigDecimal lastAllowedTime = new TimeStamp().asBigDecimal()
                    .subtract(new BigDecimal(cachedPeriodInMs).divide(new BigDecimal(1000)));
            while (!buffer.isEmpty() && lastAllowedTime.compareTo(TypeSupport.timestampOfAccordingly(buffer.getFirst()))
                    > 0) {
                // Discard value
                buffer.removeFirst();
            }
            return new ArrayList<T>(buffer);
        }
    }

}
