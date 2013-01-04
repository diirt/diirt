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
        if ("list".equals(args[0])) {
            String path = args[1];
            GangliaRrdCluster cluster = new GangliaRrdCluster(path);
            System.out.println("Machines: " + cluster.getMachines());
            System.out.println("Signals: " + cluster.getSignals());
        } else if ("plot".equals(args[0])) {
            String path = args[1];
            String signalX = args[2];
            String signalY = args[3];
            String signalZ = args[4];
            String filename = "out.png";
            Timestamp time = format.parse(args[5]);
            GangliaRrdCluster cluster = new GangliaRrdCluster(path);
            Point3DWithLabelDataset dataset = cluster.dataset(Arrays.asList(signalX, signalY, signalZ), time);
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
            Bubble2DGraphRenderer renderer = new Bubble2DGraphRenderer(800, 600);
            renderer.draw(image.createGraphics(), dataset);
            ImageIO.write(image, "png", new File(filename));
        } else {
            System.out.println("Command not found");
        }
    }
}
