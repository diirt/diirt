/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of source rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public class SourceRateReadWriteExpressionListImpl<R, W> implements SourceRateReadWriteExpressionList<R, W> {
    
    private List<SourceRateReadWriteExpression<R, W>> sourceRateReadWriteExpressions = new ArrayList<SourceRateReadWriteExpression<R, W>>();
    
    protected final void addThis() {
        sourceRateReadWriteExpressions.add((SourceRateReadWriteExpression<R, W>) this);
    }

    @Override
    public final SourceRateReadWriteExpressionList<R, W> and(SourceRateReadWriteExpressionList<R, W> expressions) {
        sourceRateReadWriteExpressions.addAll(expressions.getSourceRateReadWriteExpressions());
        return this;
    }

    @Override
    public final List<SourceRateReadWriteExpression<R, W>> getSourceRateReadWriteExpressions() {
        return sourceRateReadWriteExpressions;
    }

    @Override
    public final SourceRateExpressionList<R> and(SourceRateExpressionList<R> expressions) {
        return new SourceRateExpressionListImpl<R>(sourceRateReadWriteExpressions).and(expressions);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<SourceRateExpression<R>> getSourceRateExpressions() {
        return Collections.unmodifiableList(sourceRateReadWriteExpressions);
    }

    @Override
    public final WriteExpressionList<W> and(WriteExpressionList<W> expressions) {
        return new WriteExpressionListImpl<W>(sourceRateReadWriteExpressions).and(expressions);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<WriteExpression<W>> getWriteExpressions() {
        return Collections.unmodifiableList(sourceRateReadWriteExpressions);
    }
    
}
