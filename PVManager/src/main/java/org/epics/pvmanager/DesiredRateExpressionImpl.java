/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.List;

/**
 * An expression that represents a PV that is read at the UI scan rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link ExpressionLanguage}.
 *
 * @param <T> type of the expression output
 * @author carcassi
 */
public class DesiredRateExpressionImpl<T> implements DesiredRateExpression<T> {
    
    static <T> DesiredRateExpressionImpl<T> implOf(DesiredRateExpression<T> sourceRateExpression) {
        if (sourceRateExpression instanceof DesiredRateExpressionImpl) {
            return (DesiredRateExpressionImpl<T>) sourceRateExpression;
        }
        
        if (sourceRateExpression instanceof DesiredRateReadWriteExpression) {
            return ((DesiredRateReadWriteExpression<T, ?>) sourceRateExpression).getDesiredRateExpressionImpl();
        }
        
        throw new IllegalArgumentException("DesiredRateExpression must be implemented using DesiredRateExpressionImpl");
    }

    private final DataRecipeBuilder recipe;
    private final Function<T> function;
    private final String defaultName;

    /**
     * Creates a new expression at the desired rate. Use this constructor when making
     * an DesiredRateExpression out of a collector and a SourceRateExpression.
     *
     * @param expression the original source rate expression
     * @param collector the collector for the original source
     * @param defaultName the display name of the expression
     */
    public DesiredRateExpressionImpl(SourceRateExpression<?> expression, Function<T> collector, String defaultName) {
        if (!(collector instanceof Collector)){
            throw new IllegalArgumentException("collector must be of type Collector");
        }
        this.recipe = SourceRateExpressionImpl.implOf(expression).createDataRecipe((Collector) collector);
        this.function = collector;
        this.defaultName = defaultName;
    }

    /**
     * Creates a new aggregated expression. Use this constructor when making
     * a {@code DesiredRateExpression} that is a function of another
     * {@code DesiredRateExpression}.
     *
     * @param expression the expression for the argument of the function
     * @param function the function to calculate the new expression
     * @param defaultName the name of the expression
     */
    public DesiredRateExpressionImpl(DesiredRateExpression<?> expression, Function<T> function, String defaultName) {
        // TODO: maybe another constructor for no parent expression?
        if (expression == null) {
            this.recipe = new DataRecipeBuilder();
        } else {
            this.recipe = implOf(expression).recipe;
        }
        this.function = function;
        this.defaultName = defaultName;
    }

    /**
     * Creates a new aggregated expression. Use this constructor when making
     * a {@code DesiredRateExpression} that is a function of a number of
     * {@code DesiredRateExpression}s.
     *
     * @param childExpressions expressions for the arguments of the function
     * @param function the function that calculates the value of the new expression
     * @param defaultName the display name of the expression
     */
    public DesiredRateExpressionImpl(List<DesiredRateExpression<?>> childExpressions, Function<T> function, String defaultName) {
        this.recipe = combineRecipes(childExpressions);
        this.function = function;
        this.defaultName = defaultName;
    }

    private static DataRecipeBuilder combineRecipes(List<DesiredRateExpression<?>> childExpressions) {
        if (childExpressions.isEmpty())
            return new DataRecipeBuilder();

        DataRecipeBuilder recipe = implOf(childExpressions.get(0)).recipe;
        for (int i = 1; i < childExpressions.size(); i++) {
            DataRecipeBuilder newRecipe = implOf(childExpressions.get(i)).recipe;
            recipe.addAll(newRecipe);
        }

        return recipe;
    }

    /**
     * The default name for a PV of this expression.
     *
     * @return the default name
     */
    @Override
    public String getDefaultName() {
        return defaultName;
    }

    /**
     * The recipe for connect the channels for this expression.
     *
     * @return a data recipe
     */
    @Override
    public DataRecipe getDataRecipe() {
        return recipe.build();
    }

    /**
     * The function that calculates new values for this expression.
     *
     * @return a function
     */
    @Override
    public Function<T> getFunction() {
        return function;
    }
}
