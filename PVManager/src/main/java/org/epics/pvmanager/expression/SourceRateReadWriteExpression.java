/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.List;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.WriteFunction;

/**
 * Represents an expression that can be both read and written.
 *
 * @param <R> type of the read payload
 * @param <W> type of the write payload
 * @author carcassi
 */
public class SourceRateReadWriteExpression<R, W> implements SourceRateExpression<R>, WriteExpression<W> {
    
    private final SourceRateExpression<R> sourceRateExpression;
    private final WriteExpression<W> writeExpression;

    /**
     * Creates an expression that can be both read and written.
     * 
     * @param sourceRateExpression the read part of the expression
     * @param writeExpression the write part of the expression
     */
    public SourceRateReadWriteExpression(SourceRateExpression<R> sourceRateExpression, WriteExpression<W> writeExpression) {
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
    
    @Override
    public SourceRateExpressionImpl<R> getSourceRateExpressionImpl() {
        return sourceRateExpression.getSourceRateExpressionImpl();
    }
    
    @Override
    public WriteExpressionImpl<W> getWriteExpressionImpl() {
        return writeExpression.getWriteExpressionImpl();
    }

    @Override
    public WriteFunction<W> getWriteFunction() {
        return writeExpression.getWriteFunction();
    }

    @Override
    public WriteBuffer createWriteBuffer() {
        return writeExpression.createWriteBuffer();
    }

    @Override
    public WriteExpressionList<W> and(WriteExpressionList<W> expressions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<WriteExpression<W>> getWriteExpressions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
