/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_TIME_String;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListInt;

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
    public ListInt getSizes() {
        return new ArrayInt(dbrValue.getStringValue().length);
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
