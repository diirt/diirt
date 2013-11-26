/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderConfiguration;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.PVWriterConfiguration;
import org.epics.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
public abstract class TestPhase {
    private final Map<String, PVWriter<?>> pvWriters = new ConcurrentHashMap<>();
    private final List<PVReader<?>> pvReaders = new CopyOnWriteArrayList<>();
    private final Log phaseLog = new Log();
    
    protected <T> TestPhase addReader(PVReaderConfiguration<T> reader, TimeDuration maxRate) {
        PVReader<T> pvReader = reader.readListener(phaseLog.createReadListener()).maxRate(maxRate);
        pvReaders.add(pvReader);
        return this;
    }
    
    protected <T> TestPhase addWriter(String name, PVWriterConfiguration<T> writer) {
        PVWriter<T> pvWriter = writer.writeListener(phaseLog.<T>createWriteListener(name)).async();
        if (pvWriters.containsKey(name)) {
            throw new IllegalArgumentException("Writer called " + name + " already exists");
        }
        pvWriters.put(name, pvWriter);
        return this;
    }
    
    protected void write(String name, Object obj) {
        @SuppressWarnings("unchecked")
        PVWriter<Object> pvWriter = (PVWriter<Object>) pvWriters.get(name);
        pvWriter.write(obj);
    }
    
    protected void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestPhase.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
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
