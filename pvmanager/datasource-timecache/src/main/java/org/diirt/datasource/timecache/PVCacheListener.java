/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.SortedSet;

import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;

/**
 * {@link PVCache} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface PVCacheListener {

    /** Informs that a new set of {@link Data} is available in storage. */
    public void newDataInCache(final SortedSet<Data> newData,
            final TimeInterval newDataInterval,
            final IntervalsList completedIntervals);

    public void updatedCompletedIntervals(final IntervalsList completedIntervals);

}
