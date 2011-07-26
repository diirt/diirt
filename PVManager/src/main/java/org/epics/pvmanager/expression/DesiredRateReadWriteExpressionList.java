/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of desired rate expression, to have functions that work on multiple
 * expressions at the same time.
 *
 * @author carcassi
 */
public interface DesiredRateReadWriteExpressionList<R, W> extends DesiredRateExpressionList<R>, WriteExpressionList<W> {
    
    public DesiredRateReadWriteExpressionList<R, W> and(DesiredRateReadWriteExpressionList<R, W> expressions);

    public List<DesiredRateReadWriteExpression<R, W>> getDesiredRateReadWriteExpressions();
}
