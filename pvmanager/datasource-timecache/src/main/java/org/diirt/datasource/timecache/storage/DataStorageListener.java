/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.storage;

import org.diirt.datasource.timecache.util.TimestampsSet;
import org.diirt.util.time.Timestamp;

/**
 * {@link DataStorage} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataStorageListener {

        /**
         * Informs that the specified set of {@link Timestamp} have been removed
         * from storage.
         * @param lostSet {@link TimestampsSet} of lost {@link Timestamp}.
         */
        public void dataLoss(final TimestampsSet lostSet);

}
