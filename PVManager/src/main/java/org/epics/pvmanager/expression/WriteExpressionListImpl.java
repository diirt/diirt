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
public class WriteExpressionListImpl<T> implements WriteExpressionList<T> {
    
    private List<WriteExpression<T>> writeExpressions;
    
    protected void addThis() {
        writeExpressions.add((WriteExpression<T>) this);
    }

    public WriteExpressionListImpl() {
        this.writeExpressions = new ArrayList<WriteExpression<T>>();
    }

    WriteExpressionListImpl(Collection<? extends WriteExpression<T>> writeExpressions) {
        this.writeExpressions = new ArrayList<WriteExpression<T>>(writeExpressions);
    }
    
    @Override
    public WriteExpressionListImpl<T> and(WriteExpressionList<T> expressions) {
        writeExpressions.addAll(expressions.getWriteExpressions());
        return this;
    }

    @Override
    public List<WriteExpression<T>> getWriteExpressions() {
        return writeExpressions;
    }
    
}
