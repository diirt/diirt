/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;

/**
 * Write listener to wait that a certain number of notifications.
 *
 * @author carcassi
 */
public class CountDownPVWriterListener<T> implements PVWriterListener<T> {

    private volatile CountDownLatch latch;
    private volatile PVWriterEvent<T> event;
    private volatile String threadName;
    private AtomicInteger notificationCount = new AtomicInteger();

    public CountDownPVWriterListener(int count) {
        latch = new CountDownLatch(count);
    }

    @Override
    public void pvChanged(PVWriterEvent<T> event) {
        this.event = event;
        notificationCount.incrementAndGet();
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
     * The total number of notifications on this listener.
     *
     * @return the number of notifications
     */
    public int getNotificationCount() {
        return notificationCount.get();
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

}
