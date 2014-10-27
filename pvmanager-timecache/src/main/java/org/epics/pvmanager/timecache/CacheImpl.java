/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.epics.pvmanager.timecache.query.Query;
import org.epics.pvmanager.timecache.query.QueryImpl;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.vtype.VType;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheImpl implements Cache {

	private boolean statisticsEnabled = false;
	private Map<String, PVCache> cachedPVs = new ConcurrentHashMap<String, PVCache>();

	/** {@inheritDoc} */
	@Override
	public <V extends VType> Query createQuery(String channelName,
			Class<V> type, QueryParameters parameters) {
		PVCache pvCache = (PVCache) cachedPVs.get(channelName);
		if (pvCache == null) {
			pvCache = PVCacheFactory.createPVCache(channelName, type);
			pvCache.setStatisticsEnabled(statisticsEnabled);
			cachedPVs.put(channelName, pvCache);
		}
		Query query = new QueryImpl(pvCache);
		query.update(parameters);
		return query;
	}

	/**
	 * @return number of initialised {@link PVCache}.
	 */
	public int getCount() {
		return cachedPVs.size();
	}

	/** {@inheritDoc} */
	@Override
	public void setStatisticsEnabled(boolean enabled) {
		this.statisticsEnabled = enabled;
		for (PVCache pvCache : cachedPVs.values())
			pvCache.setStatisticsEnabled(statisticsEnabled);
	}

	/** {@inheritDoc} */
	@Override
	public CacheStatistics getStatistics() {
		final CacheStatistics stats = new CacheStatistics();
		for (Entry<String, PVCache> entry : cachedPVs.entrySet())
			stats.addStats(entry.getKey(), entry.getValue().getStatistics());
		return stats;
	}

}
