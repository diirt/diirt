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
public class SourceRateReadWriteExpressionImpl<R, W> extends SourceRateReadWriteExpressionListImpl<R, W> implements SourceRateReadWriteExpression<R, W> {
    
    private final SourceRateExpression<R> sourceRateExpression;
    private final WriteExpression<W> writeExpression;
    
    {
        // Make sure that the list includes this expression
        addThis();
    }

    @Override
    public SourceRateReadWriteExpressionImpl<R, W> as(String name) {
        sourceRateExpression.as(name);
        writeExpression.getWriteExpressionImpl().as(name);
        return this;
    }

    /**
     * Creates an expression that can be both read and written.
     * 
     * @param sourceRateExpression the read part of the expression
     * @param writeExpression the write part of the expression
     */
    public SourceRateReadWriteExpressionImpl(SourceRateExpression<R> sourceRateExpression, WriteExpression<W> writeExpression) {
        this.sourceRateExpression = sourceRateExpression;
        this.writeExpression = writeExpression;
    }

    @Override
    public String getName() {
        return sourceRateExpression.getName();
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
    
}
