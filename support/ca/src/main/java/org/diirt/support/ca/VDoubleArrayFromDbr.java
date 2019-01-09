/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Double;

import static org.epics.util.array.CollectionNumbers.toListDouble;
import static org.epics.util.array.CollectionNumbers.toListInt;

import java.util.List;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VFloatArray;

/**
 *
 * @author carcassi
 */
class VDoubleArrayFromDbr extends VNumberMetadata<DBR_TIME_Double, DBR_CTRL_Double> {

    public VDoubleArrayFromDbr(DBR_TIME_Double dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VDoubleArray getVDoubleArray() {
        return VDoubleArray.of(toListDouble(dbrValue.getDoubleValue()),
                              toListInt(dbrValue.getDoubleValue().length),
                              getAlarm(),
                              getTime(),
                              getDisplay());
    }

}
