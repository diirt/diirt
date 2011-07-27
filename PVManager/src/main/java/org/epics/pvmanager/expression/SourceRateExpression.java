/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.expression;

import org.epics.pvmanager.Function;

/**
 * An expression that represent a pv read at the CA rate.
 * Objects of this class are not created directly but through the operators defined
 * in {@link ExpressionLanguage}.
 *
 * @param <T> type returned by the expression
 * @author carcassi
 */
public interface SourceRateExpression<T> extends SourceRateExpressionList<T>, SourceRateExpressionImplProvider<T> {
    
    /**
     * Changes the name for this expression
     * 
     * @param name new name
     * @return this
     */
    public SourceRateExpression<T> as(String name);

    /**
     * Name representation of the expression.
     *
     * @return a name
     */
    public String getDefaultName();

    /**
     * Returns the function represented by this expression.
     *
     * @return the function
     */
    public Function<T> getFunction();

}
