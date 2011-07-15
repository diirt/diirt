/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class ReadWriteExpression<R, W> implements SourceRateExpression<R>, WriteExpression<W> {
    
    private final SourceRateExpression<R> sourceRateExpression;
    private final WriteExpression<W> writeExpression;

    public ReadWriteExpression(SourceRateExpression<R> sourceRateExpression, WriteExpression<W> writeExpression) {
        this.sourceRateExpression = sourceRateExpression;
        this.writeExpression = writeExpression;
    }

    @Override
    public String getDefaultName() {
        return sourceRateExpression.getDefaultName();
    }

    @Override
    public Function<R> getFunction() {
        return sourceRateExpression.getFunction();
    }
    
    SourceRateExpressionImpl<R> getSourceRateExpressionImpl() {
        return SourceRateExpressionImpl.implOf(sourceRateExpression);
    }
    
    WriteExpressionImpl<W> getWriteExpressionImpl() {
        return WriteExpressionImpl.implOf(writeExpression);
    }
    
}
