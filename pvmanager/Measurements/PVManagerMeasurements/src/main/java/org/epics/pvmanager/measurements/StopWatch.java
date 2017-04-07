/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.measurements;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class StopWatch {

    public class Entry {
        public final long startNS;
        public final long endNS;

        private Entry(long startNS, long endNS) {
            this.startNS = startNS;
            this.endNS = endNS;
        }

        public long timeNS() {
            return endNS - startNS;
        }
    }

    private long currentStart;
    private final List<Entry> entries = new ArrayList<Entry>();

    public void start() {
        currentStart = System.nanoTime();
    }

    public void stop() {
        entries.add(new Entry(currentStart, System.nanoTime()));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public Entry lastEntry() {
        return entries.get(entries.size() - 1);
    }

    public void printStatistics(PrintStream out) {
        out.println("Results:");
        out.println("Number of attempts: " + getNAttempts());
        out.println("Average ns: " + getAverageNS());
        out.println("Std dev ns: " + getStdDevNS());
    }

    public void printStatisticsMS(PrintStream out) {
        out.println("Results:");
        out.println("Number of attempts: " + getNAttempts());
        out.println("Average ms: " + getAverageNS() / 1000000);
        out.println("Std dev ms: " + getStdDevNS() / 1000000);
    }

    public void printLast(PrintStream out) {
        out.println("Results:");
        out.println("Total time ms: " + lastEntry().timeNS() / 100000);
    }

    public int getNAttempts() {
        return entries.size();
    }

    public double getAverageNS() {
        double total = 0;
        for (Entry entry : entries) {
            total += entry.timeNS();
        }
        return total / getNAttempts();
    }

    public double getStdDevNS() {
        double total2 = 0;
        double avg = getAverageNS();
        for (Entry entry : entries) {
            total2 += (entry.timeNS() - avg) * (entry.timeNS() - avg);
        }
        return Math.sqrt(total2 / getNAttempts());
    }
}
