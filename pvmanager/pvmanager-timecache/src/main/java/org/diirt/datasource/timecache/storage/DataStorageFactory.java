/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.storage;

import java.util.Collection;

import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.vtype.VType;

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
	 */
	public static <V extends VType> DataStorage createStorage(Class<V> type) {
		return new SimpleMemoryStorage();
	}

}
