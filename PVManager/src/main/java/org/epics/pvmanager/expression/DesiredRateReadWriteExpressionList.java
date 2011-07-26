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
public class DesiredRateReadWriteExpressionList<R, W> {
    
    private List<DesiredRateReadWriteExpression<R, W>> desiredRateExpressions = new ArrayList<DesiredRateReadWriteExpression<R, W>>();
    
    public DesiredRateReadWriteExpressionList<R, W> and(DesiredRateReadWriteExpression<R, W> expression) {
        desiredRateExpressions.add(expression);
        return this;
    }
    
    public DesiredRateReadWriteExpressionList<R, W> and(DesiredRateReadWriteExpressionList<R, W> expressions) {
        desiredRateExpressions.addAll(expressions.desiredRateExpressions);
        return this;
    }

    List<DesiredRateReadWriteExpression<R, W>> getDesiredRateExpressions() {
        return desiredRateExpressions;
    }
    
}
