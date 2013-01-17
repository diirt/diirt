/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
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
import org.epics.graphene.Bubble2DGraphRenderer;
import org.epics.graphene.Point3DWithLabelDataset;

/**
 *
 * @author carcassi
 */
public class BubbleUtil {
    public static void createBubblePlot(String filename, final Point3DWithLabelDataset dataset) throws IOException {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");

        html.append("<html>\n");
        html.append("    <head>\n");
        html.append("        <title></title>\n");
        html.append("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
        html.append("    </head>\n");
        html.append("    <body>\n");
        html.append("        <img src=\"").append(filename).append(".png\" usemap=\"#graph\">\n");
        html.append("        <map name=\"graph\">\n");
        Bubble2DGraphRenderer renderer = new Bubble2DGraphRenderer(800, 600) {
            protected void newValue(double x, double y, double size, int i) {
                html.append("            <area shape=\"circle\" coords=\"" + (int) x + "," + (int) y + "," + (int) size + "\" href=\"#" + dataset.getLabels().get(i) + "\" alt=\"" + dataset.getLabels().get(i) + "\">\n");
            }
        };
        renderer.draw(image.createGraphics(), dataset);
        html.append("        </map>\n");
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
