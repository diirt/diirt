/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.epics.pvmanager.data.ValueFactory.*;

/**
 * Formats a data type to a String representation. This class provide default
 * implementations that can format scalars and arrays to an arbitrary
 * precision and a maximum number of array elements.
 *
 * @author carcassi
 */
public abstract class ValueFormat extends Format {

    // Number format to be used to format primitive values
    private NumberFormat numberFormat;

    /**
     * Formats the given data object. For scalars and arrays redirects
     * to the appropriate methods. For anything else uses Object.toString().
     *
     * @param data data object to format
     * @return a String representation
     */
    @Override
    public StringBuffer format(Object data, StringBuffer toAppendTo, FieldPosition pos) {
        if (data == null)
            return toAppendTo;

        if (data instanceof Scalar)
            return format((Scalar) data, toAppendTo, pos);

        if (data instanceof Array)
            return format((Array) data, toAppendTo, pos);

        return toAppendTo.append(data);
    }

    /**
     * Formats an scalar.
     *
     * @param scalar data object to format
     * @return a String representation
     */
    public String format(Scalar scalar) {
        return format((Object) scalar);
    }

    /**
     * Formats an array.
     *
     * @param array data object to format
     * @return a String representation
     */
    public String format(Array array) {
        return format((Object) array);
    }

    /**
     * Formats a scalar.
     *
     * @param scalar data object to format
     * @param toAppendTo output buffer
     * @param pos the field position
     * @return the output buffer
     */
    protected abstract StringBuffer format(Scalar scalar, StringBuffer toAppendTo, FieldPosition pos);

    /**
     * Formats an array.
     *
     * @param array data object to format
     * @param toAppendTo output buffer
     * @param pos the field position
     * @return the output buffer
     */
    protected abstract StringBuffer format(Array array, StringBuffer toAppendTo, FieldPosition pos);

    /**
     * Returns the NumberFormat used to format the numeric values.
     * If null, it will use the NumberFormat from the value Display.
     *
     * @return a NumberFormat
     */
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /**
     * Changes the NumberFormat used to format the numeric values.
     * If null, it will use the NumberFormat from the value Display.
     *
     * @param numberFormat a NumberFormat
     */
    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object parseObject(String source, Object reference) {
        if (reference instanceof VDouble) {
            return parseDouble(source);
        }
        if (reference instanceof VFloat) {
            return parseFloat(source);
        }
        if (reference instanceof VInt) {
            return parseInt(source);
        }
        if (reference instanceof VShort) {
            return parseShort(source);
        }
        
        throw new IllegalArgumentException("Type " + ValueUtil.typeOf(reference) + " is not supported");
    }
    
    public double parseDouble(String source) {
        try {
            double value = Double.parseDouble(source);
            return value;
        } catch (NumberFormatException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public float parseFloat(String source) {
        try {
            float value = Float.parseFloat(source);
            return value;
        } catch (NumberFormatException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public int parseInt(String source) {
        try {
            int value = Integer.parseInt(source);
            return value;
        } catch (NumberFormatException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
    public short parseShort(String source) {
        try {
            short value = Short.parseShort(source);
            return value;
        } catch (NumberFormatException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
