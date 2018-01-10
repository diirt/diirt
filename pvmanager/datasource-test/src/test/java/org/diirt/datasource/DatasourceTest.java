/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.diirt.datasource.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class DatasourceTest {

    public DatasourceTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private class MockDataSource extends DataSource {

        public MockDataSource(boolean writeable) {
            super(writeable);
        }

        @Override
        protected String channelHandlerLookupName(String channelName) {
            if ("changeit".equals(channelName))
                return "first";
            return channelName;
        }

        @Override
        protected ChannelHandler createChannel(String channelName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    };

    @Mock ChannelHandler channel1;
    @Mock ChannelHandler channel2;

    @Test
    public void connect1() {
        // A simple recipe with one channel

        ReadExpressionTester exp = new ReadExpressionTester(latestValueOf(channel("first")));
        ReadRecipe dataRecipe = exp.getReadRecipe();

        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");

        dataSource.connectRead(dataRecipe);

        verify(dataSource, timeout(100)).channel("first");
        verify(channel1, timeout(100)).addReader(exp.recipeFor("first").getReadSubscription());
    }

    @Test
    public void connect2() {
        // A recipe with two channels

        ReadExpressionTester exp = new ReadExpressionTester(mapOf(latestValueOf(channel("first").and(channel("second")))));
        ReadRecipe dataRecipe = exp.getReadRecipe();

        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");
        doReturn(channel2).when(dataSource).createChannel("second");

        dataSource.connectRead(dataRecipe);

        verify(dataSource, timeout(100)).channel("first");
        verify(dataSource, timeout(100)).channel("second");
        verify(channel1, timeout(100)).addReader(exp.recipeFor("first").getReadSubscription());
        verify(channel2, timeout(100)).addReader(exp.recipeFor("second").getReadSubscription());
    }

    @Test
    public void connect3() {
        // Two recipe with the same channel: create only one

        ReadExpressionTester exp1 = new ReadExpressionTester(latestValueOf(channel("first")));
        ReadExpressionTester exp2 = new ReadExpressionTester(latestValueOf(channel("first")));
        ReadRecipe dataRecipe1 = exp1.getReadRecipe();
        ReadRecipe dataRecipe2 = exp2.getReadRecipe();

        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");

        dataSource.connectRead(dataRecipe1);
        dataSource.connectRead(dataRecipe2);

        verify(dataSource, timeout(100).times(2)).channel("first");
        verify(dataSource, timeout(100)).createChannel("first");
        verify(channel1, timeout(100)).addReader(exp1.recipeFor("first").getReadSubscription());
        verify(channel1, timeout(100)).addReader(exp2.recipeFor("first").getReadSubscription());
    }

    @Test
    public void connect4() {
        // A simple recipe with one channel

        ReadExpressionTester exp = new ReadExpressionTester(latestValueOf(channel("changeit")));
        ReadRecipe dataRecipe = exp.getReadRecipe();

        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("changeit");

        dataSource.connectRead(dataRecipe);

        verify(dataSource, timeout(100)).channel("changeit");
        verify(dataSource, timeout(100)).createChannel("changeit");
        verify(channel1, timeout(100)).addReader(exp.recipeFor("changeit").getReadSubscription());
        assertThat(dataSource.getChannels().get("first"), not(equalTo(null)));
        assertThat(dataSource.getChannels().get("changeit"), equalTo(null));
    }
}
