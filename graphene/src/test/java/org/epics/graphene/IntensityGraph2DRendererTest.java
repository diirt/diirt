/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.epics.util.array.*;

/**
 * 
 * @authors asbarber, jkfeng, sjdallst
 */
public class IntensityGraph2DRendererTest {
    public IntensityGraph2DRendererTest(){  
    }
        private static Cell2DDataset largeDataset;
    
    /**
     * Sets up the large dataset used in the tests
     * @throws Exception 
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random rand = new Random(1);
        int nSamples = 1000*1000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        ArrayDouble data = new ArrayDouble(waveform);
        largeDataset = Cell2DDatasets.linearRange(data,RangeUtil.range(0, 1000), 1000, RangeUtil.range(0, 1000), 1000);
    }

    /**
     * Empties the memory used in the large dataset
     * @throws Exception 
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }
    
    
    /**
     * Tests case of:
     * <ul>
     *      <li>Min Value = Last Value</li>
     *      <li>There exists more than one min value</li>
     * </ul>
     * 
     * @throws Exception Test fails
     */    
    @Test
    public void test1() throws Exception {
        double listOfData [] = new double[10*10];
            for(int i = 0; i < (10*10); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.1", image);
            
    }
    
    @Test
    public void test2() throws Exception {
        double listOfData [] = new double[640*10];
            for(int i = 0; i < (640*10); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.2", image);
            
    }
    
    @Test
    public void test3() throws Exception {
        double listOfData [] = new double[640*10];
            for(int i = 0; i < (640*10); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.3", image);
            
    }
    
    @Test
    public void test4() throws Exception {
        double listOfData [] = new double[10*480];
            for(int i = 0; i < (10*480); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.4", image);
            
    }
    
    @Test
    public void test5() throws Exception {
        double listOfData [] = new double[10*480];
            for(int i = 0; i < (10*480); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.5", image);
            
    }
    
    @Test
    public void test6() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.6", image);
            
    }
    
    @Test
    public void test7() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.7", image);
            
    }
    
    @Test
    public void test8() throws Exception {
        double listOfData [] = new double[640*480];
        Random rand = new Random(0);
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = rand.nextDouble();
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            renderer.update(update);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.8", image);
            
    }
    
    @Test
    public void test9() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                if(i%1240 == 0)
                    listOfData[i] = 0;
                else
                    listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            renderer.update(update);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.9", image);
            
    }
    
    @Test
    public void test10() throws Exception {
        double listOfData [] = new double[640*480];
        Random rand = new Random(0);
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = rand.nextDouble();
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = (Graphics2D) image.getGraphics();
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            update.valueColorScheme(ColorScheme.JET);
            renderer.update(update);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.10", image);
            
    }
}
