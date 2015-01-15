/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;


import java.util.List;

import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.vtype.ArrayDimensionDisplay;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVDoubleArray extends AlarmTimeDisplayExtractor implements VDoubleArray {

	private final ListInt size;
	private final ListDouble list;
	
	public PVFieldToVDoubleArray(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVDoubleArray(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVDoubleArray(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);
		
		if (field instanceof PVDoubleArray)
		{
			PVDoubleArray valueField = (PVDoubleArray)field;

			DoubleArrayData data = new DoubleArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayDouble(data.data);
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
	 * @see org.epics.pvmanager.data.VDoubleArray#getData()
	 */
	@Override
	public ListDouble getData() {
		return list;
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
