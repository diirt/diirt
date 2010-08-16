/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.List;

/**
 * An expression that represents a PV that is read at the UI scan rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link PVExpressionLanguage}.
 *
 * @author carcassi
 */
public class AggregatedExpression<T> {

    private final DataSourceRecipe recipe;
    private final Function<T> function;
    private final String defaultName;

    public AggregatedExpression(DataSourceRecipe recipe, Function<T> function, String defaultName) {
        this.recipe = recipe;
        this.function = function;
        this.defaultName = defaultName;
    }

    public AggregatedExpression(List<AggregatedExpression<?>> childExpressions, Function<T> function, String defaultName) {
        this.recipe = combineRecipes(childExpressions);
        this.function = function;
        this.defaultName = defaultName;
    }

    private static DataSourceRecipe combineRecipes(List<AggregatedExpression<?>> childExpressions) {
        if (childExpressions.isEmpty())
            return new DataSourceRecipe();

        DataSourceRecipe recipe = childExpressions.get(0).getDataSourceRecipe();
        for (int i = 1; i < childExpressions.size(); i++) {
            DataSourceRecipe newRecipe = childExpressions.get(i).getDataSourceRecipe();
            recipe = recipe.includeRecipe(newRecipe);
        }

        return recipe;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public DataSourceRecipe getDataSourceRecipe() {
        return recipe;
    }

    public Function<T> getFunction() {
        return function;
    }
}
