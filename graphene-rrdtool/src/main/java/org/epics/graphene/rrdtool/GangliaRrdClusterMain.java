/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;

/**
 *
 * @author carcassi
 */
public class GangliaRrdClusterMain {
    
    private static TimestampFormat format = new TimestampFormat("yyyyMMddHHmmss");
    
    public static void main(String[] args) throws Exception {
        Pattern filePattern = Pattern.compile(".*\\.local", Pattern.CASE_INSENSITIVE);
        if ("list".equals(args[0])) {
            String path = args[1];
            GangliaRrdCluster cluster = new GangliaRrdCluster(path);
            cluster.setDirPattern(filePattern);
            System.out.println("Machines: " + cluster.getMachines());
            System.out.println("Signals: " + cluster.getSignals());
        } else if ("plot".equals(args[0])) {
            String path = args[1];
            String signalX = args[2];
            String signalY = args[3];
            String signalZ = args[4];
            String filename = "out";
            Timestamp time = format.parse(args[5]);
            GangliaRrdCluster cluster = new GangliaRrdCluster(path);
            cluster.setDirPattern(filePattern);
            Point3DWithLabelDataset dataset = cluster.dataset(Arrays.asList(signalX, signalY, signalZ), time);
            StringBuilder html = new StringBuilder();
            BubbleUtil.createBubblePlot(filename, dataset,
                    "http://ganglia-um.aglt2.org/ganglia/?m=load_one&r=day&s=ascending&c=UM-Worker-Nodes&h=DATASETLABEL&sh=1&hc=4&z=small",
                    path, signalX, signalY, signalZ, time);
        } else {
            System.out.println("Command not found");
        }
    }
}
