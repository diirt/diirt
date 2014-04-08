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
package org.epics.pvmanager.timecache.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryResultImpl implements QueryResult {

	private List<QueryData> dataList = new ArrayList<QueryData>();

	public void addData(QueryData data) {
		if (data != null)
			dataList.add(data);
	}

	@Override
	public List<QueryData> getData() {
		return dataList;
	}

}
