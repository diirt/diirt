/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.diirt.datasource.ExceptionHandler;

/**
 * Read listener to wait that a certain number of notifications.
 *
 * @author carcassi
 */
public class CountDownWriteFunction extends ExceptionHandler {

    private volatile CountDownLatch latch;
    private volatile Exception exception;

    public CountDownWriteFunction(int count) {
        latch = new CountDownLatch(count);
    }

    @Override
    public void handleException(Exception esception) {
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
     * @throws InterruptedException if interrupted
     */
    public boolean await(Duration duration)
    throws InterruptedException {
        return latch.await(duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    public Exception getException() {
        return exception;
    }

}
