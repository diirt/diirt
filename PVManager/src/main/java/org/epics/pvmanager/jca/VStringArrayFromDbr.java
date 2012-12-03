/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.dbr.DBR_TIME_String;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.epics.pvmanager.data.VStringArray;

/**
 *
 * @author carcassi
 */
class VStringArrayFromDbr extends VMetadata<DBR_TIME_String> implements VStringArray {
    
    private List<String> data;

    public VStringArrayFromDbr(DBR_TIME_String dbrValue, JCAConnectionPayload connPayload) {
        super(dbrValue, connPayload);
        data = Collections.unmodifiableList(Arrays.asList(dbrValue.getStringValue()));
    }
    
    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public List<Integer> getSizes() {
        return Collections.singletonList(dbrValue.getStringValue().length);
    }

}
