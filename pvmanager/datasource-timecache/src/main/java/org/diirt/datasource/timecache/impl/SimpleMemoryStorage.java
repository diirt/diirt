/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.impl;

import java.lang.ref.ReferenceQueue;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.storage.DataStorage;
import org.diirt.datasource.timecache.storage.DataStorageListener;
import org.diirt.datasource.timecache.storage.MemoryStoredData;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.TimestampsSet;
import org.diirt.util.time.TimeInterval;
import org.diirt.vtype.VType;

/**
 * {@link DataStorage} first implementation which handles only memory.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class SimpleMemoryStorage implements DataStorage {

    private NavigableMap<Instant, MemoryStoredData> cache = new ConcurrentSkipListMap<Instant, MemoryStoredData>();

    /** Reference queue for cleared SoftReference objects. */
    private final ReferenceQueue<VType> queue = new ReferenceQueue<VType>();

    private List<DataStorageListener> listeners = new ArrayList<DataStorageListener>();

    private int chunkSize = 1000;
    private Duration minChunkDuration = null;

    public SimpleMemoryStorage() {
    }

    public SimpleMemoryStorage(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /** {@inheritDoc} */
    @Override
    public DataChunk getData(String channelName, Instant from) {
        processQueue();
        DataChunk chunk = new DataChunk(chunkSize);
        Instant currentKey = null;
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
        Instant begin = null;
        Instant end = null;
        Instant intervalStart = interval.getStart();
        Instant intervalEnd = interval.getEnd();
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
        Instant begin = null;
        Instant end = null;
        Instant intervalStart = interval.getStart();
        Instant intervalEnd = interval.getEnd();
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
        Duration chunkDuration = Duration.between(chunk.getInterval().getStart(), chunk.getInterval().getEnd());
        if (minChunkDuration == null) {
            minChunkDuration = chunkDuration;
        } else if (chunkDuration.compareTo(minChunkDuration) < 0) {
            chunkDuration = minChunkDuration;
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
        lostSet.setTolerance(minChunkDuration);
        MemoryStoredData msd = null;
        while ((msd = (MemoryStoredData) queue.poll()) != null) {
            cache.remove(msd.getTimestamp());
            lostSet.add(msd.getTimestamp());
        }
        if (lostSet.isEmpty())
            return;
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

    // Useful to debug
    public int getStoredSampleCount() {
        return cache.size();
    }

    @Override
    public void clearAll() {
        TimestampsSet lostSet = new TimestampsSet();
        lostSet.setTolerance(minChunkDuration);
        Iterator<Instant> keyIterator = cache.keySet().iterator();
        while (keyIterator.hasNext())
            lostSet.add(keyIterator.next());
        cache.clear();
        for (DataStorageListener l : listeners)
            l.dataLoss(lostSet);
    }

}
