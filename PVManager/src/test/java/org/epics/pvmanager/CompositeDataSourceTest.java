/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class CompositeDataSourceTest {

    private static ExceptionHandler rethrow = new ExceptionHandler() {

        @Override
        public void handleException(Exception ex) {
            if (ex instanceof RuntimeException)
                throw (RuntimeException) ex;
            throw new RuntimeException("rethrown", ex);
        }

    };

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
        composite.setDefaultDataSource("mock1");

        // Call only default
        DataRecipe recipe = new DataRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Call and check
        composite.connect(recipe);
        assertThat(mock1.getDataRecipe().getChannelRecipes(), equalTo(recipe.getChannelRecipes()));
        assertThat(mock2.getDataRecipe(), nullValue());
    }

    @Test
    public void testMixedCall() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setDefaultDataSource("mock1");

        // Call only default
        DataRecipe recipe = new DataRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        caches.put("mock1://pv02", new ValueCache<Double>(Double.class));
        caches.put("mock2://pv04", new ValueCache<Double>(Double.class));
        caches.put("mock1://pv05", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);
        
        // Call and check
        composite.connect(recipe);
        Collection<ChannelRecipe> mock1Caches = mock1.getDataRecipe().getChannelRecipes();
        Collection<ChannelRecipe> mock2Caches = mock2.getDataRecipe().getChannelRecipes();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(channelNames(mock1Caches), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(channelNames(mock2Caches), hasItem("pv04"));

        // Check close
        DataRecipe mock1Connect = mock1.getDataRecipe();
        DataRecipe mock2Connect = mock2.getDataRecipe();
        composite.disconnect(recipe);
        assertSame(mock1Connect, mock1.getDataRecipe());
        assertSame(mock2Connect, mock2.getDataRecipe());
    }
    
    private Set<String> channelNames(Collection<ChannelRecipe> channelRecipes) {
        Set<String> names = new HashSet<String>();
        for (ChannelRecipe channelRecipe : channelRecipes) {
            names.add(channelRecipe.getChannelName());
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
        DataRecipe recipe = new DataRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Should cause error
        composite.connect(recipe);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDefaultDoesntExist() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);

        // Should cause error
        composite.setDefaultDataSource("wrong");
    }

    @Test
    public void testDifferentDelimiter() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setDefaultDataSource("mock1");
        composite.setDelimiter("?");

        // Call only default
        DataRecipe recipe = new DataRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        caches.put("mock1?pv02", new ValueCache<Double>(Double.class));
        caches.put("mock2?pv04", new ValueCache<Double>(Double.class));
        caches.put("mock1?pv05", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Call and check
        composite.connect(recipe);
        Collection<ChannelRecipe> mock1Caches = mock1.getDataRecipe().getChannelRecipes();
        Collection<ChannelRecipe> mock2Caches = mock2.getDataRecipe().getChannelRecipes();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(channelNames(mock1Caches), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(channelNames(mock2Caches), hasItem("pv04"));
    }

    @Test (expected=IllegalArgumentException.class)
    public void testEmpty() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();

        // Call only default
        DataRecipe recipe = new DataRecipe(rethrow);
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("mock://pv03", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Should cause error
        composite.connect(recipe);
    }

}