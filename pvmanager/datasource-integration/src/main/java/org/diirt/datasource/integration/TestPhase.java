/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderConfiguration;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterConfiguration;
import java.time.Duration;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public abstract class TestPhase {
    private final Map<String, PVWriter<?>> pvWriters = new ConcurrentHashMap<>();
    private final Map<String, PVReader<?>> pvReaders = new ConcurrentHashMap<>();
    private final Log phaseLog = new Log();

    private int debugLevel;

    public String getName() {
        return getClass().getSimpleName();
    }

    protected <T> TestPhase addReader(PVReaderConfiguration<T> reader, Duration maxRate) {
        PVReader<T> pvReader = reader.readListener(phaseLog.createReadListener()).maxRate(maxRate);
        pvReaders.put(pvReader.getName(), pvReader);
        if (getDebugLevel() >= 2) {
            System.out.println("Adding reader '" + pvReader.getName() + "'");
        }
        return this;
    }

    protected <T> TestPhase addWriter(String name, PVWriterConfiguration<T> writer) {
        PVWriter<T> pvWriter = writer.writeListener(phaseLog.<T>createWriteListener(name)).async();
        if (pvWriters.containsKey(name)) {
            throw new IllegalArgumentException("Writer called " + name + " already exists");
        }
        pvWriters.put(name, pvWriter);
        if (getDebugLevel() >= 2) {
            System.out.println("Adding writer '" + name + "'");
        }
        return this;
    }

    protected void write(String name, Object obj) {
        if (getDebugLevel() >= 2) {
            System.out.println("Writing '" + obj + "' to '" + name + "'");
        }
        @SuppressWarnings("unchecked")
        PVWriter<Object> pvWriter = (PVWriter<Object>) pvWriters.get(name);
        pvWriter.write(obj);
    }

    protected void waitFor(String name, String value, int msTimeout) {
        if (getDebugLevel() >= 2) {
            System.out.println("Waiting for '" + value + "' on '" + name + "'");
        }
        PVReaderValueCondition cond = new PVReaderValueCondition(VTypeMatchMask.VALUE, newVString(value, alarmNone(), timeNow()));
        @SuppressWarnings("unchecked")
        PVReader<Object> pvReader = (PVReader<Object>) pvReaders.get(name);
        boolean conditionMet = cond.waitOn(pvReader, msTimeout);
        if (!conditionMet) {
            throw new RuntimeException("Waiting failed on " + name + " value " + value);
        }
    }

    protected Object valueFor(String name) {
        @SuppressWarnings("unchecked")
        PVReader<Object> pvReader = (PVReader<Object>) pvReaders.get(name);
        return pvReader.getValue();
    }

    protected void pause(long ms) {
        if (getDebugLevel() >= 3) {
            System.out.println("Pause " + ms + " ms");
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestPhase.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }

    private void closeAll() {
        for (PVReader<?> pvReader : pvReaders.values()) {
            pvReader.close();
        }
    }

    public abstract void run() throws Exception;

    public abstract void verify(Log log);

    public void execute() {
        try {
            System.out.println("Starting " + getName());
            run();
        } catch (Exception ex) {
            Logger.getLogger(TestPhase.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean verifyFailed = false;
        closeAll();
        try {
            verify(phaseLog);
            System.out.println("Run " + phaseLog.getTestCount() + " tests");
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

    public int getDebugLevel() {
        return debugLevel;
    }

    public void setDebugLevel(int debugLevel) {
        if (debugLevel < 0 || debugLevel > 4) {
            throw new IllegalArgumentException("Debug level can only be 0, 1 or 2");
        }
        this.debugLevel = debugLevel;
    }
}
