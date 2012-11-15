/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.WriteExpression;

/**
 *
 * @author carcassi
 */
public class WriteExpressionTester {

    private WriteExpression<?> expression;
    private WriteBuffer recipe;
    private NewQueueCollector<Exception> exceptionCollector = new NewQueueCollector<>(10);
    private NewConnectionCollector connCollector = new NewConnectionCollector();

    public WriteExpressionTester(WriteExpression<?> expression) {
        this.expression = expression;
        WriteBufferBuilder builder = new WriteBufferBuilder();
        expression.fillWriteBuffer(null, builder);
        this.recipe = builder.build(exceptionCollector, connCollector);
    }

    public Object readValue(String name) {
        for (ChannelWriteBuffer channelBuffer : recipe.getChannelWriteBuffers()) {
            if (channelBuffer.getChannelName().equals(name)) {
                @SuppressWarnings("unchecked")
                ValueCache<Object> cache = (ValueCache<Object>) channelBuffer.getWriteSubscription().getCache();
                return cache.getValue();
            }
        }
        throw new IllegalStateException("Can't find buffer for channel '" + name + "'");
    }
    
    public ChannelWriteBuffer recipeFor(String channelName) {
        for (ChannelWriteBuffer channelBuffer : recipe.getChannelWriteBuffers()) {
            if (channelBuffer.getChannelName().equals(channelName)) {
                return channelBuffer;
            }
        }
        return null;
    }
    
    public WriteBuffer getWriteBuffer() {
        return recipe;
    }
    
    public WriteFunction<Object> getWriteFunction() {
        return (WriteFunction<Object>) expression.getWriteFunction();
    }
    
    public WriteExpression<?> getExpression() {
        return expression;
    }
    
    public void setValue(Object value) {
        getWriteFunction().setValue(value);
    }
}
