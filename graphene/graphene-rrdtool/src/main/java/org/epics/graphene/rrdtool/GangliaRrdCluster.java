/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.graphene.Point3DWithLabelDatasets;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class GangliaRrdCluster {

    private static Logger log = Logger.getLogger(GangliaRrdCluster.class.getName());

    private final File baseDir;
    private Set<String> machines;
    private Set<String> signals;
    private Map<String, Set<String>> machinesToSignals;
    private Pattern filePattern = rrdFilePattern;
    private Pattern dirPattern = null;

    public GangliaRrdCluster(String baseDirectory) {
        log.fine("Reading Ganglia directory at " + baseDirectory);
        baseDir = new File(baseDirectory);
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException(baseDirectory + "is not a directory");
        }
        scanDirectory();
        log.finest("Machines " + machines + "\nSignals " + signals);
    }

    public void setDirPattern(Pattern filePattern) {
        this.dirPattern = filePattern;
        scanDirectory();
    }

    public Pattern getDirPattern() {
        return dirPattern;
    }

    private static Pattern rrdFilePattern = Pattern.compile(".*\\.rrd", Pattern.CASE_INSENSITIVE);

    private void scanDirectory() {
        // All the subdirectories are taken to be machine names
        File[] subdirs = baseDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isDirectory()) {
                    return false;
                }
                if (dirPattern != null && !dirPattern.matcher(pathname.getName()).matches()) {
                    return false;
                }
                return true;
            }
        });

        signals = new HashSet<>();
        machines = new HashSet<>();
        machinesToSignals = new HashMap<>();

        for (int i = 0; i < subdirs.length; i++) {
            File subdir = subdirs[i];
            String[] rrdFiles = subdir.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return filePattern.matcher(name).matches();
                }
            });
            HashSet<String> machineSignals = new HashSet<>();
            for (int j = 0; j < rrdFiles.length; j++) {
                String rrdFileName = rrdFiles[j];
                String signal = rrdFileName.substring(0, rrdFileName.length() - 4);
                signals.add(signal);
                machineSignals.add(signal);
            }
            String machine = subdir.getName();
            machinesToSignals.put(machine, machineSignals);
            machines.add(machine);
        }
    }

    public Set<String> getMachines() {
        return machines;
    }

    public Set<String> getSignals() {
        return signals;
    }

    private RrdToolReader reader = new RrdToolReader();

    public Point3DWithLabelDataset dataset(List<String> machineNames, List<String> signals, Timestamp time) {
        if (signals.size() != 3) {
            throw new IllegalArgumentException("3 signal names are required");
        }

        if (machineNames.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one machine");
        }

        return Point3DWithLabelDatasets.build(dataset(machineNames, signals.get(0), time),
                dataset(machineNames, signals.get(1), time),
                dataset(machineNames, signals.get(2), time),
                machineNames);
    }

    public Point3DWithLabelDataset dataset(List<String> signals, Timestamp time) {
        log.fine("Creating dataset for signals " + signals + " at " + time);
        List<String> machineNames = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : machinesToSignals.entrySet()) {
            String machine = entry.getKey();
            Set<String> machineSignals = entry.getValue();
            if (machineSignals.containsAll(signals)) {
                machineNames.add(machine);
            }
        }

        return dataset(machineNames, signals, time);
    }

    private ListDouble dataset(List<String> machineNames, String signal, Timestamp time) {
        log.fine("Creating dataset for signal " + signal + " on machines " + machineNames + " at " + time);
        double[] values = new double[machineNames.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = getValue(machineNames.get(i), signal, time);
        }
        return new ArrayDouble(values);
    }

    public double getValue(String machine, String signal, Timestamp time) {
        log.fine("Get value for signal " + signals + " on machine " + machine + " at " + time);
        Set<String> machineSignals = machinesToSignals.get(machine);
        if (machineSignals == null || !machineSignals.contains(signal)) {
            return Double.NaN;
        }
        TimeSeriesMulti data = reader.readFile(baseDir.getAbsolutePath() + File.separator + machine + File.separator + signal + ".rrd",
                                           "AVERAGE", time.minus(TimeDuration.ofMinutes(30)), time.plus(TimeDuration.ofMinutes(30)));
        ListDouble series = data.getValues().values().iterator().next();
        List<Timestamp> times = data.getTime();

        // Get the value that is the one right before the time becomes
        // greater
        int i = 0;
        while (i < times.size() && times.get(i).compareTo(time) <= 0) {
            i++;
        }
        if (i != 0) {
            i--;
        }

        return series.getDouble(i);
    }

}
