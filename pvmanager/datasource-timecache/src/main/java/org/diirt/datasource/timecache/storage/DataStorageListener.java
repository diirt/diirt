/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.storage;

import org.diirt.datasource.timecache.util.TimestampsSet;

/**
 * {@link DataStorage} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataStorageListener {

    /**
     * Informs that the specified set of {@link Instant} have been removed
     * from storage.
     * @param lostSet {@link TimestampsSet} of lost {@link Instant}.
     */
    public void dataLoss(final TimestampsSet lostSet);

}
