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
import org.junit.Ignore;

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
    public void rectangles() throws Exception {
        double listOfData [] = new double[10*10];
            for(int i = 0; i < (10*10); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.rectangles", image);
            
    }
    
    @Test
    public void rectanglesSolid() throws Exception {
        double listOfData [] = new double[640*10];
            for(int i = 0; i < (640*10); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.rectanglesSolid", image);
            
    }
    
    @Test
    public void smallX() throws Exception {
        double listOfData [] = new double[640*10];
            for(int i = 0; i < (640*10); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 10), 10);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallX", image);
            
    }
    
    @Test
    public void smallXSolid() throws Exception {
        double listOfData [] = new double[10*480];
            for(int i = 0; i < (10*480); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXSolid", image);
            
    }
    
    @Test
    public void smallY() throws Exception {
        double listOfData [] = new double[10*480];
            for(int i = 0; i < (10*480); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 10), 10, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallY", image);
            
    }
    
    @Test
    public void smallYSolid() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = 1;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallYSolid", image);
            
    }
    
    @Test
    public void smallXAndY() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = i;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXAndY", image);
            
    }
    
    @Test
    public void smallXAndYWithLegend() throws Exception {
        // TODO: fix formatting (indents do not follow)
        
        double listOfData [] = new double[640*480];
        Random rand = new Random(0);
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = rand.nextDouble();
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            // TODO: replace all the IntensityGraph2DRendererUpdate constructors with:
            // renderer.newUpdate()
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            update.drawLegend(true);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXAndYWithLegend", image);
            
    }
    
    @Test
    public void smallXandYSinglePixels() throws Exception {
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
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            renderer.update(renderer.newUpdate().colorMap(NumberColorMaps.GRAY));
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXAndYSinglePixels", image);
            
    }
    
    //Tests ColorScheme with JET colors.
    @Test
    public void smallXAndYJet() throws Exception {
        double listOfData [] = new double[640*480];
        Random rand = new Random(0);
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = rand.nextDouble();
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            update.colorMap(NumberColorMaps.JET);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXAndYJet", image);
            
    } 
    
    //Single-value test.
    @Test
    public void smallXAndYSingleValue() throws Exception {
        double listOfData [] = new double[640*480];
            for(int i = 0; i < (640*480); i++){
                listOfData[i] = 10000000;
            }
            ArrayDouble dataList = new ArrayDouble(listOfData);
            Cell2DDataset data = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 640), 640, RangeUtil.range(0, 480), 480);
            BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            update.colorMap(NumberColorMaps.JET);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.smallXAndYSingleValue", image);
            
    }
    
    @Test
    public void customBoundaries() throws Exception {
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100), new ArrayDouble(0, 1, 2, 4, 9, 16, 25, 36, 49, 64, 81, 100));
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.colorMap(NumberColorMaps.JET);
        renderer.optimizeColorScheme = true;
        renderer.update(update);
        renderer.draw(graphBuffer, data);
        renderer.drawArray(graphBuffer,data);

        ImageAssert.compareImages("intensityGraph2D.customBoundaries", image);
    }
    
    @Test
    //@Ignore("This dataset does not draw correctly")
    public void test13() throws Exception {
        int size = 100;
        double[] boundaries = new double[size];
        boundaries[0] = 0;
        for (int i = 1; i < boundaries.length; i++) {
            boundaries[i] = 90.0 + i * 10.0 / size;
        }
        Cell2DDataset data = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x * y;
            }
        }, new ArrayDouble(boundaries), new ArrayDouble(boundaries));
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(100, 100);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.colorMap(NumberColorMaps.JET);
        renderer.update(update);
        renderer.draw(graphBuffer, data);
        renderer.drawArray(graphBuffer,data);

        ImageAssert.compareImages("intensityGraph2D.13", image);
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
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.colorMap(NumberColorMaps.JET);
        update.xAxisRange(AxisRanges.absolute(20,80));
        update.yAxisRange(AxisRanges.absolute(20,80));
        renderer.update(update);
        renderer.draw(graphBuffer,data);
        
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
        GraphBuffer graphBuffer = new GraphBuffer(image);
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640, 480);
        IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
        update.drawLegend(true);
        update.colorMap(NumberColorMaps.JET);
        update.xAxisRange(AxisRanges.absolute(-20, 120));
        update.yAxisRange(AxisRanges.absolute(-20,120));
        renderer.update(update);
        renderer.draw(graphBuffer,data);
        
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
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.drawLegend(true);
            update.colorMap(NumberColorMaps.JET);
            update.rightMargin(20);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
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
            GraphBuffer graphBuffer = new GraphBuffer(image);
            IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(640,480);
            IntensityGraph2DRendererUpdate update = new IntensityGraph2DRendererUpdate();
            update.colorMap(NumberColorMaps.JET);
            update.rightMargin(20);
            renderer.update(update);
            renderer.draw(graphBuffer, data);
            
            ImageAssert.compareImages("intensityGraph2D.UpdateRightMargin", image);
            
    }
}
