/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.epics.vtype.VShort;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Short;

/**
 *
 * @author carcassi
 */
class VShortFromDbr extends VNumberMetadata<DBR_TIME_Short, DBR_CTRL_Double> {

    public VShortFromDbr(DBR_TIME_Short dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    public VShort getVShort() {
        return VShort.of(dbrValue.getShortValue()[0], getAlarm(), getTime(), getDisplay());
    }
}
