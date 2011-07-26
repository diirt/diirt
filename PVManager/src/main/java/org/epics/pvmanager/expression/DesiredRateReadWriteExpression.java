/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.List;
import org.epics.pvmanager.DataRecipe;
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
public class DesiredRateReadWriteExpression<R, W> extends DesiredRateReadWriteExpressionListImpl<R, W> implements DesiredRateExpression<R>, WriteExpression<W> {
    
    private final DesiredRateExpression<R> desiredRateExpression;
    private final WriteExpression<W> writeExpression;
    
    {
        // Make sure that the list includes this expression
        addThis();
    }

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
    
    @Override
    public DesiredRateExpressionImpl<R> getDesiredRateExpressionImpl() {
        return desiredRateExpression.getDesiredRateExpressionImpl();
    }
    
    @Override
    public WriteExpressionImpl<W> getWriteExpressionImpl() {
        return writeExpression.getWriteExpressionImpl();
    }

    @Override
    public DataRecipe getDataRecipe() {
        return desiredRateExpression.getDataRecipe();
    }

    @Override
    public DesiredRateExpressionList<R> and(DesiredRateExpressionList<R> expressions) {
        return desiredRateExpression.and(expressions);
    }

    @Override
    public List<DesiredRateExpression<R>> getDesiredRateExpressions() {
        return desiredRateExpression.getDesiredRateExpressions();
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
