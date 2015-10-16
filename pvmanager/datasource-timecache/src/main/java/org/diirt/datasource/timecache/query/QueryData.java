/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.util.List;

import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.VType;

/**
 * Represents a chunk of data.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface QueryData {

        /**
         * The time range where the data is defined
         */
        public TimeInterval getTimeInterval();

        /**
         * The number of elements. <p> Both data and timestamps will have this
         * number of elements.
         */
        public int getCount();

        /**
         * The time for each element.
         */
        public List<Timestamp> getTimestamps();

        public List<VType> getData();

}
