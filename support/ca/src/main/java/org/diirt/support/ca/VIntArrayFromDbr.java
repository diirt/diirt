/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import static org.epics.util.array.CollectionNumbers.toListInt;

import org.epics.vtype.VIntArray;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Int;

/**
 *
 * @author carcassi
 */
class VIntArrayFromDbr extends VNumberMetadata<DBR_TIME_Int, DBR_CTRL_Double> {

    public VIntArrayFromDbr(DBR_TIME_Int dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VIntArray getVIntArray() {
        return VIntArray.of(toListInt(dbrValue.getIntValue()),
                            toListInt(dbrValue.getIntValue().length),
                            getAlarm(),
                            getTime(),
                            getDisplay());
    }
}
