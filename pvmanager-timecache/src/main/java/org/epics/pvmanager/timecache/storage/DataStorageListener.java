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
package org.epics.pvmanager.timecache.storage;

import org.epics.pvmanager.timecache.util.TimestampsSet;
import org.epics.util.time.Timestamp;

/**
 * {@link DataStorage} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataStorageListener {

	/**
	 * Informs that the specified set of {@link Timestamp} have been removed
	 * from storage.
	 * @param lostSet {@link TimestampsSet} of lost {@link Timestamp}.
	 */
	public void dataLoss(TimestampsSet lostSet);

}
