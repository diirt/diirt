/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.WriteFunction;
import org.epics.util.time.TimeDuration;

/**
 * Read listener to wait that a certain number of notifications.
 *
 * @author carcassi
 */
public class CountDownWriteFunction implements WriteFunction<Exception> {

    private volatile CountDownLatch latch;
    private volatile Exception exception;
    
    public CountDownWriteFunction(int count) {
        latch = new CountDownLatch(count);
    }

    @Override
    public void writeValue(Exception esception) {
        this.exception = esception;
        latch.countDown();
    }

    /**
     * Changes the count back to count.
     * 
     * @param count new value for count
     */
    public void resetCount(int count) {
        latch = new CountDownLatch(count);
    }
    
    /**
     * Current count.
     * 
     * @return current count
     */
    public int getCount() {
        return (int) latch.getCount();
    }
    
    /**
     * Waits that the listener count goes to zero.
     * 
     * @param duration time to wait
     * @return false if count didn't go to zero
     * @throws InterruptedException 
     */
    public boolean await(TimeDuration duration) 
    throws InterruptedException {
        return latch.await(duration.toNanosLong(), TimeUnit.NANOSECONDS);
    }

    public Exception getException() {
        return exception;
    }
    
}
