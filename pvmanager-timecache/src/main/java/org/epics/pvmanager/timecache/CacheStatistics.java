/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/*******************************************************************************
 * Copyright (c) 2010-2014 ITER Organization.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.epics.pvmanager.timecache;

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
