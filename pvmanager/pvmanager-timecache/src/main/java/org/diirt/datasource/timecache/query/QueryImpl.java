/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.PVCache;
import org.diirt.datasource.timecache.PVCacheListener;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryImpl implements Query, PVCacheListener {

	private static final Logger log = Logger
			.getLogger(SimpleFileDataSource.class.getName());

	private class StoredDataListener implements DataRequestListener {

		@Override
		public void newData(DataChunk chunk, DataRequestThread thread) {
			log.log(Level.INFO, "STORAGE: " + CacheHelper.format(chunk.getInterval())
							+ ": " + chunk.getDatas().size());
			// TODO: more precise statistics ? there is some more informations available from thread
			if (cache.isStatisticsEnabled() && !chunk.isEmpty())
				cache.getStatistics().foundStoredData();
			if (chunk != null && !chunk.isEmpty()) {
				updateChunks(chunk.getDatas(), true);
				completedInterval.addToSelf(chunk.getInterval());
			}
		}

		@Override
		public void intervalComplete(DataRequestThread thread) {
			runningThreadToStorage = null;
			completedInterval.addToSelf(interval);
		}

	}

	private final PVCache cache;
	private TimeInterval interval;
	private List<QueryChunk> chunks;

	private TimeDuration chunkDuration = TimeDuration.ofSeconds(1);

	// Used for statistics
	private Timestamp lastRequestStart = null;
	private DataRequestThread runningThreadToStorage;
	private IntervalsList completedInterval = new IntervalsList();

	public QueryImpl(final PVCache cache) {
		this.cache = cache;
		this.cache.addListener(this);
		this.chunks = Collections
				.synchronizedList(new LinkedList<QueryChunk>());
	}

	/** {@inheritDoc} */
	@Override
	public void newData(SortedSet<Data> datas) {
		log.log(Level.INFO, "SOURCE: "
						+ CacheHelper.format(TimeInterval.between(
								datas.first().getTimestamp(), 
								datas.last().getTimestamp()))
						+ ": " + datas.size());
		if (datas != null && !datas.isEmpty()
				&& interval.contains(datas.first().getTimestamp()))
			updateChunks(datas, true);
	}

	private void updateChunks(SortedSet<Data> datas, boolean isASync) {
		Iterator<Data> itData = datas.iterator();
		Iterator<QueryChunk> itChunk = chunks.iterator();
		QueryChunk currentChunk = itChunk.next();
		while (itData.hasNext()) {
			Data currentData = itData.next();
			if (!currentChunk.addData(currentData)) {
				boolean found = false;
				while (itChunk.hasNext()) {
					currentChunk = itChunk.next();
					if (currentChunk.addData(currentData)) {
						found = true;
						break;
					}
				}
				if (!found) break;
			}
		}
		if (cache.isStatisticsEnabled() && isASync) {
			//checkCompletedIntervals();
			boolean allCompleted = true;
			for (QueryChunk chunk : chunks)
				if (!chunk.isComplete())
					allCompleted = false;
			if (allCompleted)
				cache.getStatistics().intervalsCompleted(interval,
						lastRequestStart);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void update(QueryParameters queryParameters) {
		this.interval = queryParameters.timeInterval
				.toAbsoluteInterval(Timestamp.now());
		// TODO keep chunks if new interval intersects old one ?
		chunkDuration = this.interval.getStart()
				.durationBetween(interval.getEnd()).dividedBy(100);
		synchronized (chunks) {
			for (QueryChunk chunk : chunks)
				chunk.clearDataAndStatus();
			chunks.clear();
			Timestamp start = interval.getStart();
			Timestamp end = start.plus(chunkDuration);
			while (end.compareTo(interval.getEnd()) < 0) {
				chunks.add(new QueryChunk(TimeInterval.between(start, end)));
				start = end;
				end = start.plus(chunkDuration);
			}
			chunks.add(new QueryChunk(TimeInterval.between(start, interval.getEnd())));
		}
		if (cache.isStatisticsEnabled()) {
			lastRequestStart = Timestamp.now();
			cache.getStatistics().intervalRequested(interval, lastRequestStart);
		}
		completedInterval = new IntervalsList();
		runningThreadToStorage = cache.retrieveDataAsync(interval);
		runningThreadToStorage.addListener(new StoredDataListener());
		runningThreadToStorage.start();
	}

	/** {@inheritDoc} */
	@Override
	public QueryResult getResult() {
		for (QueryChunk chunk : chunks)
			if (chunk.isComplete())
				chunk.clearDataAndStatus();
		updateChunks(cache.retrieveDataSync(interval), false);
		QueryResultImpl result = new QueryResultImpl();
		synchronized (chunks) {
			checkCompletedIntervals();
			for (QueryChunk chunk : chunks)
				result.addData(chunk.toQueryData());
			clearCompletedChunks();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public QueryResult getUpdate() {
		QueryResultImpl result = new QueryResultImpl();
		synchronized (chunks) {
			checkCompletedIntervals();
			for (QueryChunk chunk : chunks)
				if (chunk.isComplete())
					result.addData(chunk.toQueryData());
			clearCompletedChunks();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		this.cache.removeListener(this);
		for (QueryChunk chunk : chunks)
			chunk.clearDataAndStatus();
		chunks.clear();
	}

	// Request cache for completed intervals and update chunk status
	private void checkCompletedIntervals() {
		IntervalsList completedIntervals = cache.getCompletedIntervalsList();
		if (runningThreadToStorage != null) // still retrieving data from storage
			completedIntervals.intersectSelf(completedInterval);
		for (QueryChunk chunk : chunks)
			if (!chunk.isComplete()
					&& completedIntervals.contains(chunk.getTimeInterval()))
				chunk.markComplete();
	}

	private void clearCompletedChunks() {
		for (QueryChunk chunk : chunks)
			if (chunk.isComplete())
				chunk.clearData();
	}

	// Usefull to debug
	public PVCache getCache() {
		return cache;
	}

	// Usefull to debug
	public boolean isComplete() {
		boolean allCompleted = true;
		synchronized (chunks) {
			for (QueryChunk chunk : chunks)
				if (!chunk.isComplete())
					allCompleted = false;
		}
		return allCompleted;
	}

}
