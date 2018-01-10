/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VType;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VStatistics;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VString;
import java.util.Arrays;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.ValueCache;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.junit.Test;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.vtype.ValueFactory.*;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.WriteExpressionTester;
import org.diirt.datasource.expression.ChannelExpression;
import org.diirt.datasource.expression.DesiredRateReadWriteExpression;
import org.diirt.util.array.*;
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
        ListDouble reference = new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vDoubleArrayConstant2() {
        DesiredRateExpression<VDoubleArray> exp = vConst(new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDoubleArray> cache = (ValueCache<VDoubleArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VDoubleArray.class));
        ListDouble reference = new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vIntArrayConstant1() {
        DesiredRateExpression<VIntArray> exp = vConst(new int[] {0, 1, 2, 3, 4});
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VIntArray.class));
        ListInt reference = new ArrayInt(0, 1, 2, 3, 4);
        assertThat(cache.readValue().getData(), equalTo(reference));
    }

    @Test
    public void vIntArrayConstant2() {
        DesiredRateExpression<VIntArray> exp = vConst(new ArrayInt(0, 1, 2, 3, 4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.readValue(), not(nullValue()));
        assertThat(cache.readValue(), instanceOf(VIntArray.class));
        ListInt reference = new ArrayInt(0, 1, 2, 3, 4);
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
        ReadExpressionTester exp = new ReadExpressionTester(vStringOf(latestValueOf(vType("pv"))));
        exp.writeValue("pv", newVDouble(3.0));
        String string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "3.0"));

        exp.writeValue("pv", newVInt(5, alarmNone(), timeNow(), displayNone()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "5"));

        exp.writeValue("pv", newVEnum(2, Arrays.asList("A", "B", "C", "D", "E"), alarmNone(), timeNow()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "C"));

        exp.writeValue("pv", newVString("Test", alarmNone(), timeNow()));
        string = ((VString) exp.getValue()).getValue();
        assertThat(string, equalTo((Object) "Test"));
    }

    @Test
    public void vStringOf2() {
        DesiredRateReadWriteExpression<VString, String> vStringOf = vStringOf(latestValueOf(vType("pv")));
        ReadExpressionTester readExp = new ReadExpressionTester(vStringOf);
        WriteExpressionTester writeExp = new WriteExpressionTester(vStringOf);

        // Read a VDouble
        readExp.writeValue("pv", newVDouble(3.0));
        String string = ((VString) readExp.getValue()).getValue();
        assertThat(string, equalTo((Object) "3.0"));

        // Write a string, and get a double
        writeExp.setValue("3.14");
        double value = (double) writeExp.readValue("pv");
        assertThat(value, equalTo(3.14));

        // Read a VDoubleArray
        readExp.writeValue("pv", newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow()));
        string = ((VString) readExp.getValue()).getValue();
        assertThat(string, equalTo((Object) "TWO"));

        // Write a string, and get a double
        writeExp.setValue("THREE");
        int intValue = (int) writeExp.readValue("pv");
        assertThat(intValue, equalTo(2));
//
//        // Read a VDoubleArray
//        readExp.writeValue("pv", newVDoubleArray(new double[] {1.0, 2.0, 3.0}, displayNone()));
//        string = ((VString) readExp.getValue()).getValue();
//        assertThat(string, equalTo((Object) "[1.0, 2.0, 3.0]"));
//
//        // Write a string, and get a double
//        writeExp.setValue("3.0, 2.0, 1.0");
//        double[] values = (double[]) writeExp.readValue("pv");
//        assertThat(values, equalTo(new double[] {3.0, 2.0, 1.0}));
    }
}
