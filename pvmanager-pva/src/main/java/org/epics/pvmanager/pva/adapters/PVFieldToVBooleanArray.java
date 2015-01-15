/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;


import org.epics.pvdata.pv.BooleanArrayData;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.util.array.ArrayBoolean;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListBoolean;
import org.epics.util.array.ListInt;
import org.epics.vtype.VBooleanArray;
import org.epics.vtype.VTypeToString;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVBooleanArray extends AlarmTimeDisplayExtractor implements VBooleanArray {

	private final ListInt size;
	private final ListBoolean list;
	
	public PVFieldToVBooleanArray(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVBooleanArray(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVBooleanArray(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);

		if (field instanceof PVBooleanArray)
		{
			PVBooleanArray valueField = (PVBooleanArray)field;

			BooleanArrayData data = new BooleanArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayBoolean(data.data);
		}
		else
		{
			size = null;
			list = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public ListInt getSizes() {
		return size;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VBooleanArray#getData()
	 */
	@Override
	public ListBoolean getData() {
		return list;
	}
    
    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
