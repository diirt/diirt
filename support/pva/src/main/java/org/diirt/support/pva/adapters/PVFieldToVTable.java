/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import java.util.ArrayList;
import java.util.List;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.StringArrayData;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VTypeToString;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVTable implements VTable {

    private final List<Class<?>> types;
    private final List<String> names;
    private final List<Object> values;
    private final int rowCount;

        /**
         * @param pvField
         * @param disconnected
         */
        public PVFieldToVTable(PVStructure pvField, boolean disconnected) {

                PVStringArray labelsField =
                        (PVStringArray)pvField.getScalarArrayField("labels", ScalarType.pvString);
                String[] labels;
                if (labelsField != null)
                {
                        StringArrayData data = new StringArrayData();
                        labelsField.get(0, labelsField.getLength(), data);
                        labels = data.data;
                }
                else
                        labels = null;

                PVStructure valueField = pvField.getStructureField("value");
                if (valueField != null)
                {
                        PVField[] cols = valueField.getPVFields();
                        int numCols = cols.length;
                        types = new ArrayList<Class<?>>(numCols);
                        names = new ArrayList<String>(numCols);
                        values = new ArrayList<Object>(numCols);

                        int maxRowCount = 0;

                        int nameIndex = 0;
                for (PVField pvColumn : valueField.getPVFields())
                {
                        PVScalarArray scalarArray = (PVScalarArray)pvColumn;

                        types.add(NTUtils.scalarArrayElementClass(scalarArray));
                        values.add(NTUtils.scalarArrayToList(scalarArray, true));
                        names.add(labels != null ? labels[nameIndex] : pvColumn.getFieldName());

                        int len = scalarArray.getLength();
                        if (len > maxRowCount) maxRowCount = len;

                        nameIndex++;
                }

                rowCount = maxRowCount;
                }
                else
                {
                        names = null;
                        types = null;
                        values = null;
                        rowCount = -1;
                }
        }

    @Override
    public int getColumnCount() {
        return names.size();
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public Class<?> getColumnType(int column) {
        return types.get(column);
    }

    @Override
    public String getColumnName(int column) {
        return names.get(column);
    }

    @Override
    public Object getColumnData(int column) {
        return values.get(column);
    }

        @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
