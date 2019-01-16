/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;

import org.epics.util.array.CollectionNumbers;
import org.epics.util.array.ListNumber;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.SimpleValueFormat;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VEnum;
import org.epics.vtype.VImage;
import org.epics.vtype.VImageDataType;
import org.epics.vtype.VImageType;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFormat;

/**
 * Various utility methods for runtime handling of the types defined in this
 * package.
 *
 * @author carcassi
 */
public class ValueUtil {

    private ValueUtil() {
        // Can't instantiate
    }

    /**
     * Extracts a numeric value for the object. If it's a numeric scalar,
     * the value is returned. If it's a numeric array, the first element is
     * returned. If it's a numeric multi array, the value of the first
     * element is returned.
     *
     * @param obj an object implementing a standard type
     * @return the numeric value
     */
    public static Double numericValueOf(Object obj) {
        if (obj instanceof VNumber) {
            Number value = ((VNumber) obj).getValue();
            if (value != null) {
                return value.doubleValue();
            }
        }

        if (obj instanceof VBoolean) {
            return (double) (((VBoolean) obj).getValue() ? 1 : 0);
        }

        if (obj instanceof VEnum) {
            return (double) ((VEnum) obj).getIndex();
        }

        if (obj instanceof VNumberArray) {
            ListNumber data = ((VNumberArray) obj).getData();
            if (data != null && data.size() != 0) {
                return data.getDouble(0);
            }
        }

//        if (obj instanceof VEnumArray) {
//            ListNumber data = ((VEnumArray) obj).getIndexes();
//            if (data != null && data.size() != 0) {
//                return data.getDouble(0);
//            }
//        }

        return null;
    }


    /**
     * Extracts the values of a column, making sure it contains
     * numeric values.
     *
     * @param table a table
     * @param columnName the name of the column to extract
     * @return the values; null if the columnName is null or is not found
     * @throws IllegalArgumentException if the column is found but does not contain numeric values
     */
    public static ListNumber numericColumnOf(VTable table, String columnName) {
        if (columnName == null) {
            return null;
        }

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (columnName.equals(table.getColumnName(i))) {
                if (table.getColumnType(i).isPrimitive()) {
                    return (ListNumber) table.getColumnData(i);
                } else {
                    throw new IllegalArgumentException("Column '" + columnName + "' is not numeric (contains "
                            + table.getColumnType(i).getSimpleName() + ")");
                }
            }
        }

        throw new IllegalArgumentException("Column '" + columnName +"' was not found");
    }

    /**
     * Returns the alarm with highest severity. null values can either be
     * ignored or treated as UNDEFINED severity.
     *
     * @param args a list of values
     * @param considerNull whether to consider null values
     * @return the highest alarm; can't be null
     */
    public static Alarm highestSeverityOf(final List<Object> args, final boolean considerNull) {
        Alarm finalAlarm = Alarm.none();
        for (Object object : args) {
            Alarm newAlarm;
            if (object == null && considerNull) {
                newAlarm = Alarm.of(AlarmSeverity.UNDEFINED, null, "No Value");
            } else {
                newAlarm = Alarm.alarmOf(object);
                if (newAlarm == null) {
                    newAlarm = Alarm.none();
                }
            }
            if (newAlarm.getSeverity().compareTo(finalAlarm.getSeverity()) > 0) {
                finalAlarm = newAlarm;
            }
        }

        return finalAlarm;
    }

    /**
     * Returns the time with latest timestamp.
     *
     * @param args
     *            a list of values
     * @return the latest time; can be null
     */
    public static Time latestTimeOf(final List<Object> args) {
        Time finalTime = null;
        for (Object object : args) {
            Time newTime;
            if (object != null) {
                newTime = Time.timeOf(object);
                if (newTime != null
                        && (finalTime == null || newTime.getTimestamp().compareTo(finalTime.getTimestamp()) > 0)) {
                    finalTime = newTime;
                }
            }
        }

        return finalTime;
    }

    /**
     * Returns the time with latest valid timestamp or now.
     *
     * @param args a list of values
     * @return the latest time; can't be null
     */
    public static Time latestValidTimeOrNowOf(final List<Object> args) {
        Time finalTime = null;
        for (Object object : args) {
            Time newTime;
            if (object != null) {
                newTime = Time.timeOf(object);
                if (newTime != null && newTime.isValid()
                        && (finalTime == null || newTime.getTimestamp().compareTo(finalTime.getTimestamp()) > 0)) {
                    finalTime = newTime;
                }
            }
        }

        if (finalTime == null) {
            finalTime = Time.now();
        }

        return finalTime;
    }

    /**
     * Normalizes the given value according to the given range;
     *
     * @param value a value
     * @param lowValue the lowest value in the range
     * @param highValue the highest value in the range
     * @return the normalized value, or null if any value is null
     */
    public static Double normalize(Number value, Number lowValue, Number highValue) {
        if (value == null || lowValue == null || highValue == null) {
            return null;
        }

        return (value.doubleValue() - lowValue.doubleValue()) / (highValue.doubleValue() - lowValue.doubleValue());
    }

    /**
     * Extracts the numericValueOf the object and normalizes according
     * to the display range.
     *
     * @param obj an object implementing a standard type
     * @return the value normalized in its display range, or null
     *         if no value or display information is present
     */
    public static Double normalizedNumericValueOf(Object obj) {
        return Display.displayOf(obj).getDisplayRange().normalize(numericValueOf(obj));
    }

    
    /**
     * Converts a VImage to an AWT BufferedImage, so that it can be displayed.
     * The content of the vImage buffer is copied, so further changes to the
     * VImage will not modify the BufferedImage.
     * 
     * @param vImage
     *            the image to be converted
     * @return a new BufferedImage
     */
    public static BufferedImage toImage(VImage vImage) {
        if (vImage.getVImageType() == VImageType.TYPE_3BYTE_BGR) {
            BufferedImage image = new BufferedImage(vImage.getWidth(), vImage.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);
            ListNumber data = vImage.getData();
            for (int i = 0; i < data.size(); i++) {
                ((DataBufferByte) image.getRaster().getDataBuffer()).getData()[i] = data.getByte(i);
            }
            return image;
        } else {
            throw new UnsupportedOperationException(
                    "No support for creating a BufferedImage from Image Type: " + vImage.getVImageType());
        }
    }

    /**
     * Converts an AWT BufferedImage to a VImage.
     * <p>
     * Currently, only TYPE_3BYTE_BGR is supported
     *
     * @param image buffered image
     * @return a new image
     */
    public static VImage toVImage(BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);
            newImage.getGraphics().drawImage(image, 0, 0, null);
            image = newImage;
        }

        byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        return VImage.of(image.getHeight(), image.getWidth(), CollectionNumbers.toListByte(buffer),
                VImageDataType.pvByte, VImageType.TYPE_3BYTE_BGR, Alarm.none(), Time.now());
    }

    private static volatile ValueFormat defaultValueFormat = new SimpleValueFormat(3);

    /**
     * The default format for VTypes.
     *
     * @return the default format
     */
    public static ValueFormat getDefaultValueFormat() {
        return defaultValueFormat;
    }

    /**
     * Changes the default format for VTypes.
     *
     * @param defaultValueFormat the new default format
     */
    public static void setDefaultValueFormat(ValueFormat defaultValueFormat) {
        ValueUtil.defaultValueFormat = defaultValueFormat;
    }

    /**
     * Checks whether the display limits are non-null and non-NaN.
     *
     * @param display a display
     * @return true if the display limits have actual values
     */
    public static boolean displayHasValidDisplayLimits(Display display) {
        Double min = display.getDisplayRange().getMinimum();
        Double max = display.getDisplayRange().getMaximum();
        if (min == null || min.isNaN()) {
            return false;
        }
        if (max == null || max.isNaN()) {
            return false;
        }
        return true;
    }

    /**
     * Extracts the values of a column, making sure it contains
     * strings.
     *
     * @param table a table
     * @param columnName the name of the column to extract
     * @return the values; null if the columnName is null or is not found
     * @throws IllegalArgumentException if the column is found but does not contain string values
     */
    public static List<String> stringColumnOf(VTable table, String columnName) {
        if (columnName == null) {
            return null;
        }

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (columnName.equals(table.getColumnName(i))) {
                if (table.getColumnType(i).equals(String.class)) {
                    @SuppressWarnings("unchecked")
                    List<String> result = (List<String>) table.getColumnData(i);
                    return result;
                } else {
                    throw new IllegalArgumentException("Column '" + columnName +"' is not string (contains " + table.getColumnType(i).getSimpleName() + ")");
                }
            }
        }

        throw new IllegalArgumentException("Column '" + columnName +"' was not found");
    }
}
