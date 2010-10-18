/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.data.VEnum;
import org.epics.pvmanager.data.VString;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.ThreadSwitch;
import org.epics.pvmanager.jca.JCASupport;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class JCAClientExample {

    private static final String channelName = "SR:C01-BI:G02A<BPM:L1>Pos-X";

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(JCASupport.jca());
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());

        testVDoubleSupport();
        testVIntSupport();
        testVStringSupport();
        testVEnumSupport();

    }

    private static void testVDoubleSupport() throws Exception {
        final PV<VDouble> pv = PVManager.read(vDouble(channelName)).atHz(10);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntSupport() throws Exception {
            final PV<VInt> pv = PVManager.read(vInt(channelName)).atHz(10);
            pv.addPVValueChangeListener(new PVValueChangeListener() {

                @Override
                public void pvValueChanged() {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
                }
            });

            Thread.sleep(10000);

            pv.close();
    }

    private static void testVStringSupport() throws Exception {
        final PV<VString> pv = PVManager.read(vString(channelName)).atHz(10);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVEnumSupport() throws Exception {
        final PV<VEnum> pv = PVManager.read(vEnum(channelName + ".SCAN")).atHz(10);
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
