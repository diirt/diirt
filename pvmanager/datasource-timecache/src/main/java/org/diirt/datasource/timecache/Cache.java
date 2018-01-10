/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.vtype.VType;

/**
 * Cache main interface: creates and manages all {@link PVCache}. Initialises
 * {@link Query} with the corresponding {@link PVCache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface Cache {

        /**
         * Creates a new {@link Query}.
         * @param channelName channel to be requested.
         * @param type VType of data to be pulled by the query.
         * @param parameters {@link QueryParameters}.
         * @return initialised query without data.
         */
        public <V extends VType> Query createQuery(String channelName,
                        Class<V> type, QueryParameters parameters);

        public void addConfig(Parameter p, CacheConfig config);

        public void removeConfig(Parameter p);

        public void setStatisticsEnabled(boolean enabled);

        public CacheStatistics getStatistics();

        public void flush();

}
