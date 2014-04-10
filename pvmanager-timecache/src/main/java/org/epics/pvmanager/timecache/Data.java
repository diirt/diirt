/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import org.epics.util.time.Timestamp;
import org.epics.vtype.VType;

/**
 * Represents a sample.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public interface Data extends Comparable<Data> {

	public Timestamp getTimestamp();

	public VType getValue();

}
