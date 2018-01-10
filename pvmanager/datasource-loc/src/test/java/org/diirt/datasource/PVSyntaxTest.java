/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.expression.ChannelExpressionList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.diirt.datasource.expression.DesiredRateReadWriteExpressionList;
import org.diirt.datasource.expression.SourceRateReadWriteExpressionImpl;
import org.diirt.datasource.expression.WriteExpression;
import java.util.Map;
import java.util.Set;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.WriteExpressionTester;
import org.diirt.datasource.expression.DesiredRateReadWriteExpression;
import org.diirt.datasource.loc.LocalDataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.datasource.ExpressionLanguage.*;
import static org.diirt.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class PVSyntaxTest {

    @Test
    public void readMap() throws Exception {
        DataSource dataSource = new LocalDataSource();

        PVReader<Map<String, Object>> pvReader =
                PVManager.read(mapOf(latestValueOf(channel("channel1"))
                                    .and(latestValueOf(channel("channel2")))))
                .from(dataSource).maxRate(ofHertz(50));
        pvReader.close();
    }

    @Test
    public void readWriteMap() throws Exception {
        WriteExpressionTester exp = new WriteExpressionTester(mapOf(latestValueOf(channel("channel1")).and(latestValueOf(channel("channel2")))));
        WriteRecipe buffer = exp.getWriteRecipe();
        assertThat(buffer.getChannelWriteRecipes().size(), equalTo(2));
        assertThat(channelNames(buffer), containsInAnyOrder("channel1", "channel2"));
    }

    private static Collection<String> channelNames(WriteRecipe buffer) {
        Set<String> names = new HashSet<String>();
        for (ChannelWriteRecipe channelWriteBuffer : buffer.getChannelWriteRecipes()) {
            names.add(channelWriteBuffer.getChannelName());
        }
        return names;
    }

    private static ChannelWriteRecipe channelWriteBuffer(String channelName, WriteRecipe buffer) {
        for (ChannelWriteRecipe channelWriteBuffer : buffer.getChannelWriteRecipes()) {
            if (channelWriteBuffer.getChannelName().equals(channelName)) {
                return channelWriteBuffer;
            }
        }
        return null;
    }

    @Test
    public void channelList1() {
        List<String> names = Arrays.asList("channel1", "channel2", "channel3");
        ChannelExpressionList<Object, Object> exp =
                channels("channel1", "channel2", "channel3").after("master1");
        int index = 0;
        for (WriteExpression<Object> writeExp : exp.getWriteExpressions()) {
            WriteRecipe buffer = new WriteExpressionTester(writeExp).getWriteRecipe();
            assertThat(buffer.getChannelWriteRecipes().size(), equalTo(1));
            WriteCache<?> writeCache = channelWriteBuffer(names.get(index), buffer).getWriteSubscription().getWriteCache();
            assertThat(writeCache.getPrecedingChannels(), hasSize(1));
            assertThat(writeCache.getPrecedingChannels(), contains("master1"));
            index++;
        }
    }

    @Test
    public void channelList2() {
        List<String> names = Arrays.asList("channel1", "channel2", "channel3");
        ChannelExpressionList<Object, Object> exp =
                channels(names).after("master1");
        int index = 0;
        for (WriteExpression<Object> writeExp : exp.getWriteExpressions()) {
            WriteRecipe buffer = new WriteExpressionTester(writeExp).getWriteRecipe();
            assertThat(buffer.getChannelWriteRecipes().size(), equalTo(1));
            WriteCache<?> writeCache = channelWriteBuffer(names.get(index), buffer).getWriteSubscription().getWriteCache();
            assertThat(writeCache.getPrecedingChannels(), hasSize(1));
            assertThat(writeCache.getPrecedingChannels(), contains("master1"));
            index++;
        }
    }

    @Test
    public void latestValueOf1() {
        DesiredRateReadWriteExpressionList<Object, Object> exp = latestValueOf(channels("one", "two", "three"));
        assertThat(exp.getDesiredRateReadWriteExpressions(), hasSize(3));
    }

    @Test
    public void writeMap1() {
        WriteExpression<Map<String, Object>> mapOf = mapOf(channel("first").and(channels("second", "third").after("first")));
        WriteExpressionTester exp = new WriteExpressionTester(mapOf);
        WriteRecipe buffer = exp.getWriteRecipe();
        assertThat(buffer.getChannelWriteRecipes(), hasSize(3));
        assertThat(channelWriteBuffer("first", buffer).getWriteSubscription().getWriteCache().getPrecedingChannels(), hasSize(0));
        assertThat(channelWriteBuffer("second", buffer).getWriteSubscription().getWriteCache().getPrecedingChannels(), contains("first"));
        assertThat(channelWriteBuffer("third", buffer).getWriteSubscription().getWriteCache().getPrecedingChannels(), contains("first"));
    }

    @Test
    public void rename1() {
        SourceRateReadWriteExpressionImpl<Object, Object> exp = channel("myChannel").as("myName");
        assertThat(exp.getName(), equalTo("myName"));
        ReadExpressionTester finalExp = new ReadExpressionTester(latestValueOf(exp));
        ReadRecipe recipe = finalExp.getReadRecipe();
        assertThat(recipe.getChannelReadRecipes(), hasSize(1));
        assertThat(finalExp.recipeFor("myChannel"), notNullValue());
    }

}
