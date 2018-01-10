/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Byte;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VTypeToString;

/**
 *
 * @author carcassi
 */
class VByteFromDbr extends VNumberMetadata<DBR_TIME_Byte, DBR_CTRL_Double> implements VByte {

    public VByteFromDbr(DBR_TIME_Byte dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    @Override
    public Byte getValue() {
        return dbrValue.getByteValue()[0];
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
