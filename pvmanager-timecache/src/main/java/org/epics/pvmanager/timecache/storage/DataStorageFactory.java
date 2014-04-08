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

import java.util.Collection;

import org.epics.pvmanager.timecache.impl.SimpleMemoryStorage;
import org.epics.vtype.VType;

/**
 * {@link DataStorage} factory.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataStorageFactory {

	/**
	 * Build the list of {@link DataStorage} corresponding to the specified
	 * type. TODO: build the list from extension points.
	 * @param type {@link VType}
	 * @return {@link Collection} of {@link DataStorage}
	 * @throws Exception
	 */
	public static <V extends VType> DataStorage createStorage(Class<V> type)
			throws Exception {
		return new SimpleMemoryStorage();
	}

}
