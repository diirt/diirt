/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
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
    
    //Tests ColorScheme with JET colors.
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
            double start = 0;
            double end = 0;
            start = System.currentTimeMillis();
            renderer.drawTest(g,data,image);
            end = System.currentTimeMillis();
            System.out.println(end-start);
            start = System.currentTimeMillis();
            renderer.draw(g, data);
            end = System.currentTimeMillis();
            System.out.println(end-start);
            
            ImageAssert.compareImages("intensityGraph2D.10", image);
            
    } 
    
    //Single-value test.
    @Test
    public void test11() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = 10000000;
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
            
            ImageAssert.compareImages("intensityGraph2D.11", image);
            
    }
    
    @Test
    public void test12() throws Exception {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.valueColorScheme(ColorScheme.JET);
        renderer.update(update);
        renderer.draw(g, data);

        ImageAssert.compareImages("intensityGraph2D.12", image);
    }
    
    @Test
    public void ZoomInTest() throws Exception {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.valueColorScheme(ColorScheme.JET);
        update.xAxisRange(AxisRanges.absolute(20,80));
        update.yAxisRange(AxisRanges.absolute(20,80));
        renderer.update(update);
        renderer.draw(g,data);
        ImageAssert.compareImages("intensityGraph2D.ZoomIn", image);
    }
    
    @Test
    public void ZoomOutTest() throws Exception {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.valueColorScheme(ColorScheme.JET);
        update.xAxisRange(AxisRanges.absolute(-20, 120));
        update.yAxisRange(AxisRanges.absolute(-20,120));
        renderer.update(update);
        renderer.draw(g,data);
        ImageAssert.compareImages("intensityGraph2D.ZoomOut", image);
    }
    
    @Test
    public void UpdateRightMarginWLegendTest() throws Exception {
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
            update.rightMargin(20);
            renderer.update(update);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.UpdateRightMarginWLegend", image);
            
    }
    
    @Test
    public void UpdateRightMarginTest() throws Exception {
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
            update.valueColorScheme(ColorScheme.JET);
            update.rightMargin(20);
            renderer.update(update);
            renderer.draw(g, data);
            
            ImageAssert.compareImages("intensityGraph2D.UpdateRightMargin", image);
            
    }
}
