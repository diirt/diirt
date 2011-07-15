/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Arrays;

/**
 *
 * @author carcassi
 */
public class ChannelExpression<R, W> extends ReadWriteExpression<R, W> {

    ChannelExpression(String channelName, Class<R> readClass, Class<W> writeClass) {
        super(new SourceRateExpressionImpl<R>(channelName, readClass), new WriteExpressionImpl<W>(channelName));
    }
    
    public ChannelExpression<R, W> after(String... channelNames) {
        ((WriteCache<W>) getWriteExpressionImpl().getWriteFunction()).setPrecedingChannels(Arrays.asList(channelNames));
        return this;
    }
    
    
}
