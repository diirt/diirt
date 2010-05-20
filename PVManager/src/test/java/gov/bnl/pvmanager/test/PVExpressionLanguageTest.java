/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.test;

import gov.bnl.pvmanager.AggregatedExpression;
import gov.bnl.pvmanager.Expression;
import gov.bnl.pvmanager.types.DoubleStatistics;
import org.junit.Test;
import static gov.bnl.pvmanager.types.ExpressionLanguage.*;
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
