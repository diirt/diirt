/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/**
 * 
 */
package org.epics.pvmanager.pva.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarArray;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Type;
import org.epics.pvdata.pv.Union;
import org.epics.pvmanager.formula.FormulaFunction;
import org.epics.pvmanager.formula.FormulaFunctionSet;
import org.epics.pvmanager.formula.FormulaFunctionSetDescription;
import org.epics.pvmanager.pva.PVATypeAdapter;
import org.epics.pvmanager.pva.PVAVTypeAdapterSet;
import org.epics.pvmanager.pva.adapters.PVAPVField;
import org.epics.pvmanager.pva.adapters.PVAPVStructure;
import org.epics.pvmanager.pva.adapters.PVFieldToVBoolean;
import org.epics.pvmanager.pva.adapters.PVFieldToVBooleanArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVByte;
import org.epics.pvmanager.pva.adapters.PVFieldToVByteArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVDouble;
import org.epics.pvmanager.pva.adapters.PVFieldToVDoubleArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVFloat;
import org.epics.pvmanager.pva.adapters.PVFieldToVFloatArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVInt;
import org.epics.pvmanager.pva.adapters.PVFieldToVIntArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVLong;
import org.epics.pvmanager.pva.adapters.PVFieldToVLongArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVShort;
import org.epics.pvmanager.pva.adapters.PVFieldToVShortArray;
import org.epics.pvmanager.pva.adapters.PVFieldToVString;
import org.epics.pvmanager.pva.adapters.PVFieldToVStringArray;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VBooleanArray;
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
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VShort;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;



/**
 * A set of functions to work with PVAPVStructure-s.
 * @author msekoranja
 */
public class PVAPVStructureFunctionSet extends FormulaFunctionSet {

	public PVAPVStructureFunctionSet() {
        super(
                new FormulaFunctionSetDescription("pvData",
                        "pvData related functions")
                .addFormulaFunction(new PVAPVStructureVTableFormulaFunction())
                .addFormulaFunction(new PVAPVStructureFieldFormulaFunction())
                .addFormulaFunction(new PVAPVStructurePVFieldFormulaFunction())
                .addFormulaFunction(new PVAPVStructureToStringFormulaFunction())
            );
	}

	// tableOf(PVAPVStructure)
	static class PVAPVStructureVTableFormulaFunction implements FormulaFunction
	{

		@Override
		public Object calculate(List<Object> args) {
			PVAPVStructure value = (PVAPVStructure) args.get(0);
			if (value == null)
				return null;
			
			PVStructure pvStructure = value.getPVStructure();

			int numberOfFields = pvStructure.getNumberFields();
			ArrayList<Class<?>> types = new ArrayList<Class<?>>(numberOfFields);
			ArrayList<Object> values = new ArrayList<Object>(numberOfFields);
			for (PVField pvF : pvStructure.getPVFields())
			{
				/*
				// to String implementation 
				types.add(String.class);
				ArrayList<String> columnData = new ArrayList<String>();
				columnData.add(pvF.toString());
				values.add(columnData);
				*/
				
				types.add(toVTypeClass(pvF.getField()));
				ArrayList<Object> columnData = new ArrayList<Object>();
				columnData.add(toVType(pvF, null));
				values.add(columnData);
				
			}

			return ValueFactory.newVTable(
						types,
						Arrays.asList(pvStructure.getStructure().getFieldNames()),
						values);
		}
		
		@Override
		public List<String> getArgumentNames() {
			return Arrays.asList("pvStructure");
		}

		@Override
		public List<Class<?>> getArgumentTypes() {
			return Arrays.<Class<?>> asList(PVAPVStructure.class);
		}

		@Override
		public String getDescription() {
			return "Returns PVAPVStructure as VTable";
		}

		@Override
		public String getName() {
			return "tableOf";
		}

		@Override
		public Class<?> getReturnType() {
			return VTable.class;
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public boolean isVarArgs() {
			return false;
		}
		
	}
	
	// field("fieldName", PVAPVStructure)
	static class PVAPVStructureFieldFormulaFunction implements FormulaFunction
	{

		@Override
		public Object calculate(List<Object> args) {
			VString vfieldName = (VString) args.get(0);
			if (vfieldName == null)
				return null;
			String fieldName = vfieldName.getValue();
			
			PVAPVStructure value = (PVAPVStructure) args.get(1);
			if (value == null)
				return null;
			
			PVStructure pvStructure = value.getPVStructure();
			PVField pvField = pvStructure.getSubField(fieldName);

			PVStructure pvMetadataParent = (fieldName.equals("value")) ? pvStructure : null;
		
			if (pvField != null)
				return toVType(pvField, pvMetadataParent);
			
			return null;
		}

		@Override
		public List<String> getArgumentNames() {
			return Arrays.asList("fieldName", "pvStructure");
		}

		@Override
		public List<Class<?>> getArgumentTypes() {
			return Arrays.<Class<?>> asList(VString.class, PVAPVStructure.class);
		}

		@Override
		public String getDescription() {
			return "Returns field of a PVStructure";
		}

		@Override
		public String getName() {
			return "field";
		}

		@Override
		public Class<?> getReturnType() {
			return Object.class;
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public boolean isVarArgs() {
			return false;
		}
		
	}

	// pvField("fieldName", PVAPVStructure)
	static class PVAPVStructurePVFieldFormulaFunction implements FormulaFunction
	{

		@Override
		public Object calculate(List<Object> args) {
			VString vfieldName = (VString) args.get(0);
			if (vfieldName == null)
				return null;
			String fieldName = vfieldName.getValue();
			
			PVAPVStructure value = (PVAPVStructure) args.get(1);
			if (value == null)
				return null;
			
			PVStructure pvStructure = value.getPVStructure();
			PVField pvField = pvStructure.getSubField(fieldName);

			if (pvField != null)
			{
				if (pvField instanceof PVStructure)
					return new PVAPVStructure((PVStructure)pvField, false);
				else
					return new PVAPVField(pvField, false);
			}
			else
				return null;
		}

		@Override
		public List<String> getArgumentNames() {
			return Arrays.asList("fieldName", "pvStructure");
		}

		@Override
		public List<Class<?>> getArgumentTypes() {
			return Arrays.<Class<?>> asList(VString.class, PVAPVStructure.class);
		}

		@Override
		public String getDescription() {
			return "Returns PVField field of a PVStructure";
		}

		@Override
		public String getName() {
			return "pvField";
		}

		@Override
		public Class<?> getReturnType() {
			return PVAPVField.class;
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public boolean isVarArgs() {
			return false;
		}
		
	}

	// toString(PVAPVField)
	static class PVAPVStructureToStringFormulaFunction implements FormulaFunction
	{

		@Override
		public Object calculate(List<Object> args) {
			PVAPVField value = (PVAPVField) args.get(0);
			String str = (value == null ? "null" : value.toString());
			
			return ValueFactory.newVString(str, ValueFactory.alarmNone(), ValueFactory.timeNow());
		}
		
		@Override
		public List<String> getArgumentNames() {
			return Arrays.asList("pvField");
		}

		@Override
		public List<Class<?>> getArgumentTypes() {
			return Arrays.<Class<?>> asList(PVAPVField.class);
		}

		@Override
		public String getDescription() {
			return "Returns a string representation of a PVField";
		}

		@Override
		public String getName() {
			return "toString";
		}

		@Override
		public Class<?> getReturnType() {
			return VString.class;
		}

		@Override
		public boolean isPure() {
			return true;
		}

		@Override
		public boolean isVarArgs() {
			return false;
		}
		
	}

	public static Object toVType(PVField pvField, PVStructure pvMetadataParent) {
		
		if (pvField instanceof PVStructure)
		{
			PVStructure pvFieldStructure = (PVStructure)pvField;
			Structure fieldStructure = pvFieldStructure.getStructure();
			
			Set<PVATypeAdapter> typeAdapters = PVAVTypeAdapterSet.converters;
		    for (PVATypeAdapter converter : typeAdapters) {
		        if (converter.match(fieldStructure))
		        	return converter.createValue(pvFieldStructure, null, false);
		    }
	
		    // not found, simply return PVAPVStructure
			return new PVAPVStructure((PVStructure)pvField, false);
		}
		else if (pvField instanceof PVScalar)
		{
			PVScalar pvScalar = (PVScalar)pvField;
			switch (pvScalar.getScalar().getScalarType())
			{
			case pvDouble:
				return new PVFieldToVDouble(pvField, pvMetadataParent, false);
			case pvFloat:
				return new PVFieldToVFloat(pvField, pvMetadataParent, false);
			case pvInt:
			case pvUInt:
				return new PVFieldToVInt(pvField, pvMetadataParent, false);
			case pvLong:
			case pvULong:
				return new PVFieldToVLong(pvField, pvMetadataParent, false);
			case pvShort:
			case pvUShort:
				return new PVFieldToVShort(pvField, pvMetadataParent, false);
			case pvByte:
			case pvUByte:
				return new PVFieldToVByte(pvField, pvMetadataParent, false);
			case pvString:
				return new PVFieldToVString(pvField, pvMetadataParent, false);
			case pvBoolean:
				return new PVFieldToVBoolean(pvField, pvMetadataParent, false);
			default:
				throw new RuntimeException("unsupported scalar type: " + pvScalar.getScalar());
			}
		}
		else if (pvField instanceof PVScalarArray)
		{
			PVScalarArray pvScalarArray = (PVScalarArray)pvField;
			switch (pvScalarArray.getScalarArray().getElementType())
			{
			case pvDouble:
				return new PVFieldToVDoubleArray(pvField, pvMetadataParent, false);
			case pvFloat:
				return new PVFieldToVFloatArray(pvField, pvMetadataParent, false);
			case pvInt:
			case pvUInt:
				return new PVFieldToVIntArray(pvField, pvMetadataParent, false);
			case pvLong:
			case pvULong:
				return new PVFieldToVLongArray(pvField, pvMetadataParent, false);
			case pvShort:
			case pvUShort:
				return new PVFieldToVShortArray(pvField, pvMetadataParent, false);
			case pvByte:
			case pvUByte:
				return new PVFieldToVByteArray(pvField, pvMetadataParent, false);
			case pvString:
				return new PVFieldToVStringArray(pvField, pvMetadataParent, false);
			case pvBoolean:
				return new PVFieldToVBooleanArray(pvField, pvMetadataParent, false);
			default:
				throw new RuntimeException("unsupported scalar array element type: " + pvScalarArray.getScalarArray());
			}
		}
		else if (pvField instanceof PVUnion)
		{
			PVUnion pvUnion = (PVUnion)pvField;
			return toVType(pvUnion.get(), pvMetadataParent);
		}
		else
			return new PVAPVField(pvField, false);
	}

	public static Class<?> toVTypeClass(Field field) {
		
		Type fieldType = field.getType();
		if (fieldType == Type.structure)
		{
			Structure fieldStructure = (Structure)field;
			
			Set<PVATypeAdapter> typeAdapters = PVAVTypeAdapterSet.converters;
		    for (PVATypeAdapter converter : typeAdapters) {
		        if (converter.match(fieldStructure))
		        	return converter.getClass();
		    }
	
		    // not found, simply return PVAPVStructure
			return PVAPVStructure.class;
		}
		else if (fieldType == Type.scalar)
		{
			Scalar scalar = (Scalar)field;
			switch (scalar.getScalarType())
			{
			case pvDouble:
				return VDouble.class;
			case pvFloat:
				return VFloat.class;
			case pvInt:
			case pvUInt:
				return VInt.class;
			case pvLong:
			case pvULong:
				return VLong.class;
			case pvShort:
			case pvUShort:
				return VShort.class;
			case pvByte:
			case pvUByte:
				return VByte.class;
			case pvString:
				return VString.class;
			case pvBoolean:
				return VBoolean.class;
			default:
				throw new RuntimeException("unsupported scalar type: " + scalar.getScalarType());
			}
		}
		else if (fieldType == Type.scalarArray)
		{
			ScalarArray scalarArray = (ScalarArray)field;
			switch (scalarArray.getElementType())
			{
			case pvDouble:
				return VDoubleArray.class;
			case pvFloat:
				return VFloatArray.class;
			case pvInt:
			case pvUInt:
				return VIntArray.class;
			case pvLong:
			case pvULong:
				return VLongArray.class;
			case pvShort:
			case pvUShort:
				return VShortArray.class;
			case pvByte:
			case pvUByte:
				return VByteArray.class;
			case pvString:
				return VStringArray.class;
			case pvBoolean:
				return VBooleanArray.class;
			default:
				throw new RuntimeException("unsupported scalar array element type: " + scalarArray.getElementType());
			}
		}
		else if (fieldType == Type.union)
		{
			Union u = (Union)field;
			if (u.isVariant())
				return Object.class;
			else
			{
				boolean allScalars = true;
				boolean allScalarArrays = true;
				boolean allNumeric = true;
				boolean sameFields = true;
				Field lastField = null;
				for (Field uf : u.getFields())
				{
					if (lastField != null)
						if (!uf.equals(lastField))
							sameFields = false;
	
					Type uft = uf.getType();
					if (uft != Type.scalar)
						allScalars = false;
					else
					{
						Scalar s = (Scalar)uf;
						if (!s.getScalarType().isNumeric())
							allNumeric = false;
					}
					
					if (uft != Type.scalarArray)
						allScalarArrays = false;
					{
						ScalarArray s = (ScalarArray)uf;
						if (!s.getElementType().isNumeric())
							allNumeric = false;
					}
					
					lastField = uf;
				}
				
				if (sameFields)
					return toVTypeClass(lastField);
				else if (allNumeric)
				{
					if (allScalars)
						return VNumber.class;
					else if (allScalarArrays)
						return VNumberArray.class;
					else
						return Object.class;
				}
				else 
					return Object.class; 
			}
		}
		else
			return Object.class; // TODO PVUnionArray, PVStructureArray
	}
}
