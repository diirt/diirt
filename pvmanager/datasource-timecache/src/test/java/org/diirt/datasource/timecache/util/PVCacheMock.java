/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.PVCache;
import org.diirt.datasource.timecache.PVCacheListener;
import org.diirt.datasource.timecache.PVCacheStatistics;
import org.diirt.util.time.TimeInterval;

public class PVCacheMock implements PVCache {

        private List<PVCacheListener> listeners = Collections
                        .synchronizedList(new LinkedList<PVCacheListener>());
        private final String channelName = "TEST-BTY0:RAMP2";
        private final PVCacheStatistics stats = new PVCacheStatistics(channelName);
        private IntervalsList completedIntervals = new IntervalsList();
        private DataRequestThread storageThread;

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

        public void setStorageThread(DataRequestThread thread) {
                this.storageThread = thread;
        }

        @Override
        public DataRequestThread retrieveDataAsync(TimeInterval interval) {
                return storageThread;
        }

        @Override
        public SortedSet<Data> retrieveDataSync(TimeInterval interval) {
                return new TreeSet<Data>();
        }

        public void addCompletedInterval(TimeInterval interval) {
                if (interval != null)
                        completedIntervals.addToSelf(interval);
        }

        @Override
        public IntervalsList getCompletedIntervalsList() {
                return completedIntervals;
        }

        @Override
        public void setStatisticsEnabled(boolean enabled) {
        }

        @Override
        public boolean isStatisticsEnabled() {
                return true;
        }

        @Override
        public PVCacheStatistics getStatistics() {
                return stats;
        }

        @Override
        public boolean isProcessingSources() {
                return false;
        }

        @Override
        public String getChannelName() {
                return channelName;
        }

        @Override
        public void flush() {
        }

}
