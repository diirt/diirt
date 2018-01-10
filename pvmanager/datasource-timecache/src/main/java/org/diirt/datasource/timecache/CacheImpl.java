/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryImpl;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.vtype.VType;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheImpl implements Cache {

    private boolean statisticsEnabled = false;
    private Map<String, PVCache> cachedPVs = new ConcurrentHashMap<String, PVCache>();
    private Map<Parameter, CacheConfig> configMap = new ConcurrentHashMap<Parameter, CacheConfig>();

    public CacheImpl(CacheConfig defaultConfig) {
        configMap.put(Parameter.Default, defaultConfig);
    }

    /** {@inheritDoc} */
    @Override
    public <V extends VType> Query createQuery(String channelName,
            Class<V> type, QueryParameters parameters) {
        CacheConfig config = configMap.get(parameters.config);
        if (config == null)
            config = configMap.get(Parameter.Default);
        PVCache pvCache = (PVCache) cachedPVs.get(channelName);
        if (pvCache == null) {
            pvCache = new PVCacheImpl(channelName, config.getSources(), config.getStorage());
            // TODO: calculate retrieval gap from statistics
            ((PVCacheImpl) pvCache).setRetrievalGap(config.getRetrievalGap());
            pvCache.setStatisticsEnabled(statisticsEnabled);
            cachedPVs.put(channelName, pvCache);
        }
        Query query = new QueryImpl(pvCache, config.getNbOfChunksPerQuery());
        query.update(parameters);
        return query;
    }

    /**
     * @return number of initialized {@link PVCache}.
     */
    public int getCount() {
        return cachedPVs.size();
    }

    /** {@inheritDoc} */
    @Override
    public void addConfig(Parameter p, CacheConfig config) {
        if (p != null)
            configMap.put(p, config);
    }

    /** {@inheritDoc} */
    @Override
    public void removeConfig(Parameter p) {
        if (p != null && !p.equals(Parameter.Default))
            configMap.remove(p);
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
        final CacheStatistics stats = CacheStatistics.get();
        for (Entry<String, PVCache> entry : cachedPVs.entrySet())
            stats.addPVStats(entry.getKey(), entry.getValue().getStatistics());
        return stats;
    }

    /** {@inheritDoc} */
    @Override
    public void flush() {
        for (PVCache pvCache : cachedPVs.values())
            pvCache.flush();
    }

}
