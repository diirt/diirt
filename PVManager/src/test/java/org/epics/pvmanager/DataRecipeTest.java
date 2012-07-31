/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.pvmanager.test.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.epics.pvmanager.PVWriterListener;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.ExpressionTester;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ReadFailException;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.WriteFailException;
import org.epics.pvmanager.util.TimeDuration;
import org.epics.pvmanager.util.TimeStamp;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.test.ExpressionLanguage.*;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.data.VType;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.pvmanager.expression.DesiredRateExpression;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class DataRecipeTest {

    @Test
    public void connectionFunction() throws Exception{
        DataRecipeBuilder builder = new DataRecipeBuilder();
        Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
        @SuppressWarnings("unchecked")
        ValueCache cache1 = new ValueCache(Object.class);
        caches.put("one", cache1);
        @SuppressWarnings("unchecked")
        Collector<Object> coll1 = new QueueCollector<Object>(cache1, 1);
        builder.addCollector(coll1, caches);
        
        caches = new HashMap<String, ValueCache>();
        @SuppressWarnings("unchecked")
        ValueCache cache2 = new ValueCache(Object.class);
        caches.put("two", cache2);
        @SuppressWarnings("unchecked")
        Collector<Object> coll2 = new QueueCollector<Object>(cache2, 1);
        builder.addCollector(coll2, caches);
        
        DataRecipe recipe = builder.build();
        Collector<Boolean> connCollector = recipe.getConnectionCollector();
        Map<String, ValueCache<Boolean>> connCaches = recipe.getConnectionCaches();

        assertThat(connCaches.size(), equalTo(2));
        assertThat(connCaches.keySet(), contains("two", "one"));
        
        connCaches.get("one").setValue(false);
        connCollector.collect();
        connCaches.get("two").setValue(true);
        connCollector.collect();
        List<Boolean> value = connCollector.getValue();
        assertThat(value.size(), equalTo(1));
        assertThat(value.get(0), equalTo(false));
        
        connCaches.get("one").setValue(true);
        connCollector.collect();
        value = connCollector.getValue();
        assertThat(value.size(), equalTo(1));
        assertThat(value.get(0), equalTo(true));
        
        connCaches.get("two").setValue(false);
        connCollector.collect();
        value = connCollector.getValue();
        assertThat(value.size(), equalTo(1));
        assertThat(value.get(0), equalTo(false));
    }
}
