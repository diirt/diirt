/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.List;

/**
 * A list of desired rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public interface SourceRateReadWriteExpressionList<R, W> extends SourceRateExpressionList<R>, WriteExpressionList<W> {
    
    public SourceRateReadWriteExpressionList<R, W> and(SourceRateReadWriteExpressionList<R, W> expressions);

    public List<SourceRateReadWriteExpression<R, W>> getSourceRateReadWriteExpressions();
}
