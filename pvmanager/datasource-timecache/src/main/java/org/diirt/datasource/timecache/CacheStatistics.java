/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.diirt.datasource.timecache.query.QueryStatistics;

/**
 * Statistics of {@link Cache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheStatistics {

    private static CacheStatistics stats = new CacheStatistics();

    public static CacheStatistics get() {
        return stats;
    }

    private SortedMap<String, PVCacheStatistics> pvStats = new TreeMap<String, PVCacheStatistics>();
    private List<QueryStatistics> queryStats = new ArrayList<QueryStatistics>();

    public void addQueryStats(QueryStatistics stats) {
        if (stats != null)
            queryStats.add(stats);
    }

    public void addPVStats(String channelName, PVCacheStatistics stats) {
        if (stats != null)
            pvStats.put(channelName, stats);
    }

    public void print() {
        System.out.println("\n" + printQueryStats());
        System.out.println(printPVCacheStats());
    }

    private String printQueryStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queries:\n");
        for (QueryStatistics stats : queryStats) {
            sb.append(stats.toConsoleString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String printPVCacheStats() {
        StringBuilder sb = new StringBuilder();
        for (PVCacheStatistics stats : pvStats.values()) {
            sb.append(stats.toConsoleString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
