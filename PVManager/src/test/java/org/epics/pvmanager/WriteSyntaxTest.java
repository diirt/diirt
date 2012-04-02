/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.pvmanager.expression.WriteExpression;
import org.epics.pvmanager.expression.WriteExpressionImpl;
import org.epics.pvmanager.loc.LocalDataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class WriteSyntaxTest {

    @Test
    public void simpleWriter() {
        String channelName = "test";
        ChannelExpression<Object, Object> chExpr = channel(channelName);
        assertThat(chExpr.getName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) chExpr.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) chExpr.getWriteFunction()).getPrecedingChannels().isEmpty(), equalTo(true));
        WriteExpression<Object> expr = channel(channelName).after("a", "b");
        //assertThat(expr.getName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getPrecedingChannels(), hasSize(2));
        assertThat(((WriteCache<Object>) expr.getWriteFunction()).getPrecedingChannels(), contains("a", "b"));
        
        PVWriter<Object> writer = PVManager.write(channel(channelName)).from(new LocalDataSource()).sync();
        writer.write(10);
    }

}
