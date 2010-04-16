/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.bnl.nsls2.pvmanager.MockConnectionManager;
import gov.bnl.nsls2.pvmanager.MonitorRecipe;
import gov.bnl.nsls2.pvmanager.TimeDuration;
import gov.bnl.nsls2.pvmanager.TimedCacheCollector;
import gov.bnl.nsls2.pvmanager.ValueCache;
import gov.bnl.nsls2.pvmanager.jca.JCASupport;
import java.util.ArrayList;
import java.util.List;
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
public class SynchronizedArrayTest {

    public SynchronizedArrayTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Installs JCA types
        JCASupport.jca();
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
        int nPvs = 100;
        List<ValueCache<DBR_TIME_Double>> caches = new ArrayList<ValueCache<DBR_TIME_Double>>();
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<DBR_TIME_Double>> collectors = new ArrayList<TimedCacheCollector<DBR_TIME_Double>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<DBR_TIME_Double>(DBR_TIME_Double.class));
            collectors.add(new TimedCacheCollector<DBR_TIME_Double>(caches.get(i), TimeDuration.ms(1000)));
            MonitorRecipe monitorRecipe = new MonitorRecipe();
            monitorRecipe.cache = caches.get(i);
            monitorRecipe.collector = collectors.get(i);
            monitorRecipe.pvName = MockConnectionManager.mockPVName(1, 100, 300) + "linear";
            names.add("pv" + i);
            MockConnectionManager.mockData().monitor(monitorRecipe);
        }
        SynchronizedArrayAggregator<DBR_TIME_Double> aggregator =
                new SynchronizedArrayAggregator<DBR_TIME_Double>(names, collectors, TimeDuration.ms(100));

        // After 100 ms there should be one element
        Thread.sleep(100);
        aggregator.getValue();
        for (DBR_TIME_Double data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = ((double[]) data.getValue())[0];
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, (Double) ((double[]) data.getValue())[0]);
                }
            }
        }

        Thread.sleep(500);
        aggregator.getValue();
        for (DBR_TIME_Double data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = ((double[]) data.getValue())[0];
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, (Double) ((double[]) data.getValue())[0]);
                }
            }
        }
    }

}