/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.expression;

import org.epics.pvmanager.WriteBufferBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.WriteFunction;

/**
 * An expression that represent a pv write.
 * Objects of this class are not created directly but through the operators defined
 * in {@link ExpressionLanguage}.
 *
 * @param <T> type taken by the expression
 * @author carcassi
 */
public class WriteExpressionImpl<T> extends WriteExpressionListImpl<T> implements WriteExpression<T> {

    private Map<String, WriteCache<?>> writeCaches;
    private WriteFunction<T> writeFunction;
    private String defaultName;
    
    {
        // Make sure that the list includes this expression
        addThis();
    }

    /**
     * Constructor that represents a single channel of a particular type.
     *
     * @param channelName the name of the channel
     */
    public WriteExpressionImpl(String channelName) {
        WriteCache<T> cache = new WriteCache<T>();
        writeCaches = new HashMap<String, WriteCache<?>>();
        writeCaches.put(channelName, cache);
        this.writeFunction = cache;
        this.defaultName = channelName;
    }

    public WriteExpression<T> as(String name) {
        defaultName = name;
        return this;
    }

    /**
     * Creates a new write expression.
     * 
     * @param childExpression the expression used as arguments by this expression
     * @param function the function that will decompose the payload for this expression
     * @param defaultName the name for this expression
     */
    public WriteExpressionImpl(WriteExpression<?> childExpression, WriteFunction<T> function, String defaultName) {
        this(Collections.<WriteExpression<?>>singletonList(childExpression), function, defaultName);
    }

    /**
     * Creates a new write expression.
     * 
     * @param childExpressions the expressions used as arguments by this expression
     * @param function the function that will decompose the payload for this expression
     * @param defaultName the name for this expression
     */
    public WriteExpressionImpl(List<WriteExpression<?>> childExpressions, WriteFunction<T> function, String defaultName) {
        writeCaches = new HashMap<String, WriteCache<?>>();
        for (WriteExpression<?> childExpression : childExpressions) {
            for (Map.Entry<String, WriteCache<?>> entry : childExpression.getWriteExpressionImpl().getWriteCaches().entrySet()) {
                String pvName = entry.getKey();
                if (writeCaches.keySet().contains(pvName)) {
                    throw new RuntimeException("Can't define a write operation that writes to the same channel more than once.");
                }
                writeCaches.put(pvName, entry.getValue());
            }
        }
        this.writeFunction = function;
        this.defaultName = defaultName;
    }

    /**
     * Name representation of the expression.
     *
     * @return a name
     */
    @Override
    public String getDefaultName() {
        return defaultName;
    }

    /**
     * Returns all the {@link ValueCache}s required by this expression.
     *
     * @return the value caches for this expression
     */
    private Map<String, WriteCache<?>> getWriteCaches() {
        return writeCaches;
    }

    /**
     * Returns the function represented by this expression.
     *
     * @return the function
     */
    @Override
    public WriteFunction<T> getWriteFunction() {
        return writeFunction;
    }

    /**
     * Creates a data recipe for the given expression.
     *
     * @param collector the collector to be notified by changes in this expression
     * @return a data recipe
     */
    @Override
    public WriteBuffer createWriteBuffer() {
        WriteBufferBuilder buffer = new WriteBufferBuilder();
        buffer.addCaches(writeCaches);
        return buffer.build();
    }

    @Override
    public WriteExpressionImpl<T> getWriteExpressionImpl() {
        return this;
    }

}
