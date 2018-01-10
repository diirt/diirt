/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the ConnectionCollector.
 *
 * @author carcassi
 */
public class ConnectionCollectorTest {

    @Test
    public void inputOutput() {
        ConnectionCollector collector = new ConnectionCollector();
        assertThat(collector.readValue(), equalTo(true));

        WriteFunction<Boolean> firstWriteFunction = collector.addChannel("first");
        assertThat(collector.readValue(), equalTo(false));

        firstWriteFunction.writeValue(false);
        assertThat(collector.readValue(), equalTo(false));

        WriteFunction<Boolean> secondWriteFunction = collector.addChannel("second");
        secondWriteFunction.writeValue(true);
        assertThat(collector.readValue(), equalTo(false));

        firstWriteFunction.writeValue(true);
        assertThat(collector.readValue(), equalTo(true));

        collector.removeChannel("second");
        assertThat(collector.readValue(), equalTo(true));

        secondWriteFunction = collector.addChannel("second");
        assertThat(collector.readValue(), equalTo(false));
    }

    @Test(expected=IllegalArgumentException.class)
    public void removingUnknownChannel() {
        ConnectionCollector collector = new ConnectionCollector();
        collector.removeChannel("never");
    }

    @Test(expected=IllegalStateException.class)
    public void usingDeregisteredWriteFunction() {
        ConnectionCollector collector = new ConnectionCollector();
        WriteFunction<Boolean> channelWriteFunction = collector.addChannel("first");
        collector.removeChannel("first");
        channelWriteFunction.writeValue(true);
    }

    @Test
    public void sameChannelMultipleTimes() {
        ConnectionCollector collector = new ConnectionCollector();
        WriteFunction<Boolean> channelWriteFunction1 = collector.addChannel("first");
        WriteFunction<Boolean> channelWriteFunction2 = collector.addChannel("first");
        assertThat(collector.readValue(), equalTo(false));

        channelWriteFunction1.writeValue(true);
        assertThat(channelWriteFunction1, sameInstance(channelWriteFunction2));
        assertThat(collector.readValue(), equalTo(true));

        collector.removeChannel("first");
        assertThat(collector.readValue(), equalTo(true));
        channelWriteFunction1.writeValue(false);
        assertThat(collector.readValue(), equalTo(false));

        collector.removeChannel("first");
        assertThat(collector.readValue(), equalTo(true));
    }
}
