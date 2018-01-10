/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.Test;
import java.util.List;
import org.junit.BeforeClass;
import org.diirt.util.stats.Ranges;
import org.junit.Ignore;

/**
 *
 * @author sjdallst
 */
public class MultiYAxisGraph2DRendererTest extends BaseGraphTest<MultiAxisLineGraph2DRendererUpdate, MultiAxisLineGraph2DRenderer> {

    /**
     * Tests the functions in MultiAxisLineGraph2DRenderer
     */
    public MultiYAxisGraph2DRendererTest() {
        super("multiYAxisGraph2D");
    }
    private List<Point2DDataset> cosine2Dataset(){
        double [][] initialData= new double [2][100];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)(i)*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 2; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> cosine3Dataset(){
    double [][] initialData= new double [3][100];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = (double)i*Math.cos((double)j/100 * 6 * Math.PI);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear1Dataset() {
        double[][] initialData = new double[1][100];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 1; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear2Dataset(){
        double[][] initialData = new double[2][100];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 2; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear3Dataset(){
        double[][] initialData = new double[3][100];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 3; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear4Dataset(){
        double [][] initialData= new double [4][100];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 4; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear5Dataset(){
        double[][] initialData = new double[5][100];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 5; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear6Dataset(){
        double[][] initialData = new double[6][100];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = i;
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 6; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> linear10Dataset(){
        double [][] initialData= new double [10][100];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 100; j++){
                initialData[i][j] = i;
            }
        }

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 10; i++){
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }
    private List<Point2DDataset> rangeDataset() {
        double[][] initialData = new double[6][100];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 100; j++) {
                initialData[i][j] = Math.pow((double) j, ((double) i) / 5);
            }
        }

        //Creates a sparkline graph
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for (int i = 0; i < 6; i++) {
            data.add(Point2DDatasets.lineData(initialData[i]));
        }
        return data;
    }


    @Override
    public MultiAxisLineGraph2DRenderer createRenderer() {
        return new MultiAxisLineGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(MultiAxisLineGraph2DRenderer renderer) {
        List<Point2DDataset> data = linear1Dataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        renderer.draw(g, data);
        return image;
    }
    private static Point2DDataset largeDataset;

    /**
     * Sets up the large dataset used in the tests
     */
    @BeforeClass
    public static void setUpClass() {
        double[] waveform = Point2DTestDatasets.randomDataset();
        largeDataset = org.diirt.graphene.Point2DDatasets.lineData(waveform);
    }

    /**
     * Empties the memory used in the large dataset
     */
    @AfterClass
    public static void tearDownClass() {
        largeDataset = null;
    }

    @Test
    public void oneGraph() throws Exception {
        List<Point2DDataset> data = linear1Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.oneGraph", image);
    }

    @Test
    public void twoGraph() throws Exception {
        List<Point2DDataset> data = linear2Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image

        ImageAssert.compareImages("multiYAxisGraph2D.twoGraph", image);
    }

    @Test
    public void threeGraph() throws Exception {
        List<Point2DDataset> data = linear3Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.threeGraph", image);
    }

    @Test
    public void fiveGraph() throws Exception {
        List<Point2DDataset> data = linear5Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.fiveGraph", image);
    }

    @Test
    public void sixGraph() throws Exception {
        List<Point2DDataset> data = linear6Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraph", image);
    }

    @Test
    public void sixGraphWithRange() throws Exception {
        List<Point2DDataset> data = rangeDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraphWithRange", image);
    }

    @Test
    public void minGraphWidth1() throws Exception {
        List<Point2DDataset> data = rangeDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().minimumGraphWidth(600));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.minGraphWidth1", image);
    }

    @Test
    public void updateInterpolations() throws Exception {
        List<Point2DDataset> data = rangeDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.updateInterpolations", image);
    }

    @Test
    public void evenDivide() throws Exception {
        List<Point2DDataset>data = linear2Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.evenDivide", image);
    }

    @Test
    public void oneGraphSplit() throws Exception {
        //Creates a sparkline graph
        List<Point2DDataset> data = linear1Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.oneGraph", image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noGraphs() throws Exception {

        List<Point2DDataset> data = new ArrayList<Point2DDataset>();

        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.draw(g, data);

    }

    @Test
    public void multipleCosine() throws Exception {
        List<Point2DDataset> data = cosine3Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.multipleCosine", image);
    }


    @Test
    public void unevenDivide() throws Exception {
        List<Point2DDataset> data = linear3Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.unevenDivide", image);
    }


    @Test
    public void excessGraphs() throws Exception {
        List<Point2DDataset>data = linear5Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphs", image);
    }

    @Test
    public void excessGraphsUpdate() throws Exception {
        List<Point2DDataset>data = linear5Dataset();
        BufferedImage image = new BufferedImage(640, 550, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().imageHeight(550));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphsUpdate", image);
    }

    @Test
    public void excessGraphsAfterUpdate() throws Exception {
        List<Point2DDataset>data = linear5Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,550);
        renderer.update(renderer.newUpdate().imageHeight(550));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.excessGraphsAfterUpdate", image);
    }

    @Test
    public void updateMargins() throws Exception {
        List<Point2DDataset> data = cosine2Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().marginBetweenGraphs(50));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateMargins", image);
    }

    @Test
    public void updateRanges() throws Exception {
        List<Point2DDataset> data = linear4Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        HashMap<Integer, Range> map = new HashMap<Integer, Range>();
        map.put(1, Ranges.range(-50,50));
        renderer.update(renderer.newUpdate().setRanges(map));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateRanges", image);
    }

    @Test
    public void marginsTooBig() throws Exception {
        List<Point2DDataset> data = cosine3Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().marginBetweenGraphs(100));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.marginsTooBig", image);
    }

    @Test
    @Ignore
    public void resizing() throws Exception {
        List<Point2DDataset> data = new ArrayList<Point2DDataset>();
        for(int i = 0; i < 3; i++){
            data.add(Point2DTestDatasets.sineDataset(100, 50, 0, 1, 0, Ranges.range(0, 99)));
        }

        BufferedImage image = new BufferedImage(640, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().separateAreas(true));

        // Gradually reduce the image to simulate window being stretched
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(400));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(390));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(380));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(370));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(360));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(350));
        renderer.draw(g, data);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR).imageHeight(340));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.resizing", image);
    }

    @Test
    public void updateMinimumGraphHeights() throws Exception {
        List<Point2DDataset> data = linear10Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().minimumGraphHeight(50));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateMinimumGraphHeights", image);
    }

    @Test
    public void updateInterpolationsSplit() throws Exception {
        List<Point2DDataset> data = cosine2Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(640,480);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.update(renderer.newUpdate().separateAreas(true));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.split.updateInterpolations", image);
    }
}
