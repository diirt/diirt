/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.List;
import java.util.Random;
import org.epics.pvmanager.TimeInterval;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 *
 * @author carcassi
 */
class Replay extends SimFunction<VDouble> {

    public Replay(String uri) {
        super(0.010, VDouble.class);
    }

    @Override
    public VDouble nextValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected List<VDouble> createValues(TimeInterval interval) {
        return super.createValues(interval);
    }

}
