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

import java.util.SortedSet;

import org.epics.pvmanager.timecache.source.DataSource;
import org.epics.pvmanager.timecache.storage.DataStorage;
import org.epics.pvmanager.timecache.util.IntervalsList;
import org.epics.util.time.TimeInterval;

/**
 * Cache interface for a single PV. Retrieves samples from sources via
 * {@link DataSource} interface and stores them into storage via
 * {@link DataStorage} interface. Notifies the registered
 * {@link PVCacheListener} when new samples have been retrieved. Manages the
 * lists of requested/completed intervals.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface PVCache {

	/**
	 * Start processing live data samples. NOT IMPLEMENTED IN FIRST VERSION.
	 */
	public void startLiveDataProcessing();

	/**
	 * Stop processing live data samples. NOT IMPLEMENTED IN FIRST VERSION.
	 */
	public void stopLiveDataProcessing();

	/**
	 * Add a {@link PVCacheListener} to be notified when new samples are
	 * available.
	 * @param listener to be added
	 */
	public void addListener(PVCacheListener listener);

	/**
	 * Remove a {@link PVCacheListener}.
	 * @param listener to be removed
	 */
	public void removeListener(PVCacheListener listener);

	/**
	 * Asynchronously retrieves samples from sources for the requested
	 * {@link TimeInterval} and asynchronously requests {@link DataStorage} for
	 * immediately available samples.
	 * @param interval {@link TimeInterval} to be retrieved.
	 */
	public void retrieveDataAsync(TimeInterval interval);

	/**
	 * Synchronously requests {@link DataStorage} for immediately available
	 * samples.
	 * @param interval {@link TimeInterval} to be retrieved.
	 */
	public SortedSet<Data> retrieveDataSync(TimeInterval interval);

	/**
	 * Get the completed intervals list. An {@link TimeInterval} is completed
	 * when all {@link DataSource} have finished responding.
	 * @return {@link IntervalsList} the list of completed intervals.
	 */
	public IntervalsList getCompletedIntervalsList();

	public void setStatisticsEnabled(boolean enabled);

	public boolean isStatisticsEnabled();

	public PVCacheStatistics getStatistics();

}
