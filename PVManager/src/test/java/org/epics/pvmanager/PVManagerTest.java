/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.epics.pvmanager;

import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class PVManagerTest {

    public PVManagerTest() {
    }

    @Test(expected=IllegalStateException.class)
    public void lackDataSource() {
        PVManager.setDefaultDataSource(null);
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());

        PVManager.read(channel("test")).atHz(10);
    }

    @Test
    public void overrideDataSource() {
        PVManager.setDefaultDataSource(new JCADataSource());
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());

        PVManager.read(channel("test")).from(SimulationDataSource.simulatedData()).atHz(10);
    }

    @Test(expected=IllegalStateException.class)
    public void lackThreadSwitch() {
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
        PVManager.setDefaultThread(null);

        PVManager.read(channel("test")).atHz(10);
    }

    @Test
    public void overrideThreadSwitch() {
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());

        PVManager.read(channel("test")).andNotify(ThreadSwitch.onSwingEDT()).atHz(10);
    }

}