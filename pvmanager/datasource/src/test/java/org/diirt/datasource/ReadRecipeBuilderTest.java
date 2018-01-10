/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ReadRecipeBuilderTest {

    @Test
    public void testAdd1() {
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache valueCache = new ValueCacheImpl<Object>(Object.class);
        builder.addChannel("test", valueCache);
        WriteFunction<Exception> writeCache = new WriteCache<Exception>();
        ConnectionCollector connectionCollector = new ConnectionCollector();
        ReadRecipe recipe = builder.build(writeCache, connectionCollector);
        assertThat(recipe.getChannelReadRecipes().size(), equalTo(1));
        ChannelReadRecipe channelRecipe = recipe.getChannelReadRecipes().iterator().next();
        assertThat(channelRecipe.getChannelName(), equalTo("test"));
        assertThat(channelRecipe.getReadSubscription().getValueCache(), equalTo(valueCache));
        assertThat(channelRecipe.getReadSubscription().getExceptionWriteFunction(), equalTo(writeCache));
    }

    @Test
    public void testAdd2() {
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache valueCache1 = new ValueCacheImpl<Object>(Object.class);
        ValueCache valueCache2 = new ValueCacheImpl<Object>(Object.class);
        builder.addChannel("test", valueCache1);
        builder.addChannel("test", valueCache2);
        WriteFunction<Exception> writeCache = new WriteCache<Exception>();
        ConnectionCollector connectionCollector = new ConnectionCollector();
        ReadRecipe recipe = builder.build(writeCache, connectionCollector);
        assertThat(recipe.getChannelReadRecipes().size(), equalTo(2));
    }
}
