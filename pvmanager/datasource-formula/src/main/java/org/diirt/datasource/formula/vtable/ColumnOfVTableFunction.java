/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vtable;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.diirt.datasource.formula.FormulaFunction;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInteger;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;

/**
 * Extracts a columns from a VTable.
 *
 * @author carcassi
 */
class ColumnOfVTableFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "columnOf";
    }

    @Override
    public String getDescription() {
        return "Extracts a column from the given table";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(VTable.class, VString.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("table", "columName");
    }

    @Override
    public Class<?> getReturnType() {
        return VType.class;
    }

    @Override
    public Object calculate(final List<Object> args) {
        VTable table = (VTable) args.get(0);
        VString columnName = (VString) args.get(1);

        if (columnName == null || table == null) {
            return null;
        }

        int index = -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (Objects.equals(columnName.getValue(), table.getColumnName(i))) {
                index = i;
            }
        }
        if (index == -1) {
            throw new RuntimeException("Table does not contain column '" + columnName.getValue() + "'");
        }

        Class<?> type = table.getColumnType(index);

        if (String.class.isAssignableFrom(type)) {
            @SuppressWarnings("unchecked")
            List<String> data = (List<String>) table.getColumnData(index);
            return VStringArray.of(data, Alarm.none(), Time.now());
        }

        if (Double.TYPE.isAssignableFrom(type)) {
            ListDouble data = (ListDouble) table.getColumnData(index);
            return VDoubleArray.of(data, Alarm.none(), Time.now(), Display.none());
        }

        if (Integer.TYPE.isAssignableFrom(type)) {
            ListInteger data = (ListInteger) table.getColumnData(index);
            return VIntArray.of(data, Alarm.none(), Time.now(), Display.none());
        }

        throw new RuntimeException("Unsupported type " + type.getSimpleName());
    }

}
