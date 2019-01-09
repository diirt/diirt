/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import static org.epics.util.array.CollectionNumbers.toListFloat;
import static org.epics.util.array.CollectionNumbers.toListInt;

import org.epics.vtype.VFloatArray;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Float;

/**
 *
 * @author carcassi
 */
class VFloatArrayFromDbr extends VNumberMetadata<DBR_TIME_Float, DBR_CTRL_Double> {

    public VFloatArrayFromDbr(DBR_TIME_Float dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VFloatArray getVFloatArray() {
        return VFloatArray.of(toListFloat(dbrValue.getFloatValue()),
                              toListInt(dbrValue.getFloatValue().length),
                              getAlarm(),
                              getTime(),
                              getDisplay());
    }

}
