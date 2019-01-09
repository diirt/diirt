/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.epics.vtype.VByte;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Byte;

/**
 *
 * @author carcassi
 */
class VByteFromDbr extends VNumberMetadata<DBR_TIME_Byte, DBR_CTRL_Double> {

    public VByteFromDbr(DBR_TIME_Byte dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VByte getVByte() {
        return VByte.of(dbrValue.getByteValue()[0], getAlarm(), getTime(), getDisplay());
    }
}
