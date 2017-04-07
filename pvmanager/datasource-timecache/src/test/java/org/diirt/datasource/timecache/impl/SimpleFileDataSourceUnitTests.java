/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.time.TimeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link SimpleFileDataSource}: read samples from a file dump of
 * 'sample_view' from Archive RDB.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class SimpleFileDataSourceUnitTests {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Map<DataRequestThread, Set<Instant>> dataTimes = new HashMap<DataRequestThread, Set<Instant>>();
    private static Instant start = null;
    private static Instant end = null;
    private static boolean finished = false;

    // listener that counts data in the requested interval
    private static class TUListener implements DataRequestListener {
        @Override
        public void newData(DataChunk chunk, DataRequestThread thread) {
            System.out.println(CacheHelper.format(TimeInterval.between(
                    chunk.getDatas().first().getTimestamp(),
                    chunk.getDatas().last().getTimestamp()))
                    + ": " + chunk.getDatas().size());
            for (Data d : chunk.getDatas()) {
                if (d.getTimestamp().compareTo(start) >= 0) {
                    if (dataTimes.get(thread).contains(d.getTimestamp()))
                        System.out.println("D: " + CacheHelper.format(d.getTimestamp()));
                    dataTimes.get(thread).add(d.getTimestamp());
                }
            }
        }

        @Override
        public void intervalComplete(DataRequestThread thread) {
            finished = true;
        }
    }

    // starts the thread and counts received data
    private static void startAndCount(DataRequestThread thread) {
        if (dataTimes.get(thread) == null)
            dataTimes.put(thread, new TreeSet<Instant>());
        dataTimes.get(thread).clear();
        finished = false;
        thread.start();
        int limit = 0;
        while (!finished && limit <= 60) { // 30s
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            limit++;
        }
    }

    /**
     * Test that wrong parameters setting in DataSource.getData does not raise
     * an exception and results in an empty chunk. Test that DataSource.getData
     * returns an ordered chunk with all samples times superior to the specified
     * one. Test that all samples times are included in DataChunk.getInterval.
     */
    @Test
    public void readData() {
        DataSource source = new SimpleFileDataSource(
                "src/test/resources/archive-export.csv");
        try {
            start = dateFormat.parse("2014-03-14 16:00").toInstant();

            // test wrong parameters
            DataChunk chunk = null;
            chunk = source.getData(null, null);
            Assert.assertTrue(chunk.isEmpty());
            chunk = source.getData("TEST-BTY0:AI1", null);
            Assert.assertTrue(chunk.isEmpty());
            chunk = source.getData(null, start);
            Assert.assertTrue(chunk.isEmpty());

            // test chunk time interval
            chunk = source.getData("TEST-BTY0:AI1", start);
            Assert.assertTrue(chunk.isFull());
            TimeInterval chunkInterval = chunk.getInterval();
            Assert.assertEquals(chunkInterval.getStart(), chunk.getDatas().first().getTimestamp());
            Assert.assertEquals(chunkInterval.getEnd(), chunk.getDatas().last().getTimestamp());
            Assert.assertTrue(chunkInterval.getStart().compareTo(start) >= 0);

            // test values are ordered by timestamp
            Iterator<Data> itChunk = chunk.getDatas().iterator();
            Instant next = null;
            Instant previous = itChunk.next().getTimestamp();
            while (itChunk.hasNext()) {
                next = itChunk.next().getTimestamp();
                Assert.assertTrue(previous.compareTo(next) < 0);
                previous = next;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that {@link DataRequestThread} reads all samples from a small dedicated
     * file. Test that samples are grouped in chunks. Test that result are
     * returned in chronological order. Test that wrong intervals values do not
     * raise an exception.
     */
    @Test
    public void readInterval() {
        DataSource source = new SimpleFileDataSource(
                "src/test/resources/mini-archive-export.csv");
        DataRequestThread thread = null;
        try {
            start = dateFormat.parse("2014-03-14 16:00").toInstant();
            end = dateFormat.parse("2014-03-14 17:00").toInstant();

            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(start, end));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(234, dataTimes.get(thread).size());

            // inverse interval => same result
            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(end, start));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(234, dataTimes.get(thread).size());

            // empty interval => no data
            start = dateFormat.parse("2014-03-14 17:00").toInstant();
            end = dateFormat.parse("2014-03-14 18:00").toInstant();
            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(start, end));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(0, dataTimes.get(thread).size());

            // only start => same result
            start = dateFormat.parse("2014-03-14 16:00").toInstant();
            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(start, null));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(234, dataTimes.get(thread).size());

            // only end => no data
            end = dateFormat.parse("2014-03-14 17:00").toInstant();
            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(null, end));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(0, dataTimes.get(thread).size());

            // infinite => no data
            end = dateFormat.parse("2014-03-14 17:00").toInstant();
            thread = new DataRequestThread("TEST-BTY0:AI1", source,
                    TimeInterval.between(null, null));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(0, dataTimes.get(thread).size());

            // test wrong parameters
            try {
                thread = new DataRequestThread(null, source,
                        TimeInterval.between(null, null));
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(true);
            }

            try {
                thread = new DataRequestThread("TEST-BTY0:AI1", null,
                        TimeInterval.between(null, null));
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(true);
            }

            try {
                thread = new DataRequestThread("TEST-BTY0:AI1", source, null);
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the source can be requested by more than one thread at the same
     * time and on the same channel.
     */
    @Test
    public void readMultiple() {
        DataSource source = new SimpleFileDataSource(
                "src/test/resources/mini-archive-export.csv");
        try {
            start = dateFormat.parse("2014-03-14 16:00").toInstant();
            end = dateFormat.parse("2014-03-14 17:00").toInstant();

            DataRequestThread thread1 = new DataRequestThread(
                    "TEST-BTY0:AI1", source, TimeInterval.between(start, end));
            DataRequestThread thread2 = new DataRequestThread(
                    "TEST-BTY0:AI1", source, TimeInterval.between(start, end));
            DataRequestThread thread3 = new DataRequestThread(
                    "TEST-BTY0:AI1", source, TimeInterval.between(start, end));
            thread1.addListener(new TUListener());
            thread2.addListener(new TUListener());
            thread3.addListener(new TUListener());
            startAndCount(thread1);
            startAndCount(thread2);
            startAndCount(thread3);
            Assert.assertEquals(234, dataTimes.get(thread1).size());
            Assert.assertEquals(234, dataTimes.get(thread2).size());
            Assert.assertEquals(234, dataTimes.get(thread3).size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that no data is missing when requesting the source.
     */
    @Test
    public void readCount() {
        DataSource source = new SimpleFileDataSource(
                "src/test/resources/archive-ramps-1H.csv", 10);
        try {
            start = dateFormat.parse("2014-12-04 00:00").toInstant();
            end = dateFormat.parse("2014-12-04 00:30").toInstant();
            DataRequestThread thread = new DataRequestThread("TEST-BTY0:RAMP2",
                    source, TimeInterval.between(start, end));
            thread.addListener(new TUListener());
            startAndCount(thread);
            Assert.assertEquals(900, dataTimes.get(thread).size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
