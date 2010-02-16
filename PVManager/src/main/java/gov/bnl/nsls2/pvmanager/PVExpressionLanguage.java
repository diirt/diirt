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
        return new PVExpression<TypeDouble>(name);
    }

    public static AggregatedPVExpression<TypeDouble> averageOf(PVExpression<TypeDouble> doublePv) {
        MonitorRecipe<TypeDouble> recipe = new MonitorRecipe<TypeDouble>();
        recipe.cache = new ValueCache<TypeDouble>(TypeDouble.class);
        recipe.collector = new Collector(recipe.cache);
        recipe.pvName = doublePv.getName();
        return new AggregatedPVExpression<TypeDouble>(recipe, TypeDouble.class, new AverageAggregator(recipe.collector));
    }

    public static AggregatedPVExpression<TypeStatistics> statisticsOf(PVExpression<TypeDouble> doublePv) {
        MonitorRecipe<TypeDouble> recipe = new MonitorRecipe<TypeDouble>();
        recipe.cache = new ValueCache<TypeDouble>(TypeDouble.class);
        recipe.collector = new Collector(recipe.cache);
        recipe.pvName = doublePv.getName();
        return new AggregatedPVExpression<TypeStatistics>(recipe, TypeStatistics.class, new StatisticsAggregator(recipe.collector));
    }

}
