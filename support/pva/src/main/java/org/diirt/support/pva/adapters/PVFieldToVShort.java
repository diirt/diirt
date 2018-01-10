/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VTypeToString;

/**
 * @author dkumar
 */
public class PVFieldToVShort extends AlarmTimeDisplayExtractor implements VShort {

        protected final Short value;

        public PVFieldToVShort(PVStructure pvField, boolean disconnected) {
                this("value", pvField, disconnected);
        }

        public PVFieldToVShort(String fieldName, PVStructure pvField, boolean disconnected) {
                this(pvField.getSubField(fieldName), pvField, disconnected);
        }

        public PVFieldToVShort(PVField field, PVStructure pvParent, boolean disconnected) {
                super(pvParent, disconnected);

                if (field instanceof PVScalar)
                        value = convert.toShort((PVScalar)field);
                else
                        value = null;
        }

        @Override
        public Short getValue() {
                return value;
        }

        @Override
        public String toString() {
                return VTypeToString.toString(this);
        }
}
