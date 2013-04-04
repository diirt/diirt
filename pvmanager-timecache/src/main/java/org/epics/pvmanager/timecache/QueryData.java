/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.timecache;

import java.util.List;
import org.epics.util.array.ListNumber;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 * Represents a chunk of data, accessible in memory for immediate use.
 *
 * @author carcassi
 */
public interface QueryData {

    /**
     * The time range where the data is defined
     * 
     * @return 
     */
    public TimeInterval getTimeInterval();

    /**
     * The number of elements.
     * <p>
     * Both data and timestamps will have this number of elements.
     * 
     * @return 
     */
    public int getCount();

    /**
     * The data.
     * <p>
     * TODO: To have a common interface, I think we have to return object here
     * and can't use generics. Returning List of Objects would be impractical
     * when that object is a double. Plus, we can't use generic to discover
     * the type of the list anyway. So, we return object, and then
     * will have a few other interface that specify what the object is.
     * 
     * @return 
     */
    public Object getData();

    /**
     * The time for each element.
     * 
     * @return 
     */
    public List<Timestamp> getTimestamps();
}
