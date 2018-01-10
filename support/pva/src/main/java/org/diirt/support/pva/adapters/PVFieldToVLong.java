/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VTypeToString;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVLong extends AlarmTimeDisplayExtractor implements VLong {

        protected final Long value;

        public PVFieldToVLong(PVStructure pvField, boolean disconnected) {
                this("value", pvField, disconnected);
        }

        public PVFieldToVLong(String fieldName, PVStructure pvField, boolean disconnected) {
                this(pvField.getSubField(fieldName), pvField, disconnected);
        }

        public PVFieldToVLong(PVField field, PVStructure pvParent, boolean disconnected) {
                super(pvParent, disconnected);

                if (field instanceof PVScalar)
                        value = convert.toLong((PVScalar)field);
                else
                        value = null;
        }

        @Override
    public Long getValue()
    {
        return value;
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
