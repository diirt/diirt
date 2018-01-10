/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VTypeToString;

/**
 * @author dkumar
 */
public class PVFieldToVBoolean extends AlarmTimeDisplayExtractor implements VBoolean {

  protected final Boolean value;

        public PVFieldToVBoolean(PVStructure pvField, boolean disconnected) {
                this("value", pvField, disconnected);
        }

        public PVFieldToVBoolean(String fieldName, PVStructure pvField, boolean disconnected) {
                this(pvField.getSubField(fieldName), pvField, disconnected);
        }

        public PVFieldToVBoolean(PVField field, PVStructure pvParent, boolean disconnected) {
                super(pvParent, disconnected);

                if (field instanceof PVBoolean)
                        value = ((PVBoolean)field).get();
                else if (field instanceof PVScalar)
                        value = convert.toInt((PVScalar)field) != 0;
                else
                        value = null;
  }


  @Override
  public Boolean getValue() {
    return value;
  }


  @Override
  public String toString() {
    return VTypeToString.toString(this);
  }
}
