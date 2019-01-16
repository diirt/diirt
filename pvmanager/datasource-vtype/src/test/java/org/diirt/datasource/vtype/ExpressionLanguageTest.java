/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VType;
import org.epics.vtype.VInt;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VEnum;
import org.epics.vtype.VStatistics;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInteger;
import org.epics.util.array.ListNumber;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VString;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.ValueCache;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.junit.Test;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.WriteExpressionTester;
import org.diirt.datasource.expression.ChannelExpression;
import org.diirt.datasource.expression.DesiredRateReadWriteExpression;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    //
    // Testing channel expressions
    //

    @Test
    public void vType1() {
        ChannelExpression<VType, Object> myPv = vType("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VType> cache = (ValueCache<VType>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VType.class));
        assertThat(cache.readValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vNumber1() {
        ChannelExpression<VNumber, Number> myPv = vNumber("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VNumber> cache = (ValueCache<VNumber>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VNumber.class));
        assertThat(cache.readValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vDouble1() {
        ChannelExpression<VDouble, Double> myPv = vDouble("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDouble> cache = (ValueCache<VDouble>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VDouble.class));
        assertThat(cache.readValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vInt1() {
        ChannelExpression<VInt, Integer> myPv = vInt("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VInt> cache = (ValueCache<VInt>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VInt.class));
        assertThat(cache.readValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vNumberArray1() {
        ChannelExpression<VNumberArray, ListNumber> myPv = vNumberArray("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VNumberArray> cache = (ValueCache<VNumberArray>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VNumberArray.class));
        assertThat(cache.readValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    //
    // Testing constant expressions
    //

    @Test
    public void vDoubleConstant1() {
        DesiredRateExpression<VDouble> exp = vConst(3.14);
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        assertThat(exp.getName(), equalTo("3.14"));
        ValueCache<VDouble> cache = (ValueCache<VDouble>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VDouble.class));
        assertThat(cache.readValue().getValue(), equalTo(3.14));
    }

    @Test
    public void vIntConstant1() {
        DesiredRateExpression<VInt> exp = vConst(314);
        assertThat(exp.getName(), equalTo("314"));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VInt> cache = (ValueCache<VInt>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VInt.class));
        assertThat(cache.readValue().getValue(), equalTo(314));
    }

    @Test
    public void vDoubleArrayConstant1() {
        DesiredRateExpression<VDoubleArray> exp = vConst(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDoubleArray> cache = (ValueCache<VDoubleArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VDoubleArray.class));
        ListDouble reference = ArrayDouble.of(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vDoubleArrayConstant2() {
        DesiredRateExpression<VDoubleArray> exp = vConst(ArrayDouble.of(0.0, 0.1, 0.2, 0.3, 0.4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDoubleArray> cache = (ValueCache<VDoubleArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VDoubleArray.class));
        ListDouble reference = ArrayDouble.of(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vIntArrayConstant1() {
        DesiredRateExpression<VIntArray> exp = vConst(new int[] {0, 1, 2, 3, 4});
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VIntArray.class));
        ListInteger reference = ArrayInteger.of(0, 1, 2, 3, 4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vIntArrayConstant2() {
        DesiredRateExpression<VIntArray> exp = vConst(ArrayInteger.of(0, 1, 2, 3, 4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VIntArray.class));
        ListInteger reference = ArrayInteger.of(0, 1, 2, 3, 4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vAverageOf1() {
        DesiredRateExpression<VDouble> avgOfMyPV = averageOf(vDouble("my pv"));
        assertThat(avgOfMyPV.getName(), equalTo("avg(my pv)"));
    }

    @Test
    public void statisticsOf1() {
        DesiredRateExpression<VStatistics> statsOfMyPV = statisticsOf(vDouble("my pv"));
        assertThat(statsOfMyPV.getName(), equalTo("stats(my pv)"));
    }

    @Test
    public void vStringOf1() {
        // TODO shroffk fix the NumberFormatter to format Double 3.0 as "3.0"
        ReadExpressionTester exp = new ReadExpressionTester(vStringOf(latestValueOf(vType("pv"))));
        exp.writeValue("pv", VDouble.of(3.1, Alarm.none(), Time.now(), Display.none()));
        String string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "3.1"));

        exp.writeValue("pv", VInt.of(5, Alarm.none(), Time.now(), Display.none()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "5"));

        exp.writeValue("pv", VEnum.of(2, EnumDisplay.of(Arrays.asList("A", "B", "C", "D", "E")), Alarm.none(), Time.now()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "C"));

        exp.writeValue("pv", VString.of("Test", Alarm.none(), Time.now()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "Test"));
    }

    @Test
    public void vStringOf2() {
        DesiredRateReadWriteExpression<VString, String> vStringOf = vStringOf(latestValueOf(vType("pv")));
        ReadExpressionTester readExp = new ReadExpressionTester(vStringOf);
        WriteExpressionTester writeExp = new WriteExpressionTester(vStringOf);

        // Read a VDouble
        // TODO shroffk fix the NumberFormatter to format Double 3.0 as "3.0"
        readExp.writeValue("pv", VDouble.of(3.1, Alarm.none(), Time.now(), Display.none()));
        String string = ((VString) readExp.getValue()).getValue();
        assertThat(string, equalTo((Object) "3.1"));

        // Write a string, and get a double
        writeExp.setValue("3.14");
        double value = (double) writeExp.readValue("pv");
        assertThat(value, equalTo(3.14));

        // Read a VDoubleArray
        readExp.writeValue("pv", VEnum.of(1, EnumDisplay.of(Arrays.asList("ONE", "TWO", "THREE")), Alarm.none(), Time.now()));
        string = ((VString) readExp.getValue()).getValue();
        assertThat(string, equalTo((Object) "TWO"));

        // Write a string, and get a double
        writeExp.setValue("THREE");
        int intValue = (int) writeExp.readValue("pv");
        assertThat(intValue, equalTo(2));
//
//        // Read a VDoubleArray
//        readExp.writeValue("pv", VDouble.ofArray(new double[] {1.0, 2.0, 3.0}, Display.none()));
//        string = ((VString) readExp.getValue()).getValue();
//        assertThat(string, equalTo((Object) "[1.0, 2.0, 3.0]"));
//
//        // Write a string, and get a double
//        writeExp.setValue("3.0, 2.0, 1.0");
//        double[] values = (double[]) writeExp.readValue("pv");
//        assertThat(values, equalTo(new double[] {3.0, 2.0, 1.0}));
    }
}
