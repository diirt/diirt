/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample.jca;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VShortArray;
import static org.diirt.util.time.TimeDuration.*;

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
        final PVReader<Object> pv = PVManager.read(channel("carcassi:compressExample2"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        PVReader<Object> pv = event.getPvReader();
                        long pause = System.currentTimeMillis() - start;
                        start = System.currentTimeMillis();
                        if (pv.getValue() instanceof VShortArray) {
                            System.out.println(pause + " ms - " + pv.getValue() + " - count " + ((VShortArray) pv.getValue()).getData().size());
                        } else if (pv.getValue() instanceof VDoubleArray) {
                            System.out.println(pause + " ms - " + pv.getValue() + " - count " + ((VDoubleArray) pv.getValue()).getData().size());
                        } else {
                            System.out.println(pause + " ms - " + pv.getValue());
                        }
                        Exception ex = pv.lastException();
                        if (ex != null) {
                            ex.printStackTrace();
                        }
                    }
                })
                .maxRate(ofHertz(50));
        
        Thread.sleep(5000);
        pv.close();
        PVManager.getDefaultDataSource().close();
    }
}
