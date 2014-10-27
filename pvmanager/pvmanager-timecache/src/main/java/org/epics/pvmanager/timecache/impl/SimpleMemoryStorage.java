/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache.impl;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;

import org.epics.pvmanager.timecache.Data;
import org.epics.pvmanager.timecache.DataChunk;
import org.epics.pvmanager.timecache.storage.DataStorage;
import org.epics.pvmanager.timecache.storage.DataStorageListener;
import org.epics.pvmanager.timecache.storage.MemoryStoredData;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.pvmanager.timecache.util.TimestampsSet;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.epics.vtype.VType;

/**
 * {@link DataStorage} first implementation which handles only memory.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class SimpleMemoryStorage implements DataStorage {

	private NavigableMap<Timestamp, MemoryStoredData> cache = new ConcurrentSkipListMap<Timestamp, MemoryStoredData>();

	/** Reference queue for cleared SoftReference objects. */
	private final ReferenceQueue<VType> queue = new ReferenceQueue<VType>();

	private List<DataStorageListener> listeners = new ArrayList<DataStorageListener>();

	/** {@inheritDoc} */
	@Override
	public DataChunk getData(String channelName, Timestamp from) {
		processQueue();
		DataChunk chunk = new DataChunk();
		Timestamp currentKey = null;
		if (from == null) currentKey = cache.firstKey();
		else currentKey = cache.ceilingKey(from);
		if (currentKey == null) return chunk;
		Data currentData = cache.get(currentKey);
		while (chunk.add(currentData)) {
			currentKey = cache.higherKey(currentKey);
			if (currentKey == null) break;
			currentData = cache.get(currentKey);
		}
		return chunk;
	}

	/** {@inheritDoc} */
	@Override
	public SortedSet<Data> getAvailableData(TimeInterval interval) {
		processQueue();
		if (interval == null)
			return new TreeSet<Data>();
		interval = CacheHelper.arrange(interval);
		Timestamp begin = null;
		Timestamp end = null;
		Timestamp intervalStart = interval.getStart();
		Timestamp intervalEnd = interval.getEnd();
		if (intervalStart == null && intervalEnd == null) {
			begin = cache.firstKey();
			end = cache.lastKey();
		} else if (intervalStart == null) {
			begin = cache.firstKey();
			end = cache.floorKey(intervalEnd);
		} else if (intervalEnd == null) {
			begin = cache.ceilingKey(intervalStart);
			end = cache.lastKey();
		} else {
			begin = cache.ceilingKey(intervalStart);
			end = cache.floorKey(intervalEnd);
		}
		if (begin == null || end == null)
			return new TreeSet<Data>();
		return new TreeSet<Data>(cache.subMap(begin, true, end, true).values());
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAvailableData(TimeInterval interval) {
		if (interval == null)
			return false;
		interval = CacheHelper.arrange(interval);
		Timestamp begin = null;
		Timestamp end = null;
		Timestamp intervalStart = interval.getStart();
		Timestamp intervalEnd = interval.getEnd();
		if (intervalStart == null && intervalEnd == null) {
			begin = cache.firstKey();
			end = cache.lastKey();
		} else if (intervalStart == null) {
			begin = cache.firstKey();
			end = cache.floorKey(intervalEnd);
		} else if (intervalEnd == null) {
			begin = cache.ceilingKey(intervalStart);
			end = cache.lastKey();
		} else {
			begin = cache.ceilingKey(intervalStart);
			end = cache.floorKey(intervalEnd);
		}
		if (begin == null || end == null)
			return false;
		return interval.contains(begin) && interval.contains(end);
	}

	/** {@inheritDoc} */
	@Override
	public SortedSet<Data> storeData(DataChunk chunk) {
		processQueue();
		TreeSet<Data> set = new TreeSet<Data>();
		if (chunk == null) return set;
		for (Data data : chunk.getDatas()) {
			MemoryStoredData msd = new MemoryStoredData(data.getTimestamp(),
					data.getValue(), queue);
			cache.put(data.getTimestamp(), msd);
			set.add(msd);
		}
		return set;
	}

	/**
	 * Here we go through the ReferenceQueue and remove garbage collected
	 * MemoryStoredData objects from the HashMap by looking them up using the
	 * MemoryStoredData.timestamp data member.
	 */
	private void processQueue() {
		TimestampsSet lostSet = new TimestampsSet();
		MemoryStoredData msd = null;
		while ((msd = (MemoryStoredData) queue.poll()) != null) {
			cache.remove(msd.getTimestamp());
			lostSet.add(msd.getTimestamp());
		}
		for (DataStorageListener l : listeners)
			l.dataLoss(lostSet);
	}

	/** {@inheritDoc} */
	@Override
	public void addListener(DataStorageListener listener) {
		if (listener != null)
			listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void removeListener(DataStorageListener listener) {
		if (listener != null)
			listeners.remove(listener);
	}

}
