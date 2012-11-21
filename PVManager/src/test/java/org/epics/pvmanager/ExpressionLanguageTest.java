/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.Cache;
import org.epics.pvmanager.expression.ChannelExpression;
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
}
