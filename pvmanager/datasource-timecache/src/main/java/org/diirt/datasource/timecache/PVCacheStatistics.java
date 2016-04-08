/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.util.time.TimeInterval;

/**
 * Statistics of {@link PVCache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheStatistics {

    private final String channelName;
    private SortedMap<Integer, DataRequestStatistics> requestStatsByID = Collections
            .synchronizedSortedMap(new TreeMap<Integer, DataRequestStatistics>());

    public PVCacheStatistics(String channelName) {
        this.channelName = channelName;
    }

    public void intervalRequested(int requestID, DataSource source) {
        requestStatsByID.put(requestID, new DataRequestStatistics(source));
    }

    /**
     * Setting interval when the request ends avoid managing changes due to
     * request optimization
     */
    public void intervalsCompleted(int requestID, TimeInterval interval) {
        DataRequestStatistics rs = requestStatsByID.get(requestID);
        rs.intervalCompleted();
        rs.setInterval(interval);
    }

    public List<DataRequestStatistics> getRequestStatsIn(TimeInterval interval) {
        List<DataRequestStatistics> stats_list = new LinkedList<DataRequestStatistics>();
        Iterator<DataRequestStatistics> it_rs = requestStatsByID.values().iterator();
        while (it_rs.hasNext()) {
            DataRequestStatistics rs = it_rs.next();
            if (interval.contains(rs.getStart())) {
                stats_list.add(rs);
            }
        }
        return stats_list;
    }

    public String toConsoleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Requested intervals for " + channelName + " (start => interval: duration):\n");
        for (DataRequestStatistics stats : requestStatsByID.values()) {
            sb.append(stats.toConsoleString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
