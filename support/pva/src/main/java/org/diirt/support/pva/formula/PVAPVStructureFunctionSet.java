/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByte;
import org.epics.pvdata.pv.PVUInt;
import org.epics.pvdata.pv.PVULong;
import org.epics.pvdata.pv.PVUShort;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarArray;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Type;
import org.epics.pvdata.pv.Union;
import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;
import org.diirt.support.pva.PVATypeAdapter;
import org.diirt.support.pva.PVAVTypeAdapterSet;
import org.diirt.support.pva.adapters.PVAPVField;
import org.diirt.support.pva.adapters.PVAPVStructure;
import org.diirt.support.pva.adapters.PVFieldToVBoolean;
import org.diirt.support.pva.adapters.PVFieldToVBooleanArray;
import org.diirt.support.pva.adapters.PVFieldToVByte;
import org.diirt.support.pva.adapters.PVFieldToVByteArray;
import org.diirt.support.pva.adapters.PVFieldToVDouble;
import org.diirt.support.pva.adapters.PVFieldToVDoubleArray;
import org.diirt.support.pva.adapters.PVFieldToVFloat;
import org.diirt.support.pva.adapters.PVFieldToVFloatArray;
import org.diirt.support.pva.adapters.PVFieldToVInt;
import org.diirt.support.pva.adapters.PVFieldToVIntArray;
import org.diirt.support.pva.adapters.PVFieldToVLong;
import org.diirt.support.pva.adapters.PVFieldToVLongArray;
import org.diirt.support.pva.adapters.PVFieldToVShort;
import org.diirt.support.pva.adapters.PVFieldToVShortArray;
import org.diirt.support.pva.adapters.PVFieldToVString;
import org.diirt.support.pva.adapters.PVFieldToVStringArray;
import java.time.Instant;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VBooleanArray;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VLongArray;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VShortArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;



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

                Class<?> clazz = toVTableColumnClass(pvF.getField());
                types.add(clazz);
                ArrayList<Object> columnData = new ArrayList<Object>();
                columnData.add(toVTableColumnValue(pvF, clazz));
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

    public static Object toVTableColumnValue(PVField pvField, Class<?> vtableClass) {

        if (pvField instanceof PVStructure)
        {
            if (vtableClass.equals(Instant.class))
                return toVType(pvField, null);
            else
                return null;
        }
        else if (pvField instanceof PVScalar)
        {
            PVScalar pvScalar = (PVScalar)pvField;
            switch (pvScalar.getScalar().getScalarType())
            {
            case pvDouble:
                return ((PVDouble)pvScalar).get();
            case pvFloat:
                return ((PVFloat)pvScalar).get();
            case pvInt:
                return ((PVInt)pvScalar).get();
            case pvUInt:
                return ((PVUInt)pvScalar).get();
            case pvLong:
                return ((PVLong)pvScalar).get();
            case pvULong:
                return ((PVULong)pvScalar).get();
            case pvShort:
                return ((PVShort)pvScalar).get();
            case pvUShort:
                return ((PVUShort)pvScalar).get();
            case pvByte:
                return ((PVByte)pvScalar).get();
            case pvUByte:
                return ((PVUByte)pvScalar).get();
            case pvString:
                return ((PVString)pvScalar).get();
            case pvBoolean:
                return ((PVBoolean)pvScalar).get();
            default:
                throw new RuntimeException("unsupported scalar type: " + pvScalar.getScalar());
            }
        }
        else if (pvField instanceof PVScalarArray)
        {
            return null;
        }
        else if (pvField instanceof PVUnion)
        {
            return null;
        }
        else
            return null;
    }

    /*
    static Map<Class<?>,Class<?>> VTypeToVTable = new HashMap<Class<?>,Class<?>>();
    static
    {
        VTypeToVTable.put(VByte.class, Byte.class);
        VTypeToVTable.put(VShort.class, Short.class);
        VTypeToVTable.put(VInt.class, Integer.class);
        VTypeToVTable.put(VLong.class, Long.class);
        VTypeToVTable.put(VBoolean.class, Boolean.class);
        VTypeToVTable.put(VString.class, String.class);
        VTypeToVTable.put(VDouble.class, Double.class);
        VTypeToVTable.put(VFloat.class, Float.class);
        VTypeToVTable.put(Timestamp.class, Timestamp.class);
        VTypeToVTable.put(VNumber.class, Number.class);
    }

    public static Class<?> toVTableColumnClass(Field field) {

        Class<?> vclazz = toVTypeClass(field);

        Class<?> vTableType = VTypeToVTable.get(vclazz);
        if (vTableType != null)
            return vTableType;
        else
            return String.class;
    }
    */

    public static Class<?> toVTableColumnClass(Field field) {

        Type fieldType = field.getType();
        if (fieldType == Type.structure)
        {
            Structure fieldStructure = (Structure)field;

            if (fieldStructure.getID().equals("time_t"))
                return Instant.class;
            else
                return String.class;
        }
        else if (fieldType == Type.scalar)
        {
            Scalar scalar = (Scalar)field;
            switch (scalar.getScalarType())
            {
            case pvDouble:
                return Double.class;
            case pvFloat:
                return Float.class;
            case pvInt:
            case pvUInt:
                return Integer.class;
            case pvLong:
            case pvULong:
                return Long.class;
            case pvShort:
            case pvUShort:
                return Short.class;
            case pvByte:
            case pvUByte:
                return Byte.class;
            case pvString:
                return String.class;
            case pvBoolean:
                return Boolean.class;
            default:
                throw new RuntimeException("unsupported scalar type: " + scalar.getScalarType());
            }
        }
        else if (fieldType == Type.scalarArray)
        {
            return String.class;
        }
        else if (fieldType == Type.union)
        {
            Union u = (Union)field;
            if (u.isVariant())
                return String.class;
            else
            {
                return String.class;
                /*
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
                    return toVTableColumnClass(lastField);
                else if (allNumeric)
                {
                    if (allScalars)
                        return String.class;
                        //return VNumber.class;
                    else if (allScalarArrays)
                        return String.class;
                    else
                        return String.class;
                }
                else
                    return String.class;
                */
            }
        }
        else
            return String.class;
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
