/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.io.PrintStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.time.TimeInterval;
import org.diirt.vtype.VType;

/**
 * Represents a chunk of {@link Data} with all {@link Instant} within a fixed
 * {@link TimeInterval}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryChunk {

    // TODO: remove in final version...
    private static final boolean DEBUG = true;
    private static PrintStream ps = CacheHelper.ps;
    private final Query query;
    // ...until here and all debug blocks

    public static int Added = 1;
    public static int Updated = 0;
    public static int Ignored = -1;

    private static enum Status {
        NoDataReceived, SomeDataReceived, AllDataReceived, Blank
    }

    private final TimeInterval timeInterval;
    private SortedSet<Data> dataSet = Collections
            .synchronizedSortedSet(new TreeSet<Data>());
    private Status status = Status.NoDataReceived;
    private boolean sent = false;

    public QueryChunk(TimeInterval timeInterval, Query query) {
        this.query = query;
        this.timeInterval = timeInterval;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public int addData(Data data, boolean forceUpdate) {
        if (data == null
                || (isComplete() && !forceUpdate)
                || !timeInterval.contains(data.getTimestamp()))
            return Ignored;
        if (isComplete() && DEBUG) {
            ps.println(query + ": " + this + ": REOPENED CHUNK with " + CacheHelper.format(data.getTimestamp()));
        }
        status = Status.SomeDataReceived;
        if (dataSet.add(data)) return Added;
        else return Updated;
    }

    public void markComplete() {
        if (this.dataSet.isEmpty()) this.status = Status.Blank;
        else this.status = Status.AllDataReceived;
    }

    public void invalidate() {
        if (this.dataSet.isEmpty()) this.status = Status.NoDataReceived;
        else this.status = Status.SomeDataReceived;
    }

    public void markSent() {
        this.sent = true;
    }

    public boolean hasBeenSent() {
        return sent;
    }

    public boolean isComplete() {
        return status.equals(Status.AllDataReceived)
                || status.equals(Status.Blank);
    }

    public void clearDataAndStatus() {
        this.dataSet.clear();
        this.status = Status.NoDataReceived;
    }

    /**
     * Transform the chunk to the corresponding {@link QueryData}
     * implementation.
     */
    public QueryData toQueryData() {
        switch (this.status) {
        case NoDataReceived:
        case SomeDataReceived:
            // We return only completed chunk
            return new QueryDataNR(timeInterval);
        case AllDataReceived:
            SortedMap<Instant, VType> sortedMap = new TreeMap<Instant, VType>();
            Iterator<Data> itData = dataSet.iterator();
            while (itData.hasNext()) {
                Data data = itData.next();
                // TODO null is a specific case => update status ?
                if (data.getValue() != null)
                    sortedMap.put(data.getTimestamp(), data.getValue());
            }
            if (DEBUG) {
                ps.println(query + ": " + this + ": READ CHUNK");
            }
            return new QueryDataComplete(timeInterval, sortedMap);
        case Blank:
            return new QueryDataBlank(timeInterval);
        default:
            return null;
        }
    }

    // Useful for debug
    public int getDataCount() {
        return dataSet.size();
    }

    @Override
    public String toString() {
        return "QueryChunk (" + CacheHelper.format(timeInterval) + " "
                + dataSet.size() + " values, status: " + status + ")";
    }

}
