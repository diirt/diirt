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
import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class ScatterStatistics2DGraphRendererTest {
    
    public ScatterStatistics2DGraphRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    public static Statistics gaussianStatistics(Random rand, double avg, double stdDev, int count) {
        double[] data = new double[count];
        for (int i = 0; i < count; i++) {
            data[i] = rand.nextGaussian() * stdDev + avg;
        }
        return StatisticsUtil.statisticsOf(new ArrayDouble(data));
    }
    
    @Test
    public void test1() throws Exception {
        List<Statistics> x = new ArrayList<Statistics>();
        List<Statistics> y = new ArrayList<Statistics>();
        Random rand = new Random(0);
        double[] xAvg = new double[] {0,1,2,3};
        double[] yAvg = new double[] {0,1,2,3};
        for (int i = 0; i < xAvg.length; i++) {
            x.add(gaussianStatistics(rand, xAvg[i], 1, 500));
            y.add(gaussianStatistics(rand, yAvg[i], 1, 500));
        }
        
        Statistics2DDataset data = Statistics2DDatasets.statisticsData(x,y);
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        ScatterStatistics2DGraphRenderer renderer = new ScatterStatistics2DGraphRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("scatterStat2D.1", image);
    }
}
