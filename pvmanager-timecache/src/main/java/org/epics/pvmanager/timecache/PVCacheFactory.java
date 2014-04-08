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

import org.epics.pvmanager.timecache.source.DataSourceFactory;
import org.epics.pvmanager.timecache.storage.DataStorageFactory;
import org.epics.vtype.VType;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheFactory {

	public static <V extends VType> PVCache createPVCache(
			String channelName, Class<V> type) throws Exception {
		return new PVCacheImpl(channelName,
				DataSourceFactory.createSources(type),
				DataStorageFactory.createStorage(type));
	}

}
