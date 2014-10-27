/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sim;

import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import java.util.concurrent.Callable;
import org.epics.pvmanager.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.diirt.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.epics.pvmanager.test.ThreadTestingUtil.*;

/**
 *
 * @author carcassi
 */
public class BlackBoxTest {

    @Test
    public void multipleRead1() throws Exception {
        String channelName = "ramp(0,100,1,0.01)";
        DataSource dataSource = new SimulationDataSource();
        
        PVReader<Object> pv1 = PVManager.read(channel(channelName)).from(dataSource).maxRate(ofHertz(50));
        PVReader<Object> pv2 = PVManager.read(channel(channelName)).from(dataSource).maxRate(ofHertz(50));
        waitForValue(pv1, ofMillis(100));
        waitForValue(pv2, ofMillis(100));

        assertThat(pv1.getValue(), not(nullValue()));
        assertThat(pv2.getValue(), not(nullValue()));
        assertThat(pv1.isConnected(), equalTo(true));
        assertThat(pv2.isConnected(), equalTo(true));
        pv1.close();
        pv2.close();
    }
    
}
