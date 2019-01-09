/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.epics.vtype.VFloat;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Float;

/**
 *
 * @author carcassi
 */
class VFloatFromDbr extends VNumberMetadata<DBR_TIME_Float, DBR_CTRL_Double> {

    public VFloatFromDbr(DBR_TIME_Float dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VFloat getVFloat() {
        return VFloat.of(dbrValue.getFloatValue()[0], getAlarm(), getTime(), getDisplay());
    }

}
