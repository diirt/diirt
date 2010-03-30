/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.bnl.nsls2.pvmanager.types.DoubleStatistics;
import org.junit.Test;
import static gov.bnl.nsls2.pvmanager.PVExpressionLanguage.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class PVExpressionLanguageTest {

    @Test
    public void expressions() {
        PVExpression<Double> myPv = doublePv("my pv");
        assertThat(myPv.getDefaultName(), equalTo("my pv"));
        AggregatedPVExpression<Double> avgOfMyPV = averageOf(doublePv("my pv"));
        assertThat(avgOfMyPV.getDefaultName(), equalTo("avg(my pv)"));
        AggregatedPVExpression<DoubleStatistics> statsOfMyPV = statisticsOf(doublePv("my pv"));
        assertThat(statsOfMyPV.getDefaultName(), equalTo("stats(my pv)"));
    }

}
