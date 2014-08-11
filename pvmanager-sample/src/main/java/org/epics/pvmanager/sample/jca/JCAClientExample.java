/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.vtype.VEnum;
import org.epics.vtype.VString;
import org.epics.vtype.VDouble;
import org.epics.vtype.VInt;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderListener;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VStringArray;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import org.epics.util.time.TimeDuration;
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
        System.out.println(Double.NEGATIVE_INFINITY);
        System.out.println(System.getProperty("java.library.path"));
        
        System.loadLibrary("jca");
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().jcaContextClass(JCALibrary.JNI_THREAD_SAFE).build());
        PVReader<Object> pvReader = PVManager.read(channel("counter1"))
                .readListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                System.out.println(event.getPvReader().getValue());
            }
        })
                .maxRate(TimeDuration.ofMillis(100));
        
        Thread.sleep(5000);
        pvReader.close();
        
        //        testNativeTypeSupport();
        //        testVDoubleSupport();
        //        testVIntSupport();
        //        testVStringSupport();
        //        testVEnumSupport();
        //        testVDoubleArraySupport();
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
        PVReader<VFloatArray> pv = PVManager.read(vFloatArray(doublePV))
                .readListener(new PVReaderListener<VFloatArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VFloatArray> event) {
                        PVReader<VFloatArray> pv = event.getPvReader();
                        System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));
        
        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleArraySupport() throws Exception {
        PVReader<VDoubleArray> pv = PVManager.read(vDoubleArray(doubleArrayPV))
                .readListener(new PVReaderListener<VDoubleArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VDoubleArray> event) {
                        PVReader<VDoubleArray> pv = event.getPvReader();
                        logException(pv.lastException());
                        if (pv.getValue() != null) {
                            System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                        }
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVByteArraySupport() throws Exception {
        PVReader<VByteArray> pv = PVManager.read(vByteArray(doublePV))
                .readListener(new PVReaderListener<VByteArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VByteArray> event) {
                        PVReader<VByteArray> pv = event.getPvReader();
                            System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVShortArraySupport() throws Exception {
        final PVReader<VShortArray> pv = PVManager.read(vShortArray(doublePV))
                .readListener(new PVReaderListener<VShortArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VShortArray> event) {
                        PVReader<VShortArray> pv = event.getPvReader();
                            System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));
        
        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntArraySupport() throws Exception {
        final PVReader<VIntArray> pv = PVManager.read(vIntArray(doublePV))
                .readListener(new PVReaderListener<VIntArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VIntArray> event) {
                        PVReader<VIntArray> pv = event.getPvReader();
                        logException(pv.lastException());
                        System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));
        
        Thread.sleep(10000);

        pv.close();
    }

    private static void testVStringArraySupport() throws Exception {
        final PVReader<VStringArray> pv = PVManager.read(vStringArray(doublePV))
                .readListener(new PVReaderListener<VStringArray>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VStringArray> event) {
                        PVReader<VStringArray> pv = event.getPvReader();
                        System.out.println(pv.getValue().getData() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVDoubleSupport() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(vDouble(doublePV))
                .readListener(new PVReaderListener<VDouble>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VDouble> event) {
                        PVReader<VDouble> pv = event.getPvReader();
                        System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVIntSupport() throws Exception {
            final PVReader<VInt> pv = PVManager.read(vInt(intPV))
                .readListener(new PVReaderListener<VInt>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VInt> event) {
                        PVReader<VInt> pv = event.getPvReader();
                            System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                    }
                })
                .maxRate(ofHertz(10));

            Thread.sleep(10000);

            pv.close();
    }

    private static void testVStringSupport() throws Exception {
        final PVReader<VString> pv = PVManager.read(vString(stringPV))
                .readListener(new PVReaderListener<VString>() {

                    @Override
                    public void pvChanged(PVReaderEvent<VString> event) {
                        PVReader<VString> pv = event.getPvReader();
                        logException(pv.lastException());
                        if (pv.getValue() != null) {
                            System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                        }
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }

    private static void testVEnumSupport() throws Exception {
        final PVReader<VEnum> pv = PVManager.read(vEnum(enumPV))
                .readListener(new PVReaderListener<VEnum>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VEnum> event) {
                        PVReader<VEnum> pv = event.getPvReader();
                        logException(pv.lastException());
                        if (pv.getValue() != null) {
                            System.out.println(pv.getValue().getValue() + " " + pv.getValue().getTimestamp().toDate() + " " + pv.getValue().getAlarmSeverity());
                        }
                    }
                })
                .maxRate(ofHertz(10));

        Thread.sleep(10000);

        pv.close();
    }
}
