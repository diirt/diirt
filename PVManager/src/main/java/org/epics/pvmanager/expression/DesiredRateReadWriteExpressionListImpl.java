/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of desired rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public class DesiredRateReadWriteExpressionListImpl<R, W> implements DesiredRateReadWriteExpressionList<R, W> {
    
    private List<DesiredRateReadWriteExpression<R, W>> desiredRateReadWriteExpressions = new ArrayList<DesiredRateReadWriteExpression<R, W>>();

    @Override
    public DesiredRateReadWriteExpressionList<R, W> and(DesiredRateReadWriteExpressionList<R, W> expressions) {
        desiredRateReadWriteExpressions.addAll(expressions.getDesiredRateReadWriteExpressions());
        return this;
    }

    @Override
    public List<DesiredRateReadWriteExpression<R, W>> getDesiredRateReadWriteExpressions() {
        return desiredRateReadWriteExpressions;
    }

    @Override
    public DesiredRateExpressionList<R> and(DesiredRateExpressionList<R> expressions) {
        return new DesiredRateExpressionListImpl<R>(desiredRateReadWriteExpressions).and(expressions);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DesiredRateExpression<R>> getDesiredRateExpressions() {
        return Collections.unmodifiableList(desiredRateReadWriteExpressions);
    }

    @Override
    public WriteExpressionList<W> and(WriteExpressionList<W> expressions) {
        return new WriteExpressionListImpl<W>(desiredRateReadWriteExpressions).and(expressions);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WriteExpression<W>> getWriteExpressions() {
        return Collections.unmodifiableList(desiredRateReadWriteExpressions);
    }
    
}
