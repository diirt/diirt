/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.impl;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorageListener;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.datasource.timecache.util.TimestampsSet;
import org.diirt.util.time.TimeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link SimpleMemoryStorage}: stores provided samples and releases them
 * properly when memory is full.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class SimpleMemoryStorageUnitTests {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Test that wrong parameters setting does not raise an exception. Test that
     * no data is lost when writing/reading from storage. Test that exotic
     * intervals are well handled.
     */
    @Test
    public void testStorage() {
        DataSource source = new SimpleFileDataSource(
                "src/test/resources/archive-export.csv");
        SimpleMemoryStorage storage = new SimpleMemoryStorage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            // retrieve chunk
            Date startDate = sdf.parse("2014-03-14 16:00");
            DataChunk chunk = source.getData("TEST-BTY0:AI1", startDate.toInstant());
            int chunkSize = chunk.getDatas().size();
            TimeInterval chunkInterval = chunk.getInterval();

            // test wrong parameters
            SortedSet<Data> dataSet = null;
            storage.storeData(null);
            dataSet = storage.getAvailableData(null);
            Assert.assertTrue(dataSet.isEmpty());
            dataSet = storage.getAvailableData(chunkInterval);
            Assert.assertTrue(dataSet.isEmpty());

            // store/retrieve data and compare
            storage.storeData(chunk);
            dataSet = storage.getAvailableData(chunkInterval);
            Assert.assertEquals(chunkSize, dataSet.size());
            Iterator<Data> itSet = dataSet.iterator();
            Iterator<Data> itChunk = chunk.getDatas().iterator();
            for (int index = 0; index < chunkSize; index++) {
                Assert.assertTrue(itChunk.hasNext());
                Assert.assertTrue(itSet.hasNext());
                Assert.assertEquals(itChunk.next().getTimestamp(),
                        itSet.next().getTimestamp());
            }

            // test intervals with one/both missing border
            dataSet = storage.getAvailableData(TimeInterval.between(chunkInterval.getStart(), null));
            Assert.assertEquals(chunkSize, dataSet.size());
            dataSet = storage.getAvailableData(TimeInterval.between(null, chunkInterval.getEnd()));
            Assert.assertEquals(chunkSize, dataSet.size());
            dataSet = storage.getAvailableData(TimeInterval.between(null, null));
            Assert.assertEquals(chunkSize, dataSet.size());

            // test interval with reversed begin/end
            dataSet = storage.getAvailableData(TimeInterval.between(
                    chunkInterval.getEnd(), chunkInterval.getStart()));
            Assert.assertEquals(chunkSize, dataSet.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the storage properly notify its listeners when
     * {@link SoftReference} are released.
     */
    @Test
    public void testOverflow() {
        final DataSource source = new SimpleFileDataSource(
                "src/test/resources/archive-ramps-1W.csv", 1000);
        final SimpleMemoryStorage storage = new SimpleMemoryStorage();
        try {
            Instant start = dateFormat.parse("2014-11-27 00:00").toInstant();
            Instant end = dateFormat.parse("2014-12-01 00:00").toInstant();
            DataRequestThread thread = new DataRequestThread("TEST-BTY0:RAMP2",
                    source, TimeInterval.between(start, end));

            // hold strong references
            final List<Data> references = new ArrayList<Data>();
            thread.addListener(new DataRequestListener() {
                @Override
                public void newData(DataChunk chunk, DataRequestThread thread) {
                    references.addAll(storage.storeData(chunk));
                }
                @Override
                public void intervalComplete(DataRequestThread thread) {
                }
            });
            thread.run();
            Assert.assertEquals(173000, storage.getStoredSampleCount());

            final IntervalsList lostIntervals = new IntervalsList();
            final AtomicInteger lostDataCount = new AtomicInteger(0);
            storage.addListener(new DataStorageListener() {
                @Override
                public void dataLoss(TimestampsSet lostSet) {
                    lostDataCount.getAndAdd(lostSet.getSize());
                    lostIntervals.addToSelf(lostSet.toIntervalsList());
                }
            });
            /* Force releasing SoftReferences */
            try {
                final List<long[]> memhog = new LinkedList<long[]>();
                while (true) {
                    memhog.add(new long[102400]);
                }
            } catch (final OutOfMemoryError e) {
                /*
                 * At this point all SoftReferences have been released -
                 * GUARANTEED.
                 */
                e.printStackTrace();
            }
            // trigger queue processing
            storage.storeData(null);
            Assert.assertEquals(0, storage.getStoredSampleCount());
            Assert.assertEquals(173000, lostDataCount.get());
            System.out.println(lostIntervals);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
