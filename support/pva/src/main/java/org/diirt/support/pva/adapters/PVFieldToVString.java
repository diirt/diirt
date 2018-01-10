/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTypeToString;

/**
 * @author msekoranja
 */
public class PVFieldToVString extends AlarmTimeDisplayExtractor implements VString {
        // NOTE: VString does not extend Display, it's OK (will be undefined)

        protected final String value;

        public PVFieldToVString(PVStructure pvField, boolean disconnected) {
                this("value", pvField, disconnected);
        }

        public PVFieldToVString(String fieldName, PVStructure pvField, boolean disconnected) {
                this(pvField.getSubField(fieldName), pvField, disconnected);
        }

        public PVFieldToVString(PVField field, PVStructure pvParent, boolean disconnected) {
                super(pvParent, disconnected);

                if (field instanceof PVScalar)
                        value = convert.toString((PVScalar)field);
                else
                        value = null;
        }

        @Override
        public String getValue() {
                return value;
        }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
