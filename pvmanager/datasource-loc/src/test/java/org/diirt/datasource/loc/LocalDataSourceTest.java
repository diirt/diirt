/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.loc;

import static org.diirt.datasource.ExpressionLanguage.channels;
import static org.diirt.datasource.ExpressionLanguage.latestValueOf;
import static org.diirt.datasource.ExpressionLanguage.mapOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.diirt.datasource.ConnectionCollector;
import org.diirt.datasource.QueueCollector;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.ReadRecipe;
import org.diirt.datasource.ReadRecipeBuilder;
import org.diirt.datasource.ValueCache;
import org.diirt.datasource.ValueCacheImpl;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.junit.Test;

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

    @Test
    public void venum1() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("venum<VEnum>(1, \"A\",\"B\",\"C\")", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());

        dataSource1.connectRead(recipe);
        Thread.sleep(100);
        Object value = valueCache.readValue();
        dataSource1.disconnectRead(recipe);
        assertThat(value, instanceOf(VEnum.class));
        VEnum val = (VEnum) value;
        assertThat(val.getLabels(), equalTo(Arrays.asList("A", "B", "C")));
        assertThat(val.getIndex(), equalTo(1));
    }

    @Test(expected=RuntimeException.class)
    public void invalidVEnum() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        ReadRecipeBuilder builder = new ReadRecipeBuilder();
        ValueCache<Object> valueCache = new ValueCacheImpl<>(Object.class);
        builder.addChannel("venum<VEnum>(1, 1.0, 2.0)", valueCache);
        ReadRecipe recipe = builder.build(new QueueCollector<Exception>(10), new ConnectionCollector());
        builder.addChannel("venum", valueCache);
        // Throws RuntimeException
        dataSource1.connectRead(recipe);
    }

}
