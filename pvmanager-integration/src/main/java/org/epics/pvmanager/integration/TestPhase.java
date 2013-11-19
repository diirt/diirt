/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderConfiguration;
import org.epics.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
public abstract class TestPhase {
    private final List<PVReader<?>> pvReaders = new CopyOnWriteArrayList<>();
    private final Log phaseLog = new Log();
    
    protected <T> TestPhase addReader(PVReaderConfiguration<T> reader, TimeDuration maxRate) {
        PVReader<T> pvReader = reader.readListener(phaseLog.createListener()).maxRate(maxRate);
        pvReaders.add(pvReader);
        return this;
    }
    
    private void closeAll() {
        for (PVReader<?> pvReader : pvReaders) {
            pvReader.close();
        }
    }
    
    public abstract void run() throws Exception;
    
    public abstract void verify(Log log);
    
    public void execute() {
        try {
            run();
        } catch (Exception ex) {
            Logger.getLogger(TestPhase.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean verifyFailed = false;
        closeAll();
        try {
            verify(phaseLog);
        } catch (Exception ex) {
            System.out.println("Verify failed:");
            ex.printStackTrace(System.out);
            verifyFailed = true;
        }
        if (!phaseLog.isCorrect() || verifyFailed) {
            System.out.println("Errors:");
            for (String error : phaseLog.getErrors()) {
                System.out.println(error);
            }
            System.out.println("Log:");
            phaseLog.print(System.out);
        }
    }
    
}
