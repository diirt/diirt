/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.epics.pvmanager.AggregatedExpression;
import org.epics.pvmanager.Expression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class PVExpressionLanguageTest {

    @Test
    public void expressions() {
        Expression<VDouble> myPv = vDouble("my pv");
        assertThat(myPv.getDefaultName(), equalTo("my pv"));
        AggregatedExpression<VDouble> avgOfMyPV = averageOf(vDouble("my pv"));
        assertThat(avgOfMyPV.getDefaultName(), equalTo("avg(my pv)"));
        AggregatedExpression<VStatistics> statsOfMyPV = statisticsOf(vDouble("my pv"));
        assertThat(statsOfMyPV.getDefaultName(), equalTo("stats(my pv)"));
    }

}
