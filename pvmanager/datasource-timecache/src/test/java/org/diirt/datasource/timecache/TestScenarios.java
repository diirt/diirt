/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryData;
import org.diirt.datasource.timecache.query.QueryImpl;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.datasource.timecache.query.QueryResult;
import org.diirt.datasource.timecache.query.QueryStatistics;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.TimeRelativeInterval;
import org.diirt.vtype.VType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TestScenarios {

    private static final int CHUNK_SIZE = 1000;
    private static final int NB_CHUNKS = 100;

    private class QueryDataCounter extends Thread {

        private final Query query;
        private final int timeout;

        private boolean finished = false;

        private Set<Instant> timestamps;
        private IntervalsList receivedIntervals = new IntervalsList();
        private int receivedChunksCount = 0;
        private int updateCount = 0;
        private int resultCount = 0;
        private int doubloonCount = 0;

        public QueryDataCounter(Query query, int timeout) {
            this.query = query;
            this.timeout = timeout;
            this.timestamps = new TreeSet<Instant>();
        }

        /** {@inheritDoc} */
        @Override
        public void run() {
            finished = false;
            QueryResult result = null;
            int limit = 0;
            while (receivedChunksCount < NB_CHUNKS && limit <= timeout) {
                try {
                    Thread.sleep(1000);
                    result = query.getUpdate();
                    for (QueryData data : result.getData()) {
                        receivedChunksCount++;
                        updateCount += data.getCount();
                        receivedIntervals.addToSelf(data.getTimeInterval());
                        for (Instant t : data.getTimestamps()) {
                            if (timestamps.contains(t)) doubloonCount++;
                            else timestamps.add(t);
                        }
                    }
                } catch (InterruptedException e) {
                }
                limit++;
            }
            if (limit > timeout)
                System.out.println(query + ": TIMEOUT !");
            result = query.getResult();
            for (QueryData data : result.getData())
                resultCount += data.getCount();
            query.close();
            finished = true;
        }

        public boolean isFinished() {
            return finished;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public int getStorageHit() {
            return ((QueryImpl) query).getStatistics().getStorageHit();
        }

        public int getSourceHit() {
            return ((QueryImpl) query).getStatistics().getSourceHit();
        }

        public PVCache getPVCache() {
            return ((QueryImpl) query).getCache();
        }

        public boolean verify() {
            return receivedChunksCount == NB_CHUNKS
                    && receivedIntervals.isConnex()
                    && ((QueryImpl) query).getInterval().equals(receivedIntervals.getTimeInterval(0));
        }

        public int getResultCount() {
            return resultCount;
        }

        public int getDoubloonCount() {
            return doubloonCount;
        }

        public int getTimetampCount() {
            return timestamps.size();
        }
    }

    private Cache createCache(boolean overlap) {
        CacheConfig config = new CacheConfig();
        config.setNbOfChunksPerQuery(NB_CHUNKS);
        config.addSource(new SimpleFileDataSource(S3, CHUNK_SIZE));
        config.addSource(new SimpleFileDataSource(S2, CHUNK_SIZE));
        config.addSource(new SimpleFileDataSource(S1, CHUNK_SIZE));
        if (overlap)
            config.addSource(new SimpleFileDataSource(S4, CHUNK_SIZE));
        config.setStorage(new SimpleMemoryStorage(CHUNK_SIZE));
        Cache cache = new CacheImpl(config);
        cache.setStatisticsEnabled(true);
        return cache;
    }

    private QueryDataCounter createQuery(Cache cache, String PV, String t1, String t2) throws Exception {
            Instant start = dateFormat.parse(t1).toInstant();
            Instant end = dateFormat.parse(t2).toInstant();
        Query q = cache.createQuery(PV, VType.class, new QueryParameters()
                .timeInterval(TimeRelativeInterval.of(start, end)));
        return new QueryDataCounter(q, 15);
    }

    private Map<String, List<DataRequestStatistics>> mapStatsBySource(
            List<DataRequestStatistics> requestStats) {
        Map<String, List<DataRequestStatistics>> stats_map = new HashMap<String, List<DataRequestStatistics>>();
        stats_map.put(S1, new LinkedList<DataRequestStatistics>());
        stats_map.put(S2, new LinkedList<DataRequestStatistics>());
        stats_map.put(S3, new LinkedList<DataRequestStatistics>());
        stats_map.put(S4, new LinkedList<DataRequestStatistics>());
        for (DataRequestStatistics drs : requestStats) {
            switch (((SimpleFileDataSource) drs.getSource()).getCsvFile()) {
            case S1:
                stats_map.get(S1).add(drs);
                break;
            case S2:
                stats_map.get(S2).add(drs);
                break;
            case S3:
                stats_map.get(S3).add(drs);
                break;
            case S4:
                stats_map.get(S4).add(drs);
                break;
            default:
                break;
            }
        }
        return stats_map;
    }

    private TimeInterval timeIntervalBetween(String t1, String t2)
            throws Exception {
        return TimeInterval.between(LocalDateTime.parse(t1, tsFormat).toInstant(ZoneOffset.UTC), LocalDateTime.parse(t2, tsFormat).toInstant(ZoneOffset.UTC));
    }

    final private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    final private static DateTimeFormatter tsFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    // final private static String PV1 = "TEST-BTY0:RAMP1";
    final private static String PV2 = "TEST-BTY0:RAMP2";
    final private static String S1 = "src/test/resources/archive-ramps-1H.csv"; // from '2014-12-04 00:00:01' to '2014-12-04 01:00:01'
    final private static String S2 = "src/test/resources/archive-ramps-1D.csv"; // from '2014-12-03 00:00:00' to '2014-12-04 00:00:00'
    final private static String S3 = "src/test/resources/archive-ramps-1W.csv"; // from '2014-11-25 23:59:59' to '2014-12-02 23:59:59'
    final private static String S4 = "src/test/resources/archive-ramps-2DUS.csv"; // from '2014-12-02 00:00:00' to '2014-12-04 00:00:00' but only multiple of 5

    /**
     * Query termination: when finished or forced (for instance, query for 1
     * year of archived samples, then chunk of data are progressively displayed,
     * but the user does not wait and closes the plot or starts to zoom on
     * already plotted samples)
     */
    @Test
    public void testCase1() {
        try {
            Cache cache = createCache(false);
            Query query = null;

            Instant start = dateFormat.parse("2014-11-26 00:00").toInstant();
            Instant end = dateFormat.parse("2014-12-03 00:00").toInstant();
            long startTime = System.currentTimeMillis();
            query = cache.createQuery(PV2, VType.class, new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(start, end)));

            Set<Instant> timestamps = new HashSet<Instant>();
            QueryResult result = null;
            int doubloonCount = 0;
            // 1 week = 302400, let stop at half
            while (timestamps.size() < 130000) {
                try {
                    Thread.sleep(1000);
                    result = query.getUpdate();
                    for (QueryData data : result.getData()) {
                        for (Instant t : data.getTimestamps()) {
                            if (timestamps.contains(t))
                                doubloonCount++;
                            else timestamps.add(t);
                        }
                    }
                    if (((QueryImpl) query).isCompleted())
                        break;
                } catch (InterruptedException e) {
                }
            }
            query.close();
            long stopTime = System.currentTimeMillis();
            long elapsedTime1 = stopTime - startTime;
            Assert.assertEquals(0, doubloonCount);
            // Query should be terminated, so no new data
            Assert.assertEquals(0, query.getUpdate().getData().size());
            QueryStatistics qs = ((QueryImpl) query).getStatistics();
            // All data should have been loaded from source
            Assert.assertTrue(timestamps.size() <= qs.getSourceHit());
            Assert.assertEquals(0, qs.getStorageHit());

            // Start a new query on a smaller interval
            start = dateFormat.parse("2014-11-26 00:00").toInstant();
            end = dateFormat.parse("2014-11-29 00:00").toInstant();
            startTime = System.currentTimeMillis();
            query = cache.createQuery(PV2, VType.class, new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(start, end)));

            timestamps = new HashSet<Instant>();
            result = null;
            doubloonCount = 0;
            int limit = 0;
            // Wait max 1 minute
            while (limit <= 60) {
                try {
                    Thread.sleep(1000);
                    result = query.getUpdate();
                    for (QueryData data : result.getData()) {
                        for (Instant t : data.getTimestamps()) {
                            if (timestamps.contains(t))
                                doubloonCount++;
                            else timestamps.add(t);
                        }
                    }
                    if (((QueryImpl) query).isCompleted())
                        break;
                } catch (InterruptedException e) {
                }
                limit++;
            }
            query.close();
            stopTime = System.currentTimeMillis();
            long elapsedTime2 = stopTime - startTime;
            Assert.assertEquals(0, doubloonCount);
            Assert.assertEquals(129600, timestamps.size());
            // Query should be terminated, so no new data
            Assert.assertEquals(0, query.getUpdate().getData().size());
            qs = ((QueryImpl) query).getStatistics();
            // All data should have been loaded from source
            Assert.assertEquals(0, qs.getSourceHit());
            Assert.assertEquals(timestamps.size(), qs.getStorageHit());
            // Should be quicker
            Assert.assertTrue(elapsedTime2 < elapsedTime1);
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Use cases for sequential queries:
     * a. 1rst query on a large time interval finished
     * b. 2nd request for a smaller time interval - case of zoom - samples are in the cache
     * c. 3rd request for time interval - case of pan:
     *   i. Before the smaller one - samples are in the cache
     *   ii. Before the large one - samples are not in the cache
     */
    @Test
    public void testCase2() {
        try {
            Cache cache = createCache(false);

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("a", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-28 00:00"));
            queryMap.get("a").run();
            queryMap.put("b", createQuery(cache, PV2, "2014-11-27 12:00", "2014-11-28 00:00"));
            queryMap.get("b").run();
            queryMap.put("ci", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-27 12:00"));
            queryMap.get("ci").run();
            queryMap.put("cii", createQuery(cache, PV2, "2014-11-26 20:00", "2014-11-27 00:00"));
            queryMap.get("cii").run();

            // a. 1rst query on a large time interval finished => all from source
            Assert.assertEquals(43200, queryMap.get("a").getUpdateCount());
            Assert.assertEquals(43200, queryMap.get("a").getSourceHit());
            Assert.assertEquals(0, queryMap.get("a").getStorageHit());

            // b. 2nd request for a smaller time interval - case of zoom => samples are in the cache
            Assert.assertEquals(21600, queryMap.get("b").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("b").getSourceHit());
            Assert.assertEquals(21600, queryMap.get("b").getStorageHit());

            // ci. Before b. => samples are in the cache
            Assert.assertEquals(21600, queryMap.get("ci").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("ci").getSourceHit());
            Assert.assertEquals(21600, queryMap.get("ci").getStorageHit());

            // cii. Before a. => samples are not in the cache
            Assert.assertEquals(7200, queryMap.get("cii").getUpdateCount());
            Assert.assertEquals(7200, queryMap.get("cii").getSourceHit());
            Assert.assertEquals(0, queryMap.get("cii").getStorageHit());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Use cases for parallel queries:
     * a. 1rst query on a large time interval on going
     * b. 2rnd query on:
     *   i. Time interval within the 1rst one
     *   ii. Time interval before the 1rst one:
     *     1. With gap between the 2 time intervals
     *     2. Continuous to the 1rst time interval
     *     3. Overlap with the 1rst time interval
     *   iii. Time interval after the 1rst one:
     *     1. With gap between the 2 time intervals
     *     2. Continuous to the 1rst time interval
     *   iv. Time interval that includes the 1rst one: before it and after it
     */
    @Test
    public void testCase3() {
        try {
            Cache cache = createCache(false);

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("a", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-28 00:00"));
            queryMap.put("bi", createQuery(cache, PV2, "2014-11-27 06:00", "2014-11-27 12:00"));
            queryMap.put("bii1", createQuery(cache, PV2, "2014-11-26 20:00", "2014-11-26 23:00"));
            queryMap.put("bii2", createQuery(cache, PV2, "2014-11-26 21:00", "2014-11-27 00:00"));
            queryMap.put("bii3", createQuery(cache, PV2, "2014-11-26 22:00", "2014-11-27 01:00"));
            queryMap.put("biii1", createQuery(cache, PV2, "2014-11-28 02:00", "2014-11-28 06:00"));
            queryMap.put("biii2", createQuery(cache, PV2, "2014-11-28 00:00", "2014-11-28 04:00"));
            queryMap.put("biv", createQuery(cache, PV2, "2014-11-26 22:00", "2014-11-28 04:00"));

            // Run in parallel
            for (QueryDataCounter qdc : queryMap.values())
                qdc.start();
            boolean allFinished = false;
            while (!allFinished) {
                allFinished = true;
                for (QueryDataCounter qdc : queryMap.values())
                    if (!qdc.isFinished())
                        allFinished = false;
            }

            // Check that there is no missing data
            Assert.assertEquals(43200, queryMap.get("a").getUpdateCount());
            Assert.assertEquals(10800, queryMap.get("bi").getUpdateCount());
            Assert.assertEquals(5400, queryMap.get("bii1").getUpdateCount());
            Assert.assertEquals(5400, queryMap.get("bii2").getUpdateCount());
            Assert.assertEquals(5400, queryMap.get("bii3").getUpdateCount());
            Assert.assertEquals(7200, queryMap.get("biii1").getUpdateCount());
            Assert.assertEquals(7200, queryMap.get("biii2").getUpdateCount());
            Assert.assertEquals(54000, queryMap.get("biv").getUpdateCount());

            // Check coherence
            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Force refresh of the cache.
     */
    @Test
    public void testCase4() {
        try {
            Cache cache = createCache(false);

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("a", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-30 00:00"));
            queryMap.get("a").run();
            queryMap.put("b", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-30 00:00"));
            queryMap.get("b").run();
            cache.flush();
            queryMap.put("c", createQuery(cache, PV2, "2014-11-27 00:00", "2014-11-30 00:00"));
            queryMap.get("c").run();

            // a. all data from sources
            Assert.assertEquals(129600, queryMap.get("a").getUpdateCount());
            Assert.assertEquals(129600, queryMap.get("a").getSourceHit());
            Assert.assertEquals(0, queryMap.get("a").getStorageHit());

            // b. same request => all data from storage
            Assert.assertEquals(129600, queryMap.get("b").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("b").getSourceHit());
            Assert.assertEquals(129600, queryMap.get("b").getStorageHit());

            // c. same request after flush => all data from sources
            Assert.assertEquals(129600, queryMap.get("c").getUpdateCount());
            Assert.assertEquals(129600, queryMap.get("c").getSourceHit());
            Assert.assertEquals(0, queryMap.get("c").getStorageHit());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Setup of 3 data sources with S1 = 1 hour of samples, S2 = 1 day of samples, S3 = 1 week of samples - no overlapping.
     * Simple queries:
     * a. 1rst query on 1 hour - S1 only should provide all samples
     * b. 2nd request within 1 hour - case of zoom - samples are in the cache
     * c. 3rd request before 1 day - case of pan:
     *   i. But within 1 day - S2 should provide samples
     *   ii. Within 1 week - S3 should provide samples
     *   iii. Before 1 week - no archived sample
     */
    @Test
    public void testCase5() {
        try {
            Cache cache = createCache(false);

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("a", createQuery(cache, PV2, "2014-12-04 00:00", "2014-12-04 01:00"));
            queryMap.get("a").run();
            queryMap.put("b", createQuery(cache, PV2, "2014-12-04 00:15", "2014-12-04 00:45"));
            queryMap.get("b").run();
            queryMap.put("ci", createQuery(cache, PV2, "2014-12-03 00:15", "2014-12-04 00:15"));
            queryMap.get("ci").run();
            queryMap.put("cii", createQuery(cache, PV2, "2014-11-30 00:15", "2014-12-03 00:15"));
            queryMap.get("cii").run();
            queryMap.put("ciii", createQuery(cache, PV2, "2014-11-23 00:00", "2014-11-25 00:00"));
            queryMap.get("ciii").run();

            // a. all samples from S1
            Assert.assertEquals(1800, queryMap.get("a").getUpdateCount());
            Assert.assertEquals(1800, queryMap.get("a").getSourceHit());
            Assert.assertEquals(0, queryMap.get("a").getStorageHit());

            // b. samples from S1 are already in cache
            Assert.assertEquals(900, queryMap.get("b").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("b").getSourceHit());
            Assert.assertEquals(900, queryMap.get("b").getStorageHit());

            // ci. S2 part from sources, S1 part from storage
            Assert.assertEquals(43200, queryMap.get("ci").getUpdateCount());
            Assert.assertEquals(42750, queryMap.get("ci").getSourceHit());
            Assert.assertEquals(450, queryMap.get("ci").getStorageHit());

            // cii. all samples from S2 + S3
            Assert.assertEquals(129600, queryMap.get("cii").getUpdateCount());
            Assert.assertEquals(129600, queryMap.get("cii").getSourceHit());
            Assert.assertEquals(0, queryMap.get("cii").getStorageHit());

            // ciii. no samples
            Assert.assertEquals(0, queryMap.get("ciii").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("ciii").getSourceHit());
            Assert.assertEquals(0, queryMap.get("ciii").getStorageHit());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Setup of 3 data sources with S1 = 1 hour of samples, S2 = 1 day of samples, S3 = 1 week of samples - no overlapping.
     * Optimized queries:
     * a. Queries with time gap:
     *   i. 1rst query for 10 minutes within 1 hour - S1 only should provide some samples
     *   ii. 2nd request for 20 minutes within 1 week with gap with previous time interval - samples in the gap should also be retrieved
     *   iii. 3rd request for 15 minutes within the previous gap - samples should be in the cache already
     */
    @Test
    public void testCase6() {
        try {
            Cache cache = createCache(false);

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("ai", createQuery(cache, PV2, "2014-12-04 00:10", "2014-12-04 00:20"));
            queryMap.get("ai").run();
            queryMap.put("aii", createQuery(cache, PV2, "2014-12-02 01:00", "2014-12-02 01:20"));
            queryMap.get("aii").run();
            // Wait for sample in the gap to be retrieved
            while (queryMap.get("aii").getPVCache().isProcessingSources()) {
                Thread.sleep(500);
            }
            queryMap.put("aiii", createQuery(cache, PV2, "2014-12-03 06:00", "2014-12-03 06:15"));
            queryMap.get("aiii").run();

            // ai. all samples from sources
            Assert.assertEquals(300, queryMap.get("ai").getUpdateCount());
            Assert.assertEquals(300, queryMap.get("ai").getSourceHit());
            Assert.assertEquals(0, queryMap.get("ai").getStorageHit());

            // aii. all samples from sources
            Assert.assertEquals(600, queryMap.get("aii").getUpdateCount());
            Assert.assertEquals(600, queryMap.get("aii").getSourceHit());
            Assert.assertEquals(0, queryMap.get("aii").getStorageHit());

            // aiii. samples in the gap already in cache
            Assert.assertEquals(450, queryMap.get("aiii").getUpdateCount());
            Assert.assertEquals(0, queryMap.get("aiii").getSourceHit());
            Assert.assertEquals(450, queryMap.get("aiii").getStorageHit());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Setup of 3 data sources with S1 = 1 hour of samples, S2 = 1 day of samples, S3 = 1 week of samples - no overlapping.
     * Optimized queries:
     * b. Queries with time overlapping:
     *   i. 1rst request for 4 days within the 1 week on going - S3 provides the samples
     *   ii. Meantime, 2nd request for 2  days within the 1 week:
     *     1. 1 day overlap and 1 day after the first time interval -> should shorten the first query
     *     2. 1 day before the first interval and 1 day overlap -> should shorten the second query
     */
    @Test
    public void testCase7() {
        try {
            Cache cache = createCache(false);
            Instant case_start = Instant.now();

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("bi", createQuery(cache, PV2, "2014-11-27 00:00", "2014-12-01 00:00"));
            queryMap.put("bii1", createQuery(cache, PV2, "2014-11-30 00:00", "2014-12-02 00:00"));
            queryMap.put("bii2", createQuery(cache, PV2, "2014-11-26 00:00", "2014-11-28 00:00"));

            // Run in parallel
            for (QueryDataCounter qdc : queryMap.values())
                qdc.start();
            boolean allFinished = false;
            while (!allFinished) {
                allFinished = true;
                for (QueryDataCounter qdc : queryMap.values())
                    if (!qdc.isFinished())
                        allFinished = false;
            }
            Instant case_end = Instant.now();

            PVCacheStatistics cache_stats = queryMap.get("bi").getPVCache().getStatistics();
            List<DataRequestStatistics> requestStats_list = cache_stats
                    .getRequestStatsIn(TimeInterval.between(case_start, case_end));
            Map<String, List<DataRequestStatistics>> requestStats_map = mapStatsBySource(requestStats_list);
            for (DataRequestStatistics drs : requestStats_map.get(S3))
                System.out.println(CacheHelper.format(drs.getInterval()));
            Assert.assertEquals(4, requestStats_map.get(S3).size());
            // 1st query shortened by optimization = 1st part
            Assert.assertTrue(CacheHelper.contains(requestStats_map.get(S3).get(0).getInterval(), timeIntervalBetween("2014-11-27T00:00:00", "2014-11-30T00:00:00")));
            // 2nd query missing part, exclude 2014-12-01T00:00:00
            Assert.assertTrue(CacheHelper.contains(requestStats_map.get(S3).get(1).getInterval(), timeIntervalBetween("2014-12-01T00:00:01", "2014-12-02T00:00:00")));
            // 2nd part of the first query from optimization
            Assert.assertTrue(CacheHelper.contains(requestStats_map.get(S3).get(2).getInterval(), timeIntervalBetween("2014-11-30T00:00:00", "2014-12-01T00:00:00")));
            // 3rd query missing part, exclude 2014-12-27T00:00:00
            Assert.assertTrue(CacheHelper.contains(requestStats_map.get(S3).get(3).getInterval(), timeIntervalBetween("2014-11-26T00:00:00", "2014-11-26T23:59:59")));

            // b. all samples from sources
            Assert.assertEquals(172800, queryMap.get("bi").getUpdateCount());
            Assert.assertEquals(86400, queryMap.get("bii1").getUpdateCount());
            Assert.assertEquals(86400, queryMap.get("bii2").getUpdateCount());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Setup of 3 data sources with S1 = 1 hour of samples, S2 = 1 day of samples, S3 = 1 week of samples - no overlapping.
     * Setup of a 4th data source S4 with ramp1 PV that overlap 1 day S2 data source.
     * a. Query for on 1 day -> S2 and S4 should answer.
     *   S4 is expected to be faster as it has less data, then S2 should fill the gap between under-sampled data.
     */
    @Test
    public void testCase8() {
        try {
            Cache cache = createCache(true);
            Instant case_start = Instant.now();

            Map<String, QueryDataCounter> queryMap = new HashMap<String, QueryDataCounter>();
            queryMap.put("a", createQuery(cache, PV2, "2014-12-03 00:00", "2014-12-04 00:00"));
            queryMap.get("a").run();

            Instant case_end = Instant.now();
            PVCacheStatistics cache_stats = queryMap.get("a").getPVCache().getStatistics();
            List<DataRequestStatistics> requestStats_list = cache_stats
                    .getRequestStatsIn(TimeInterval.between(case_start, case_end));
            Assert.assertEquals(4, requestStats_list.size());

            Map<String, List<DataRequestStatistics>> requestStats_map = mapStatsBySource(requestStats_list);
            // S4 request is expected to be faster as it has less data
            Assert.assertTrue(requestStats_map.get(S4).get(0).getDuration()
                    .compareTo(requestStats_map.get(S2).get(0).getDuration()) < 0);

            Assert.assertEquals(43200, queryMap.get("a").getUpdateCount());
            Assert.assertEquals(43200, queryMap.get("a").getSourceHit());
            Assert.assertEquals(0, queryMap.get("a").getStorageHit());

            for(QueryDataCounter qdc : queryMap.values()) {
                Assert.assertTrue(qdc.verify());
                Assert.assertEquals(qdc.getUpdateCount(), qdc.getResultCount());
                Assert.assertEquals(qdc.getUpdateCount() ,qdc.getTimetampCount());
                Assert.assertEquals(0, qdc.getDoubloonCount());
            }
            cache.getStatistics().print();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
