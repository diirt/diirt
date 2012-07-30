/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.epics.pvmanager.Function;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.util.array.*;
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
        assertThat(cache.getValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vNumber1() {
        ChannelExpression<VNumber, Number> myPv = vNumber("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VNumber> cache = (ValueCache<VNumber>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VNumber.class));
        assertThat(cache.getValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vDouble1() {
        ChannelExpression<VDouble, Double> myPv = vDouble("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDouble> cache = (ValueCache<VDouble>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VDouble.class));
        assertThat(cache.getValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vInt1() {
        ChannelExpression<VInt, Integer> myPv = vInt("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VInt> cache = (ValueCache<VInt>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VInt.class));
        assertThat(cache.getValue(), nullValue());
        assertThat(myPv.getName(), equalTo("my pv"));
    }

    @Test
    public void vNumberArray1() {
        ChannelExpression<VNumberArray, ListNumber> myPv = vNumberArray("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VNumberArray> cache = (ValueCache<VNumberArray>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VNumberArray.class));
        assertThat(cache.getValue(), nullValue());
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
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VDouble.class));
        assertThat(cache.getValue().getValue(), equalTo(3.14));
    }
    
    @Test
    public void vIntConstant1() {
        DesiredRateExpression<VInt> exp = vConst(314);
        assertThat(exp.getName(), equalTo("314"));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VInt> cache = (ValueCache<VInt>) exp.getFunction();
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VInt.class));
        assertThat(cache.getValue().getValue(), equalTo(314));
    }
    
    @Test
    public void vDoubleArrayConstant1() {
        DesiredRateExpression<VDoubleArray> exp = vConst(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDoubleArray> cache = (ValueCache<VDoubleArray>) exp.getFunction();
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VDoubleArray.class));
        ListDouble reference = new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.getValue().getData(), equalTo(reference));
    }
    
    @Test
    public void vDoubleArrayConstant2() {
        DesiredRateExpression<VDoubleArray> exp = vConst(new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDoubleArray> cache = (ValueCache<VDoubleArray>) exp.getFunction();
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VDoubleArray.class));
        ListDouble reference = new ArrayDouble(0.0, 0.1, 0.2, 0.3, 0.4);
        assertThat(cache.getValue().getData(), equalTo(reference));
    }
    
    @Test
    public void vIntArrayConstant1() {
        DesiredRateExpression<VIntArray> exp = vConst(0, 1, 2, 3, 4);
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VIntArray.class));
        ListInt reference = new ArrayInt(0, 1, 2, 3, 4);
        assertThat(cache.getValue().getData(), equalTo(reference));
    }
    
    @Test
    public void vIntArrayConstant2() {
        DesiredRateExpression<VIntArray> exp = vConst(new ArrayInt(0, 1, 2, 3, 4));
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VIntArray> cache = (ValueCache<VIntArray>) exp.getFunction();
        assertThat(cache.getValue(), not(nullValue()));
        assertThat(cache.getValue(), instanceOf(VIntArray.class));
        ListInt reference = new ArrayInt(0, 1, 2, 3, 4);
        assertThat(cache.getValue().getData(), equalTo(reference));
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
}
