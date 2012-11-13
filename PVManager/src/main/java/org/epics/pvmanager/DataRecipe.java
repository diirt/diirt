/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents all the information necessary to connect to a {@link DataSource}.
 * It represents the contact between PVManager and the {@code DataSource}.
 *
 * @author carcassi
 */
public class DataRecipe {
    
    private final Collection<ChannelRecipe> channelRecipes;

    DataRecipe(Collection<ChannelRecipe> channelRecipes) {
        this.channelRecipes = channelRecipes;
    }

    public Collection<ChannelRecipe> getChannelRecipes() {
        return channelRecipes;
    }
}
