/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Statistics of {@link Cache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheStatistics {

	private SortedMap<String, PVCacheStatistics> pvStats = new TreeMap<String, PVCacheStatistics>();

	public void addStats(String channelName, PVCacheStatistics stats) {
		pvStats.put(channelName, stats);
	}

	public String print() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, PVCacheStatistics> statsEntry : pvStats.entrySet()) {
			sb.append("-----------------------------------------------\n");
			sb.append(">>>>" + statsEntry.getKey() + "<<<<\n");
			sb.append(statsEntry.getValue().print() + "\n");
			sb.append("-----------------------------------------------\n");
		}
		return sb.toString();
	}
}
