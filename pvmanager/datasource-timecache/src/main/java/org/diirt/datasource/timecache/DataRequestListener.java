/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

/**
 * {@link DataRequestThread} listener.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface DataRequestListener {

        /**
         * Informs that a new chunk is available from the specified
         * {@link DataRequestThread}.
         */
        public void newData(DataChunk chunk, DataRequestThread thread);

        /**
         * Informs that the specified {@link DataRequestThread} has finished
         * requesting the source.
         */
        public void intervalComplete(DataRequestThread thread);

}
