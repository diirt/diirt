/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Map;
import org.epics.pvmanager.data.VDouble;
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
    public void readAndWriteMap() throws Exception {
//        String channelName = "test";
//        DataSource dataSource = new LocalDataSource();
//        
//        PV<Map<String, Object>, Map<String, Object>> pv = PVManager.readAndWrite(mapOf(latestValueOf(channels("channel1", "channel2", "channel3")))).from(dataSource).synchWriteAndReadEvery(hz(50));
//        assertThat(pv.getValue(), nullValue());
//        
//        pv.write(10);
//        Thread.sleep(50);
//        assertThat(pv.getValue(), not(nullValue()));
//        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
    }
    
}
