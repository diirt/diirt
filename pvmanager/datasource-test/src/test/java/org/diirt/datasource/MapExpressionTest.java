/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import static java.time.Duration.ofMillis;
import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.datasource.ExpressionLanguage.latestValueOf;
import static org.diirt.datasource.ExpressionLanguage.readMapOf;
import static org.diirt.datasource.ExpressionLanguage.writeMapOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.diirt.datasource.expression.ReadMap;
import org.diirt.datasource.expression.WriteMap;
import org.diirt.datasource.test.MockDataSource;
import org.diirt.datasource.test.WriteRecipeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests map expression with a full pipeline.
 *
 * @author carcassi
 */
public class MapExpressionTest {

    public MapExpressionTest() {
    }

    @Before
    public void setUp() {
        pvReader = null;
        dataSource = new MockDataSource();
    }

    @After
    public void tearDown() throws InterruptedException {
        if (pvReader != null) {
            pvReader.close();
            pvReader = null;
        }
        if (pvWriter != null) {
            pvWriter.close();
            pvWriter = null;
        }

        Thread.sleep(400);

        assertThat(dataSource.getConnectedReadRecipes(), equalTo(Collections.<ChannelReadRecipe>emptyList()));
        assertThat(dataSource.getConnectedWriteRecipes(), equalTo(Collections.<ChannelWriteRecipe>emptyList()));
    }

    private PVReader<?> pvReader;
    private PVWriter<Map<String, Object>> pvWriter;
    private MockDataSource dataSource;

    @Test
    public void map1() {
        ReadMap<Object> map = readMapOf(Object.class);
        pvReader = PVManager.read(map).from(dataSource).maxRate(ofMillis(100));
        map.add(latestValueOf(channel("test1")));
        assertThat(dataSource.getReadRecipe(), not(equalTo(null)));
        assertThat(dataSource.getConnectedReadRecipes(), hasSize(1));
        map.add(latestValueOf(channel("test2")));
        assertThat(dataSource.getReadRecipe(), not(equalTo(null)));
        assertThat(dataSource.getConnectedReadRecipes(), hasSize(2));
    }

    @Test
    public void map2() {
        ReadMap<Object> map = readMapOf(Object.class);
        pvReader = PVManager.read(map).from(dataSource).maxRate(ofMillis(100));
        map.add(latestValueOf(channel("test1")));
        map.add(latestValueOf(channel("test2")));
        assertThat(dataSource.getReadRecipe(), not(equalTo(null)));
        assertThat(dataSource.getConnectedReadRecipes(), hasSize(2));
        map.remove("test2");
        assertThat(dataSource.getConnectedReadRecipes(), hasSize(1));
    }

    @Test
    public void map3() throws Exception {
        WriteMap<Object> map = writeMapOf(Object.class);
        pvWriter = PVManager.write(map).from(dataSource).async();
        map.add(latestValueOf(channel("test1")));
        map.add(latestValueOf(channel("test2")));
        assertThat(dataSource.getWriteRecipe(), not(equalTo(null)));
        assertThat(dataSource.getConnectedWriteRecipes(), hasSize(2));
        Map<String, Object> values = new HashMap<>();
        values.put("test1", "testing1");
        values.put("test2", "testing2");
        pvWriter.write(values);
        Thread.sleep(100);
        assertThat(dataSource.getWriteRecipeForWrite(), not(equalTo(null)));
        assertThat(WriteRecipeUtil.valueFor(dataSource.getWriteRecipeForWrite(), "test1"), equalTo((Object) "testing1"));
        assertThat(WriteRecipeUtil.valueFor(dataSource.getWriteRecipeForWrite(), "test1"), equalTo((Object) "testing1"));

        map.remove("test2");
        assertThat(dataSource.getConnectedWriteRecipes(), hasSize(1));
    }


}