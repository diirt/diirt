/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.test.WriteRecipeUtil;
import org.epics.pvmanager.expression.WriteExpression;

/**
 *
 * @author carcassi
 */
public class WriteExpressionTester {

    private WriteExpression<?> expression;
    private PVWriterDirector<?> pvWriterDirector = new PVWriterDirector<Object>(null, null, null, null, null, null, null, null);

    public WriteExpressionTester(WriteExpression<?> expression) {
        this.expression = expression;
        pvWriterDirector.connectExpression(expression);
    }

    public Object readValue(String name) {
        return WriteRecipeUtil.valueFor(pvWriterDirector.getCurrentWriteRecipe(), name);
    }
    
    public ChannelWriteRecipe recipeFor(String channelName) {
        return WriteRecipeUtil.recipeFor(pvWriterDirector.getCurrentWriteRecipe(), channelName);
    }
    
    public WriteRecipe getWriteRecipe() {
        return pvWriterDirector.getCurrentWriteRecipe();
    }
    
    @SuppressWarnings("unchecked")
    public WriteFunction<Object> getWriteFunction() {
        return (WriteFunction<Object>) expression.getWriteFunction();
    }
    
    public WriteExpression<?> getExpression() {
        return expression;
    }
    
    public void setValue(Object value) {
        getWriteFunction().writeValue(value);
    }
}
