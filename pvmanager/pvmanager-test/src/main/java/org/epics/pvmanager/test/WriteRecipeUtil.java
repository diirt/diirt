/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.ChannelWriteRecipe;
import org.epics.pvmanager.WriteRecipe;

/**
 *
 * @author carcassi
 */
public class WriteRecipeUtil {

    public static ChannelWriteRecipe recipeFor(WriteRecipe writeRecipe, String channelName) {
        ChannelWriteRecipe foundRecipe = null;
        for (ChannelWriteRecipe channelWriteRceipe : writeRecipe.getChannelWriteRecipes()) {
            if (channelWriteRceipe.getChannelName().equals(channelName)) {
                if (foundRecipe != null) {
                    throw new IllegalStateException("Multiple recipe founds for channel '" + channelName + "'");
                }
                return channelWriteRceipe;
            }
        }
        if (foundRecipe == null) {
            
        }
        
        throw new IllegalStateException("Can't find reciep for channel '" + channelName + "'");
    }

    public static Object valueFor(WriteRecipe writeRecipe, String channelName) {
        return recipeFor(writeRecipe, channelName).getWriteSubscription().getWriteCache().getValue();
    }
}
