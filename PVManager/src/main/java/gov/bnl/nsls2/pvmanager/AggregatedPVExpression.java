/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.List;

/**
 *
 * @author carcassi
 */
public class AggregatedPVExpression<T extends PVType<T>> {

    private List<MonitorRecipe> recipes;
    private Class<T> outputType;
    private PVFunction<T> function;

    AggregatedPVExpression(List<MonitorRecipe> recipes, Class<T> outputType, Aggregator<T> aggregator) {
        this.recipes = recipes;
        this.outputType = outputType;
        this.function = aggregator;
    }

    List<MonitorRecipe> getMonitorRecipes() {
        return recipes;
    }

    Class<T> getOutputType() {
        return outputType;
    }

    PVFunction<T> getFunction() {
        return function;
    }
}
