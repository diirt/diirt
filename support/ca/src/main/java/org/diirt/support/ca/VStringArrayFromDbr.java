/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.epics.vtype.VStringArray;

import gov.aps.jca.dbr.DBR_TIME_String;

/**
 *
 * @author carcassi
 */
class VStringArrayFromDbr extends VMetadata<DBR_TIME_String> {

    private List<String> data;

    public VStringArrayFromDbr(DBR_TIME_String dbrValue, JCAConnectionPayload connPayload) {
        super(dbrValue, connPayload);
        data = Collections.unmodifiableList(Arrays.asList(dbrValue.getStringValue()));
    }

    public VStringArray getVStringArray() {
        return VStringArray.of(data, getAlarm(), getTime());
    }

}
