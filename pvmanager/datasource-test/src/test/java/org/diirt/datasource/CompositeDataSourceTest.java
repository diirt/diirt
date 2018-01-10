/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.test.MockDataSource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests CompositeDataSource.
 *
 * @author carcassi
 */
public class CompositeDataSourceTest {

    @Before
    public void setUp() {
        mock1 = new MockDataSource();
        mock2 = new MockDataSource();
    }

    @After
    public void tearDown() {
    }

    MockDataSource mock1;

    MockDataSource mock2;

    @Test
    public void testAllDefault() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("mock1"));

        // Call only default
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("pv01", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("pv03", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Call and check
        composite.connectRead(recipe);
        assertThat(mock1.getReadRecipe().getChannelReadRecipes(), equalTo(recipe.getChannelReadRecipes()));
        assertThat(mock2.getReadRecipe(), nullValue());
    }

    @Test
    public void testMixedCall() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("mock1"));

        // Call only default
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("pv01", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("pv03", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock1://pv02", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock2://pv04", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock1://pv05", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Call and check
        composite.connectRead(recipe);
        Collection<ChannelReadRecipe> mock1Caches = mock1.getReadRecipe().getChannelReadRecipes();
        Collection<ChannelReadRecipe> mock2Caches = mock2.getReadRecipe().getChannelReadRecipes();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(channelNames(mock1Caches), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(channelNames(mock2Caches), hasItem("pv04"));

        // Check close
        ReadRecipe mock1Connect = mock1.getReadRecipe();
        ReadRecipe mock2Connect = mock2.getReadRecipe();
        composite.disconnectRead(recipe);
        assertEquals(mock1Connect, mock1.getReadRecipe());
        assertEquals(mock2Connect, mock2.getReadRecipe());
    }

    private Set<String> channelNames(Collection<ChannelReadRecipe> channelRecipes) {
        Set<String> names = new HashSet<String>();
        for (ChannelReadRecipe channelRecipe : channelRecipes) {
            names.add(channelRecipe.getChannelName());
        }
        return names;
    }

    private Set<String> channelWriteNames(Collection<ChannelWriteRecipe> channelWriteBuffers) {
        Set<String> names = new HashSet<String>();
        for (ChannelWriteRecipe channelWriteBuffer : channelWriteBuffers) {
            names.add(channelWriteBuffer.getChannelName());
        }
        return names;
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoDefault() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);

        // Call only default
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("pv01", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("pv03", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Should cause error
        composite.connectRead(recipe);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDefaultDoesntExist() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("wrong"));

        // Call only default
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("pv01", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("pv03", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Should cause error
        composite.connectRead(recipe);
    }

    @Test
    public void testDifferentDelimiter() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("mock1").delimiter("?"));

        // Call only default
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("pv01", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("pv03", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock1?pv02", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock2?pv04", new ValueCacheImpl<Double>(Double.class));
        builder.addChannel("mock1?pv05", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Call and check
        composite.connectRead(recipe);
        Collection<ChannelReadRecipe> mock1Caches = mock1.getReadRecipe().getChannelReadRecipes();
        Collection<ChannelReadRecipe> mock2Caches = mock2.getReadRecipe().getChannelReadRecipes();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(channelNames(mock1Caches), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(channelNames(mock2Caches), hasItem("pv04"));
    }

    @Test (expected=IllegalArgumentException.class)
    public void testReadEmpty() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();

        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        builder.addChannel("mock://pv03", new ValueCacheImpl<Double>(Double.class));
        ReadRecipe recipe = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Should cause error
        composite.connectRead(recipe);
    }

    @Test (expected=IllegalArgumentException.class)
    public void testWriteEmpty() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();

        // Write pv with no datasource match
        WriteRecipeBuilder builder = new WriteRecipeBuilder();
        builder.addChannel("mock://pv03", new WriteCache<>("mock://pv03"));
        WriteRecipe buffer = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Should cause error
        composite.connectWrite(buffer);
    }

    @Test
    public void testWriteMixedCall() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("mock1"));

        WriteRecipeBuilder builder = new WriteRecipeBuilder();
        builder.addChannel("pv01", new WriteCache<>("pv01"));
        builder.addChannel("pv03", new WriteCache<>("pv03"));
        builder.addChannel("mock1://pv02", new WriteCache<>("mock1://pv02"));
        builder.addChannel("mock2://pv04", new WriteCache<>("mock2://pv04"));
        builder.addChannel("mock1://pv05", new WriteCache<>("mock1://pv05"));
        WriteRecipe buffer = builder.build(new ValueCacheImpl<Exception>(Exception.class), new ConnectionCollector());

        // Call and check
        composite.connectWrite(buffer);
        Collection<ChannelWriteRecipe> mock1Buffers = mock1.getWriteRecipe().getChannelWriteRecipes();
        Collection<ChannelWriteRecipe> mock2Buffers = mock2.getWriteRecipe().getChannelWriteRecipes();
        assertThat(mock1Buffers.size(), equalTo(4));
        assertThat(mock2Buffers.size(), equalTo(1));
        assertThat(channelWriteNames(mock1Buffers), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(channelWriteNames(mock2Buffers), hasItem("pv04"));

        // Check close
        WriteRecipe mock1Connect = mock1.getWriteRecipe();
        WriteRecipe mock2Connect = mock2.getWriteRecipe();
        composite.disconnectWrite(buffer);
        assertEquals(mock1Connect, mock1.getWriteRecipe());
        assertEquals(mock2Connect, mock2.getWriteRecipe());
    }

}