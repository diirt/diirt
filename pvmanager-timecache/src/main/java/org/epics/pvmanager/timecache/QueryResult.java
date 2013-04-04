/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.timecache;

import java.util.List;

/**
 *
 * @author carcassi
 */
public interface QueryResult {
    public List<QueryData> getData();
}
