/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map;
import java.util.HashMap;
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

    public CompositeDataSourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mock1Recipe = null;
        mock2Recipe = null;
    }

    @After
    public void tearDown() {
    }

    private DataRecipe mock1Recipe;
    private DataRecipe mock2Recipe;
    DataSource mock1 = new DataSource(true) {

        @Override
        protected ChannelHandler createChannel(String channelName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void connect(DataRecipe recipe) {
            mock1Recipe = recipe;
        }

        @Override
        public void disconnect(DataRecipe recipe) {
            mock1Recipe = recipe;
        }


    };

    DataSource mock2 = new DataSource(true) {

        @Override
        protected ChannelHandler createChannel(String channelName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void connect(DataRecipe recipe) {
            mock2Recipe = recipe;
        }

        @Override
        public void disconnect(DataRecipe recipe) {
            mock2Recipe = recipe;
        }
    };

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
        assertThat(mock1Recipe.getChannelsPerCollectors(), equalTo(recipe.getChannelsPerCollectors()));
        assertThat(mock2Recipe, nullValue());
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
        Map<String, ValueCache> mock1Caches = mock1Recipe.getChannelsPerCollectors().values().iterator().next();
        Map<String, ValueCache> mock2Caches = mock2Recipe.getChannelsPerCollectors().values().iterator().next();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(mock1Caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Caches.keySet(), hasItem("pv04"));

        // Check close
        DataRecipe mock1Connect = mock1Recipe;
        DataRecipe mock2Connect = mock2Recipe;
        composite.disconnect(recipe);
        assertSame(mock1Connect, mock1Recipe);
        assertSame(mock2Connect, mock2Recipe);
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
        Map<String, ValueCache> mock1Caches = mock1Recipe.getChannelsPerCollectors().values().iterator().next();
        Map<String, ValueCache> mock2Caches = mock2Recipe.getChannelsPerCollectors().values().iterator().next();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(mock1Caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Caches.keySet(), hasItem("pv04"));
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