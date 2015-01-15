/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;


import java.util.List;

import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.util.array.ArrayByte;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListByte;
import org.epics.util.array.ListInt;
import org.epics.vtype.ArrayDimensionDisplay;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVByteArray extends AlarmTimeDisplayExtractor implements VByteArray {

	private final ListInt size;
	private final ListByte list;
	
	public PVFieldToVByteArray(PVStructure pvField, boolean disconnected) {
		this("value", pvField, disconnected);
	}

	public PVFieldToVByteArray(String fieldName, PVStructure pvField, boolean disconnected) {
		this(pvField.getSubField(fieldName), pvField, disconnected);
	}

	public PVFieldToVByteArray(PVField field, PVStructure pvParent, boolean disconnected) {
		super(pvParent, disconnected);

		if (field instanceof PVByteArray)
		{
			PVByteArray valueField = (PVByteArray)field;

			ByteArrayData data = new ByteArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayByte(data.data);
		}
		else if (field instanceof PVUByteArray)
		{
			PVUByteArray valueField = (PVUByteArray)field;

			ByteArrayData data = new ByteArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.list = new ArrayByte(data.data);
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
	 * @see org.epics.pvmanager.data.VByteArray#getData()
	 */
	@Override
	public ListByte getData() {
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
