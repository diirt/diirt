/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorage;
import org.diirt.datasource.timecache.storage.DataStorageListener;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.datasource.timecache.util.TimestampsSet;
import java.time.Duration;
import org.diirt.util.time.TimeInterval;
import java.time.Instant;

/**
 * {@link PVCache} first implementation. Handles the communication between a
 * list of sources, a storage and registered queries threw
 * {@link PVCacheListener}. Manages a list of requested intervals which are
 * intersected with new requested {@link TimeInterval}. Manages a list a
 * completed intervals which is the result of the concatenation of the lists of
 * completed intervals per sources. When data is lost from storage, the
 * {@link IntervalsList} generated from the {@link TimestampsSet} is subtracted
 * to all requested/completed intervals lists.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheImpl implements PVCache, DataStorageListener {

    private static final Logger log = Logger.getLogger(PVCacheImpl.class.getName());

    private class DataFromSourceListener implements DataRequestListener {

        @Override
        public void newData(final DataChunk chunk, final DataRequestThread thread) {
            if (chunk == null || chunk.isEmpty() || thread == null)
                return;
            Instant start = thread.getInterval().getStart();
            Instant end = thread.getLastReceived();
            final TimeInterval interval = TimeInterval.between(start, end);
            updateService.execute(new Runnable() {
                public void run() {
                    final SortedSet<Data> storedDatas = storage.storeData(chunk);
                    int index = dataSources.indexOf(thread.getSource());
                    IntervalsList ilist = completedIntervalsBySource.get(index);
                    ilist.addToSelf(interval);
                    updateCompletedIntervals();
                    final IntervalsList completedIntervalsSnaphsot = getCompletedIntervalsList();
                    synchronized (listeners) {
                        for (final PVCacheListener l : listeners)
                            l.newDataInCache(storedDatas, chunk.getInterval(),
                                    completedIntervalsSnaphsot);
                    }
                }
            });
        }

        @Override
        public void intervalComplete(final DataRequestThread thread) {
            if (thread == null)
                return;
            final TimeInterval interval = thread.getInterval();
            updateService.execute(new Runnable() {
                public void run() {
                    int index = dataSources.indexOf(thread.getSource());
                    IntervalsList ilist = completedIntervalsBySource.get(index);
                    ilist.addToSelf(interval);
                    updateCompletedIntervals();
                    final IntervalsList completedIntervalsSnaphsot = getCompletedIntervalsList();
                    synchronized (listeners) {
                        for (final PVCacheListener l : listeners)
                            l.updatedCompletedIntervals(completedIntervalsSnaphsot);
                    }
                    if(isStatisticsEnabled()) {
                        stats.intervalsCompleted(thread.getRequestID(), interval);
                    }
                    synchronized (runningThreadsToSources) {
                        // Synchronized in order to keep cache 'processing sources'
                        runningThreadsToSources.remove(thread);
                        // Search for missing gaps in order to anticipate for future requests
                        if (runningThreadsToSources.isEmpty()) {
                            IntervalsList missingGaps = retrieveMissingGaps();
                            for (TimeInterval ti : missingGaps.getIntervals()) {
                                retrieveData(ti);
                            }
                        }
                    }
                }
            });
        }

    }

    private boolean statisticsEnabled = false;
    private final PVCacheStatistics stats;

    private final String channelName;
    private final List<DataSource> dataSources;
    private final DataStorage storage;

    private List<PVCacheListener> listeners;
    private ExecutorService updateService = Executors.newSingleThreadExecutor();
    private List<DataRequestThread> runningThreadsToSources;

    private Map<Integer, IntervalsList> completedIntervalsBySource;

    private IntervalsList completedIntervals = new IntervalsList();
    private IntervalsList requestedIntervals = new IntervalsList();

    private Duration retrievalGap = Duration.ofHours(168); // 1 week

    public PVCacheImpl(String channelName, Collection<DataSource> dataSources, DataStorage storage) {
        this.listeners = Collections.synchronizedList(new LinkedList<PVCacheListener>());
        this.runningThreadsToSources = Collections.synchronizedList(new LinkedList<DataRequestThread>());
        this.channelName = channelName;
        this.stats = new PVCacheStatistics(channelName);
        this.storage = storage;
        this.storage.addListener(this);
        this.dataSources = Collections
                .unmodifiableList(new ArrayList<DataSource>(dataSources));
        this.completedIntervalsBySource = new TreeMap<Integer, IntervalsList>();
        for (int index = 0; index < this.dataSources.size(); index++)
            this.completedIntervalsBySource.put(index, new IntervalsList());
    }

    /** {@inheritDoc} */
    @Override
    public void startLiveDataProcessing() {
        // not handled in first version
    }

    /** {@inheritDoc} */
    @Override
    public void stopLiveDataProcessing() {
        // not handled in first version
    }

    /** {@inheritDoc} */
    @Override
    public void addListener(PVCacheListener listener) {
        if (listener != null)
            listeners.add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeListener(PVCacheListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public DataRequestThread retrieveDataAsync(TimeInterval newIntervalToRetrieve) {
        if (newIntervalToRetrieve == null)
            return null;
        newIntervalToRetrieve = CacheHelper.arrange(newIntervalToRetrieve);
        IntervalsList missing_intervals = retrieveMissingIntervals(newIntervalToRetrieve);
        for (TimeInterval ti : missing_intervals.getIntervals()) {
            retrieveData(ti);
        }
        optimizeRunningRequests(newIntervalToRetrieve);
        try {
            return new DataRequestThread(channelName, storage, newIntervalToRetrieve);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    private void retrieveData(TimeInterval newIntervalToRetrieve) {
        log.log(Level.INFO,
                "START requesting SOURCES: " + CacheHelper.format(newIntervalToRetrieve) + " for " + channelName);
        for (DataSource s : dataSources) {
            DataRequestThread thread = null;
            try {
                thread = new DataRequestThread(channelName, s, newIntervalToRetrieve);
                thread.addListener(new DataFromSourceListener());
                thread.start();
                runningThreadsToSources.add(thread);
                if (isStatisticsEnabled()) {
                    stats.intervalRequested(thread.getRequestID(), thread.getSource());
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
        requestedIntervals.addToSelf(newIntervalToRetrieve);
    }

    private void optimizeRunningRequests(TimeInterval newIntervalToRetrieve) {
        synchronized (runningThreadsToSources) {
            Iterator<DataRequestThread> it_threads = runningThreadsToSources.iterator();
            List<DataRequestThread> threads_to_launch = new ArrayList<DataRequestThread>();
            while (it_threads.hasNext()) {
                DataRequestThread current_thread = it_threads.next();
                TimeInterval current_thread_interval = current_thread.getInterval();

                if (CacheHelper.intersects(current_thread_interval, newIntervalToRetrieve)
                        && newIntervalToRetrieve.getStart().compareTo(current_thread.getLastReceived()) > 0) {

                    TimeInterval first_part = TimeInterval.between(current_thread_interval.getStart(), newIntervalToRetrieve.getStart());
                    TimeInterval second_part = TimeInterval.between(newIntervalToRetrieve.getStart(), current_thread_interval.getEnd());

                    current_thread.setInterval(first_part);
                    try {
                        DataRequestThread new_thread = new DataRequestThread(
                                channelName, current_thread.getSource(), second_part);
                        new_thread.addListener(new DataFromSourceListener());
                        threads_to_launch.add(new_thread);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
            for (DataRequestThread drt : threads_to_launch) {
                drt.start();
                runningThreadsToSources.add(drt);
                if (isStatisticsEnabled()) {
                    stats.intervalRequested(drt.getRequestID(), drt.getSource());
                }
            }
        }
    }

    private IntervalsList retrieveMissingGaps() {
        IntervalsList missingGaps = new IntervalsList();
        Iterator<TimeInterval> iterator = this.requestedIntervals.getIntervals().iterator();
        TimeInterval previous = null;
        TimeInterval current = null;
        if (iterator.hasNext())
            current = iterator.next();
        while (iterator.hasNext()) {
            previous = current;
            current = iterator.next();
            if (previous.getEnd().plus(retrievalGap).compareTo(current.getStart()) > 0) {
                missingGaps.addToSelf(TimeInterval.between(previous.getEnd(), current.getStart()));
            }
        }
        return missingGaps;
    }

    private IntervalsList retrieveMissingIntervals(TimeInterval interval) {
        IntervalsList iList = new IntervalsList(interval);
        iList.subtractFromSelf(requestedIntervals);
        return iList;
    }

    private synchronized void updateCompletedIntervals() {
        IntervalsList tmpList = null;
        for (IntervalsList ilist : completedIntervalsBySource.values()) {
            if (tmpList == null) tmpList = new IntervalsList(ilist);
            else tmpList.intersectSelf(ilist);
        }
        this.completedIntervals = tmpList;
    }

    /** {@inheritDoc} */
    @Override
    public SortedSet<Data> retrieveDataSync(TimeInterval interval) {
        return storage.getAvailableData(interval);
    }

    /** {@inheritDoc} */
    @Override
    public void dataLoss(final TimestampsSet lostSet) {
        IntervalsList deletedIntervals = lostSet.toIntervalsList();
        requestedIntervals.subtractFromSelf(deletedIntervals);
        for (IntervalsList ilist : completedIntervalsBySource.values())
            ilist.subtractFromSelf(deletedIntervals);
        updateCompletedIntervals();
        log.log(Level.INFO, "dataLoss in " + deletedIntervals + " for " + channelName);
        final IntervalsList completedIntervalsSnaphsot = new IntervalsList(completedIntervals);
        synchronized (listeners) {
            for (final PVCacheListener l : listeners)
                l.updatedCompletedIntervals(completedIntervalsSnaphsot);
        }
    }

    /** {@inheritDoc} */
    @Override
    public IntervalsList getCompletedIntervalsList() {
        return new IntervalsList(this.completedIntervals);
    }

    /** {@inheritDoc} */
    @Override
    public void setStatisticsEnabled(boolean enabled) {
        this.statisticsEnabled = enabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public PVCacheStatistics getStatistics() {
        return stats;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isProcessingSources() {
        return runningThreadsToSources.size() > 0;
    }

    /** {@inheritDoc} */
    @Override
    public String getChannelName() {
        return channelName;
    }

    /** {@inheritDoc} */
    @Override
    public void flush() {
        storage.clearAll();
    }

    // Useful to configuration, TODO: improve (see CacheImpl)
    public void setRetrievalGap(Duration retrievalGap) {
        this.retrievalGap = retrievalGap;
    }

    // Useful to debug
    public DataStorage getStorage() {
        return storage;
    }

}
