/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.expression;

import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.Function;

/**
 * An expression representing a PV read at the desired rate.
 * <p>
 * Don't implement objects with this interface, use {@link DesiredRateExpressionImpl}.
 *
 * @param <R> type of the read payload
 * @author carcassi
 */
public interface DesiredRateExpression<R> extends DesiredRateExpressionList<R>, DesiredRateExpressionImplProvider<R> {
    
    /**
     * Changes the name for this expression
     * 
     * @param name new name
     * @return this
     */
    public DesiredRateExpression<R> as(String name);
    
    /**
     * The default name for a PV of this expression.
     *
     * @return the default name
     */
    public String getDefaultName();
    
    /**
     * The recipe for connect the channels for this expression.
     *
     * @return a data recipe
     */
    public DataRecipe getDataRecipe();
    
    /**
     * The function that calculates new values for this expression.
     *
     * @return a function
     */
    public Function<R> getFunction();
    
}
