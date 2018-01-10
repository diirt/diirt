/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryChunk;
import org.diirt.datasource.timecache.query.QueryData;
import org.diirt.datasource.timecache.query.QueryImpl;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.datasource.timecache.query.QueryResult;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.datasource.timecache.util.PVCacheMock;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.TimeRelativeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link QueryImpl}: fills chunks with samples retrieved from sources or
 * storage.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryUnitTests {

    final private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Instant timeOf(String date) throws ParseException {
        return dateFormat.parse(date).toInstant();
    }

    /**
     * Test that the right numbers of chunks is created per query.
     */
    @Test
    public void testUpdateWithoutStorage() {
        try {
            PVCacheMock cache = new PVCacheMock();
            Instant start = timeOf("2014-11-26 00:00");
            Instant end = timeOf("2014-11-27 00:00");
            QueryParameters params = new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(start, end));

            // 24 chunk per 1 day query => 1 hour per chunk
            Query query = new QueryImpl(cache, 24);
            query.update(params);
            List<QueryChunk> chunks = ((QueryImpl) query).getChunks();
            Assert.assertEquals(24, chunks.size());
            for (QueryChunk chunk : chunks) {
                Duration chunkDuration = Duration.between(chunk.getTimeInterval().getEnd(),chunk.getTimeInterval().getStart()).abs();
                Assert.assertEquals(3600, chunkDuration.getSeconds(), 1);
            }

            // 100 chunk per 1 day query => 864 seconds per chunk
            query = new QueryImpl(cache, 100);
            query.update(params);
            chunks = ((QueryImpl) query).getChunks();
            Assert.assertEquals(100, chunks.size());
            for (QueryChunk chunk : chunks) {
                Duration chunkDuration = Duration.between(chunk.getTimeInterval().getEnd(),chunk.getTimeInterval().getStart()).abs();
                Assert.assertEquals(864, chunkDuration.getSeconds(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the right numbers of chunks is created per query.
     * Test that each created chunk is filled properly by samples from a simulated storage.
     */
    @Test
    public void testUpdateWithStorage() {
        try {
            PVCacheMock cache = new PVCacheMock();
            Instant start = timeOf("2014-11-26 00:00");
            Instant end = timeOf("2014-11-27 00:00");
            QueryParameters params = new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(start, end));

            // 100 chunk per 1 day query => 864 seconds per chunk
            Query query = new QueryImpl(cache, 100);

            // Simulate data from storage by adding a storage thread to the mock
            DataSource source = new SimpleFileDataSource("src/test/resources/archive-ramps-1W.csv");
            DataRequestThread thread = new DataRequestThread("TEST-BTY0:RAMP2",
                    source, TimeInterval.between(start, end));
            final AtomicBoolean intervalCompleted = new AtomicBoolean(false);
            thread.addListener(new DataRequestListener() {
                @Override
                public void newData(DataChunk chunk, DataRequestThread thread) {
                }
                @Override
                public void intervalComplete(DataRequestThread thread) {
                    intervalCompleted.getAndSet(true);
                }
            });
            cache.setStorageThread(thread);

            // Start the query
            query.update(params);
            int limit = 0;
            while (!intervalCompleted.get() && limit <= 60) { // 30s
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                limit++;
            }

            // Check that chunks contains the right number of samples
            List<QueryChunk> chunks = ((QueryImpl) query).getChunks();
            Assert.assertEquals(100, chunks.size());
            for (QueryChunk chunk : chunks) {
                    Duration chunkDuration = Duration.between(chunk.getTimeInterval().getEnd(),chunk.getTimeInterval().getStart()).abs();
                Assert.assertEquals(864, chunkDuration.getSeconds(), 1);
                Assert.assertEquals(432, chunk.getDataCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the right numbers of chunks is created per query.
     * Test that each created chunk is filled properly by samples from a simulated source.
     */
    @Test
    public void testNewData() {
        try {
            // 24 chunk per 1 day query => 1 hour per chunk => 1800 data
            subTestNewData(24, 1800);
            // 100 chunk per 1 day query => 864 seconds per chunk => 423 data
            subTestNewData(100, 432);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    // Subroutine of testNewData
    private void subTestNewData(int nbOfChunks, int expectedCount)
            throws Exception {
        PVCacheMock cache = new PVCacheMock();
        final Instant start = timeOf("2014-11-27 00:00");
        final Instant end = timeOf("2014-11-28 00:00");
        // The query will request for completed intervals in order to mark
        // chunks as completed, so we define them in the mock
        cache.addCompletedInterval(TimeInterval.between(start, end));

        QueryParameters params = new QueryParameters()
                .timeInterval(TimeRelativeInterval.of(start, end));
        final Query query = new QueryImpl(cache, nbOfChunks);

        // Start the query
        query.update(params);

        // Simulate new data from source
        DataSource source = new SimpleFileDataSource("src/test/resources/archive-ramps-1W.csv");
        DataRequestThread thread = new DataRequestThread("TEST-BTY0:RAMP2",
                source, TimeInterval.between(start, end));
        final IntervalsList completedIntervals = new IntervalsList();
        thread.addListener(new DataRequestListener() {
            @Override
            public void newData(DataChunk chunk, DataRequestThread thread) {
                completedIntervals.addToSelf(chunk.getInterval());
                ((QueryImpl) query).newDataInCache(chunk.getDatas(), chunk.getInterval(), completedIntervals);
            }
            @Override
            public void intervalComplete(DataRequestThread thread) {
                ((QueryImpl) query).updatedCompletedIntervals(new IntervalsList(TimeInterval.between(start, end)));
            }
        });
        thread.run();
        // Wait for remaining tasks to be executed in ExecutorService
        while (((QueryImpl) query).isProcessingData())
            Thread.sleep(500);

        // Check that each chunk contains the expected number of samples
        List<QueryChunk> chunks = ((QueryImpl) query).getChunks();
        for (QueryChunk chunk : chunks) {
            Assert.assertEquals(expectedCount, chunk.getDataCount());
        }

        QueryResult result = query.getUpdate();
        Assert.assertEquals(nbOfChunks, result.getData().size());
        for (QueryData data : result.getData()) {
            Assert.assertEquals(expectedCount, data.getCount());
        }

        // All chunks should have been marked as sent
        chunks = ((QueryImpl) query).getChunks();
        for (QueryChunk chunk : chunks) {
            Assert.assertTrue(chunk.hasBeenSent());
        }
    }

}
