/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
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
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import static org.epics.util.time.TimeDuration.*;

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
        System.out.println("Test");
        System.out.println(System.getProperty("java.library.path"));
        
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().jcaContextClass(JCALibrary.JNI_THREAD_SAFE).build());

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
            final PVReader<Object> pv = PVManager.read(channel(doublePV)).maxRate(ofHertz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VDouble value = (VDouble) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(stringPV)).maxRate(ofHertz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VString value = (VString) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(enumPV)).maxRate(ofHertz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VEnum value = (VEnum) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(intPV)).maxRate(ofHertz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VInt value = (VInt) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
        {
            final PVReader<Object> pv = PVManager.read(channel(doubleArrayPV)).maxRate(ofHertz(10));
            Thread.sleep(250);
            logException(pv.lastException());
            VDoubleArray value = (VDoubleArray) pv.getValue();
            System.out.println(value.getClass());
            pv.close();
        }
    }

    private static void testVFloatArraySupport() throws Exception {
        final PVReader<VFloatArray> pv = PVManager.read(vFloatArray(doublePV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleArraySupport() throws Exception {
        final PVReader<VDoubleArray> pv = PVManager.read(vDoubleArray(doubleArrayPV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVByteArraySupport() throws Exception {
        final PVReader<VByteArray> pv = PVManager.read(vByteArray(doublePV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVShortArraySupport() throws Exception {
        final PVReader<VShortArray> pv = PVManager.read(vShortArray(doublePV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntArraySupport() throws Exception {
        final PVReader<VIntArray> pv = PVManager.read(vIntArray(doublePV)).maxRate(ofHertz(10));
        logException(pv.lastException());
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVStringArraySupport() throws Exception {
        final PVReader<VStringArray> pv = PVManager.read(vStringArray(doublePV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(Arrays.toString(pv.getValue().getArray()) + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleSupport() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(vDouble(doublePV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntSupport() throws Exception {
            final PVReader<VInt> pv = PVManager.read(vInt(intPV)).maxRate(ofHertz(10));
            pv.addPVReaderListener(new PVReaderListener() {

                @Override
                public void pvChanged() {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                }
            });

            Thread.sleep(10000);

            pv.close();
    }

    private static void testVStringSupport() throws Exception {
        final PVReader<VString> pv = PVManager.read(vString(stringPV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVEnumSupport() throws Exception {
        final PVReader<VEnum> pv = PVManager.read(vEnum(enumPV)).maxRate(ofHertz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                logException(pv.lastException());
                if (pv.getValue() != null) {
                    System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                }
            }
        });

        Thread.sleep(10000);

        pv.close();
    }
}
