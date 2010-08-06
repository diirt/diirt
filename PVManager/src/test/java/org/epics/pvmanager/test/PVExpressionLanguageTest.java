/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.test;

import org.epics.pvmanager.AggregatedExpression;
import org.epics.pvmanager.Expression;
import org.epics.pvmanager.types.DoubleStatistics;
import org.junit.Test;
import static org.epics.pvmanager.types.ExpressionLanguage.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class PVExpressionLanguageTest {

    @Test
    public void expressions() {
        Expression<Double> myPv = doublePv("my pv");
        assertThat(myPv.getDefaultName(), equalTo("my pv"));
        AggregatedExpression<Double> avgOfMyPV = averageOf(doublePv("my pv"));
        assertThat(avgOfMyPV.getDefaultName(), equalTo("avg(my pv)"));
        AggregatedExpression<DoubleStatistics> statsOfMyPV = statisticsOf(doublePv("my pv"));
        assertThat(statsOfMyPV.getDefaultName(), equalTo("stats(my pv)"));
    }

}
