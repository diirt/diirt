/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
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
public class RrdToolBubbleGraphMain {
    
    private static TimestampFormat format = new TimestampFormat("yyyyMMddHHmmss");
    
    public static void main(String[] args) throws Exception {
        List<String> signals = new ArrayList<>();
        Timestamp start = null;
        Timestamp end = null;
        String filename = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-s")) {
                i++;
                start = format.parse(args[i]);
            } else if (arg.equals("-e")) {
                i++;
                end = format.parse(args[i]);
            } else if (arg.equals("-o")) {
                i++;
                filename = args[i];
                if (!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
            } else  {
                signals.add(arg);
            }
        }
        List<TimeSeries> data = RrdToolDB.fetchData(signals, start, end);
        Map<String, TimeSeries> series = new HashMap<>();
        for (int i = 0; i < signals.size(); i++) {
            series.put(signals.get(i), data.get(i));
        }
        TimeSeriesMulti correlated = TimeSeriesMulti.synchronizeSeries(series);
        final Point3DWithLabelDataset dataset = Point3DWithLabelDatasets.build(correlated.getValues().get(signals.get(0)), 
                                                correlated.getValues().get(signals.get(1)), 
                                                correlated.getValues().get(signals.get(2)), 
                                                Collections.nCopies(correlated.getValues().get(signals.get(0)).size(), "label"));
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");

        html.append("<html>\n");
        html.append("    <head>\n");
        html.append("        <title></title>\n");
        html.append("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
        html.append("    </head>\n");
        html.append("    <body>\n");
        html.append("        <img src=\"").append(filename.substring(0, filename.length() - 4)).append("\" usemap=\"#graph\">\n");
        Bubble2DGraphRenderer renderer = new Bubble2DGraphRenderer(800, 600) {
            protected void newValue(double x, double y, double size, int i) {
                html.append("    <area shape=\"circle\" coords=\"" + (int) x + "," + (int) y + "," + (int) size + "\" href=\"#" + dataset.getLabels().get(i) + "\" alt=\"" + dataset.getLabels().get(i) + "\">\n");
            }
        };
        renderer.draw(image.createGraphics(), dataset);
        html.append("    </body>\n");
        html.append("</html>\n");
        BufferedWriter writer =
                Files.newBufferedWriter(
                FileSystems.getDefault().getPath(".", filename.substring(0, filename.length() - 4) + ".html"),
                Charset.forName("US-ASCII"));

        writer.write(html.toString());
        writer.close();
        ImageIO.write(image, "png", new File(filename));
    }
}
