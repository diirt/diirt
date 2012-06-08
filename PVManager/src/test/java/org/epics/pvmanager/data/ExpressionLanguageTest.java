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
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {
    
    // Testing channel expressions

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
    public void vDouble1() {
        ChannelExpression<VDouble, Double> myPv = vDouble("my pv");
        assertThat(myPv.getFunction(), instanceOf(ValueCache.class));
        ValueCache<VDouble> cache = (ValueCache<VDouble>) myPv.getFunction();
        assertThat(cache.getType(), equalTo(VDouble.class));
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
