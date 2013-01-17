/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.epics.graphene.Bubble2DGraphRenderer;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.graphene.Point3DWithLabelDatasets;
import org.epics.graphene.Scatter2DGraphRenderer;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
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
            html.append("<p>From directory: " + path + "</p>");
            html.append("<p>X: " + signalX + "</p>");
            html.append("<p>Y: " + signalY + "</p>");
            html.append("<p>Size: " + signalZ + "</p>");
            BubbleUtil.createBubblePlot(filename, dataset,
                    "http://ganglia-um.aglt2.org/ganglia/?m=load_one&r=day&s=ascending&c=UM-Worker-Nodes&h=DATASETLABEL&sh=1&hc=4&z=small",
                    html.toString());
        } else {
            System.out.println("Command not found");
        }
    }
}
