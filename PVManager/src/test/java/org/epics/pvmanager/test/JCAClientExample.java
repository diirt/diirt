/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.test;

import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.ThreadSwitch;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.jca.JCASupport;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class JCAClientExample {
    public static void main(String[] args) throws Exception {
        PVManager.setConnectionManager(JCASupport.jca());
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());

        final PV<VDouble> pv = PVManager.read(vDouble("SR:C01-BI:G02A<BPM:L1>Pos-X")).atHz(10);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }
}
