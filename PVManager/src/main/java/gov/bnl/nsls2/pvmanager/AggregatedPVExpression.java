/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class AggregatedPVExpression<T extends PVType<T>> {

    private ConnectionRecipe recipe;
    private Class<T> outputType;
    private Aggregator<T> aggregator;

    AggregatedPVExpression(ConnectionRecipe recipe, Class<T> outputType, Aggregator<T> aggregator) {
        this.recipe = recipe;
        this.outputType = outputType;
        this.aggregator = aggregator;
    }

    ConnectionRecipe getRecipe() {
        return recipe;
    }

    Class<T> getOutputType() {
        return outputType;
    }

    Aggregator<T> getAggregator() {
        return aggregator;
    }
}
