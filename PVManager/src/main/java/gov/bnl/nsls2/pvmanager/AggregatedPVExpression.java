/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.List;

/**
 * An expression that reprents a PV that is read at the UI scan rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link PVExpressionLanguage}.
 *
 * @author carcassi
 */
public class AggregatedPVExpression<T> {

    private List<MonitorRecipe> recipes;
    private Class<T> outputType;
    private PVFunction<T> function;
    private final String defaultName;

    AggregatedPVExpression(List<MonitorRecipe> recipes, Class<T> outputType, Aggregator<T, ?> aggregator, String defaultName) {
        this.recipes = recipes;
        this.outputType = outputType;
        this.function = aggregator;
        this.defaultName = defaultName;
    }

    String getDefaultName() {
        return defaultName;
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
