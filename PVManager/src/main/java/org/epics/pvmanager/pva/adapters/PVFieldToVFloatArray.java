/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Collections;
import java.util.List;

import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VFloatArray;
import org.epics.util.array.ListFloat;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVFloatArray extends AlarmTimeDisplayExtractor implements VFloatArray {

	private final float[] array;
	private final ListFloat list;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVFloatArray(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
		
		PVFloatArray valueField =
			(PVFloatArray)pvField.getScalarArrayField("value", ScalarType.pvFloat);
		if (valueField != null)
		{
			FloatArrayData data = new FloatArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.array = data.data;
			this.list = new ListFloat() {
				
				@Override
				public int size() {
					return array.length;
				}
				
				@Override
				public float getFloat(int index) {
					return array[index];
				}
			};
		}
		else
		{
			array = null;
			list = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public List<Integer> getSizes() {
		return Collections.singletonList(array.length);
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VFloatArray#getData()
	 */
	@Override
	public ListFloat getData() {
		return list;
	}

}
