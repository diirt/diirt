/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache.query;

import org.epics.util.time.TimeRelativeInterval;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryParameters {

	TimeRelativeInterval timeInterval;

	public QueryParameters timeInterval(TimeRelativeInterval timeInterval) {
		this.timeInterval = timeInterval;
		return this;
	}

}
