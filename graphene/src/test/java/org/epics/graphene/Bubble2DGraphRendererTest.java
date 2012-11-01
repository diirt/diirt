/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class Bubble2DGraphRendererTest {
    
    public Bubble2DGraphRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test1() throws Exception {
        Random rand = new Random(0);
        int size = 50;
        ArrayDouble x = new ArrayDouble(new double[size], false);
        ArrayDouble y = new ArrayDouble(new double[size], false);
        ArrayDouble z = new ArrayDouble(new double[size], false);
        String[] labelSet = new String[] {"First", "Second", "Third", "Fourth", "Fifth"};
        List<String> labels = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            x.setDouble(i, rand.nextGaussian());
            y.setDouble(i, rand.nextGaussian());
            z.setDouble(i, 5.0 + rand.nextGaussian());
            labels.add(labelSet[rand.nextInt(labelSet.length)]);
        }
        
        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Bubble2DGraphRenderer renderer = new Bubble2DGraphRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubble2D.1", image);
    }
}
