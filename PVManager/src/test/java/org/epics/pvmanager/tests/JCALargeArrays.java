/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VShortArray;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class JCALargeArrays {
   static     long  start = System.currentTimeMillis();
    public static void main(String[] args) throws Exception {
        System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "1");
//        System.setProperty("gov.aps.jca.jni.JNIContext.max_array_bytes", "10000000");
        JCADataSource source = new JCADataSource();
        source.getContext().printInfo();
        PVManager.setDefaultDataSource(source);
        final PVReader<Object> pv = PVManager.read(channel("carcassi:compressExample2")).maxRate(ofHertz(50));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged(PVReader pvReader) {
                long pause = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                if (pv.getValue() instanceof VShortArray)
                    System.out.println(pause + " ms - " + pv.getValue() + " - count " + ((VShortArray) pv.getValue()).getArray().length);
                else if (pv.getValue() instanceof VDoubleArray)
                    System.out.println(pause + " ms - " + pv.getValue() + " - count " + ((VDoubleArray) pv.getValue()).getArray().length);
                else
                    System.out.println(pause + " ms - " + pv.getValue());
                Exception ex = pv.lastException();
                if (ex != null) {
                    ex.printStackTrace();
                }
                  
            }
        });
        
        Thread.sleep(5000);
        pv.close();
        PVManager.getDefaultDataSource().close();
    }
}
