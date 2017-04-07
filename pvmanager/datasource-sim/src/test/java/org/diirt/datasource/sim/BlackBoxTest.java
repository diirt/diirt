/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import org.diirt.util.time.TimeDuration;

import static org.diirt.datasource.ExpressionLanguage.*;
import static java.time.Duration.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.diirt.datasource.test.ThreadTestingUtil.*;

/**
 *
 * @author carcassi
 */
public class BlackBoxTest {

    @Test
    public void multipleRead1() throws Exception {
        String channelName = "ramp(0,100,1,0.01)";
        DataSource dataSource = new SimulationDataSource();

        PVReader<Object> pv1 = PVManager.read(channel(channelName)).from(dataSource).maxRate(TimeDuration.ofHertz(50));
        PVReader<Object> pv2 = PVManager.read(channel(channelName)).from(dataSource).maxRate(TimeDuration.ofHertz(50));
        waitForValue(pv1, ofMillis(200));
        waitForValue(pv2, ofMillis(200));

        assertThat(pv1.getValue(), not(nullValue()));
        assertThat(pv2.getValue(), not(nullValue()));
        assertThat(pv1.isConnected(), equalTo(true));
        assertThat(pv2.isConnected(), equalTo(true));
        pv1.close();
        pv2.close();
    }

}
