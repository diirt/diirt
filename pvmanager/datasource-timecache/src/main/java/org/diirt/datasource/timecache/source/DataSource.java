/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.source;

import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.util.time.Timestamp;

/**
 * Retrieves samples from a source.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataSource {

        /**
         * Read samples from source ordered by {@link Timestamp} and starting with
         * the specified one. Polled by {@link DataRequestThread}.
         * @param channelName channel to read.
         * @param from lowest {@link Timestamp} of the first returned sample.
         * @return {@link DataChunk} a chunk of ordered samples with all
         *         {@link Timestamp} superior to the specified argument.
         */
        public DataChunk getData(String channelName, Timestamp from);

}
