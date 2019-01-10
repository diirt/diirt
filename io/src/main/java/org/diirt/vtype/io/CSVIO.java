/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.diirt.util.text.CsvParser;
import org.diirt.util.text.CsvParserResult;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInteger;
import org.epics.util.array.ListNumber;
import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VEnum;
//import org.epics.vtype.VEnumArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;

/**
 *
 * @author carcassi
 */
public class CSVIO {

    // TODO: we should take these from a default place
    private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.N Z"); //$NON-NLS-1$

    public void export(Object value, Writer writer) {
        if (!canExport(value)) {
            throw new IllegalArgumentException("Type " + value.getClass().getSimpleName() + " is not supported by this data export.");
        }

        try {
            Time time = Time.timeOf(value);
            if (time != null && time.getTimestamp() != null) {
                writer.append("\"")
                        .append(timeFormat.format(ZonedDateTime.ofInstant(time.getTimestamp(), ZoneId.systemDefault())))
                        .append("\" ");
            }

            Alarm alarm = Alarm.alarmOf(value);
            if (alarm != null) {
                writer.append(alarm.getSeverity().name())
                        .append(" ")
                        .append(alarm.getName());
            }

            if (value instanceof VNumber) {
                writer.append(" ")
                        .append(Double.toString(((VNumber) value).getValue().doubleValue()));
            }

            if (value instanceof VString) {
                writer.append(" \"")
                        .append(((VString) value).getValue())
                        .append("\"");
            }

            if (value instanceof VEnum) {
                writer.append(" \"")
                        .append("\"");
            }

            if (value instanceof VNumberArray) {
                ListNumber data = ((VNumberArray) value).getData();
                for (int i = 0; i < data.size(); i++) {
                    writer.append(" ")
                            .append(Double.toString(data.getDouble(i)));
                }
            }

            if (value instanceof VStringArray) {
                List<String> data = ((VStringArray) value).getData();
                for (int i = 0; i < data.size(); i++) {
                    writer.append(" \"")
                            .append(data.get(i))
                            .append("\"");
                }
            }

//            if (value instanceof VEnumArray) {
//                List<String> data = ((VEnumArray) value).getData();
//                for (int i = 0; i < data.size(); i++) {
//                    writer.append(" \"")
//                            .append(data.get(i))
//                            .append("\"");
//                }
//            }

            if (value instanceof VTable) {
                VTable table = (VTable) value;
                boolean first = true;
                for (int i = 0; i < table.getColumnCount(); i++) {
                    if (first) {
                        first = false;
                    } else {
                        writer.append(" ");
                    }
                    writer.append("\"")
                            .append(table.getColumnName(i))
                            .append("\"");
                }
                writer.append("\n");
                for (int row = 0; row < table.getRowCount(); row++) {
                    first = true;
                    for (int column = 0; column < table.getColumnCount(); column++) {
                        if (first) {
                            first = false;
                        } else {
                            writer.append(" ");
                        }
                        writer.append(toString(table, row, column));
                    }
                    writer.append("\n");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Write failed", e);
        }
    }

    public VTable importVTable(Reader reader) {
        CsvParser parser = CsvParser.AUTOMATIC;
        CsvParserResult result = parser.parse(reader);
        if (!result.isParsingSuccessful()) {
            throw new RuntimeException(result.getMessage());
        }
        if (result.getRowCount() == 0) {
            throw new RuntimeException("Malformed table: no lines");
        }

        return VTable.of(result.getColumnTypes(), result.getColumnNames(), result.getColumnValues());
    }

    private String toString(VTable table, int row, int column) {
        Class<?> clazz = table.getColumnType(column);
        if (clazz.equals(String.class)) {
            return "\"" + ((List) table.getColumnData(column)).get(row) + "\"";
        }
        if (clazz.equals(Double.TYPE)) {
            return Double.toString(((ListDouble) table.getColumnData(column)).getDouble(row));
        }
        if (clazz.equals(Integer.TYPE)) {
            return Integer.toString(((ListInteger) table.getColumnData(column)).getInt(row));
        }
        if (clazz.equals(Instant.class)) {
            @SuppressWarnings("unchecked")
            List<?> timestamp = (List<?>) table.getColumnData(column);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant((Instant)(timestamp.get(row)), ZoneId.systemDefault());
            System.out.println(timeFormat.format(zonedDateTime));
            return "\"" + timeFormat.format(zonedDateTime) + "\"";
        }
        throw new UnsupportedOperationException("Can't export columns of type " + clazz.getSimpleName());
    }

    public boolean canExport(Object data) {
        if (data instanceof VNumber) {
            return true;
        }

        if (data instanceof VString) {
            return true;
        }

        if (data instanceof VEnum) {
            return true;
        }

        if (data instanceof VNumberArray) {
            return true;
        }

        if (data instanceof VStringArray) {
            return true;
        }
//
//        if (data instanceof VEnumArray) {
//            return true;
//        }

        if (data instanceof VTable) {
            return true;
        }

        return false;
    }
}
