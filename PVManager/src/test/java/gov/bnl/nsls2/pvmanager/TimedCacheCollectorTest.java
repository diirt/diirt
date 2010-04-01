/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.bnl.nsls2.pvmanager.types.JCATypeSupport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class TimedCacheCollectorTest {

    public TimedCacheCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void correctNumberOfValuesInCache() throws InterruptedException {
        JCATypeSupport.addJCATypeSupport();
        ValueCache<DBR_TIME_Double> cache =
                new ValueCache<DBR_TIME_Double>(DBR_TIME_Double.class);
        TimedCacheCollector<DBR_TIME_Double> collector =
                new TimedCacheCollector<DBR_TIME_Double>(cache, 1000);
        MonitorRecipe monitorRecipe = new MonitorRecipe();
        monitorRecipe.cache = cache;
        monitorRecipe.collector = collector;
        monitorRecipe.pvName = MockConnectionManager.mockPVName(1, 100, 300);
        MockConnectionManager.useMockConnectionManager();
        ConnectionManager.getInstance().monitor(monitorRecipe);

        // After 100 ms there should be one element
        Thread.sleep(100);
        assertTrue(Math.abs(1 - collector.getData().size()) < 2);

        // After another second there should be 10 or 11 samples
        Thread.sleep(1000);
        assertTrue(Math.abs(11 - collector.getData().size()) < 2);
        
        // After another second there should be 10 or 11 samples
        Thread.sleep(1000);
        assertTrue(Math.abs(11 - collector.getData().size()) < 2);
    }

}