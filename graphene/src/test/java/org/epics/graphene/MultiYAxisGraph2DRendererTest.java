/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT All rights
 * reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Test;
import java.util.List;
import org.junit.BeforeClass;
import org.epics.util.array.*;

/**
 *
 * @author sjdallst
 */
public class MultiYAxisGraph2DRendererTest extends BaseGraphTest<MultiYAxisGraph2DRendererUpdate, MultiYAxisGraph2DRenderer> {

    /**
     * Tests the functions in MultiYAxisGraph2DRenderer
     */
    public MultiYAxisGraph2DRendererTest() {
        super("multiYAxisGraph2D");
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
    public MultiYAxisGraph2DRenderer createRenderer() {
        return new MultiYAxisGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(MultiYAxisGraph2DRenderer renderer) {
        List<Point2DDataset> data = linear1Dataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        renderer.draw(g, data);
        return image;
    }
    private static Point2DDataset largeDataset;

    /**
     * Sets up the large dataset used in the tests
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        double[] waveform = Point2DTestDatasets.randomDataset();
        largeDataset = org.epics.graphene.Point2DDatasets.lineData(waveform);
    }

    /**
     * Empties the memory used in the large dataset
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }

    @Test
    public void oneGraph() throws Exception {
        List<Point2DDataset> data = linear1Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.oneGraph", image);
    }

    @Test
    public void twoGraph() throws Exception {
        List<Point2DDataset> data = linear2Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image

        ImageAssert.compareImages("multiYAxisGraph2D.twoGraph", image);
    }

    @Test
    public void threeGraph() throws Exception {
        List<Point2DDataset> data = linear3Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.threeGraph", image);
    }

    @Test
    public void fiveGraph() throws Exception {
        List<Point2DDataset> data = linear5Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.fiveGraph", image);
    }

    @Test
    public void sixGraph() throws Exception {
        List<Point2DDataset> data = linear6Dataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraph", image);
    }

    @Test
    public void sixGraphWithRange() throws Exception {
        List<Point2DDataset> data = rangeDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.sixGraphWithRange", image);
    }

    @Test
    public void minGraphWidth1() throws Exception {
        List<Point2DDataset> data = rangeDataset();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
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
        MultiYAxisGraph2DRenderer renderer = new MultiYAxisGraph2DRenderer(640, 480);
        renderer.update(renderer.newUpdate().interpolation(InterpolationScheme.LINEAR));
        renderer.draw(g, data);

        //Compares to correct image
        ImageAssert.compareImages("multiYAxisGraph2D.updateInterpolations", image);
    }
}
