/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene.rrdtool;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
public class GangliaRrdCluster {
    
    private final File baseDir;
    private Set<String> machines;
    private Set<String> signals;
    private Map<String, Set<String>> machinesToSignals;
    
    public GangliaRrdCluster(String baseDirectory) {
        baseDir = new File(baseDirectory);
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException(baseDirectory + "is not a directory");
        }
        scanDirectory();
    }
    
    private static Pattern rrdFilePattern = Pattern.compile(".rrd$", Pattern.CASE_INSENSITIVE);
    
    private void scanDirectory() {
        // All the subdirectories are taken to be machine names
        File[] subdirs = baseDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
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
                    return rrdFilePattern.matcher(name).matches();
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
    
}
