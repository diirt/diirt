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
    private PVReaderDirector<?> pvReaderDirector = new PVReaderDirector<Object>(null, null, null, null, null);

    public ExpressionTester(DesiredRateExpression<?> expression) {
        this.expression = expression;
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        expression.fillReadRecipe(null, builder);
        this.readRecipe = builder.build(exceptionCollector, connCollector);
    }

    public void writeValue(String name, Object value) {
        boolean written = false;
        for (ChannelReadRecipe channelRecipe : readRecipe.getChannelReadRecipes()) {
            if (channelRecipe.getChannelName().equals(name)) {
                @SuppressWarnings("unchecked")
                ValueCache<Object> cache = (ValueCache<Object>) channelRecipe.getReadSubscription().getValueCache();
                cache.setValue(value);
                written = true;
            }
        }
        if (!written) {
            throw new IllegalStateException("Can't find recipe for channel '" + name + "'");
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
