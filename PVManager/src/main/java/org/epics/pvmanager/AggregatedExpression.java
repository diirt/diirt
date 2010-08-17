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
 * @param <T> type of the expression output
 * @author carcassi
 */
public class AggregatedExpression<T> {

    private final DataRecipe recipe;
    private final Function<T> function;
    private final String defaultName;

    /**
     * Creates a new aggregated expression. Use this constructor when making
     * an aggregated expression out of a collector and a non-aggregated expression.
     *
     * @param recipe the list of channels needed for this expression
     * @param function the function that calculates the value of the new expression
     * @param defaultName the display name of the expression
     */
    public AggregatedExpression(DataRecipe recipe, Function<T> function, String defaultName) {
        this.recipe = recipe;
        this.function = function;
        this.defaultName = defaultName;
    }

    /**
     * Creates a new aggregated expression. Use this constructor when making
     * an aggregated expression out of different aggregated expressions.
     *
     * @param childExpressions expression on which the new expression depends on
     * @param function the function that calculates the value of the new expression
     * @param defaultName the display name of the expression
     */
    public AggregatedExpression(List<AggregatedExpression<?>> childExpressions, Function<T> function, String defaultName) {
        this.recipe = combineRecipes(childExpressions);
        this.function = function;
        this.defaultName = defaultName;
    }

    private static DataRecipe combineRecipes(List<AggregatedExpression<?>> childExpressions) {
        if (childExpressions.isEmpty())
            return new DataRecipe();

        DataRecipe recipe = childExpressions.get(0).getDataRecipe();
        for (int i = 1; i < childExpressions.size(); i++) {
            DataRecipe newRecipe = childExpressions.get(i).getDataRecipe();
            recipe = recipe.includeRecipe(newRecipe);
        }

        return recipe;
    }

    /**
     * The default name for a PV of this expression.
     *
     * @return the default name
     */
    public String getDefaultName() {
        return defaultName;
    }

    /**
     * The recipe for connect the channels for this expression.
     *
     * @return a data recipe
     */
    public DataRecipe getDataRecipe() {
        return recipe;
    }

    /**
     * The function that calculates new values for this expression.
     *
     * @return a function
     */
    public Function<T> getFunction() {
        return function;
    }
}
