/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import java.util.Arrays;
import org.epics.pvmanager.data.VEnum;
import org.epics.pvmanager.data.VString;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.data.VByteArray;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VFloatArray;
import org.epics.pvmanager.data.VIntArray;
import org.epics.pvmanager.data.VShortArray;
import org.epics.pvmanager.data.VStringArray;
import org.epics.pvmanager.jca.JCASupport;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class JCAClientExample {

    private static final String doublePV = "counter1";
    private static final String enumPV = doublePV + ".SCAN";
    private static final String intPV = doublePV + ".RVAL";
    private static final String stringPV = doublePV + ".EGU";
    private static final String doubleArrayPV = "SR:C00-Glb:G00<BETA:00>RB-X";

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(JCASupport.jca());

        testNativeTypeSupport();
        testVDoubleSupport();
        testVIntSupport();
        testVStringSupport();
        testVEnumSupport();
        testVDoubleArraySupport();
//        testVFloatArraySupport();
//        testVByteArraySupport();
//        testVShortArraySupport();
//        testVIntArraySupport();
//        testVStringArraySupport();

    }

    private static void logException(Exception ex) {
        if (ex != null)
            ex.printStackTrace(System.out);
    }

    private static void testNativeTypeSupport() throws Exception {
        {
            final PVReader<Object> pv = PVManager.read(channel(doublePV)).every(hz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VDouble value = (VDouble) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(stringPV)).every(hz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VString value = (VString) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(enumPV)).every(hz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VEnum value = (VEnum) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(intPV)).every(hz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VInt value = (VInt) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(doubleArrayPV)).every(hz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VDoubleArray value = (VDoubleArray) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
    }

    private static void testVFloatArraySupport() throws Exception {
        final PVReader<VFloatArray> pv = PVManager.read(vFloatArray(doublePV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleArraySupport() throws Exception {
        final PVReader<VDoubleArray> pv = PVManager.read(vDoubleArray(doubleArrayPV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVByteArraySupport() throws Exception {
        final PVReader<VByteArray> pv = PVManager.read(vByteArray(doublePV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVShortArraySupport() throws Exception {
        final PVReader<VShortArray> pv = PVManager.read(vShortArray(doublePV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntArraySupport() throws Exception {
        final PVReader<VIntArray> pv = PVManager.read(vIntArray(doublePV)).every(hz(10));
        logException(pv.lastException());
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVStringArraySupport() throws Exception {
        final PVReader<VStringArray> pv = PVManager.read(vStringArray(doublePV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleSupport() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(vDouble(doublePV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntSupport() throws Exception {
            final PVReader<VInt> pv = PVManager.read(vInt(intPV)).every(hz(10));
            pv.addPVReaderListener(new PVReaderListener() {

                @Override
                public void pvChanged() {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
                }
            });

            Thread.sleep(10000);

            pv.close();
    }

    private static void testVStringSupport() throws Exception {
        final PVReader<VString> pv = PVManager.read(vString(stringPV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVEnumSupport() throws Exception {
        final PVReader<VEnum> pv = PVManager.read(vEnum(enumPV)).every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimeStamp().asDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }
}
