/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

/**
 * An expression that represents a PV that is read at the UI scan rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link ExpressionLanguage}.
 *
 * @param <T> type of the expression output
 * @author carcassi
 */
public interface DesiredRateExpression<T> {

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
    public Function<T> getFunction();
}
