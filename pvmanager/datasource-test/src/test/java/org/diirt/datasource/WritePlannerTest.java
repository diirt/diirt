/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.Arrays;
import java.util.Map;
import java.util.Collections;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class WritePlannerTest {

    public WritePlannerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock ChannelHandler channel1;
    @Mock ChannelHandler channel2;
    @Mock ChannelHandler channel3;
    @Mock ChannelHandler channel4;

    @Test
    public void noDependencies() {
        when(channel1.getChannelName()).thenReturn("channel1");
        when(channel2.getChannelName()).thenReturn("channel2");
        when(channel3.getChannelName()).thenReturn("channel3");

        WritePlanner planner = new WritePlanner();
        planner.addChannel(channel1, 6.28, Collections.<String>emptySet());
        planner.addChannel(channel2, 3.14, Collections.<String>emptySet());
        planner.addChannel(channel3, 1.57, Collections.<String>emptySet());

        Map<ChannelHandler, Object> nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(3));
        assertThat(nextChannels.get(channel1), equalTo((Object) 6.28));
        assertThat(nextChannels.get(channel2), equalTo((Object) 3.14));
        assertThat(nextChannels.get(channel3), equalTo((Object) 1.57));
        assertThat(planner.isDone(), equalTo(false));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));
        planner.removeChannel("channel1");
        assertThat(planner.isDone(), equalTo(false));
        planner.removeChannel("channel2");
        assertThat(planner.isDone(), equalTo(false));
        planner.removeChannel("channel3");
        assertThat(planner.isDone(), equalTo(true));
    }

    @Test
    public void someDependencies1() {
        when(channel1.getChannelName()).thenReturn("channel1");
        when(channel2.getChannelName()).thenReturn("channel2");
        when(channel3.getChannelName()).thenReturn("channel3");

        WritePlanner planner = new WritePlanner();
        planner.addChannel(channel1, 6.28, Collections.<String>emptySet());
        planner.addChannel(channel2, 3.14, Collections.singletonList("channel1"));
        planner.addChannel(channel3, 1.57, Collections.singletonList("channel1"));

        Map<ChannelHandler, Object> nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel1), equalTo((Object) 6.28));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel1");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(2));
        assertThat(nextChannels.get(channel2), equalTo((Object) 3.14));
        assertThat(nextChannels.get(channel3), equalTo((Object) 1.57));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel2");
        assertThat(planner.isDone(), equalTo(false));
        planner.removeChannel("channel3");
        assertThat(planner.isDone(), equalTo(true));
    }

    @Test
    public void someDependencies2() {
        when(channel1.getChannelName()).thenReturn("channel1");
        when(channel2.getChannelName()).thenReturn("channel2");
        when(channel3.getChannelName()).thenReturn("channel3");

        WritePlanner planner = new WritePlanner();
        planner.addChannel(channel1, 6.28, Collections.<String>emptySet());
        planner.addChannel(channel2, 3.14, Collections.singletonList("channel1"));
        planner.addChannel(channel3, 1.57, Collections.singletonList("channel2"));

        Map<ChannelHandler, Object> nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel1), equalTo((Object) 6.28));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel1");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel2), equalTo((Object) 3.14));
        assertThat(planner.isDone(), equalTo(false));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel2");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel3), equalTo((Object) 1.57));
        assertThat(planner.isDone(), equalTo(false));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel3");
        assertThat(planner.isDone(), equalTo(true));
    }

    @Test
    public void someDependencies3() {
        when(channel1.getChannelName()).thenReturn("channel1");
        when(channel2.getChannelName()).thenReturn("channel2");
        when(channel3.getChannelName()).thenReturn("channel3");
        when(channel4.getChannelName()).thenReturn("channel4");

        WritePlanner planner = new WritePlanner();
        planner.addChannel(channel1, 6.28, Collections.<String>emptySet());
        planner.addChannel(channel2, 3.14, Collections.singletonList("channel1"));
        planner.addChannel(channel3, 1.57, Collections.singletonList("channel1"));
        planner.addChannel(channel4, 12.56, Arrays.asList("channel2", "channel3"));

        Map<ChannelHandler, Object> nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel1), equalTo((Object) 6.28));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel1");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(2));
        assertThat(nextChannels.get(channel2), equalTo((Object) 3.14));
        assertThat(nextChannels.get(channel3), equalTo((Object) 1.57));
        assertThat(planner.isDone(), equalTo(false));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));

        planner.removeChannel("channel2");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));
        assertThat(planner.isDone(), equalTo(false));

        planner.removeChannel("channel3");
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(1));
        assertThat(nextChannels.get(channel4), equalTo((Object) 12.56));
        assertThat(planner.isDone(), equalTo(false));

        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));
        planner.removeChannel("channel4");
        assertThat(planner.isDone(), equalTo(true));

    }
}
