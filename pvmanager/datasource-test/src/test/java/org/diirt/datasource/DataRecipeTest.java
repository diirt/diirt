/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class DataRecipeTest {
//
//    @Test
//    public void connectionFunction() throws Exception{
//        DataRecipeBuilder builder = new DataRecipeBuilder();
//        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
//        @SuppressWarnings("unchecked")
//        ValueCache cache1 = new ValueCacheImpl(Object.class);
//        caches.put("one", cache1);
//        @SuppressWarnings("unchecked")
//        Collector<Object> coll1 = new QueueCollector<Object>(cache1, 1);
//        builder.addCollector(coll1, caches);
//
//        caches = new HashMap<String, ValueCache>();
//        @SuppressWarnings("unchecked")
//        ValueCache cache2 = new ValueCacheImpl(Object.class);
//        caches.put("two", cache2);
//        @SuppressWarnings("unchecked")
//        Collector<Object> coll2 = new QueueCollector<Object>(cache2, 1);
//        builder.addCollector(coll2, caches);
//
//        DataRecipe recipe = builder.build();
//        Collector<Boolean> connCollector = recipe.getConnectionCollector();
//        Map<String, ValueCache<Boolean>> connCaches = recipe.getConnectionCaches();
//
//        assertThat(connCaches.size(), equalTo(2));
//        assertThat(connCaches.keySet(), contains("two", "one"));
//
//        connCaches.get("one").setValue(false);
//        connCollector.collect();
//        connCaches.get("two").setValue(true);
//        connCollector.collect();
//        List<Boolean> value = connCollector.getValue();
//        assertThat(value.size(), equalTo(1));
//        assertThat(value.get(0), equalTo(false));
//
//        connCaches.get("one").setValue(true);
//        connCollector.collect();
//        value = connCollector.getValue();
//        assertThat(value.size(), equalTo(1));
//        assertThat(value.get(0), equalTo(true));
//
//        connCaches.get("two").setValue(false);
//        connCollector.collect();
//        value = connCollector.getValue();
//        assertThat(value.size(), equalTo(1));
//        assertThat(value.get(0), equalTo(false));
//    }
}
