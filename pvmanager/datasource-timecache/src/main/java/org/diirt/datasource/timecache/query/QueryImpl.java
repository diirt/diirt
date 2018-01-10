/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.diirt.datasource.timecache.CacheStatistics;
import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.PVCache;
import org.diirt.datasource.timecache.PVCacheListener;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryImpl implements Query, PVCacheListener {

    // TODO: remove in final version...
    private static final boolean DEBUG = true;
    private static PrintStream ps = CacheHelper.ps;
    // ...until here and all debug blocks

    private static AtomicInteger idCounter = new AtomicInteger(0);
    private final Integer queryID;

    private static final Logger log = Logger
            .getLogger(SimpleFileDataSource.class.getName());

    private final PVCache cache;
    private TimeInterval interval;
    private List<QueryChunk> chunks;

    private Duration chunkDuration = Duration.ofSeconds(1);
    private int nbChunks = 100;

    private DataRequestThread runningThreadToStorage;
    private IntervalsList completedIntervalsFromStorage = new IntervalsList();
    private IntervalsList completedIntervalsFromSources = new IntervalsList();

    private ExecutorService updateService = Executors.newSingleThreadExecutor();
    private AtomicInteger pendingTasksCount = new AtomicInteger(0);

    private QueryStatistics queryStatistics;

    public QueryImpl(final PVCache cache) {
        this.queryID = idCounter.getAndIncrement();
        this.cache = cache;
        this.cache.addListener(this);
        this.chunks = Collections
                .synchronizedList(new LinkedList<QueryChunk>());
    }

    public QueryImpl(final PVCache cache, final int nbChunks) {
        this(cache);
        this.nbChunks = nbChunks;
    }

    /** {@inheritDoc} */
    @Override
    public void newDataInCache(final SortedSet<Data> newData,
            final TimeInterval newDataInterval,
            final IntervalsList completedIntervals) {
        if (this.interval == null) // Query not yet initialized
            return;
        if (newData == null || newData.isEmpty() || completedIntervals == null)
            return;
        if (CacheHelper.intersects(this.interval, newDataInterval)) {
            final QueryImpl impl = this;
            updateService.execute(new Runnable() {
                public void run() {
                    if (DEBUG) {
                        ps.println(impl + ": SOURCE " + CacheHelper.format(newDataInterval) + " completedIntervals: " + completedIntervals);
                    }
                    int added = updateChunks(newData, false);
                    if (DEBUG && added == 0) {
                        ps.println(impl + ": OVERLAPPING FROM SOURCE");
                    }
                    if (cache.isStatisticsEnabled())
                        queryStatistics.newDataFromSource(added);
                    completedIntervalsFromSources = new IntervalsList(completedIntervals);
                    completedIntervalsFromSources.intersectSelf(interval);
                    checkCompletedChunks();
                    pendingTasksCount.decrementAndGet();
                }
            });
            pendingTasksCount.incrementAndGet();
        }
    }

    @Override
    public void updatedCompletedIntervals(final IntervalsList completedIntervals) {
        updateService.execute(new Runnable() {
            public void run() {
                completedIntervalsFromSources = new IntervalsList(completedIntervals);
                completedIntervalsFromSources.intersectSelf(interval);
                checkCompletedChunks();
                pendingTasksCount.decrementAndGet();
            }
        });
        pendingTasksCount.incrementAndGet();
    }

    private void handleNewDataFromStorage(final DataChunk chunk,
            final TimeInterval completedInterval) {
        if (chunk == null || chunk.isEmpty())
            return;
        final QueryImpl impl = this;
        updateService.execute(new Runnable() {
            public void run() {
                if (DEBUG) {
                    ps.println(impl + ": STORAGE " + CacheHelper.format(chunk.getInterval()));
                }
                int added = updateChunks(chunk.getDatas(), false);
                if (cache.isStatisticsEnabled())
                    queryStatistics.newDataFromStorage(added);
                completedIntervalsFromStorage.addToSelf(completedInterval);
                checkCompletedChunks();
                pendingTasksCount.decrementAndGet();
            }
        });
        pendingTasksCount.incrementAndGet();
    }

    private void handleIntervalCompletedFromStorage() {
        final QueryImpl impl = this;
        updateService.execute(new Runnable() {
            public void run() {
                runningThreadToStorage = null;
                completedIntervalsFromStorage.addToSelf(interval);
                checkCompletedChunks();
                log.log(Level.INFO, impl + ": END requesting STORAGE");
                if (DEBUG) {
                    ps.println(impl + ": END requesting STORAGE");
                }
                pendingTasksCount.decrementAndGet();
            }
        });
        pendingTasksCount.incrementAndGet();
    }

    private int updateChunks(final SortedSet<Data> datas, boolean forceUpdate) {
        Iterator<Data> itData = datas.iterator();
        Data currentData = null;
        int addedDataCount = 0;
        while (itData.hasNext()) {
            currentData = itData.next();
            Iterator<QueryChunk> itChunk = chunks.iterator();
            QueryChunk currentChunk = null;
            while (itChunk.hasNext()) {
                currentChunk = itChunk.next();
                int ret = currentChunk.addData(currentData, forceUpdate);
                boolean addedToCurrent = ret >= 0;
                if (addedToCurrent) {
                    addedDataCount += ret;
                    break;
                }
            }
        }
        return addedDataCount;
    }

    /** {@inheritDoc} */
    @Override
    public void update(QueryParameters queryParameters) {
        TimeInterval newInterval = queryParameters.timeInterval.toAbsoluteInterval(Instant.now());
        queryStatistics = new QueryStatistics(cache.getChannelName(), newInterval, queryID);
        // TODO: keep chunks if new interval intersects previous one ?
        chunkDuration = Duration.between(newInterval.getStart(), newInterval.getEnd()).dividedBy(nbChunks);
        synchronized (chunks) {
            for (QueryChunk chunk : chunks)
                chunk.clearDataAndStatus();
            chunks.clear();
            Instant start = newInterval.getStart();
            Instant end = start.plus(chunkDuration);
            while (end.compareTo(newInterval.getEnd()) < 0) {
                chunks.add(new QueryChunk(TimeInterval.between(start, end), this));
                // exclude first value
                start = end.plus(IntervalsList.minDuration);
                end = start.plus(chunkDuration).minus(IntervalsList.minDuration);
            }
            chunks.add(new QueryChunk(TimeInterval.between(start, newInterval.getEnd()), this));
        }
        this.interval = newInterval;
        log.log(Level.INFO, this + ": NEW request");
        if (DEBUG) {
            ps.println(this + ": NEW request");
        }
        if (cache.isStatisticsEnabled()) {
            queryStatistics.queryStarted();
        }
        completedIntervalsFromSources = cache.getCompletedIntervalsList();
        completedIntervalsFromStorage = new IntervalsList();
        runningThreadToStorage = cache.retrieveDataAsync(interval);
        if (runningThreadToStorage == null) {
            // Consider the storage has been requested and is empty
            completedIntervalsFromStorage = new IntervalsList(interval);
            return;
        }
        runningThreadToStorage.addListener(new DataRequestListener() {
            @Override
            public void newData(DataChunk chunk, DataRequestThread thread) {
                Instant start = thread.getInterval().getStart();
                Instant end = thread.getLastReceived();
                TimeInterval completedInterval = TimeInterval.between(start, end);
                handleNewDataFromStorage(chunk, completedInterval);
            }
            @Override
            public void intervalComplete(DataRequestThread thread) {
                handleIntervalCompletedFromStorage();
            }
        });
        log.log(Level.INFO, this + ": START requesting STORAGE");
        if (DEBUG) {
            ps.println(this + ": START requesting STORAGE");
        }
        runningThreadToStorage.start();
    }

    /** {@inheritDoc} */
    @Override
    public QueryResult getResult() {
        if (DEBUG) {
            ps.println(this + ": getResult");
        }
        QueryResultImpl result = new QueryResultImpl();
        synchronized (chunks) { // lock chunks
            completedIntervalsFromSources = cache.getCompletedIntervalsList();
            completedIntervalsFromStorage = new IntervalsList(interval);
            updateChunks(cache.retrieveDataSync(interval), true);
            checkCompletedChunks();
            for (QueryChunk chunk : chunks) {
                result.addData(chunk.toQueryData());
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public QueryResult getUpdate() {
        if (DEBUG) {
            ps.println(this + ": getUpdate");
        }
        QueryResultImpl result = new QueryResultImpl();
        Iterator<QueryChunk> itChunk = chunks.iterator();
        while (itChunk.hasNext()) {
            QueryChunk chunk = itChunk.next();
            if (chunk.isComplete() && !chunk.hasBeenSent()) {
                result.addData(chunk.toQueryData());
                chunk.markSent();
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        this.cache.removeListener(this);
        if (runningThreadToStorage != null) {
            runningThreadToStorage.interrupt();
            runningThreadToStorage = null;
        }
        Iterator<QueryChunk> itChunk = chunks.iterator();
        while (itChunk.hasNext())
            itChunk.next().clearDataAndStatus();
        chunks.clear();
        CacheStatistics.get().addQueryStats(queryStatistics);
        log.log(Level.INFO, this + ": CLOSED");
        if (DEBUG) {
            ps.println(this + ": CLOSED");
        }
    }

    // Request cache for completed intervals and update chunk status
    private void checkCompletedChunks() {
        IntervalsList completedIntervals = new IntervalsList(completedIntervalsFromSources);
        completedIntervals.intersectSelf(completedIntervalsFromStorage);
        if (DEBUG) {
            ps.println(this + ": CHECKING " + completedIntervals);
        }
        Iterator<QueryChunk> itChunk = chunks.iterator();
        while (itChunk.hasNext()) {
            QueryChunk chunk = itChunk.next();
            if (!chunk.isComplete()
                    && completedIntervals.contains(chunk.getTimeInterval())) {
                chunk.markComplete();
                if (DEBUG) {
                    ps.println(this + ": COMPLETED CHUNK " + chunk);
                }
            } else if (chunk.isComplete()
                    && !completedIntervals.contains(chunk.getTimeInterval())) {
                chunk.invalidate();
                if (DEBUG) {
                    ps.println(this + ": INVALIDATED " + chunk);
                }
            }
        }
        if (cache.isStatisticsEnabled() && isCompleted()) {
            if (DEBUG) {
                ps.println(this + ": COMPLETED");
            }
            queryStatistics.queryCompleted();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompleted() {
        Iterator<QueryChunk> itChunk = chunks.iterator();
        while (itChunk.hasNext())
            if (!itChunk.next().isComplete())
                return false;
        return true;
    }

    // Useful to debug
    public PVCache getCache() {
        return cache;
    }

    // Useful to debug
    public TimeInterval getInterval() {
        return interval;
    }

    // Useful to debug
    public QueryStatistics getStatistics() {
        return queryStatistics;
    }

    // Useful to debug
    public List<QueryChunk> getChunks() {
        return chunks;
    }

    // Useful to debug
    public boolean isProcessingData() {
        return pendingTasksCount.get() > 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((queryID == null) ? 0 : queryID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryImpl other = (QueryImpl) obj;
        if (queryID == null) {
            if (other.queryID != null)
                return false;
        } else if (!queryID.equals(other.queryID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Query >> " + queryID + " << (" + CacheHelper.format(interval)
                + " for " + cache.getChannelName() + ")";
    }

}
