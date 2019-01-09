/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_LABELS_Enum;
import gov.aps.jca.dbr.DBR_TIME_Enum;
import java.util.Arrays;
import java.util.List;

import org.epics.vtype.EnumDisplay;
import org.epics.vtype.VEnum;

/**
 *
 * @author carcassi
 */
class VEnumFromDbr extends VMetadata<DBR_TIME_Enum> {

    private final DBR_LABELS_Enum metadata;

    public VEnumFromDbr(DBR_TIME_Enum dbrValue, DBR_LABELS_Enum metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, connPayload);
        this.metadata = metadata;
    }

    public List<String> getLabels() {
        if (metadata.getLabels() == null)
            throw new RuntimeException("Metadata returned no labels");
        return Arrays.asList(metadata.getLabels());
    }

    public VEnum getVEnum() {
        return VEnum.of(dbrValue.getEnumValue()[0], EnumDisplay.of(getLabels()), getAlarm(), getTime());
    }

}
