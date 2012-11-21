/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.expression.DesiredRateExpression;

/**
 *
 * @author carcassi
 */
public class ExpressionTester {

    private DesiredRateExpression<?> expression;
    private ReadRecipe readRecipe;
    private QueueCollector<Exception> exceptionCollector = new QueueCollector<>(10);
    private ConnectionCollector connCollector = new ConnectionCollector();

    public ExpressionTester(DesiredRateExpression<?> expression) {
        this.expression = expression;
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        expression.fillReadRecipe(null, builder);
        this.readRecipe = builder.build(exceptionCollector, connCollector);
    }

    public void writeValue(String name, Object value) {
        for (ChannelReadRecipe channelRecipe : readRecipe.getChannelReadRecipes()) {
            if (channelRecipe.getChannelName().equals(name)) {
                @SuppressWarnings("unchecked")
                ValueCache<Object> cache = (ValueCache<Object>) channelRecipe.getReadSubscription().getValueCache();
                cache.setValue(value);
            }
        }
    }
    
    public ChannelReadRecipe recipeFor(String channelName) {
        for (ChannelReadRecipe channelRecipe : readRecipe.getChannelReadRecipes()) {
            if (channelRecipe.getChannelName().equals(channelName)) {
                return channelRecipe;
            }
        }
        return null;
    }
    
    public ReadRecipe getReadRecipe() {
        return readRecipe;
    }
    
    public Function<?> getFunction() {
        return expression.getFunction();
    }
    
    public DesiredRateExpression<?> getExpression() {
        return expression;
    }
    
    public Object getValue() {
        return expression.getFunction().getValue();
    }
}
