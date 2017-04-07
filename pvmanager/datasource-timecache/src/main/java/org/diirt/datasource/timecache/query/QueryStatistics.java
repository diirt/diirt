/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.time.Duration;
import java.time.Instant;

import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.time.TimeInterval;

/**
 * Statistics of {@link Query}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryStatistics {

    private final String channelName;
    private final TimeInterval interval;
    private final int queryID;

    private int storageHit = 0;
    private int sourceHit = 0;
    private Duration duration;

    private Instant start;

    public QueryStatistics(String channelName, TimeInterval interval,
            int queryID) {
        this.channelName = channelName;
        this.interval = interval;
        this.queryID = queryID;
    }

    public synchronized void newDataFromStorage(int count) {
        storageHit += count;
    }

    public synchronized void newDataFromSource(int count) {
        sourceHit += count;
    }

    public void queryStarted() {
        this.start = Instant.now();
    }

    public void queryCompleted() {
        this.duration = Duration.between(start, Instant.now()).abs();
    }

    public int getStorageHit() {
        return storageHit;
    }

    public int getSourceHit() {
        return sourceHit;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getChannelName() {
        return channelName;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public int getQueryID() {
        return queryID;
    }

    public String toConsoleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Query >> " + String.format("%04d", queryID) + " << ("
                + CacheHelper.format(interval) + " for " + channelName + ")");
        sb.append("\tsourceHit: " + String.format("%10d", sourceHit));
        sb.append("\tstorageHit: " + String.format("%10d", storageHit));
        sb.append("\tduration: " + duration);
        return sb.toString();
    }

}
