/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.expression.Cache;
import org.diirt.datasource.expression.ChannelExpression;
import org.diirt.datasource.expression.ReadMap;
import org.diirt.datasource.expression.Queue;
import org.diirt.datasource.expression.ReadWriteMap;
import org.diirt.datasource.expression.WriteMap;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    //
    // Testing channel expressions
    //

    @Test
    public void channel1() {
        ChannelExpression<Object, Object> exp = channel("my pv");
        assertThat(exp.getFunction(), instanceOf(ValueCache.class));
        assertThat(exp.getName(), equalTo("my pv"));
        ValueCache<Object> cache = (ValueCache<Object>) exp.getFunction();
        assertThat(cache.getType(), equalTo(Object.class));
        assertThat(cache.readValue(), nullValue());
        WriteCache<Object> writeCache = (WriteCache<Object>) exp.getWriteFunction();
        assertThat(writeCache.getPrecedingChannels().isEmpty(), equalTo(true));
        assertThat(writeCache.getValue(), nullValue());
        assertThat(writeCache.getChannelName(), equalTo("my pv"));
    }

    @Test
    public void queue1() {
        Queue<String> queue = queueOf(String.class).maxSize(5);
        ReadExpressionTester exp = new ReadExpressionTester(queue);
        assertThat(exp.getReadRecipe().getChannelReadRecipes().isEmpty(), equalTo(true));
        assertThat(exp.getValue(), equalTo((Object) Collections.EMPTY_LIST));
        queue.add("one");
        queue.add("two");
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("one", "two")));
        queue.add("one");
        queue.add("two");
        queue.add("three");
        queue.add("four");
        queue.add("five");
        queue.add("six");
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("two", "three", "four", "five", "six")));
        assertThat(exp.getValue(), equalTo((Object) Collections.EMPTY_LIST));
    }

    @Test
    public void cache1() {
        Cache<String> cache = cacheOf(String.class).maxSize(5);
        ReadExpressionTester exp = new ReadExpressionTester(cache);
        assertThat(exp.getReadRecipe().getChannelReadRecipes().isEmpty(), equalTo(true));
        assertThat(exp.getValue(), equalTo((Object) Collections.EMPTY_LIST));
        cache.add("one");
        cache.add("two");
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("one", "two")));
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("one", "two")));
        cache.add("one");
        cache.add("two");
        cache.add("three");
        cache.add("four");
        cache.add("five");
        cache.add("six");
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("two", "three", "four", "five", "six")));
        assertThat(exp.getValue(), equalTo((Object) Arrays.asList("two", "three", "four", "five", "six")));
    }

    //
    // Testing collection expressions
    //

    @Test
    public void mapOf1() {
        // Dynamically adding constant expressions (i.e. that don't require connection)
        ReadMap<String> map = readMapOf(String.class);
        ReadExpressionTester exp = new ReadExpressionTester(map);
        Map<String, String> referenceValue = new HashMap<String, String>();
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
        map.add(constant("Gabriele").as("name"));
        referenceValue.put("name", "Gabriele");
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
        map.add(constant("Carcassi").as("surname"));
        referenceValue.put("surname", "Carcassi");
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
        assertThat(exp.getValue(), sameInstance(exp.getValue()));
        map.remove("name");
        referenceValue.remove("name");
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
    }

    @Test
    public void mapOf2() {
        ReadMap<Double> map = mapOf(constant(1.0).as("SETPOINT").and(constant(2.0).as("READBACK")));
        ReadExpressionTester exp = new ReadExpressionTester(map);
        Map<String, Double> referenceValue = new HashMap<String, Double>();
        referenceValue.put("READBACK", 2.0);
        referenceValue.put("SETPOINT", 1.0);
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
    }

    @Test
    public void mapOf3() {
        WriteMap<Double> map = mapOf(channels(Arrays.asList("SETPOINT", "READBACK"), Double.class, Double.class));
        WriteExpressionTester exp = new WriteExpressionTester(map);
        Map<String, Double> value = new HashMap<String, Double>();
        value.put("READBACK", 2.0);
        value.put("SETPOINT", 1.0);
        exp.setValue(value);
        assertThat(exp.readValue("READBACK"), equalTo((Object) 2.0));
        assertThat(exp.readValue("SETPOINT"), equalTo((Object) 1.0));
    }

    @Test
    public void mapOf4() {
        WriteMap<Double> map = mapOf(channels(Arrays.asList("SETPOINT", "READBACK"), Double.class, Double.class));
        WriteExpressionTester exp = new WriteExpressionTester(map);
        Map<String, Double> value = new HashMap<String, Double>();
        value.put("SETPOINT", 1.0);
        exp.setValue(value);
        assertThat(exp.readValue("READBACK"), equalTo(null));
        assertThat(exp.readValue("SETPOINT"), equalTo((Object) 1.0));
    }

    @Test
    public void mapOf5() throws Exception {
        ReadWriteMap<Double, Double> map = mapOf(latestValueOf(channels(Arrays.asList("SETPOINT", "READBACK"), Double.class, Double.class)));
        ReadExpressionTester readExp = new ReadExpressionTester(map);
        WriteExpressionTester writeExp = new WriteExpressionTester(map);

        Map<String, Double> referenceValue = new HashMap<String, Double>();
        referenceValue.put("READBACK", 2.0);
        referenceValue.put("SETPOINT", 5.0);
        readExp.writeValue("READBACK", 2.0);
        readExp.writeValue("SETPOINT", 5.0);
        assertThat(readExp.getValue(), equalTo((Object) referenceValue));

        Map<String, Double> value = new HashMap<String, Double>();
        value.put("SETPOINT", 1.0);
        value.put("READBACK", 3.0);
        writeExp.setValue(value);
        assertThat(writeExp.readValue("SETPOINT"), equalTo((Object) 1.0));
        assertThat(writeExp.readValue("READBACK"), equalTo((Object) 3.0));
    }

}
