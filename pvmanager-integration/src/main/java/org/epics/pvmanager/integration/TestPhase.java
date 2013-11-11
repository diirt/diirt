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
    private List<PVReader<?>> pvReaders = new CopyOnWriteArrayList<>();
    private Log phaseLog = new Log();
    private boolean done = false;
    
    protected <T> TestPhase addReader(PVReaderConfiguration<T> reader, TimeDuration maxRate) {
        PVReader<T> pvReader = reader.readListener(phaseLog.createListener()).maxRate(maxRate);
        return this;
    }
    
    public abstract void run() throws Exception;
    
    public abstract void verify(Log log);
    
    public void execute() {
        try {
            run();
        } catch (Exception ex) {
            Logger.getLogger(TestPhase.class.getName()).log(Level.SEVERE, null, ex);
        }
        verify(phaseLog);
        if (!phaseLog.isCorrect()) {
            for (String error : phaseLog.getErrors()) {
                System.out.println(error);
            }
        }
    }

}
