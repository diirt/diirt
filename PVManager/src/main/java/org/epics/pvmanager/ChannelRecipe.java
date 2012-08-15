/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class ChannelRecipe {
    private final String channelName;
    private final ChannelHandlerReadSubscription readSubscription;

    public ChannelRecipe(String channelName, ChannelHandlerReadSubscription readSubscription) {
        this.channelName = channelName;
        this.readSubscription = readSubscription;
    }
    
    public String getChannelName() {
        return channelName;
    }

    public ChannelHandlerReadSubscription getReadSubscription() {
        return readSubscription;
    }
    
    
}
