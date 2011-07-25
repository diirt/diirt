/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Map;
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
    public void readMap() throws Exception {
        DataSource dataSource = new LocalDataSource();
        
        @SuppressWarnings("unchecked")
        PVReader<Map<String, Object>> pvReader = PVManager.read(mapOf(latestValueOf(channel("channel1")), latestValueOf(channel("channel2")))).from(dataSource).every(hz(50));
        pvReader.close();
    }

    @Test
    public void readWriteMap() throws Exception {
        DataSource dataSource = new LocalDataSource();
        
        @SuppressWarnings("unchecked")
        PV<Map<String, Object>, Map<String, Object>> pv = PVManager.readAndWrite(rwMapOf(latestValueOf(channel("channel1")), latestValueOf(channel("channel2")))).from(dataSource).synchWriteAndReadEvery(hz(50));
        pv.close();
    }
    
}
