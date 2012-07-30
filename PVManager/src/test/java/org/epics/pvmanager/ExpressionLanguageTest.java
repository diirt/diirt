/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.data.*;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.util.array.*;
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
    public void vType1() {
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
}
