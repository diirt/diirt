/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.PVCacheImpl;
import org.diirt.datasource.timecache.PVCacheListener;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorage;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link PVCacheImpl}: retrieves samples from sources for a given time
 * interval and stores them.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheUnitTest {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private class PVCacheListenerCounter implements PVCacheListener {
        private final AtomicInteger newDataCount = new AtomicInteger(0);
        private final Instant start;
        private final Instant end;

        public PVCacheListenerCounter() {
            this.start = null;
            this.end = null;
        }
        public PVCacheListenerCounter(Instant start, Instant end) {
            this.start = start;
            this.end = end;
        }
        @Override
        public void newDataInCache(SortedSet<Data> newData,
                TimeInterval newDataInterval, IntervalsList completedIntervals) {
            if (this.start == null && this.end == null) {
                newDataCount.getAndAdd(newData.size());
                return;
            }
            for (Data d : newData) {
                if (d.getTimestamp().compareTo(start) >= 0
                        && d.getTimestamp().compareTo(end) <= 0) {
                    newDataCount.addAndGet(1);
                }
            }
        }
        @Override
        public void updatedCompletedIntervals(IntervalsList completedIntervals) {
            // TODO Auto-generated method stub
        }
        public int getCount() {
            return newDataCount.get();
        }
        public void reset() {
            newDataCount.getAndSet(0);
        }
    }

    private class DataRequestListenerCounter implements DataRequestListener {
        private final AtomicInteger newDataRequestThreadCount = new AtomicInteger(0);
        private final AtomicBoolean intervalCompleted = new AtomicBoolean(false);

        @Override
        public void newData(DataChunk chunk, DataRequestThread thread) {
            newDataRequestThreadCount.addAndGet(chunk.getDatas().size());
        }
        @Override
        public void intervalComplete(DataRequestThread thread) {
            intervalCompleted.getAndSet(true);
        }
        public int getCount() {
            return newDataRequestThreadCount.get();
        }
        public boolean hasCompleted() {
            return intervalCompleted.get();
        }
        public void reset() {
            newDataRequestThreadCount.getAndSet(0);
            intervalCompleted.getAndSet(false);
        }
    }

    /**
     * Test that PV cache retrieves samples in the requested interval and does
     * not request sources again if asking for the same interval. Test that no
     * sample is lost when retrieved from storage.
     */
    @Test
    public void testRetrieveSequential() {
        List<DataSource> sourcesList = new ArrayList<DataSource>();
        sourcesList.add(new SimpleFileDataSource(
                "src/test/resources/archive-ramps-1W.csv", 100));
        DataStorage storage = new SimpleMemoryStorage(100);
        try {
            PVCacheImpl cache = new PVCacheImpl("TEST-BTY0:RAMP2", sourcesList, storage);
            Instant start = dateFormat.parse("2014-11-28 00:00").toInstant();
            Instant end = dateFormat.parse("2014-11-29 00:00").toInstant();

            PVCacheListenerCounter pvCache_counter = new PVCacheListenerCounter();
            cache.addListener(pvCache_counter);

            cache.retrieveDataAsync(TimeInterval.between(start, end));
            Assert.assertTrue(cache.isProcessingSources());
            int limit = 0;
            while (cache.isProcessingSources() && limit <= 60) { // 30s
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                limit++;
            }
            IntervalsList completedIntervals = cache.getCompletedIntervalsList();
            // assert that cache has completed the requested interval
            Assert.assertTrue(completedIntervals.contains(TimeInterval.between(start, end)));
            Assert.assertEquals(43200, pvCache_counter.getCount());

            pvCache_counter.reset();
            DataRequestListenerCounter requestThread_counter = new DataRequestListenerCounter();

            // ask for smaller inner interval and verify no source is requested
            start = dateFormat.parse("2014-11-28 12:00").toInstant();
            end = dateFormat.parse("2014-11-29 00:00").toInstant();
            DataRequestThread thread = cache.retrieveDataAsync(TimeInterval.between(start, end));
            Assert.assertFalse(cache.isProcessingSources());
            thread.addListener(requestThread_counter);
            thread.run();
            Assert.assertEquals(0, pvCache_counter.getCount());
            Assert.assertEquals(21600, requestThread_counter.getCount());
            Assert.assertEquals(true, requestThread_counter.hasCompleted());

            // ask for overlapping interval and verify both source & storage are requested
            pvCache_counter.reset();
            requestThread_counter.reset();
            start = dateFormat.parse("2014-11-27 12:00").toInstant();
            end = dateFormat.parse("2014-11-28 12:00").toInstant();
            thread = cache.retrieveDataAsync(TimeInterval.between(start, end));
            Assert.assertTrue(cache.isProcessingSources());
            limit = 0;
            while (cache.isProcessingSources() && limit <= 60) { // 30s
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                limit++;
            }
            thread.addListener(requestThread_counter);
            thread.run();
            completedIntervals = cache.getCompletedIntervalsList();
            // assert cache has completed the requested interval
            Assert.assertTrue(completedIntervals.contains(TimeInterval.between(
                    dateFormat.parse("2014-11-27 12:00").toInstant(),
                    dateFormat.parse("2014-11-29 00:00").toInstant())));
            Assert.assertEquals(64800,
                    (((SimpleMemoryStorage) ((PVCacheImpl) cache).getStorage())).getStoredSampleCount());
            Assert.assertEquals(21600, pvCache_counter.getCount());
            Assert.assertEquals(43200, requestThread_counter.getCount());
            Assert.assertEquals(true, requestThread_counter.hasCompleted());

            // ask for interval before previous ones and verify only source is requested
            pvCache_counter.reset();
            requestThread_counter.reset();
            start = dateFormat.parse("2014-11-27 00:00").toInstant();
            end = dateFormat.parse("2014-11-27 12:00").toInstant();
            thread = cache.retrieveDataAsync(TimeInterval.between(start, end));
            Assert.assertTrue(cache.isProcessingSources());
            limit = 0;
            while (cache.isProcessingSources() && limit <= 60) { // 30s
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                limit++;
            }
            thread.addListener(requestThread_counter);
            thread.run();
            Assert.assertEquals(21600, pvCache_counter.getCount());
            Assert.assertEquals(21600, requestThread_counter.getCount());
            Assert.assertEquals(true, requestThread_counter.hasCompleted());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that PV cache retrieves samples in the requested interval and does
     * not request sources again if asking for the same interval. Test that no
     * sample is lost when retrieved from storage.
     */
    @Test
    public void testRetrieveParallel() {
        List<DataSource> sourcesList = new ArrayList<DataSource>();
        sourcesList.add(new SimpleFileDataSource(
                "src/test/resources/archive-ramps-1W.csv", 100));
        DataStorage storage = new SimpleMemoryStorage(100);
        try {
            PVCacheImpl cache = new PVCacheImpl("TEST-BTY0:RAMP2", sourcesList, storage);
            final Instant start1 = dateFormat.parse("2014-11-28 00:00").toInstant();
            final Instant end1 = dateFormat.parse("2014-11-29 00:00").toInstant();
            final Instant start2 = dateFormat.parse("2014-11-28 06:00").toInstant();
            final Instant end2 = dateFormat.parse("2014-11-28 12:00").toInstant();

            PVCacheListenerCounter pvCache_counter1 = new PVCacheListenerCounter(start1, end1);
            PVCacheListenerCounter pvCache_counter2 = new PVCacheListenerCounter(start2, end2);
            DataRequestListenerCounter requestThread_counter1 = new DataRequestListenerCounter();
            DataRequestListenerCounter requestThread_counter2 = new DataRequestListenerCounter();

            cache.addListener(pvCache_counter1);
            DataRequestThread thread1 = cache.retrieveDataAsync(TimeInterval.between(start1, end1));
            thread1.addListener(requestThread_counter1);
            thread1.start();
            cache.addListener(pvCache_counter2);
            DataRequestThread thread2 = cache.retrieveDataAsync(TimeInterval.between(start2, end2));
            thread2.addListener(requestThread_counter2);
            thread2.start();
            int limit = 0;
            while (limit <= 60) { // 60s
                try {
                    if (!cache.isProcessingSources()
                            && requestThread_counter1.hasCompleted()
                            && requestThread_counter2.hasCompleted())
                        break;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                limit++;
            }
            if (limit > 60)
                Assert.fail("Timeout");

            Assert.assertEquals(43200, pvCache_counter1.getCount());
            Assert.assertEquals(10800, pvCache_counter2.getCount());
            Assert.assertEquals(0, requestThread_counter1.getCount());
            Assert.assertEquals(0, requestThread_counter2.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
