/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderConfiguration;
import org.epics.pvmanager.PVReaderListener;
import org.epics.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
public class PVReaderTestCase<T> {
    
    private final PVReaderConfiguration<T> readerConf;
    private final List<PVReaderTestListener<T>> listeners = new CopyOnWriteArrayList<>();
    private PVReader<T> pvReader;
    private final CountDownLatch latch = new CountDownLatch(1);

    public PVReaderTestCase(PVReaderConfiguration<T> readerConf) {
        this.readerConf = readerConf;
    }
    
    public static <T> PVReaderTestCase<T> newTest(PVReaderConfiguration<T> readerConf) {
        return new PVReaderTestCase<>(readerConf);
    }
    
    public synchronized PVReaderTestCase<T> addListener(PVReaderListener<T> listener) {
        if (listener instanceof PVReaderTestListener) {
            listeners.add((PVReaderTestListener<T>) listener);
        }
        readerConf.readListener(listener);
        return this;
    }

    public synchronized void start(TimeDuration testDuration) {
        pvReader = readerConf.maxRate(TimeDuration.ofHertz(50));
        PVManager.getReadScannerExecutorService().schedule(new Runnable() {

            @Override
            public void run() {
                stop();
            }
        }, testDuration.toNanosLong(), TimeUnit.NANOSECONDS);
    }
    
    private synchronized void stop() {
        pvReader.close();
        pvReader = null;
        for (PVReaderTestListener<T> pVReaderTestListener : listeners) {
            pVReaderTestListener.close();
        }
        latch.countDown();
    }
    
    public void await() {
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void printErrors() {
        for (PVReaderTestListener<T> pVReaderTestListener : listeners) {
            if (!pVReaderTestListener.isSuccess()) {
                System.out.println(pVReaderTestListener.getErrorMessage());
            }
        }
    }
}
