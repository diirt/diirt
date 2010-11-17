/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.jca;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Double;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VDoubleArray;

/**
 *
 * @author carcassi
 */
class VDoubleArrayFromDbr extends VNumberMetadata<DBR_TIME_Double, DBR_CTRL_Double> implements VDoubleArray {

    public VDoubleArrayFromDbr(DBR_TIME_Double dbrValue, DBR_CTRL_Double metadata) {
        this(dbrValue, metadata, false);
    }

    public VDoubleArrayFromDbr(DBR_TIME_Double dbrValue, DBR_CTRL_Double metadata, boolean disconnected) {
        super(dbrValue, metadata, disconnected);
    }
    
    @Override
    public double[] getArray() {
        return dbrValue.getDoubleValue();
    }

    @Override
    public List<Integer> getSizes() {
        return Collections.singletonList(dbrValue.getDoubleValue().length);
    }

}
