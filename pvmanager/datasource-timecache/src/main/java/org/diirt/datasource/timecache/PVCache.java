/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.SortedSet;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorage;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;

/**
 * Cache interface for a single PV. Retrieves samples from sources via
 * {@link DataSource} interface and stores them into storage via
 * {@link DataStorage} interface. Notifies the registered
 * {@link PVCacheListener} when new samples have been retrieved. Manages the
 * lists of requested/completed intervals.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface PVCache {

    /**
     * Start processing live data samples. NOT IMPLEMENTED IN FIRST VERSION.
     */
    public void startLiveDataProcessing();

    /**
     * Stop processing live data samples. NOT IMPLEMENTED IN FIRST VERSION.
     */
    public void stopLiveDataProcessing();

    /**
     * Add a {@link PVCacheListener} to be notified when new samples are
     * available.
     * @param listener to be added
     */
    public void addListener(PVCacheListener listener);

    /**
     * Remove a {@link PVCacheListener}.
     * @param listener to be removed
     */
    public void removeListener(PVCacheListener listener);

    /**
     * Asynchronously retrieves samples from sources for the requested
     * {@link TimeInterval} and asynchronously requests {@link DataStorage} for
     * immediately available samples.
     * @param interval {@link TimeInterval} to be retrieved.
     */
    public DataRequestThread retrieveDataAsync(TimeInterval interval);

    /**
     * Synchronously requests {@link DataStorage} for immediately available
     * samples.
     * @param interval {@link TimeInterval} to be retrieved.
     */
    public SortedSet<Data> retrieveDataSync(TimeInterval interval);

    /**
     * Get the completed intervals list. An {@link TimeInterval} is completed
     * when all {@link DataSource} have finished responding.
     * @return {@link IntervalsList} the list of completed intervals.
     */
    public IntervalsList getCompletedIntervalsList();

    public void setStatisticsEnabled(boolean enabled);

    public boolean isStatisticsEnabled();

    public PVCacheStatistics getStatistics();

    public boolean isProcessingSources();

    public String getChannelName();

    public void flush();

}
