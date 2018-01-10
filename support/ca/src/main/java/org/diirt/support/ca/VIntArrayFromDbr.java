/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Int;
import java.util.List;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListInt;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
class VIntArrayFromDbr extends VNumberMetadata<DBR_TIME_Int, DBR_CTRL_Double> implements VIntArray {

    public VIntArrayFromDbr(DBR_TIME_Int dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    @Override
    public ListInt getSizes() {
        return new ArrayInt(dbrValue.getIntValue().length);
    }

    @Override
    public ListInt getData() {
        return new ArrayInt(dbrValue.getIntValue());
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

    @Override
    public List<ArrayDimensionDisplay> getDimensionDisplay() {
        return ValueUtil.defaultArrayDisplay(this);
    }

}
