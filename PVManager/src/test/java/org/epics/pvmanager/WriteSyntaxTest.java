/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.epics.pvmanager.loc.LocalDataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class WriteSyntaxTest {

    @Test
    public void simpleWriter() {
        String channelName = "test";
        ChannelExpression<Object> chExpr = toChannel(channelName);
        assertThat(chExpr.getDefaultName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) chExpr.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) chExpr.getWriteFunction()).getPrecedingChannels().isEmpty(), equalTo(true));
        WriteExpression<Object> expr = toChannel(channelName).after("a", "b");
        assertThat(expr.getDefaultName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getPrecedingChannels(), hasSize(2));
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getPrecedingChannels(), contains("a", "b"));
        
        PVWriter<Object> writer = PVManager.write(toChannel(channelName)).from(new LocalDataSource()).sync();
        writer.write(10);
    }

}
