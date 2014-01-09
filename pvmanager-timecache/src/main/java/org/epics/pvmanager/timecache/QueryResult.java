/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
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
