/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VString;
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
public class BlackBoxTest {

    @Test
    public void readAndWriteBlackBox1() throws Exception {
        String channelName = "test";
        DataSource dataSource = new LocalDataSource();
        
        PV<Object, Object> pv = PVManager.readAndWrite(channel(channelName)).from(dataSource).synchWriteAndReadEvery(hz(50));
        assertThat(pv.getValue(), nullValue());
        
        pv.write(10);
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
        pv.close();
    }

    @Test
    public void readAndWriteMap() throws Exception {
        String channel1 = "channel1";
        String channel2 = "channel2";
        DataSource dataSource = new LocalDataSource();
        
        PV<Map<String, Object>, Map<String, Object>> pv =
                PVManager.readAndWrite(rwMapOf(latestValueOf(channel("channel1")), latestValueOf(channel("channel2"))))
                .from(dataSource).synchWriteAndReadEvery(hz(50));
        assertThat(pv.getValue(), nullValue());

        Map<String, Object> newValues = new HashMap<String, Object>();
        newValues.put(channel1, "test");
        newValues.put(channel2, 10.0);
        pv.write(newValues);
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(((VString) pv.getValue().get(channel1)).getValue(), equalTo("test"));
        assertThat(((VDouble) pv.getValue().get(channel2)).getValue(), equalTo(10.0));
        pv.close();
    }
    
}
