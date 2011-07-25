/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 * Represents an expression that can be both read and written.
 *
 * @param <R> type of the read payload
 * @param <W> type of the write payload
 * @author carcassi
 */
public class DesiredRateReadWriteExpression<R, W> implements DesiredRateExpression<R>, WriteExpression<W> {
    
    private final DesiredRateExpression<R> desiredRateExpression;
    private final WriteExpression<W> writeExpression;

    /**
     * Creates an expression that can be both read and written.
     * 
     * @param desiredRateExpression the read part of the expression
     * @param writeExpression the write part of the expression
     */
    public DesiredRateReadWriteExpression(DesiredRateExpression<R> desiredRateExpression, WriteExpression<W> writeExpression) {
        this.desiredRateExpression = desiredRateExpression;
        this.writeExpression = writeExpression;
    }

    @Override
    public String getDefaultName() {
        return desiredRateExpression.getDefaultName();
    }

    @Override
    public Function<R> getFunction() {
        return desiredRateExpression.getFunction();
    }
    
    DesiredRateExpressionImpl<R> getDesiredRateExpressionImpl() {
        return DesiredRateExpressionImpl.implOf(desiredRateExpression);
    }
    
    WriteExpressionImpl<W> getWriteExpressionImpl() {
        return WriteExpressionImpl.implOf(writeExpression);
    }

    @Override
    public DataRecipe getDataRecipe() {
        return desiredRateExpression.getDataRecipe();
    }
    
}
