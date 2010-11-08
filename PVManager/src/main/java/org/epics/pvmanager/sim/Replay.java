/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeInterval;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.VDouble;

/**
 *
 * @author carcassi
 */
class Replay extends SimFunction<VDouble> {

    private TimeStamp reference = TimeStamp.now();
    private TimeDuration offset;
    private XmlValues values;

    public Replay(String uri) {
        super(0.010, VDouble.class);
        values = ReplayParser.parse(URI.create(uri));
        offset = ((VDouble) values.getValues().get(0)).getTimeStamp().durationFrom(reference);
    }

    @Override
    public VDouble nextValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected List<VDouble> createValues(TimeInterval interval) {
        TimeInterval originalInterval = interval.minus(offset);
        List<VDouble> newValues = new ArrayList<VDouble>();
        for (ReplayValue value : values.getValues()) {
            if (originalInterval.contains(value.getTimeStamp())) {
                newValues.add((VDouble) value.copy());
            }
        }
        return newValues;
    }

}
