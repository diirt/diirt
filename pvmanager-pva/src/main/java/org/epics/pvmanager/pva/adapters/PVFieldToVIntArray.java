/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;


import java.util.List;

import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListInt;
import org.epics.vtype.ArrayDimensionDisplay;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVIntArray extends AlarmTimeDisplayExtractor implements VIntArray {

	private final ListInt size;
	private final ListInt list;
	
	public PVFieldToVIntArray(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVIntArray(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVIntArray(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);

		if (field instanceof PVIntArray)
		{
			PVIntArray valueField = (PVIntArray)field;

			IntArrayData data = new IntArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayInt(data.data);
		}
		else if (field instanceof PVUIntArray)
		{
			PVUIntArray valueField = (PVUIntArray)field;

			IntArrayData data = new IntArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayInt(data.data);
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
	 * @see org.epics.pvmanager.data.VIntArray#getData()
	 */
	@Override
	public ListInt getData() {
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
