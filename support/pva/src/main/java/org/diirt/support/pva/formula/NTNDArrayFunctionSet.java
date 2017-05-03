/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;

import org.epics.pvdata.pv.BooleanArrayData;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVStructureArray;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StructureArrayData;
import org.diirt.support.pva.adapters.AlarmTimeDisplayExtractor;
import org.diirt.support.pva.adapters.NTUtils;
import org.diirt.support.pva.adapters.PVANTNDArray;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListNumber;
import org.diirt.util.array.ListNumbers;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.VImage;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;



/**
 * A set of functions to work with NTNDArray-s.
 * 
 * TODO With the new VImage supporting Image types other than TYPE_3BYTE_BGR,
 * these formulas could encapsulate the entire image data coming from pvaccess
 * and register the appropriate conversion functions in the VTypeUtil
 *
 * @author msekoranja
 */
public class NTNDArrayFunctionSet extends FormulaFunctionSet {

        public NTNDArrayFunctionSet() {
        super(
                new FormulaFunctionSetDescription("NTNDArray",
                        "NTNDArray related functions")
                .addFormulaFunction(new NTNDArrayAttributesFormulaFunction())
                .addFormulaFunction(new NTNDArrayNDArrayFormulaFunction())
                .addFormulaFunction(new NTNDArrayImageFormulaFunction())
            );
        }

        // attributes(NTNDArray)
        static class NTNDArrayAttributesFormulaFunction implements FormulaFunction
        {

                static final String[] COLUMN_NAMES = new String[] { "name", "value", "descriptor", "sourceType", "source" };
                static final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, String.class, String.class, int.class, String.class };

                @Override
                public Object calculate(List<Object> args) {
                        PVANTNDArray value = (PVANTNDArray) args.get(0);
                        if (value == null)
                                return null;

                        PVStructure pvStructure = value.getPVStructure();

                        final PVStructureArray attributeArray = pvStructure.getStructureArrayField("attribute");

                        final int rows = attributeArray.getLength();
                        final StructureArrayData attributeArrayData = new StructureArrayData();
                        attributeArray.get(0, rows, attributeArrayData);


                        ArrayList<Object> columnValues = new ArrayList<Object>(COLUMN_NAMES.length);
                        for (int index = 0; index < COLUMN_NAMES.length; index++)
                        {
                                Class<?> columnType = COLUMN_TYPES[index];

                                if (columnType.equals(String.class))
                                {
                                        ArrayList<String> columnData = new ArrayList<String>();

                                        if (index == 1)
                                        {
                                                // value is a special case, convert any to string
                                                for (int i = 0; i < rows; i++)
                                                {
                                                        PVField pvData = attributeArrayData.data[i].getUnionField(COLUMN_NAMES[index]).get();
                                                        columnData.add(
                                                                        pvData != null ? pvData.toString() : null
                                                        );
                                                }

                                        }
                                        else
                                        {
                                                for (int i = 0; i < rows; i++)
                                                        columnData.add(
                                                                        attributeArrayData.data[i].getStringField(COLUMN_NAMES[index]).get()
                                                        );
                                        }
                                        columnValues.add(columnData);
                                }
                                else if (columnType.equals(int.class))
                                {
                                        int columnData[] = new int[rows];
                                        for (int i = 0; i < rows; i++)
                                                columnData[i] = attributeArrayData.data[i].getIntField(COLUMN_NAMES[index]).get();
                                        columnValues.add(new ArrayInt(columnData));
                                }
                                else
                                        throw new RuntimeException("unsupported attribute field type");
                        }

                        return ValueFactory.newVTable(Arrays.asList(COLUMN_TYPES), Arrays.asList(COLUMN_NAMES), columnValues);
                }

                @Override
                public List<String> getArgumentNames() {
                        return Arrays.asList("ntNdArray");
                }

                @Override
                public List<Class<?>> getArgumentTypes() {
                        return Arrays.<Class<?>> asList(PVANTNDArray.class);
                }

                @Override
                public String getDescription() {
                        return "Returns NTNDArray attributes as VTable";
                }

                @Override
                public String getName() {
                        return "attributes";
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

        private final static String noUnits = ValueFactory.displayNone().getUnits();


        // ndArray(NTNDArray)
        static class NTNDArrayNDArrayFormulaFunction implements FormulaFunction
        {

                @Override
                public Object calculate(List<Object> args) {
                        PVANTNDArray value = (PVANTNDArray) args.get(0);
                        if (value == null)
                                return null;

                        PVStructure pvStructure = value.getPVStructure();

                        AlarmTimeDisplayExtractor atd = new AlarmTimeDisplayExtractor(pvStructure, false);

                        PVUnion valueUnion = pvStructure.getUnionField("value");

                        PVScalarArray scalarArray = valueUnion.get(PVScalarArray.class);
                        Object list = NTUtils.scalarArrayToList(scalarArray, true);
                        if (!(list instanceof ListNumber))
                                throw new IllegalArgumentException("value array not a list of numbers");
                        ListNumber data = (ListNumber)list;

                        PVStructureArray pvDimension = pvStructure.getStructureArrayField("dimension");
                        StructureArrayData dimensionArrayData = new StructureArrayData();
                        pvDimension.get(0,  pvDimension.getLength(), dimensionArrayData);
                        PVStructure[] dims = dimensionArrayData.data;


                        int ix = 0;
                        int[] sizes = new int[dims.length];
                        List<ArrayDimensionDisplay> dimensionDisplay = new ArrayList<ArrayDimensionDisplay>(dims.length);
                        for (PVStructure dim : dims)
                        {
                                int size = dim.getIntField("size").get();
                                sizes[ix++] = size;
                                boolean reversed = dim.getBooleanField("reverse").get();
                                dimensionDisplay.add(
                                                ValueFactory.newDisplay(
                                                                ListNumbers.linearList(0, 1, size + 1),
                                                                reversed,
                                                                noUnits)
                                                                );
                        }

                        return ValueFactory.newVNumberArray(data, new ArrayInt(sizes), dimensionDisplay, atd, atd, atd);
                }

                @Override
                public List<String> getArgumentNames() {
                        return Arrays.asList("ntNdArray");
                }

                @Override
                public List<Class<?>> getArgumentTypes() {
                        return Arrays.<Class<?>> asList(PVANTNDArray.class);
                }

                @Override
                public String getDescription() {
                        return "Returns NTNDArray as VNumberArray (nd array)";
                }

                @Override
                public String getName() {
                        return "ndArray";
                }

                @Override
                public Class<?> getReturnType() {
                        return VNumberArray.class;
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

        // image(NTNDArray)
        static class NTNDArrayImageFormulaFunction implements FormulaFunction
        {

                @Override
                public Object calculate(List<Object> args) {
                        PVANTNDArray value = (PVANTNDArray) args.get(0);
                        if (value == null)
                                return null;

                        PVStructure pvStructure = value.getPVStructure();

                        PVUnion valueUnion = pvStructure.getUnionField("value");
                        PVScalarArray pvArray = valueUnion.get(PVScalarArray.class);

                        PVStructureArray pvDimension = pvStructure.getStructureArrayField("dimension");
                        StructureArrayData dimensionArrayData = new StructureArrayData();
                        pvDimension.get(0,  pvDimension.getLength(), dimensionArrayData);
                        PVStructure[] dims = dimensionArrayData.data;

                        int ix = 0;
                        int[] sizes = new int[dims.length];
                        for (PVStructure dim : dims)
                        {
                                int size = dim.getIntField("size").get();
                                sizes[ix++] = size;
                                //boolean reversed = dim.getBooleanField("reverse").get();
                        }

                        PVStructureArray pvAttribute = pvStructure.getStructureArrayField("attribute");
                        StructureArrayData attributeArrayData = new StructureArrayData();
                        pvAttribute.get(0,  pvAttribute.getLength(), attributeArrayData);
                        PVStructure[] attrs = attributeArrayData.data;
                        int modeValue = -1;
                        for (PVStructure attr : attrs)
                                if (attr.getStringField("name").get().equals("ColorMode"))
                                {
                                        modeValue = attr.getUnionField("value").get(PVInt.class).get();
                                        break;
                                }

                        if (modeValue < 0 || modeValue >= NDColorMode.values().length)
                                throw new IllegalArgumentException("invalid or non-existent ColorMode attribute");
                        NDColorMode mode = NDColorMode.values()[modeValue];

                        return createVImage(pvArray, mode, sizes);
                }

                @Override
                public List<String> getArgumentNames() {
                        return Arrays.asList("ntNdArray");
                }

                @Override
                public List<Class<?>> getArgumentTypes() {
                        return Arrays.<Class<?>> asList(PVANTNDArray.class);
                }

                @Override
                public String getDescription() {
                        return "Returns NTNDArray as VImage";
                }

                @Override
                public String getName() {
                        return "image";
                }

                @Override
                public Class<?> getReturnType() {
                        return VImage.class;
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

        private enum NDColorMode
        {
                NDColorModeMono,    /** Monochromatic image */
                NDColorModeBayer,   /** Bayer pattern image,
                                                                                1 value per pixel but with color filter on detector */
                NDColorModeRGB1,    /** RGB image with pixel color interleave,
                                                                                data array is [3, NX, NY] */
                NDColorModeRGB2,    /** RGB image with row color interleave,
                                                                                data array is [NX, 3, NY]  */
                NDColorModeRGB3,    /** RGB image with plane color interleave,
                                                                                data array is [NX, NY, 3]  */
                NDColorModeYUV444,  /** YUV image, 3 bytes encodes 1 RGB pixel */
                NDColorModeYUV422,  /** YUV image, 4 bytes encodes 2 RGB pixel */
                NDColorModeYUV411;   /** YUV image, 6 bytes encodes 4 RGB pixels */
        };

        public static VImage createVImage(PVScalarArray valueArray, NDColorMode mode, int[] dims) {

                if (dims.length != 2 &&
                        dims.length != 3)
                        throw new IllegalArgumentException("VImage can be only created from 2-d or 3-d ndArray");

                int nx = dims[0];
                int ny = dims[1];
                int nz = (dims.length == 3) ? dims[2] : 1;

                int valueArraySize = nx * ny * nz;
                if (valueArraySize <= 0)
                        throw new IllegalArgumentException("given dimensions are not valid");

                if (valueArray.getLength() != valueArraySize)
                        throw new IllegalArgumentException("value array length does not match given dimensions");

                int width, height;
                byte[] data;

                switch (mode)
                {
                        case NDColorModeMono:
                        case NDColorModeBayer:
                        {

                                // 2D value array
                                if (nz != 1)
                                        throw new IllegalArgumentException("invalid dimensions for given color mode");

                                width = nx;
                                height = ny;

                                ScalarType scalarType = valueArray.getScalarArray().getElementType();
                                switch (scalarType)
                                {
                                        case pvBoolean:
                                        {
                                                BooleanArrayData bad = new BooleanArrayData();
                                                ((PVBooleanArray)valueArray).get(0, valueArraySize, bad);
                                                // convert b&w to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        byte c = bad.data[i] ? (byte)-1 : (byte)0;
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvByte:
                                        case pvUByte:
                                        {
                                                ByteArrayData bad = new ByteArrayData();
                                                if (scalarType == ScalarType.pvByte)
                                                        ((PVByteArray)valueArray).get(0, valueArraySize, bad);
                                                else
                                                        ((PVUByteArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        byte c = bad.data[i];
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvShort:
                                        case pvUShort:
                                        {
                                                ShortArrayData bad = new ShortArrayData();
                                                if (scalarType == ScalarType.pvShort)
                                                        ((PVShortArray)valueArray).get(0, valueArraySize, bad);
                                                else
                                                        ((PVUShortArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        byte c = (byte)(bad.data[i] >>> 8);
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvInt:
                                        case pvUInt:
                                        {
                                                IntArrayData bad = new IntArrayData();
                                                if (scalarType == ScalarType.pvInt)
                                                        ((PVIntArray)valueArray).get(0, valueArraySize, bad);
                                                else
                                                        ((PVUIntArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        byte c = (byte)(bad.data[i] >>> 24);
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvLong:
                                        case pvULong:
                                        {
                                                LongArrayData bad = new LongArrayData();
                                                if (scalarType == ScalarType.pvLong)
                                                        ((PVLongArray)valueArray).get(0, valueArraySize, bad);
                                                else
                                                        ((PVULongArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        byte c = (byte)(bad.data[i] >>> 56);
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvFloat:
                                        {
                                                FloatArrayData bad = new FloatArrayData();
                                                ((PVFloatArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        int tc = (int)(bad.data[i] * 255);   // assuming [0 - 1.0]
                                                        byte c = (tc > 127) ? (byte)(tc-256) : (byte)tc;
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        case pvDouble:
                                        {
                                                DoubleArrayData bad = new DoubleArrayData();
                                                ((PVDoubleArray)valueArray).get(0, valueArraySize, bad);
                                                // convert grayscale to BGR
                                                data = new byte[3*valueArraySize];
                                                int p = 0;
                                                for (int i = 0; i < valueArraySize; i++)
                                                {
                                                        int tc = (int)(bad.data[i] * 255);      // assuming [0 - 1.0]
                                                        byte c = (tc > 127) ? (byte)(tc-256) : (byte)tc;
                                                        // B = G = R = c
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                        data[p++] = c;
                                                }
                                                break;
                                        }

                                        default:
                                                throw new IllegalArgumentException("unsupported scalar_t[] value type");

                                }

                                break;
                        }

                        case NDColorModeRGB1:
                        {
                                if (nx != 3)
                                        throw new IllegalArgumentException("dim[0] should be 3 for NDColorModeRGB1");
                                width = ny;
                                height = nz;

                                ByteArrayData bad = new ByteArrayData();
                                ((PVByteArray)valueArray).get(0, valueArraySize, bad);
                                // convert to BGR
                                data = new byte[valueArraySize];
                                int out = 0; int in = 0;
                                while (in < valueArraySize)
                                {
                                        byte r = bad.data[in++];
                                        byte g = bad.data[in++];
                                        byte b = bad.data[in++];
                                        data[out++] = b;
                                        data[out++] = g;
                                        data[out++] = r;
                                }
                                break;
                        }

                        case NDColorModeRGB2:
                        {
                                // ny = 3
                                if (ny != 3)
                                        throw new IllegalArgumentException("dim[1] should be 3 for NDColorModeRGB2");
                                width = nx;
                                height = nz;

                                ByteArrayData bad = new ByteArrayData();
                                ((PVByteArray)valueArray).get(0, valueArraySize, bad);
                                // convert to BGR
                                data = new byte[valueArraySize];
                                int out = 0;
                                int nCols = width;
                                int nRows = height;
                                for (int row = 0; row < nRows; row++)
                                {
                                        int redIn = row * nCols * 3;
                                        int greenIn = redIn + nCols;
                                        int blueIn = greenIn + nCols;
                                        for (int col = 0; col < nCols; col++)
                                        {
                                                byte r = bad.data[redIn++];
                                                byte g = bad.data[greenIn++];
                                                byte b = bad.data[blueIn++];
                                                data[out++] = b;
                                                data[out++] = g;
                                                data[out++] = r;
                                        }
                                }
                                break;
                        }

                        case NDColorModeRGB3:
                        {
                                // nz = 3
                                if (nz != 3)
                                        throw new IllegalArgumentException("dim[2] should be 3 for NDColorModeRGB3");
                                width = nx;
                                height = ny;

                                int imageSize = width * height;

                                ByteArrayData bad = new ByteArrayData();
                                ((PVByteArray)valueArray).get(0, valueArraySize, bad);
                                // convert to BGR
                                data = new byte[valueArraySize];
                                int out = 0;
                                int redIn = 0;
                                int greenIn = imageSize;
                                int blueIn = imageSize * 2;
                                while (redIn < imageSize)
                                {
                                        byte r = bad.data[redIn++];
                                        byte g = bad.data[greenIn++];
                                        byte b = bad.data[blueIn++];
                                        data[out++] = b;
                                        data[out++] = g;
                                        data[out++] = r;
                                }
                                break;
                        }

                        case NDColorModeYUV444:
                        case NDColorModeYUV422:
                        case NDColorModeYUV411:
                        default:
                                throw new IllegalArgumentException("unsupported colorMode");
                }

                return ValueFactory.newVImage(height, width, data);
        }

}
