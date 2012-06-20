/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.epics.pvmanager.expression.DesiredRateExpression;

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
    
    public Collector<?> collectorFor(String channelName) {
        for (Entry<Collector<?>, Map<String, ValueCache>> entry : expression.getDataRecipe().getChannelsPerCollectors().entrySet()) {
            Collector<?> collector = entry.getKey();
            Map<String, ValueCache> map = entry.getValue();
            for (String name : map.keySet()) {
                if (channelName.equals(name))
                    return collector;
            }
        }
        
        return null;
    }
    
    public ValueCache cacheFor(String channelName) {
        for (Map<String, ValueCache> maps : expression.getDataRecipe().getChannelsPerCollectors().values()) {
            for (Entry<String, ValueCache> entry : maps.entrySet()) {
                String name = entry.getKey();
                ValueCache valueCache = entry.getValue();
                if (channelName.equals(name)) {
                    return valueCache;
                }
            }
        }
        
        return null;
    }
}
