/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.VType;

/**
 * Represents a completed chunk with all data available.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryDataComplete implements QueryData {

        private final TimeInterval timeInterval;
        private final SortedMap<Timestamp, VType> dataMap;

        QueryDataComplete(TimeInterval timeInterval, SortedMap<Timestamp, VType> dataMap) {
                this.dataMap = Collections.unmodifiableSortedMap(dataMap);
                this.timeInterval = timeInterval;
        }

        /** {@inheritDoc} */
        @Override
        public TimeInterval getTimeInterval() {
                return timeInterval;
        }

        /** {@inheritDoc} */
        @Override
        public int getCount() {
                return dataMap.size();
        }

        /** {@inheritDoc} */
        @Override
        public List<VType> getData() {
                return new ArrayList<VType>(dataMap.values());
        }

        /** {@inheritDoc} */
        @Override
        public List<Timestamp> getTimestamps() {
                return new ArrayList<Timestamp>(dataMap.keySet());
        }

}
