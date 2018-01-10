/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.storage;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.time.Instant;

import org.diirt.datasource.timecache.Data;
import org.diirt.vtype.VType;

/**
 * {@link Data} from {@link DataStorage} stored in memory.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class MemoryStoredData extends SoftReference<VType> implements Data {

    private final Instant timestamp;

    public MemoryStoredData(Instant timestamp, VType value, ReferenceQueue<VType> q) {
        super(value, q);
        this.timestamp = timestamp;
    }

    /** {@inheritDoc} */
    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

        /** {@inheritDoc} */
        @Override
        public VType getValue() {
                return get();
        }

        /** {@inheritDoc} */
        @Override
        public int compareTo(Data sd) {
                return timestamp.compareTo(sd.getTimestamp());
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result
                                + ((timestamp == null) ? 0 : timestamp.hashCode());
                return result;
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                MemoryStoredData other = (MemoryStoredData) obj;
                if (timestamp == null) {
                        if (other.timestamp != null)
                                return false;
                } else if (!timestamp.equals(other.timestamp))
                        return false;
                return true;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
                return "MemoryStoredData [timestamp=" + timestamp + "]";
        }

}
