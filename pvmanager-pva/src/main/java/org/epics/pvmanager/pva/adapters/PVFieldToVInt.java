/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.epics.vtype.VInt;
import org.epics.vtype.VTypeToString;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVInt extends AlarmTimeDisplayExtractor implements VInt {

	protected final Integer value;
	
	public PVFieldToVInt(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVInt(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVInt(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);
		
		if (field instanceof PVScalar)
			value = convert.toInt((PVScalar)field);
		else
			value = null;
	}

	@Override
    public Integer getValue()
    {
    	return value;
    }
    
    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
