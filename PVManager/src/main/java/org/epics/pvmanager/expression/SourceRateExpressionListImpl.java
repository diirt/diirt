/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A list of desired rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public class SourceRateExpressionListImpl<T> implements SourceRateExpressionList<T> {
    
    private List<SourceRateExpression<T>> sourceRateExpressions;
    
    protected void addThis() {
        sourceRateExpressions.add((SourceRateExpression<T>) this);
    }

    public SourceRateExpressionListImpl() {
        this.sourceRateExpressions = new ArrayList<SourceRateExpression<T>>();
    }

    SourceRateExpressionListImpl(Collection<? extends SourceRateExpression<T>> sourceRateExpressions) {
        this.sourceRateExpressions = new ArrayList<SourceRateExpression<T>>(sourceRateExpressions);
    }
    
    @Override
    public SourceRateExpressionListImpl<T> and(SourceRateExpressionList<T> expressions) {
        sourceRateExpressions.addAll(expressions.getSourceRateExpressions());
        return this;
    }

    @Override
    public List<SourceRateExpression<T>> getSourceRateExpressions() {
        return sourceRateExpressions;
    }
    
}
