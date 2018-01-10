/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.diirt.datasource.PVManager;

import static org.diirt.datasource.ExpressionLanguage.*;

import org.diirt.datasource.PVReader;
import org.diirt.datasource.loc.LocalDataSource;
import org.diirt.util.time.TimeDuration;

/**
 * Load test to see the effect on the performance of the scanning.
 * <p>
 * The test opens a number of local pvs. Since there is no load associated
 * on keeping the local pv open, the total cpu load can be assumed to be
 * the scanning. If can also be used to optimize memory consumption.
 * <p>
 * 2012/11/12 - 1,000 pvs, 1.8%; 10,000 pvs, 18.9%; 100,000 pvs, 34%. GC time ~0%
 *
 * @author carcassi
 */
public class ScannerLoadTest {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new LocalDataSource());

        System.out.println("nChannels \"timeToStart (ms)\" \"avgLoad (ms)\"");
        for (int i = 0; i < 9; i++) {
            int nPvs = (int) Math.pow(4, i);
            profile(nPvs);
        }
    }

    static OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static double measureLoad(int nSec) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        double totalSum = 0.0;
        int count = 0;
        while (System.currentTimeMillis() - timeStart < nSec * 1000) {
            Thread.sleep(250);
            double load = bean.getProcessCpuLoad();
            if (load >= 0) {
                totalSum += load;
                count++;
            }
        }
        return totalSum / count;
    }

    public static void waitForZeroLoad(int timeoutSec) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - timeStart < timeoutSec * 1000) {
            double load = bean.getProcessCpuLoad();
            if (load == 0.0) {
                return;
            }
            Thread.sleep(250);
        }
    }

    public static void profile(int nPvs) throws Exception {
        List<PVReader<Object>> pvs = new ArrayList<>();
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < nPvs; i++) {
            //PVReader<Object> pv = PVManager.read(constant(new Object())).maxRate(TimeDuration.TimeDuration.ofHertz(50));
            PVReader<Object> pv = PVManager.read(channel("channel " + i)).maxRate(TimeDuration.ofHertz(50));
            pvs.add(pv);
        }
        long startTime = System.currentTimeMillis() - timeStart;

        double avgLoad = measureLoad(5);

        System.out.println(nPvs + " " + startTime + " " + avgLoad);

        for (PVReader<Object> pv : pvs) {
            pv.close();
        }

        waitForZeroLoad(5);
    }
}
