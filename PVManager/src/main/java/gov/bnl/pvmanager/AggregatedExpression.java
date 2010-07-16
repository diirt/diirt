/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager;

import java.util.List;

/**
 * An expression that reprents a PV that is read at the UI scan rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link PVExpressionLanguage}.
 *
 * @author carcassi
 */
public class AggregatedExpression<T> {

    private List<MonitorRecipe> recipes;
    private Class<T> outputType;
    private Function<T> function;
    private final String defaultName;

    public AggregatedExpression(List<MonitorRecipe> recipes, Class<T> outputType, Function<T> function, String defaultName) {
        this.recipes = recipes;
        this.outputType = outputType;
        this.function = function;
        this.defaultName = defaultName;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public List<MonitorRecipe> getMonitorRecipes() {
        return recipes;
    }

    public Class<T> getOutputType() {
        return outputType;
    }

    public Function<T> getFunction() {
        return function;
    }
}
