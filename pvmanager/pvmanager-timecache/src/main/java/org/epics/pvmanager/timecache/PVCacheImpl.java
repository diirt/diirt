/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.epics.pvmanager.timecache.source.DataSource;
import org.epics.pvmanager.timecache.storage.DataStorage;
import org.epics.pvmanager.timecache.storage.DataStorageListener;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.pvmanager.timecache.util.IntervalsList;
import org.epics.pvmanager.timecache.util.TimestampsSet;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

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

	private class SourceDataListener implements DataRequestListener {

		@Override
		public void newData(DataChunk chunk, DataRequestThread thread) {
			if (chunk == null || chunk.isEmpty())
				return;
			SortedSet<Data> storedDatas = storage.storeData(chunk);
			for (PVCacheListener l : listeners)
				l.newData(storedDatas);
			Timestamp start = thread.getInterval().getStart();
			Timestamp end = thread.getLastReceived();
			TimeInterval interval = TimeInterval.between(start, end);
			int index = dataSources.indexOf(thread.getSource());
			IntervalsList ilist = completedIntervalsBySource.get(index);
			ilist.addToSelf(interval);
			updateCompletedIntervals();
		}

		@Override
		public void intervalComplete(DataRequestThread thread) {
			if (thread != null) {
				int index = dataSources.indexOf(thread.getSource());
				IntervalsList ilist = completedIntervalsBySource.get(index);
				ilist.addToSelf(thread.getInterval());
				updateCompletedIntervals();
				runningThreadsToSources.remove(thread);
			}
		}

	}

	private boolean statisticsEnabled = false;
	private final PVCacheStatistics stats = new PVCacheStatistics();

	private final String channelName;
	private final List<DataSource> dataSources;
	private final DataStorage storage;

	private List<PVCacheListener> listeners;
	private List<DataRequestThread> runningThreadsToSources;

	private Map<Integer, IntervalsList> completedIntervalsBySource;

	private IntervalsList completedIntervals = new IntervalsList();
	private IntervalsList requestedIntervals = new IntervalsList();

	public PVCacheImpl(String channelName, Collection<DataSource> dataSources,
			DataStorage storage) {
		this.listeners = Collections.synchronizedList(new LinkedList<PVCacheListener>());
		this.runningThreadsToSources = Collections.synchronizedList(new LinkedList<DataRequestThread>());
		this.channelName = channelName;
		this.storage = storage;
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
	public DataRequestThread retrieveDataAsync(TimeInterval interval) {
		if (interval == null)
			return null;
		interval = CacheHelper.arrange(interval);
		IntervalsList missingIntervals = retrieveMissingIntervals(interval);
		if (statisticsEnabled && !missingIntervals.getIntervals().isEmpty())
			stats.sourceRequested();
		for (TimeInterval ti : missingIntervals.getIntervals()) {
			log.log(Level.INFO,
					"Start requesting: " + CacheHelper.format(interval) + " for " + channelName);
			for (DataSource s : dataSources) {
				DataRequestThread thread = null;
				try {
					thread = new DataRequestThread(channelName, s, ti);
					thread.addListener(new SourceDataListener());
					thread.start();
					runningThreadsToSources.add(thread);
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		requestedIntervals.addToSelf(interval);
		try {
			return new DataRequestThread(channelName, storage, interval);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public SortedSet<Data> retrieveDataSync(TimeInterval interval) {
		return storage.getAvailableData(interval);
	}

	private IntervalsList retrieveMissingIntervals(TimeInterval interval) {
		IntervalsList iList = new IntervalsList(interval);
		iList.subtractFromSelf(requestedIntervals);
		return iList;
	}

	private void updateCompletedIntervals() {
		IntervalsList tmpList = null;
		for (IntervalsList ilist : completedIntervalsBySource.values()) {
			if (tmpList == null) tmpList = new IntervalsList(ilist);
			else tmpList.intersectSelf(ilist);
		}
		this.completedIntervals = tmpList;
	}

	/** {@inheritDoc} */
	@Override
	public void dataLoss(TimestampsSet lostSet) {
		IntervalsList deletedIntervals = lostSet.toIntervalsList();
		requestedIntervals.subtractFromSelf(deletedIntervals);
		for (IntervalsList ilist : completedIntervalsBySource.values())
			ilist.subtractFromSelf(deletedIntervals);
		updateCompletedIntervals();
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
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public PVCacheStatistics getStatistics() {
		return stats;
	}

	// Useful for debug
	public boolean isProcessingSources() {
		return runningThreadsToSources.size() > 0;
	}

	// Useful for debug
	public String getChannelName() {
		return channelName;
	}

}
