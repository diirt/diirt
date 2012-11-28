/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class WriteRecipeUtil {

    public static ChannelWriteRecipe recipeFor(WriteRecipe writeRecipe, String channelName) {
        ChannelWriteRecipe foundRecipe = null;
        for (ChannelWriteRecipe channelWriteRceipe : writeRecipe.getChannelWriteBuffers()) {
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
