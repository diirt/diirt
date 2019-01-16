/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.util.Arrays;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.BooleanArrayData;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StringArrayData;
import org.epics.util.array.ArrayBoolean;
import org.epics.util.array.ArrayByte;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ArrayLong;
import org.epics.util.array.ArrayShort;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VByte;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VFloat;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VLong;
import org.epics.vtype.VLongArray;
import org.epics.vtype.VShort;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VString;

public final class NTUtils {
	
	private static final Class<?>[] classLUT = {
		boolean.class, // pvBoolean
		byte.class,    // pvByte
		short.class,   // pvShort
		int.class,     // pvInt
		long.class,    // pvLong
		byte.class,   // pvUByte
		short.class,  // pvUShort
		int.class,    // pvUInt
		long.class,   // pvULong
		float.class,   // pvFloat
		double.class,  // pvDouble
		String.class   // pvString
	};

	public static Class<?> scalarClass(ScalarType scalarType)
	{
		return classLUT[scalarType.ordinal()];
	}

	public static Class<?> scalarArrayElementClass(PVScalarArray scalarArray)
	{
		return scalarClass(scalarArray.getScalarArray().getElementType());
	}

	
	private final static FieldCreate fieldCreate = FieldFactory.getFieldCreate();
	
	public static Field vtypeToField(Class<?> vtypeClass)
	{
	    if (vtypeClass == null)
	      throw new IllegalArgumentException("vtypeClass == null");

	    // TODO no complex types
	    
	    if (vtypeClass.isAssignableFrom(VDouble.class)) {
	      return fieldCreate.createScalar(ScalarType.pvDouble);
	    } else if (vtypeClass.isAssignableFrom(VFloat.class)) {
	      return fieldCreate.createScalar(ScalarType.pvFloat);
	    } else if (vtypeClass.isAssignableFrom(VString.class)) {
	      return fieldCreate.createScalar(ScalarType.pvString);
	    } else if (vtypeClass.isAssignableFrom(VInt.class)) {
	      return fieldCreate.createScalar(ScalarType.pvInt);
	    } else if (vtypeClass.isAssignableFrom(VShort.class)) {
	      return fieldCreate.createScalar(ScalarType.pvShort);
	    } else if (vtypeClass.isAssignableFrom(VLong.class)) {
	      return fieldCreate.createScalar(ScalarType.pvLong);
	    } else if (vtypeClass.isAssignableFrom(VByte.class)) {
	      return fieldCreate.createScalar(ScalarType.pvByte);
	    } else if (vtypeClass.isAssignableFrom(VBoolean.class)) {
	      return fieldCreate.createScalar(ScalarType.pvBoolean);
	      
	    } else if (vtypeClass.isAssignableFrom(VDoubleArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvDouble);
	    } else if (vtypeClass.isAssignableFrom(VFloatArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvFloat);
//	    } else if (vtypeClass.isAssignableFrom(VStringArray.class)) {
//	      return fieldCreate.createScalarArray(ScalarType.pvString);
	    } else if (vtypeClass.isAssignableFrom(VIntArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvInt);
	    } else if (vtypeClass.isAssignableFrom(VLongArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvLong);
	    } else if (vtypeClass.isAssignableFrom(VShortArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvShort);
	    } else if (vtypeClass.isAssignableFrom(VByteArray.class)) {
	      return fieldCreate.createScalarArray(ScalarType.pvByte);
	    }
	    
	    throw new IllegalArgumentException("V-type class " + vtypeClass.getSimpleName() + " not supported");
	}
	
	public static Object scalarArrayToList(PVScalarArray scalarArray, boolean readOnly)
	{
            // FIXME: This should all go away
            // FIXME: Should extract unsigned properly
            if (!readOnly) {
                throw new RuntimeException("Modifiable arrays are not supported by this function");
            }
    	int len = scalarArray.getLength(); 
		ScalarType elementType = scalarArray.getScalarArray().getElementType();
		switch (elementType)
		{
		case pvDouble:
		{
        	DoubleArrayData data = new DoubleArrayData();
        	((PVDoubleArray)scalarArray).get(0, len, data);
        	return ArrayDouble.of(data.data);
		}
		case pvFloat:
		{
        	FloatArrayData data = new FloatArrayData();
        	((PVFloatArray)scalarArray).get(0, len, data);
        	return ArrayFloat.of(data.data);
		}
		case pvInt:
		case pvUInt:
		{
        	IntArrayData data = new IntArrayData();
        	if (elementType == ScalarType.pvInt)
        		((PVIntArray)scalarArray).get(0, len, data);
        	else
        		((PVUIntArray)scalarArray).get(0, len, data);
        	return ArrayInteger.of(data.data);
		}
		case pvString:
		{
        	StringArrayData data = new StringArrayData();
        	((PVStringArray)scalarArray).get(0, len, data);
        	return Arrays.asList(data.data);
		}
		case pvLong:
		case pvULong:
		{
        	LongArrayData data = new LongArrayData();
        	if (elementType == ScalarType.pvLong)
        		((PVLongArray)scalarArray).get(0, len, data);
        	else
        		((PVULongArray)scalarArray).get(0, len, data);
        	return ArrayLong.of(data.data);
		}
		case pvShort:
		case pvUShort:
		{
        	ShortArrayData data = new ShortArrayData();
        	if (elementType == ScalarType.pvShort)
        		((PVShortArray)scalarArray).get(0, len, data);
        	else
        		((PVUShortArray)scalarArray).get(0, len, data);
        	return ArrayShort.of(data.data);
		}
		case pvByte:
		case pvUByte:
		{
        	ByteArrayData data = new ByteArrayData();
        	if (elementType == ScalarType.pvByte)
        		((PVByteArray)scalarArray).get(0, len, data);
        	else
        		((PVUByteArray)scalarArray).get(0, len, data);
        	return ArrayByte.of(data.data);
		}
		case pvBoolean:
		{
        	BooleanArrayData data = new BooleanArrayData();
        	((PVBooleanArray)scalarArray).get(0, len, data);
        	return new ArrayBoolean(data.data, readOnly);
		}
		default:
			throw new IllegalArgumentException("unsupported scalar array element type: " + elementType);
		}
	}
}
