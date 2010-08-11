/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

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

    private MonitorRecipe mock1Recipes;
    private MonitorRecipe mock2Recipes;
    DataSource mock1 = new DataSource() {

        @Override
        public void monitor(MonitorRecipe connRecipe) {
            mock1Recipes = connRecipe;
        }
    };

    DataSource mock2 = new DataSource() {

        @Override
        public void monitor(MonitorRecipe connRecipe) {
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
        MonitorRecipe recipe = new MonitorRecipe();
        recipe.collector = new QueueCollector<Double>(new ValueCache<Double>(Double.class));
        recipe.caches.put("pv01", new ValueCache<Double>(Double.class));
        recipe.caches.put("pv03", new ValueCache<Double>(Double.class));

        // Call and check
        composite.monitor(recipe);
        assertThat(mock1Recipes.caches, equalTo(recipe.caches));
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
        MonitorRecipe recipe = new MonitorRecipe();
        recipe.collector = new QueueCollector<Double>(new ValueCache<Double>(Double.class));
        recipe.caches.put("pv01", new ValueCache<Double>(Double.class));
        recipe.caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock1://pv02", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock2://pv04", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock1://pv05", new ValueCache<Double>(Double.class));

        // Call and check
        composite.monitor(recipe);
        assertThat(mock1Recipes.caches.size(), equalTo(4));
        assertThat(mock2Recipes.caches.size(), equalTo(1));
        assertThat(mock1Recipes.caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Recipes.caches.keySet(), hasItem("pv04"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoDefault() {
        // Setup composite
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("mock1", mock1);
        composite.putDataSource("mock2", mock2);

        // Call only default
        MonitorRecipe recipe = new MonitorRecipe();
        recipe.collector = new QueueCollector<Double>(new ValueCache<Double>(Double.class));
        recipe.caches.put("pv01", new ValueCache<Double>(Double.class));
        recipe.caches.put("pv03", new ValueCache<Double>(Double.class));

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
        MonitorRecipe recipe = new MonitorRecipe();
        recipe.collector = new QueueCollector<Double>(new ValueCache<Double>(Double.class));
        recipe.caches.put("pv01", new ValueCache<Double>(Double.class));
        recipe.caches.put("pv03", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock1?pv02", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock2?pv04", new ValueCache<Double>(Double.class));
        recipe.caches.put("mock1?pv05", new ValueCache<Double>(Double.class));

        // Call and check
        composite.monitor(recipe);
        assertThat(mock1Recipes.caches.size(), equalTo(4));
        assertThat(mock2Recipes.caches.size(), equalTo(1));
        assertThat(mock1Recipes.caches.keySet(), hasItems("pv01", "pv02", "pv03", "pv05"));
        assertThat(mock2Recipes.caches.keySet(), hasItem("pv04"));
    }

}