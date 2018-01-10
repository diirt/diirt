/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.SortedSet;
import java.util.TreeSet;

import org.diirt.util.time.TimeInterval;

/**
 * A chunk of {@link Data} ordered by {@link Timestamp} with a maximum size.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataChunk {

    private SortedSet<Data> datas;
    private TimeInterval interval;

    private final Integer maxData;

    public DataChunk() {
        datas = new TreeSet<Data>();
        // TODO calculate
        this.maxData = 1000;
    }

    public DataChunk(int size) {
        datas = new TreeSet<Data>();
        this.maxData = size;
    }

    /**
     * Add a data to the chunk.
     * @param data to be added.
     * @return <code>true</code> if data have been added, <code>false</code>
     *         otherwise.
     */
    public boolean add(Data data) {
        if (isFull() || data == null)
            return false;
        datas.add(data);
        interval = TimeInterval.between(
                datas.first().getTimestamp(),
                datas.last().getTimestamp());
        return true;
    }

    /** Check if the instance is empty. */
    public boolean isEmpty() {
        return datas.isEmpty();
    }

    /** Check if the instance is full. */
    public boolean isFull() {
        return datas.size() >= maxData;
    }

    public SortedSet<Data> getDatas() {
        return datas;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return "DataChunk [datas=" + datas + ", interval=" + interval + "]";
    }

}
