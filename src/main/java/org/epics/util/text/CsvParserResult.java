/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.util.text;

import java.util.List;

/**
 *
 * @author carcassi
 */
public class CsvParserResult {
    private final List<String> columnNames;
    private final List<Object> columnValues;
    private final List<Class<?>> columnTypes;

    CsvParserResult(List<String> columnNames, List<Object> columnValues, List<Class<?>> columnTypes) {
        this.columnNames = columnNames;
        this.columnValues = columnValues;
        this.columnTypes = columnTypes;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<Object> getColumnValues() {
        return columnValues;
    }

    public List<Class<?>> getColumnTypes() {
        return columnTypes;
    }
    
}
