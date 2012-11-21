/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.Cache;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.pvmanager.expression.MapExpression;
import org.epics.pvmanager.expression.Queue;
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
        assertThat(cache.getValue(), nullValue());
        WriteCache<Object> writeCache = (WriteCache<Object>) exp.getWriteFunction();
        assertThat(writeCache.getPrecedingChannels().isEmpty(), equalTo(true));
        assertThat(writeCache.getValue(), nullValue());
        assertThat(writeCache.getChannelName(), equalTo("my pv"));
    }
    
    @Test
    public void queue1() {
        Queue<String> queue = queueOf(String.class).maxSize(5);
        ExpressionTester exp = new ExpressionTester(queue);
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
        ExpressionTester exp = new ExpressionTester(cache);
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
        MapExpression<String> map = newMapOf(String.class);
        ExpressionTester exp = new ExpressionTester(map);
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
        MapExpression<Double> map = newMapOf(constant(1.0).as("SETPOINT").and(constant(2.0).as("READBACK")));
        ExpressionTester exp = new ExpressionTester(map);
        Map<String, Double> referenceValue = new HashMap<String, Double>();
        referenceValue.put("READBACK", 2.0);
        referenceValue.put("SETPOINT", 1.0);
        assertThat(exp.getValue(), equalTo((Object) referenceValue));
    }
    
}
