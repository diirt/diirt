/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

/**
 * Represents an expression that can be both read and written.
 *
 * @param <R> type of the read payload
 * @param <W> type of the write payload
 * @author carcassi
 */
public interface SourceRateReadWriteExpression<R, W> extends SourceRateExpression<R>, WriteExpression<W>, SourceRateReadWriteExpressionList<R, W> {
    
}
