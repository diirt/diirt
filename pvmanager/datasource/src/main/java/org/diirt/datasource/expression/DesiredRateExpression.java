/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.expression;

import org.diirt.datasource.PVDirector;
import org.diirt.datasource.ReadFunction;
import org.diirt.datasource.ReadRecipeBuilder;

/**
 * An expression to read at the desired rate.
 * <p>
 * Don't implement objects with this interface, use {@link DesiredRateExpressionImpl}.
 *
 * @param <R> type of the read payload
 * @author carcassi
 */
public interface DesiredRateExpression<R> extends DesiredRateExpressionList<R> {

    /**
     * Changes the name for this expression
     *
     * @param name new name
     * @return this
     */
    public DesiredRateExpression<R> as(String name);

    /**
     * Name of this expression.
     *
     * @return the expression name
     */
    public String getName();

    /**
     * Prepares the recipe to connect the channels needed by this expression.
     * <p>
     * A dynamic expression, one for which the child expressions can change,
     * can keep a reference to the director to connect/disconnect new child
     * expressions.
     *
     * @param director the director for the reader
     * @param builder the recipe to fill
     */
    public void fillReadRecipe(PVDirector director, ReadRecipeBuilder builder);

    /**
     * The function that calculates this expression.
     *
     * @return the expression function
     */
    public ReadFunction<R> getFunction();

    /**
     * The implementation of this expression.
     *
     * @return the implementation
     */
    public DesiredRateExpressionImpl<R> getDesiredRateExpressionImpl();
}
