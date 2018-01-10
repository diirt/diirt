/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StringArrayData;
import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayLong;
import org.diirt.util.array.ArrayShort;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VTypeToString;

/**
 * @author msekoranja
 * @author dkumar
 */
public class PVFieldNTNameValueToVTable implements VTable {

  private Class<?> valueType;
  private List<String> names;
  private List<Object> values;

  /**
   * @param pvField
   * @param disconnected
   */
        public PVFieldNTNameValueToVTable(PVStructure pvField, boolean disconnected) {

                PVStringArray namesField = (PVStringArray) pvField.getScalarArrayField("name", ScalarType.pvString);

                PVScalarArray scalarArray = (PVScalarArray) pvField.getSubField("value");

                if ((namesField == null) || (scalarArray == null)) {
                        this.names = null;
                        valueType = null;
                        values = null;
                        return;
                }

                StringArrayData namesData = new StringArrayData();
                namesField.get(0, namesField.getLength(), namesData);
                this.names = Arrays.asList(namesData.data);

                int numCols = this.names.size();

                this.values = new ArrayList<Object>(numCols);

                if (scalarArray instanceof PVDoubleArray) {

                        valueType = double.class;
                        DoubleArrayData data = new DoubleArrayData();
                        ((PVDoubleArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayDouble(data.data[i]));

                } else if (scalarArray instanceof PVFloatArray) {

                        valueType = float.class;
                        FloatArrayData data = new FloatArrayData();
                        ((PVFloatArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayFloat(data.data[i]));

                } else if (scalarArray instanceof PVStringArray) {

                        valueType = String.class;
                        StringArrayData data = new StringArrayData();
                        ((PVStringArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(Arrays.asList(data.data[i]));

                } else if (scalarArray instanceof PVIntArray) {

                        valueType = int.class;
                        IntArrayData data = new IntArrayData();
                        ((PVIntArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayInt(data.data[i]));

                } else if (scalarArray instanceof PVUIntArray) {

                        valueType = int.class;
                        IntArrayData data = new IntArrayData();
                        ((PVUIntArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayInt(data.data[i]));

                } else if (scalarArray instanceof PVLongArray) {

                        valueType = long.class;
                        LongArrayData data = new LongArrayData();
                        ((PVLongArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayLong(data.data[i]));

                } else if (scalarArray instanceof PVULongArray) {

                        valueType = long.class;
                        LongArrayData data = new LongArrayData();
                        ((PVULongArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayLong(data.data[i]));

                } else if (scalarArray instanceof PVByteArray) {

                        valueType = byte.class;
                        ByteArrayData data = new ByteArrayData();
                        ((PVByteArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayByte(data.data[i]));

                } else if (scalarArray instanceof PVUByteArray) {

                        valueType = byte.class;
                        ByteArrayData data = new ByteArrayData();
                        ((PVUByteArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayByte(data.data[i]));

                } else if (scalarArray instanceof PVShortArray) {

                        valueType = short.class;
                        ShortArrayData data = new ShortArrayData();
                        ((PVShortArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayShort(data.data[i]));

                } else if (scalarArray instanceof PVUShortArray) {

                        valueType = short.class;
                        ShortArrayData data = new ShortArrayData();
                        ((PVUShortArray) scalarArray).get(0, numCols, data);
                        for (int i = 0; i < numCols; i++)
                                values.add(new ArrayShort(data.data[i]));

                } else {
                        throw new IllegalArgumentException("Unsupported type for NTNameValue.value array field");
                }
        }

  @Override
  public int getColumnCount() {
    return this.names.size();
  }

  @Override
  public int getRowCount() {
    return 1;
  }

  @Override
  public Class<?> getColumnType(int column) {
    return this.valueType;
  }

  @Override
  public String getColumnName(int column) {
    return this.names.get(column);
  }

  @Override
  public Object getColumnData(int column) {
    return this.values.get(column);
  }

  @Override
  public String toString() {
    return VTypeToString.toString(this);
  }

}
