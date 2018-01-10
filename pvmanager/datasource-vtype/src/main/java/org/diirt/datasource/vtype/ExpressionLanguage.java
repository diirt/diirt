/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.VStatistics;
import org.diirt.vtype.Time;
import org.diirt.vtype.VMultiDouble;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFormat;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VShortArray;
import org.diirt.vtype.ValueUtil;
import org.diirt.vtype.VType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.diirt.datasource.ExpressionLanguage.*;

import org.diirt.datasource.LatestValueCollector;
import org.diirt.datasource.ReadFunction;
import org.diirt.datasource.WriteFunction;

import static org.diirt.vtype.ValueFactory.*;

import org.diirt.datasource.expression.ChannelExpression;
import org.diirt.datasource.expression.ChannelExpressionList;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpressionList;
import org.diirt.datasource.expression.DesiredRateExpressionListImpl;
import org.diirt.datasource.expression.DesiredRateReadWriteExpression;
import org.diirt.datasource.expression.DesiredRateReadWriteExpressionImpl;
import org.diirt.datasource.expression.Expressions;
import org.diirt.datasource.expression.SourceRateExpression;
import org.diirt.datasource.expression.SourceRateExpressionList;
import org.diirt.datasource.expression.WriteExpression;
import org.diirt.datasource.expression.WriteExpressionImpl;
import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayShort;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListNumber;
import org.diirt.util.time.TimeDuration;

/**
 * PVManager expression language support for value types.
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for value types.
        DataTypeSupport.install();
    }

    //
    // Channel expressions
    //

    /**
     * A channel with the given name that returns any of the value types.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VType, Object> vType(String name) {
        return channel(name, VType.class, Object.class);
    }

    /**
     * A list of channels with the given names that return any of the value types.
     *
     * @param names the channel names; can't be null
     * @return a list of expressions representing the channels
     */
    public static ChannelExpressionList<VType, Object> vTypes(Collection<String> names) {
        return channels(names, VType.class, Object.class);
    }

    /**
     * A channel with the given name of type VNumber.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VNumber, Number> vNumber(String name) {
        return channel(name, VNumber.class, Number.class);
    }

    /**
     * A list of channels with the given names, all of type VNumber.
     *
     * @param names the channel names; can't be null
     * @return a list of expressions representing the channels
     */
    public static ChannelExpressionList<VNumber, Number> vNumbers(Collection<String> names) {
        return channels(names, VNumber.class, Number.class);
    }

    /**
     * A channel with the given name of type VDouble.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VDouble, Double> vDouble(String name) {
        return channel(name, VDouble.class, Double.class);
    }

    /**
     * A channel with the given name of type VFloat.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VFloat, Float> vFloat(String name) {
        return channel(name, VFloat.class, Float.class);
    }

    /**
     * A channel with the given name of type VInt.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VInt, Integer> vInt(String name) {
        return channel(name, VInt.class, Integer.class);
    }

    /**
     * A channel with the given name of type VShort.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VShort, Short> vShort(String name) {
        return channel(name, VShort.class, Short.class);
    }

    /**
     * A channel with the given name of type VByte.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VByte, Byte> vByte(String name) {
        return channel(name, VByte.class, Byte.class);
    }

    /**
     * A channel with the given name of type VNumberArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VNumberArray, ListNumber> vNumberArray(String name) {
        return channel(name, VNumberArray.class, ListNumber.class);
    }

    /**
     * Transforms a list of numeric scalar into an array.
     *
     * @param expressions a list of numeric expressions
     * @return a new numeric array expression
     */
    public static DesiredRateExpression<VNumberArray>
            vNumberArrayOf(DesiredRateExpressionList<? extends VNumber> expressions) {
        // TODO - there should be a common function to extract the list of functions
        List<ReadFunction<? extends VNumber>> functions = new ArrayList<ReadFunction<? extends VNumber>>();
        for (DesiredRateExpression<? extends VNumber> expression : expressions.getDesiredRateExpressions()) {
            functions.add(expression.getFunction());
        }
        VNumbersToVNumberArrayConverter converter =
                new VNumbersToVNumberArrayConverter(functions);
        return new DesiredRateExpressionImpl<VNumberArray>(expressions, converter, "numberArrayOf");
    }

    /**
     * A channel with the given name of type VFloatArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VFloatArray, ArrayFloat> vFloatArray(String name) {
        return channel(name, VFloatArray.class, ArrayFloat.class);
    }

    /**
     * A channel with the given name of type VDoubleArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VDoubleArray, ArrayDouble> vDoubleArray(String name) {
        return channel(name, VDoubleArray.class, ArrayDouble.class);
    }

    /**
     * A channel with the given name of type VByteArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VByteArray, ArrayByte> vByteArray(String name) {
        return channel(name, VByteArray.class, ArrayByte.class);
    }

    /**
     * A channel with the given name of type VShortArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VShortArray, ArrayShort> vShortArray(String name) {
        return channel(name, VShortArray.class, ArrayShort.class);
    }

    /**
     * A channel with the given name of type VIntArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VIntArray, ArrayInt> vIntArray(String name) {
        return channel(name, VIntArray.class, ArrayInt.class);
    }

    /**
     * A channel with the given name of type VString.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VString, String> vString(String name) {
        return channel(name, VString.class, String.class);
    }

    /**
     * A channel with the given name of type VStringArray.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    @SuppressWarnings("unchecked")
    public static ChannelExpression<VStringArray, List<String>> vStringArray(String name) {
        return (ChannelExpression<VStringArray, List<String>>) (ChannelExpression) channel(name, VStringArray.class, List.class);
    }

    /**
     * A channel with the given name of type VEnum.
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static ChannelExpression<VEnum, Integer> vEnum(String name) {
        return channel(name, VEnum.class, Integer.class);
    }

    /**
     * A list of channels with the given names, all of type VDouble.
     *
     * @param names the channel names; can't be null
     * @return a list of expressions representing the channels
     */
    public static ChannelExpressionList<VDouble, Double> vDoubles(Collection<String> names) {
        return channels(names, VDouble.class, Double.class);
    }

    //
    // Constant expressions
    //

    /**
     * A constant representing a string. Alarm will be none and timestamp now.
     *
     * @param value the constant value
     * @return a string expression
     */
    public static DesiredRateExpression<VString> vConst(String value) {
        return constant(newVString(value, alarmNone(), timeNow()), value);
    }

    /**
     * A constant representing a double. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param value the constant value
     * @return a double expression
     */
    public static DesiredRateExpression<VDouble> vConst(double value) {
        return constant(newVDouble(value, alarmNone(), timeNow(), displayNone()), Double.toString(value));
    }

    /**
     * A constant representing an int. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param value the constant value
     * @return an int expression
     */
    public static DesiredRateExpression<VInt> vConst(int value) {
        return constant(newVInt(value, alarmNone(), timeNow(), displayNone()), Integer.toString(value));
    }

    /**
     * A constant representing a double array. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param values the constant values
     * @return a double array expression
     */
    public static DesiredRateExpression<VDoubleArray> vConst(double... values) {
        return constant(newVDoubleArray(new ArrayDouble(values), alarmNone(), timeNow(), displayNone()));
    }

    /**
     * A constant representing a double array. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param values the constant values
     * @return a double array expression
     */
    public static DesiredRateExpression<VDoubleArray> vConst(ListDouble values) {
        return constant(newVDoubleArray(values, alarmNone(), timeNow(), displayNone()));
    }

    /**
     * A constant representing an int array. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param values the constant values
     * @return an int array expression
     */
    public static DesiredRateExpression<VIntArray> vConst(int... values) {
        return constant(newVIntArray(new ArrayInt(values), alarmNone(), timeNow(), displayNone()));
    }

    /**
     * A constant representing an int array. Alarm will be none, timestamp now
     * and no display information.
     *
     * @param values the constant values
     * @return an int array expression
     */
    public static DesiredRateExpression<VIntArray> vConst(ListInt values) {
        return constant(newVIntArray(values, alarmNone(), timeNow(), displayNone()));
    }

    /**
     * A list of constant expressions of type VDouble.
     *
     * @param values the list of constants
     * @return a list of double expression
     */
    public static DesiredRateExpressionList<VDouble> vDoubleConstants(List<Double> values) {
        DesiredRateExpressionList<VDouble> list = new DesiredRateExpressionListImpl<VDouble>();
        for (Double value : values) {
            list.and(constant(newVDouble(value, alarmNone(), timeNow(), displayNone())));
        }
        return list;
    }

    /**
     * A list of constant expressions of type VDouble.
     *
     * @param values the list of constants
     * @return a list of int expression
     */
    public static DesiredRateExpressionList<VInt> vIntConstants(List<Integer> values) {
        DesiredRateExpressionList<VInt> list = new DesiredRateExpressionListImpl<VInt>();
        for (Integer value : values) {
            list.and(constant(newVInt(value, alarmNone(), timeNow(), displayNone())));
        }
        return list;
    }

    /**
     * A list of constant expressions of type VString.
     *
     * @param values the list of constants
     * @return a list of string expression
     */
    public static DesiredRateExpressionList<VString> vStringConstants(List<String> values) {
        DesiredRateExpressionList<VString> list = new DesiredRateExpressionListImpl<VString>();
        for (String value : values) {
            list.and(constant(newVString(value, alarmNone(), timeNow())));
        }
        return list;
    }

    /**
     * An expression that formats the given expression to a string using the
     * given format.
     *
     * @param expression the expression to format
     * @param valueFormat the format to use for the conversion
     * @return an expression with the string representation of the argument
     */
    public static DesiredRateExpression<VString> vStringOf(DesiredRateExpression<? extends VType> expression, ValueFormat valueFormat) {
        return new DesiredRateExpressionImpl<>(expression, new VStringOfFunction(expression.getFunction(), valueFormat), expression.getName());
    }

    /**
     * An expression that formats the given expression to a string using the
     * default format.
     *
     * @param expression the expression to format
     * @return an expression with the string representation of the argument
     */
    public static DesiredRateExpression<VString> vStringOf(DesiredRateExpression<? extends VType> expression) {
        return new DesiredRateExpressionImpl<>(expression, new VStringOfFunction(expression.getFunction(), ValueUtil.getDefaultValueFormat()), expression.getName());
    }

    /**
     * An expression that formats the given expression to a string using the
     * default format.
     *
     * @param expression the expression to format
     * @return an expression with the string representation of the argument
     */
    public static DesiredRateReadWriteExpression<VString, String> vStringOf(DesiredRateReadWriteExpression<? extends VType, ? extends Object> expression) {
        return vStringOf(expression, ValueUtil.getDefaultValueFormat());
    }

    /**
     * An expression that formats the given expression to a string using the
     * given format.
     *
     * @param expression the expression to format
     * @param valueFormat the format to use for the conversion
     * @return an expression with the string representation of the argument
     */
    public static DesiredRateReadWriteExpression<VString, String> vStringOf(DesiredRateReadWriteExpression<? extends VType, ? extends Object> expression,
            ValueFormat valueFormat) {
        LatestValueCollector<VType> forward = new LatestValueCollector<>();
        DesiredRateExpression<VString> readExp = new DesiredRateExpressionImpl<>(expression,
                new VStringOfFunction(expression.getFunction(), valueFormat, forward)
                , expression.getName());
        @SuppressWarnings("unchecked")
        WriteFunction<Object> writeFunction = (WriteFunction<Object>) (WriteFunction) expression.getWriteFunction();
        WriteExpression<String> writeExp = new WriteExpressionImpl<>(expression,
                new VStringOfWriteFunction(forward, valueFormat, writeFunction), expression.getName());
        return new DesiredRateReadWriteExpressionImpl<>(readExp, writeExp);
    }

    /**
     * Aggregates the sample at the scan rate and takes the average.
     *
     * @param doublePv the expression to take the average of; can't be null
     * @return an expression representing the average of the expression
     */
    public static DesiredRateExpression<VDouble> averageOf(SourceRateExpression<VDouble> doublePv) {
        DesiredRateExpression<List<VDouble>> queue = newValuesOf(doublePv);
        return new DesiredRateExpressionImpl<VDouble>(queue,
                new AverageAggregator(queue.getFunction()), "avg(" + doublePv.getName() + ")");
    }

    /**
     * Aggregates the sample at the scan rate and calculates statistical information.
     *
     * @param doublePv the expression to calculate the statistics information on; can't be null
     * @return an expression representing the statistical information of the expression
     */
    public static DesiredRateExpression<VStatistics> statisticsOf(SourceRateExpression<VDouble> doublePv) {
        DesiredRateExpression<List<VDouble>> queue = newValuesOf(doublePv);
        return new DesiredRateExpressionImpl<VStatistics>(queue,
                new StatisticsDoubleAggregator(queue.getFunction()), "stats(" + doublePv.getName() + ")");
    }

    /**
     * Applies {@link #statisticsOf(org.diirt.datasource.expression.SourceRateExpression) } to all
     * arguments.
     *
     * @param doubleExpressions a list of double expressions
     * @return a list of statistical expressions
     */
    public static DesiredRateExpressionList<VStatistics> statisticsOf(SourceRateExpressionList<VDouble> doubleExpressions) {
        DesiredRateExpressionList<VStatistics> expressions = new DesiredRateExpressionListImpl<VStatistics>();
        for (SourceRateExpression<VDouble> doubleExpression : doubleExpressions.getSourceRateExpressions()) {
            expressions.and(statisticsOf(doubleExpression));
        }
        return expressions;
    }

    /**
     * A synchronized array from the given expression.
     *
     * @param tolerance maximum time difference between samples
     * @param expressions the expressions from which to reconstruct the array
     * @return an expression for the array
     */
    public static DesiredRateExpression<VMultiDouble>
            synchronizedArrayOf(Duration tolerance, SourceRateExpressionList<VDouble> expressions) {
        return synchronizedArrayOf(tolerance, tolerance.multipliedBy(10), expressions);
    }

    /**
     * A synchronized array from the given expression.
     *
     * @param tolerance maximum time difference between samples in the
     * reconstructed array
     * @param cacheDepth maximum time difference between samples in the caches
     * used to reconstruct the array
     * @param expressions the expressions from which to reconstruct the array
     * @return an expression for the array
     */
    public static DesiredRateExpression<VMultiDouble>
            synchronizedArrayOf(Duration tolerance, Duration cacheDepth, SourceRateExpressionList<VDouble> expressions) {
        if (cacheDepth.equals(Duration.ofMillis(0)) && TimeDuration.toSecondsDouble(cacheDepth) > 0)
            throw new IllegalArgumentException("Distance between samples must be non-zero and positive");
        List<String> names = new ArrayList<String>();
        List<ReadFunction<List<VDouble>>> collectors = new ArrayList<ReadFunction<List<VDouble>>>();
        DesiredRateExpressionList<List<VDouble>> desiredRateExpressions = new DesiredRateExpressionListImpl<List<VDouble>>();
        for (SourceRateExpression<VDouble> expression : expressions.getSourceRateExpressions()) {
            DesiredRateExpression<List<VDouble>> collectorExp = timedCacheOf(expression, cacheDepth);
            desiredRateExpressions.and(collectorExp);
            collectors.add(collectorExp.getFunction());
            names.add(expression.getName());
        }
        SynchronizedVDoubleAggregator aggregator =
                new SynchronizedVDoubleAggregator(names, collectors, tolerance);
        return new DesiredRateExpressionImpl<VMultiDouble>(desiredRateExpressions,
                (ReadFunction<VMultiDouble>) aggregator, "syncArray");
    }

    /**
     * Returns all the values starting the latest value and older up to
     * the time difference given by the interval.
     *
     * @param <T> type being read
     * @param expression expression to read
     * @param maxIntervalBetweenSamples maximum time difference between values
     * @return a new expression
     */
    public static <T extends Time> DesiredRateExpression<List<T>>
            timedCacheOf(SourceRateExpression<T> expression, Duration maxIntervalBetweenSamples) {
        return new DesiredRateExpressionImpl<List<T>>(expression,
                new TimedCacheCollector<T>(expression.getFunction(), maxIntervalBetweenSamples),
                expression.getName());
    }

    /**
     * A column for an aggregated vTable.
     *
     * @param name the name of the column
     * @param values the value of the column
     * @return the column
     */
    public static VTableColumn column(String name, DesiredRateExpressionList<?> values) {
        return new VTableColumn(name, values);
    }

    /**
     * Creates a vTable by aggregating different values from different pvs.
     *
     * @param columns columns of the table
     * @return an expression for the table
     */
    public static DesiredRateExpression<VTable> vTable(VTableColumn... columns) {
        DesiredRateExpressionListImpl<Object> list = new DesiredRateExpressionListImpl<Object>();
        List<List<ReadFunction<?>>> functions = new ArrayList<List<ReadFunction<?>>>();
        List<String> names = new ArrayList<String>();
        for (VTableColumn column : columns) {
            functions.add(Expressions.functionsOf(column.getValueExpressions()));
            list.and(column.getValueExpressions());
            names.add(column.getName());
        }
        return new DesiredRateExpressionImpl<VTable>(list, new VTableAggregationFunction(functions, names), "table");
    }
}
