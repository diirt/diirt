/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.vtype.VImage;
import org.epics.vtype.ValueUtil;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVImage implements VImage {

	private final int width;
	private final int height;
	private final byte[] data;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVImage(PVStructure pvField, boolean disconnected) {
		// TODO error handling, only byte[] value supported
		
		PVIntArray dim = (PVIntArray)pvField.getScalarArrayField("dim", ScalarType.pvInt);
		if (dim != null && dim.getLength() == 2)
		{
			IntArrayData iad = new IntArrayData();
			dim.get(0, dim.getLength(), iad);
			
			width = iad.data[0];
			height = iad.data[1];
		}
		else
		{
			width = -1;
			height = -1;
		}
		
		PVByteArray array = (PVByteArray)pvField.getScalarArrayField("value", ScalarType.pvByte);
		if (array != null)
		{
			int len = array.getLength();
			ByteArrayData bad = new ByteArrayData();
			array.get(0, len, bad);
			
			int pixels = width*height;
			int expectedBytes = 3*pixels;
			
			if (len == expectedBytes)
			{
				data = bad.data;
			}
			else if (len == pixels)
			{
				// convert grayscale to RGB
				data = new byte[3*pixels];
				int p = 0;
				for (int i = 0; i < pixels; i++)
				{
					byte c = bad.data[i];
					// R = G = B = c
					data[p++] = c; 
					data[p++] = c; 
					data[p++] = c;
				}
			}
			else
			{
				data = null;
			}
		}
		else
			data = null;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Class<?> type = ValueUtil.typeOf(this);
        builder.append(type.getSimpleName())
                .append("[height=")
                .append(getHeight())
                .append(", width=")
                .append(getWidth());
        builder.append(']');
        return builder.toString();
    }

}
