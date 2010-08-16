/*
 * Copyright 2010 Brookhaven National Laboratory
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
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class CompositeDataSourceTest {

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
        mock1Recipes = null;
        mock2Recipes = null;
    }

    @After
    public void tearDown() {
    }

    private DataSourceRecipe mock1Recipes;
    private DataSourceRecipe mock2Recipes;
    DataSource mock1 = new DataSource() {

        @Override
        public void monitor(DataSourceRecipe connRecipe) {
            mock1Recipes = connRecipe;
        }
    };

    DataSource mock2 = new DataSource() {

        @Override
        public void monitor(DataSourceRecipe connRecipe) {
            mock2Recipes = connRecipe;
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
        DataSourceRecipe recipe = new DataSourceRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Call and check
        composite.monitor(recipe);
        assertThat(mock1Recipes.getChannelsPerCollectors(), equalTo(recipe.getChannelsPerCollectors()));
        assertThat(mock2Recipes, nullValue());
    }

    @Test
    public void testMixedCall() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);
        composite.setDefaultDataSource("mock1");

        // Call only default
        DataSourceRecipe recipe = new DataSourceRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        caches.put("mock1://pv02", new ValueCache<Double>(Double.class));
        caches.put("mock2://pv04", new ValueCache<Double>(Double.class));
        caches.put("mock1://pv05", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);
        
        // Call and check
        composite.monitor(recipe);
        Map<String, ValueCache> mock1Caches = mock1Recipes.getChannelsPerCollectors().values().iterator().next();
        Map<String, ValueCache> mock2Caches = mock2Recipes.getChannelsPerCollectors().values().iterator().next();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(mock1Caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Caches.keySet(), hasItem("pv04"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoDefault() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);

        // Call only default
        DataSourceRecipe recipe = new DataSourceRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Should cause error
        composite.monitor(recipe);
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
        // Call only default
        DataSourceRecipe recipe = new DataSourceRecipe();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        caches.put("pv01", new ValueCache<Double>(Double.class));
        caches.put("pv03", new ValueCache<Double>(Double.class));
        caches.put("mock1?pv02", new ValueCache<Double>(Double.class));
        caches.put("mock2?pv04", new ValueCache<Double>(Double.class));
        caches.put("mock1?pv05", new ValueCache<Double>(Double.class));
        recipe = recipe.includeCollector(new QueueCollector<Double>(new ValueCache<Double>(Double.class)),
                caches);

        // Call and check
        composite.monitor(recipe);
        Map<String, ValueCache> mock1Caches = mock1Recipes.getChannelsPerCollectors().values().iterator().next();
        Map<String, ValueCache> mock2Caches = mock2Recipes.getChannelsPerCollectors().values().iterator().next();
        assertThat(mock1Caches.size(), equalTo(4));
        assertThat(mock2Caches.size(), equalTo(1));
        assertThat(mock1Caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Caches.keySet(), hasItem("pv04"));
    }

}