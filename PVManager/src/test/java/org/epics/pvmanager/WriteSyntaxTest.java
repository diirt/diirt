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

/**
 *
 * @author carcassi
 */
public class WriteSyntaxTest {

    @Test
    public void simpleWriter() {
        String channelName = "test";
        ChannelExpression<Object, Object> chExpr = channel(channelName);
        WriteExpressionImpl<Object> chExprImpl = WriteExpressionImpl.implOf(chExpr);
        assertThat(chExpr.getDefaultName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) chExprImpl.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) chExprImpl.getWriteFunction()).getPrecedingChannels().isEmpty(), equalTo(true));
        WriteExpression<Object> expr = channel(channelName).after("a", "b");
        WriteExpressionImpl<Object> exprImpl = WriteExpressionImpl.implOf(expr);
        assertThat(exprImpl.getDefaultName(), equalTo(channelName));
        assertThat(((WriteCache<Object>) exprImpl.getWriteFunction()).getValue(), nullValue());
        assertThat(((WriteCache<Object>) exprImpl.getWriteFunction()).getPrecedingChannels(), hasSize(2));
        assertThat(((WriteCache<Object>) exprImpl.getWriteFunction()).getPrecedingChannels(), contains("a", "b"));
        
        PVWriter<Object> writer = PVManager.write(channel(channelName)).from(new LocalDataSource()).sync();
        writer.write(10);
    }

}
