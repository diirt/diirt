/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;


import java.util.List;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVFloatArray extends AlarmTimeDisplayExtractor implements VFloatArray {

	private final ListInt size;
	private final ListFloat list;


  /**
   * @param pvField
   * @param fieldName
   * @param disconnected
   */
  public PVFieldToVFloatArray(PVStructure pvField, String fieldName, boolean disconnected) {
    super(pvField, disconnected);

    PVFloatArray valueField =
      (PVFloatArray)pvField.getScalarArrayField(fieldName, ScalarType.pvFloat);
    if (valueField != null)
    {
      FloatArrayData data = new FloatArrayData();
      valueField.get(0, valueField.getLength(), data);

      this.size = new ArrayInt(data.data.length);
      this.list = new ArrayFloat(data.data);
    }
    else
    {
      size = null;
      list = null;
    }
  }


	public PVFieldToVFloatArray(PVStructure pvField, boolean disconnected) {
		this(pvField, "value", disconnected);
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public ListInt getSizes() {
		return size;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VFloatArray#getData()
	 */
	@Override
	public ListFloat getData() {
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
