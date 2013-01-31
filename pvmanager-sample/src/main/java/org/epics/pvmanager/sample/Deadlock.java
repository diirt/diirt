/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class Deadlock {

    static class PV {

        private PVReader<Object> reader;

        public PV(PVReader<Object> reader) {
            this.reader = reader;
        }

        //This synchronization will cause deadlock!!!
        public synchronized Object getValue() {
            return reader.getValue();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SetupUtil.defaultCASetup();

        final PVReader<Object> reader = PVManager.read(channel("sim://ramp(0,100,1,0.1)")).maxRate(
                ofMillis(100));

        final PV pv = new PV(reader);


        reader.addPVReaderListener(new PVReaderListener<Object>() {
            public void pvChanged(PVReaderEvent event) {

                System.out.println(Thread.currentThread().getName() + ": " + pv.getValue());

            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread2: " + pv.getValue());
                }
            }
        }, "thread2");
        thread2.start();
    }
}
