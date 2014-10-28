/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import org.diirt.datasource.timecache.source.DataSourceFactory;
import org.diirt.datasource.timecache.storage.DataStorageFactory;
import org.diirt.vtype.VType;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheFactory {

	public static <V extends VType> PVCache createPVCache(String channelName,
			Class<V> type) {
		return new PVCacheImpl(channelName,
				DataSourceFactory.createSources(type),
				DataStorageFactory.createStorage(type));
	}

}
