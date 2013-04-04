/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.timecache;

import org.epics.util.time.TimeRelativeInterval;

/**
 *
 * @author carcassi
 */
public class QueryParameters {
    TimeRelativeInterval timeInterval;
    
    public QueryParameters timeInterval(TimeRelativeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

}
