/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Float;
import java.util.List;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
class VFloatArrayFromDbr extends VNumberMetadata<DBR_TIME_Float, DBR_CTRL_Double> implements VFloatArray {

    public VFloatArrayFromDbr(DBR_TIME_Float dbrValue, DBR_CTRL_Double metadata, JCAConnectionPayload connPayload) {
        super(dbrValue, metadata, connPayload);
    }

    @Override
    public ListInt getSizes() {
        return new ArrayInt(dbrValue.getFloatValue().length);
    }

    @Override
    public ListFloat getData() {
        return new ArrayFloat(dbrValue.getFloatValue());
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
