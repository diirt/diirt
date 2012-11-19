/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;

/**
 * Represents all the information necessary to connect to a {@link DataSource}.
 * It represents the contact between PVManager and the {@code DataSource}.
 *
 * @author carcassi
 */
public class ReadRecipe {
    
    private final Collection<ChannelReadRecipe> channelRecipes;

    ReadRecipe(Collection<ChannelReadRecipe> channelRecipes) {
        this.channelRecipes = channelRecipes;
    }

    /**
     * The recipes for each channel to connect.
     * 
     * @return a set of channel recipes
     */
    public Collection<ChannelReadRecipe> getChannelReadRecipes() {
        return channelRecipes;
    }
}
