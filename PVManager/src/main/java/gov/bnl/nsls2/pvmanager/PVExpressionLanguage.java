/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class PVExpressionLanguage {
    private PVExpressionLanguage() {}

    public static PVExpression<TypeDouble> doublePv(String name) {
        return new PVExpression<TypeDouble>(name, TypeDouble.class);
    }

    public static AggregatedPVExpression<TypeDouble> averageOf(PVExpression<TypeDouble> doublePv) {
        Collector collector = new Collector(doublePv.getFunction());
        return new AggregatedPVExpression<TypeDouble>(doublePv.createMontiorRecipes(collector),
                TypeDouble.class, new AverageAggregator(collector));
    }

    public static AggregatedPVExpression<TypeStatistics> statisticsOf(PVExpression<TypeDouble> doublePv) {
        Collector collector = new Collector(doublePv.getFunction());
        return new AggregatedPVExpression<TypeStatistics>(doublePv.createMontiorRecipes(collector),
                TypeStatistics.class, new StatisticsAggregator(collector));
    }

}
