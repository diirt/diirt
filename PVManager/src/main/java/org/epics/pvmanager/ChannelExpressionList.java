/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.expression.SourceRateReadWriteExpression;
import org.epics.pvmanager.expression.SourceRateReadWriteExpressionListImpl;

/**
 * Represents a list of channel, which can be both read or written.
 *
 * @param <R> type of the read payload
 * @param <W> type of the write payload
 * @author carcassi
 */
public class ChannelExpressionList<R, W> extends SourceRateReadWriteExpressionListImpl<R, W> {

    ChannelExpressionList(Class<R> readClass, Class<W> writeClass, String... channelNames) {
        for (String channelName : channelNames) {
            and(new ChannelExpression<R, W>(channelName, readClass, writeClass));
        }
    }
    
    /**
     * For writes only, marks that these channels should be written only after the
     * given channels.
     * 
     * @param channelNames preceding channel names
     * @return this
     */
    public ChannelExpressionList<R, W> after(String... channelNames) {
        for (SourceRateReadWriteExpression<R, W> expression : getSourceRateReadWriteExpressions()) {
            ChannelExpression<R, W> channel = (ChannelExpression<R, W>) expression;
            channel.after(channelNames);
        }
        return this;
    }
    
    
}
