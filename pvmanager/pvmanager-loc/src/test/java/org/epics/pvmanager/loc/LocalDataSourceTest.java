/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.loc;

import java.util.Arrays;
import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.ConnectionCollector;
import org.epics.pvmanager.ReadRecipe;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVWriterListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.WriteRecipe;
import org.epics.pvmanager.WriteCache;
import org.diirt.vtype.VDouble;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.QueueCollector;
import org.epics.pvmanager.ReadExpressionTester;
import org.epics.pvmanager.ReadRecipeBuilder;
import org.epics.pvmanager.ValueCacheImpl;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.epics.pvmanager.expression.Queue;
import org.epics.pvmanager.loc.LocalDataSource;
import org.epics.pvmanager.loc.LocalDataSource;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class LocalDataSourceTest {

    @Test
    public void writeMultipleNamesForSameChannel() throws Exception {
        ReadExpressionTester exp = new ReadExpressionTester(mapOf(latestValueOf(channels("foo", "foo(2.0)"))));
        ReadRecipe recipe = exp.getReadRecipe();
        
        // Connect and make sure only one channel is created
        LocalDataSource dataSource = new LocalDataSource();
        dataSource.connectRead(recipe);
        assertThat(dataSource.getChannels().size(), equalTo(1));
        dataSource.disconnectRead(recipe);
        dataSource.close();
    }

    @Test
    public void initialValue1() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("iv1", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        
        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, nullValue());
    }

    @Test
    public void initialValue2() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("iv2(\"this\")", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        
        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, instanceOf(VString.class));
        VString vString = (VString) value;
        assertThat(vString.getValue(), equalTo("this"));
    }

    @Test
    public void initialValue3() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("iv3(3.14)", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        
        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, instanceOf(VDouble.class));
        VDouble vDouble = (VDouble) value;
        assertThat(vDouble.getValue(), equalTo(3.14));
    }

    @Test
    public void initialValue4() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("iv3(1.0,2.0,3.0)", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        
        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, instanceOf(VDoubleArray.class));
        VDoubleArray vDouble = (VDoubleArray) value;
        assertThat(vDouble.getData(), equalTo((ListNumber) new ArrayDouble(1.0, 2.0, 3.0)));
    }

    @Test
    public void initialValue5() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("iv3(\"A\",\"B\",\"C\")", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        
        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, instanceOf(VStringArray.class));
        VStringArray vStrings = (VStringArray) value;
        assertThat(vStrings.getData(), equalTo(Arrays.asList("A", "B", "C")));
    }
    
}
