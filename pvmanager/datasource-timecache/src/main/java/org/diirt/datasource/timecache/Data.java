/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import org.diirt.util.time.Timestamp;
import org.diirt.vtype.VType;

/**
 * Represents a sample.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface Data extends Comparable<Data> {

        public Timestamp getTimestamp();

        public VType getValue();

}
