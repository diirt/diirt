/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVStructure;
import org.epics.vtype.VFloat;
import org.epics.vtype.VTypeToString;

/**
 * @author dkumar
 */
public class PVFieldToVFloat extends AlarmTimeDisplayExtractor implements VFloat {

	protected final Float value;

	public PVFieldToVFloat(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVFloat(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVFloat(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);

		if (field instanceof PVScalar)
			value = convert.toFloat((PVScalar)field);
	    else
	    	value = null;
	}

	@Override
	public Float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return VTypeToString.toString(this);
	}
}
