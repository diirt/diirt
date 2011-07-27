/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Arrays;
import org.epics.pvmanager.expression.WriteExpression;
import java.util.Map;
import org.epics.pvmanager.expression.DesiredRateReadWriteExpression;
import org.epics.pvmanager.loc.LocalDataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

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
                .from(dataSource).every(hz(50));
        pvReader.close();
    }

    @Test
    public void readWriteMap() throws Exception {
        DesiredRateReadWriteExpression<Map<String, Object>, Map<String, Object>> map =
                mapOf(latestValueOf(channel("channel1")).and(latestValueOf(channel("channel2"))));
        WriteBuffer buffer = map.createWriteBuffer();
        assertThat(buffer.getWriteCaches().size(), equalTo(2));
        assertThat(buffer.getWriteCaches().keySet(), containsInAnyOrder("channel1", "channel2"));
    }
    
    @Test
    public void channelList() {
        ChannelExpressionList<Object, Object> exp =
                channels("channel1", "channel2", "channel3").after("master1");
        for (WriteExpression<Object> writeExp : exp.getWriteExpressions()) {
            WriteBuffer buffer = writeExp.createWriteBuffer();
            assertThat(buffer.getWriteCaches().size(), equalTo(1));
            String key = buffer.getWriteCaches().keySet().iterator().next();
            assertThat(Arrays.asList("channel1", "channel2", "channel3"), hasItem(key));
            assertThat(buffer.getWriteCaches().get(key).getPrecedingChannels(), hasSize(1));
            assertThat(buffer.getWriteCaches().get(key).getPrecedingChannels(), contains("master1"));
        }
    }
    
}
