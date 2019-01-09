/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import static org.epics.util.array.CollectionNumbers.toListByte;
import static org.epics.util.array.CollectionNumbers.toListInt;

import org.epics.vtype.VByteArray;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Byte;

/**
 *
 * @author carcassi
 */
class VByteArrayFromDbr extends VNumberMetadata<DBR_TIME_Byte, DBR_CTRL_Double> {

    public VByteArrayFromDbr(DBR_TIME_Byte dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VByteArray getVByteArray() {
        return VByteArray.of(toListByte(dbrValue.getByteValue()),
                             toListInt(dbrValue.getByteValue().length),
                             getAlarm(),
                             getTime(),
                             getDisplay());
    }
}
