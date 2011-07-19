/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class ExpressionTester {

    private DesiredRateExpression<?> expression;

    public ExpressionTester(DesiredRateExpression<?> expression) {
        this.expression = expression;
    }

    public void writeValue(String name, Object value) {
        for (Collector<?> collector : expression.getDataRecipe().getChannelsPerCollectors().keySet()) {
            @SuppressWarnings("unchecked")
            ValueCache<Object> cache = expression.getDataRecipe().getChannelsPerCollectors().get(collector).get(name);
            if (cache != null) {
                cache.setValue(value);
                collector.collect();
            }
        }
    }
}
