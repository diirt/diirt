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
package org.epics.pvmanager.timecache.source;

import org.epics.pvmanager.timecache.DataChunk;
import org.epics.pvmanager.timecache.DataRequestThread;
import org.epics.util.time.Timestamp;

/**
 * Retrieves samples from a source.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataSource {

	/**
	 * Read samples from source ordered by {@link Timestamp} and starting with
	 * the specified one. Polled by {@link DataRequestThread}.
	 * @param channelName channel to read.
	 * @param from lowest {@link Timestamp} of the first returned sample.
	 * @return {@link DataChunk} a chunk of ordered samples with all
	 *         {@link Timestamp} superior to the specified argument.
	 */
	public DataChunk getData(String channelName, Timestamp from);

}
