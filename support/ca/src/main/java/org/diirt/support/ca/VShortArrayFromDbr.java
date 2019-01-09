/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import static org.epics.util.array.CollectionNumbers.toListInt;
import static org.epics.util.array.CollectionNumbers.toListShort;

import org.epics.vtype.VShortArray;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Short;;
/**
 *
 * @author carcassi
 */
class VShortArrayFromDbr extends VNumberMetadata<DBR_TIME_Short, DBR_CTRL_Double> {

    public VShortArrayFromDbr(DBR_TIME_Short dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VShortArray getVShortArray() {
        return VShortArray.of(toListShort(dbrValue.getShortValue()),
                              toListInt(dbrValue.getShortValue().length),
                              getAlarm(),
                              getTime(),
                              getDisplay());
    }

}
