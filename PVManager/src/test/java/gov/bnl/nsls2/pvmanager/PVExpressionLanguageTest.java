/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import org.junit.Test;
import static gov.bnl.nsls2.pvmanager.PVExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class PVExpressionLanguageTest {
    @Test
    public void expressions() {
        PVExpression<TypeDouble> myPv = doublePv("my pv");
        AggregatedPVExpression<TypeDouble> avgOfMyPV = averageOf(doublePv("my pv"));
        AggregatedPVExpression<TypeStatistics> statsOfMyPV = statisticsOf(doublePv("my pv"));
    }

}
