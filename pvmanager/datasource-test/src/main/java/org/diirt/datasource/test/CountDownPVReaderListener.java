/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;

/**
 * Read listener to wait that a certain number of notifications.
 *
 * @author carcassi
 */
public class CountDownPVReaderListener implements PVReaderListener<Object> {

    private volatile CountDownLatch latch;
    private volatile PVReaderEvent<Object> event;
    private final int mask;

    public CountDownPVReaderListener(int count) {
        this(count, PVReaderEvent.CONNECTION_MASK | PVReaderEvent.EXCEPTION_MASK | PVReaderEvent.VALUE_MASK);
    }

    public CountDownPVReaderListener(int count, int mask) {
        latch = new CountDownLatch(count);
        this.mask = mask;
    }

    @Override
    public void pvChanged(PVReaderEvent<Object> event) {
        if ((event.getNotificationMask() & mask) != 0) {
            this.event = event;
            latch.countDown();
        }
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
     * @throws InterruptedException if interrupted
     */
    public boolean await(Duration duration)
    throws InterruptedException {
        return latch.await(duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    public PVReaderEvent<Object> getEvent() {
        return event;
    }

}
