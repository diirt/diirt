/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.storage;

import java.util.SortedSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.util.time.TimeInterval;

/**
 * Retrieves and stores samples from/to a storage.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataStorage extends DataSource {

    /**
     * Returns <code>true</code> if some data is available within the requested
     * interval, <code>false</code> otherwise.
     */
    public boolean hasAvailableData(TimeInterval interval);

    /**
     * Returns all available {@link Data} within the requested
     * {@link TimeInterval}.
     */
    public SortedSet<Data> getAvailableData(TimeInterval interval);

    /**
     * Stores all {@link Data} of the specified {@link DataChunk} and returns
     * the corresponding set of {@link Data} ordered by {@link Instant}.
     * @param chunk to store.
     * @return set of {@link Data} ordered by {@link Instant}.
     */
    public SortedSet<Data> storeData(DataChunk chunk);

    /**
     * Adds a {@link DataStorageListener} to be notified when data is lost.
     * @param listener to be added.
     */
    public void addListener(DataStorageListener listener);

    /**
     * Removes a {@link DataStorageListener}.
     * @param listener to be removed
     */
    public void removeListener(DataStorageListener listener);

    public void clearAll();

}
