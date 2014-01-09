/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.Collections;
import java.util.List;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 * Represents a range where no data is available
 *
 * @author carcassi
 */
public class QueryDataBlank implements QueryData {
    
    private final TimeInterval timeInterval;

    QueryDataBlank(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getData() {
        return Collections.emptyList();
    }

    @Override
    public List<Timestamp> getTimestamps() {
        return Collections.emptyList();
    }
    
    
}
