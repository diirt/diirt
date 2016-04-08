/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.json;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayLong;
import org.diirt.util.array.ArrayShort;
import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListLong;
import org.diirt.util.array.ListNumber;
import org.diirt.util.array.ListShort;

/**
 * Utility classes to convert JSON arrays to and from Lists and ListNumbers.
 *
 * @author carcassi
 */
public class JsonArrays {

    /**
     * Checks whether the array contains only numbers.
     *
     * @param array a JSON array
     * @return true if all elements are JSON numbers
     */
    public static boolean isNumericArray(JsonArray array) {
        for (JsonValue value : array) {
            if (!(value instanceof JsonNumber)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the array contains only strings.
     *
     * @param array a JSON array
     * @return true if all elements are JSON strings
     */
    public static boolean isStringArray(JsonArray array) {
        for (JsonValue value : array) {
            if (!(value instanceof JsonString)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts the given numeric JSON array to a ListDouble.
     *
     * @param array an array of numbers
     * @return a new ListDouble
     */
    public static ListDouble toListDouble(JsonArray array) {
        double[] values = new double[array.size()];
        for (int i = 0; i < values.length; i++) {
            if (array.isNull(i)) {
                values[i] = Double.NaN;
            } else {
                values[i] = array.getJsonNumber(i).doubleValue();
            }
        }
        return new ArrayDouble(values);
    }

    /**
     * Converts the given numeric JSON array to a ListFloat.
     *
     * @param array an array of numbers
     * @return a new ListFloat
     */
    public static ListFloat toListFloat(JsonArray array) {
        float[] values = new float[array.size()];
        for (int i = 0; i < values.length; i++) {
            if (array.isNull(i)) {
                values[i] = Float.NaN;
            } else {
                values[i] = (float) array.getJsonNumber(i).doubleValue();
            }
        }
        return new ArrayFloat(values);
    }

    /**
     * Converts the given numeric JSON array to a ListLong.
     *
     * @param array an array of numbers
     * @return a new ListLong
     */
    public static ListLong toListLong(JsonArray array) {
        long[] values = new long[array.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (long) array.getJsonNumber(i).longValue();
        }
        return new ArrayLong(values);
    }

    /**
     * Converts the given numeric JSON array to a ListInt.
     *
     * @param array an array of numbers
     * @return a new ListInt
     */
    public static ListInt toListInt(JsonArray array) {
        int[] values = new int[array.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) array.getJsonNumber(i).intValue();
        }
        return new ArrayInt(values);
    }

    /**
     * Converts the given numeric JSON array to a ListShort.
     *
     * @param array an array of numbers
     * @return a new ListShort
     */
    public static ListShort toListShort(JsonArray array) {
        short[] values = new short[array.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (short) array.getJsonNumber(i).intValue();
        }
        return new ArrayShort(values);
    }

    /**
     * Converts the given numeric JSON array to a ListByte.
     *
     * @param array an array of numbers
     * @return a new ListByte
     */
    public static ListByte toListByte(JsonArray array) {
        byte[] values = new byte[array.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (byte) array.getJsonNumber(i).intValue();
        }
        return new ArrayByte(values);
    }

    /**
     * Converts the given string JSON array to a List of Strings.
     *
     * @param array an array of strings
     * @return a new List of Strings
     */
    public static List<String> toListString(JsonArray array) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            strings.add(array.getString(i));
        }
        return strings;
    }


    /**
     * Converts the given JSON array to a List of Instant.
     *
     * @param array an array
     * @return a new List of Instant
     */
    public static List<Instant> toListTimestamp(JsonArray array) {
        List<Instant> timestamps = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            timestamps.add(Instant.ofEpochSecond(array.getJsonNumber(i).longValue(), 0));
        }
        return timestamps;
    }

    /**
     * Converts the given List of String to a string JSON array.
     *
     * @param list a List of Strings
     * @return an array of strings
     */
    public static JsonArrayBuilder fromListString(List<String> list) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        for (String element : list) {
            // TODO: Not clear how to handle nulls. Converting them to empty strings.
            if (element == null) {
                element = "";
            }
            b.add(element);
        }
        return b;
    }

    /**
     * Converts the given List of Instant to a JSON array.
     *
     * @param list a List of Instant
     * @return an array
     */
    public static JsonArrayBuilder fromListTimestamp(List<Instant> list) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        for (Instant element : list) {
            b.add(element.getEpochSecond());
        }
        return b;
    }

    /**
     * Converts the given ListNumber to a number JSON array.
     *
     * @param list a list of numbers
     * @return an array of numbers
     */
    public static JsonArrayBuilder fromListNumber(ListNumber list) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        if (list instanceof ListByte || list instanceof ListShort || list instanceof ListInt) {
            for (int i = 0; i < list.size(); i++) {
                b.add(list.getInt(i));
            }
        } else if (list instanceof ListLong) {
            for (int i = 0; i < list.size(); i++) {
                b.add(list.getLong(i));
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                double value = list.getDouble(i);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    b.addNull();
                } else {
                    b.add(value);
                }
            }
        }
        return b;
    }

}
