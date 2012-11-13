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
    private DataRecipe recipe;
    private NewQueueCollector<Exception> exceptionCollector = new NewQueueCollector<>(10);
    private NewConnectionCollector connCollector = new NewConnectionCollector();

    public ExpressionTester(DesiredRateExpression<?> expression) {
        this.expression = expression;
        DataRecipeBuilder builder = new DataRecipeBuilder();
        expression.fillDataRecipe(null, builder);
        this.recipe = builder.build(exceptionCollector, connCollector);
    }

    public void writeValue(String name, Object value) {
        for (ChannelRecipe channelRecipe : recipe.getChannelRecipes()) {
            if (channelRecipe.getChannelName().equals(name)) {
                @SuppressWarnings("unchecked")
                ValueCache<Object> cache = (ValueCache<Object>) channelRecipe.getReadSubscription().getValueCache();
                cache.setValue(value);
            }
        }
    }
    
    public ChannelRecipe recipeFor(String channelName) {
        for (ChannelRecipe channelRecipe : recipe.getChannelRecipes()) {
            if (channelRecipe.getChannelName().equals(channelName)) {
                return channelRecipe;
            }
        }
        return null;
    }
    
    public DataRecipe getDataRecipe() {
        return recipe;
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
