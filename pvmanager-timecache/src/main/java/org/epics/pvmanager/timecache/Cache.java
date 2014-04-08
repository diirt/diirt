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

import org.epics.pvmanager.timecache.query.Query;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.vtype.VType;

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
	 * @throws Exception
	 */
	public <V extends VType> Query createQuery(String channelName,
			Class<V> type, QueryParameters parameters) throws Exception;

	public void setStatisticsEnabled(boolean enabled);

	public CacheStatistics getStatistics();

}
