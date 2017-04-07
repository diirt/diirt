/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import org.epics.graphene.BubbleGraph2DRenderer;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;

/**
 *
 * @author carcassi
 */
public class BubbleUtil {

    private static TimestampFormat format = new TimestampFormat("yyyy/MM/dd HH:mm:ss");

    public static void createBubblePlot(String filename, final Point3DWithLabelDataset dataset, final String urlPrototype, String path, String signalX, String signalY, String signalZ, Timestamp time) throws IOException {
        if (dataset.getXStatistics() == null) {
            System.out.println("Found no valid data for x");
            return;
        }
        if (dataset.getYStatistics() == null) {
            System.out.println("Found no valid data for y");
            return;
        }
        if (dataset.getZStatistics() == null) {
            System.out.println("Found no valid data for size");
            return;
        }
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");

        html.append("<html>\n");
        html.append("    <head>\n");
        html.append("        <title></title>\n");
        html.append("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
        html.append("    </head>\n");
        html.append("    <body>\n");
        html.append("        <h1>AGLT2 node performance details</h1>\n");
        html.append("        <center>\n");
        html.append("           <p> <span class=\"rotated\"><b>" + signalY + "</b></span>\n");
        html.append("           <img src=\"").append(filename).append(".png\" usemap=\"#graph\"><br/><b>" + signalX + "</b></p>\n");
        html.append("        </center>\n");
        html.append("        <map name=\"graph\">\n");
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(800, 600) {
            {
                this.bottomAreaMargin = 10;
                this.topAreaMargin = 10;
                this.leftAreaMargin = 10;
                this.rightAreaMargin = 10;
            }
            protected void newValue(double x, double y, double size, int i) {
                String url = urlPrototype.replaceAll("DATASETLABEL", dataset.getLabels().get(i));
                html.append("            <area shape=\"circle\" coords=\"" + (int) x + "," + (int) y + "," + (int) size + "\" href=\"" + url + "\" alt=\"" + dataset.getLabels().get(i) + "\">\n");
            }
        };
        renderer.draw(image.createGraphics(), dataset);
        html.append("        </map>\n");
        html.append("        <p>Size of the bubble represents <b>bytes_in</b></p>\n");
        html.append("        <p>Data gathered from directory " + path + ". Data timestamp " + format.format(time) + "</p>\n");
        html.append("        <style>\n");
        html.append("        .rotated {\n");
        html.append("        display:block;\n");
        html.append("        position:relative; top: 300px; right: 420px;\n");
        html.append("        -webkit-transform: rotate(-90deg);\n");
        html.append("        -moz-transform: rotate(-90deg);\n");
        html.append("        }\n");
        html.append("        </style>\n");
        html.append("    </body>\n");
        html.append("</html>\n");
        BufferedWriter writer =
                Files.newBufferedWriter(
                FileSystems.getDefault().getPath(".", filename + ".html"),
                Charset.forName("US-ASCII"));

        writer.write(html.toString());
        writer.close();
        ImageIO.write(image, "png", new File(filename + ".png"));
    }
}
