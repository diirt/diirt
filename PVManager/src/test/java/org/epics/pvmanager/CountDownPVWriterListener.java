/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.epics.util.time.TimeDuration;

/**
 * Write listener to wait that a certain number of notifications.
 *
 * @author carcassi
 */
public class CountDownPVWriterListener<T> implements PVWriterListener<T> {

    private volatile CountDownLatch latch;
    private volatile PVWriterEvent<T> event;
    private volatile String threadName;
    
    public CountDownPVWriterListener(int count) {
        latch = new CountDownLatch(count);
    }

    @Override
    public void pvChanged(PVWriterEvent<T> event) {
        this.event = event;
        this.threadName = Thread.currentThread().getName();
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
     * The last notified event.
     * 
     * @return the event
     */
    public PVWriterEvent<T> getEvent() {
        return event;
    }

    /**
     * The thread name for the last notification.
     * 
     * @return the thread name
     */
    public String getThreadName() {
        return threadName;
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
    
}
