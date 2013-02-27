/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.io;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.epics.graphene.Point2DDataset;

/**
 *
 * @author carcassi
 */
public class CommaSeparatedValueIO {
    
    private static NumberFormat format = new DecimalFormat("0.####################");
    
    public static void write(Point2DDataset dataset, Writer writer) 
    throws IOException {
        writer.write("x,y");
        for (int i = 0; i < dataset.getCount(); i++) {
            writer.append("\n");
            writer.append(format.format(dataset.getXValues().getDouble(i)));
            writer.append(',');
            writer.append(format.format(dataset.getYValues().getDouble(i)));
        }
    }
    
    public static String write(Point2DDataset dataset) 
    throws IOException {
        StringWriter writer = new StringWriter();
        write(dataset, writer);
        return writer.toString();
    }
}
