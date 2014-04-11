/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache.source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.epics.pvmanager.timecache.impl.SimpleFileDataSource;
import org.epics.vtype.VType;

/**
 * {@link DataSource} factory.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataSourceFactory {

	/**
	 * Build the list of {@link DataSource} corresponding to the specified type.
	 * TODO: build the list from extension points.
	 * @param type {@link VType}
	 * @return {@link Collection} of {@link DataSource}
	 * @throws Exception
	 */
	public static <V extends VType> Collection<DataSource> createSources(
			Class<V> type) {
		List<DataSource> list = new ArrayList<DataSource>();
		list.add(new SimpleFileDataSource("resources/test/archive-export.csv"));
		list.add(new SimpleFileDataSource("resources/test/archive-export-singlePV.csv"));
		return list;
	}

}
