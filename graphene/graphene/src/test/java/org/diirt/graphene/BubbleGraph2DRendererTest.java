/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.diirt.util.array.ArrayDouble;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author carcassi
 */
public class BubbleGraph2DRendererTest extends BaseGraphTest<BubbleGraph2DRendererUpdate, BubbleGraph2DRenderer> {

    public BubbleGraph2DRendererTest() {
        super("bubbleGraph2D");
    }

    private Point3DWithLabelDataset defaultDataset() {
        Random rand = new Random(0);
        int size = 50;
        ArrayDouble x = new ArrayDouble(new double[size], false);
        ArrayDouble y = new ArrayDouble(new double[size], false);
        ArrayDouble z = new ArrayDouble(new double[size], false);
        String[] labelSet = new String[]{"First", "Second", "Third", "Fourth", "Fifth"};
        List<String> labels = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            x.setDouble(i, rand.nextGaussian());
            y.setDouble(i, rand.nextGaussian());
            z.setDouble(i, 5.0 + rand.nextGaussian());
            labels.add(labelSet[rand.nextInt(labelSet.length)]);
        }

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    private Point3DWithLabelDataset randomDataset() {
        Random rand = new Random(0);
        int size = 50;
        ArrayDouble x = new ArrayDouble(new double[size], false);
        ArrayDouble y = new ArrayDouble(new double[size], false);
        ArrayDouble z = new ArrayDouble(new double[size], false);
        String[] labelSet = new String[]{"First", "Second", "Third", "Fourth", "Fifth"};
        List<String> labels = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            x.setDouble(i, rand.nextGaussian());
            y.setDouble(i, rand.nextGaussian());
            z.setDouble(i, 5.0 + rand.nextGaussian());
            labels.add(labelSet[rand.nextInt(labelSet.length)]);
        }
        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    private Point3DWithLabelDataset consecDataset() {
        ArrayDouble x = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble y = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble z = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<String> labels = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth",
                "First", "Second", "Third", "Fourth", "Fifth");

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    private Point3DWithLabelDataset negativeExceptLastDataset() {
        ArrayDouble x = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble y = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble z = new ArrayDouble(-1, 2, -3, 4, -5, 6, -7, 8, -9, 10);
        List<String> labels = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth",
                "First", "Second", "Third", "Fourth", "Fifth");

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;

    }

    private Point3DWithLabelDataset negativeDataset() {
        ArrayDouble x = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble y = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble z = new ArrayDouble(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10);
        List<String> labels = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth",
                "First", "Second", "Third", "Fourth", "Fifth");

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    private Point3DWithLabelDataset zerosDataset() {
        ArrayDouble x = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble y = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble z = new ArrayDouble(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        List<String> labels = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth",
                "First", "Second", "Third", "Fourth", "Fifth");

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    private Point3DWithLabelDataset perfectSquareDataset() {
        ArrayDouble x = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble y = new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ArrayDouble z = new ArrayDouble(1, 4, 9, 16, 25, 36, 49, 64, 81, 100);
        List<String> labels = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth",
                "First", "Second", "Third", "Fourth", "Fifth");

        Point3DWithLabelDataset data = Point3DWithLabelDatasets.build(x, y, z, labels);
        return data;
    }

    @Override
    public BubbleGraph2DRenderer createRenderer() {
        return new BubbleGraph2DRenderer(300, 200);
    }

    @Override
    public BufferedImage draw(BubbleGraph2DRenderer renderer) {
        Point3DWithLabelDataset data = defaultDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        return image;
    }

    @Test
    public void randomValues() throws Exception {

        Point3DWithLabelDataset data = randomDataset();
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(300, 200);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.randomValues", image);

        renderer.update(renderer.newUpdate().highlightFocusValue(true).focusPixel(178, 90));
        graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.focusPixel1", image);
        assertThat(renderer.getFocusValueIndex(), equalTo(48));

        renderer.update(renderer.newUpdate().highlightFocusValue(true).focusPixel(171, 88));
        graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.focusPixel2", image);
        assertThat(renderer.getFocusValueIndex(), equalTo(6));
    }

    @Test
    public void consecutiveValues() throws Exception {

        Point3DWithLabelDataset data = consecDataset();
        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(600, 399);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.consecutiveValues", image);
    }

    @Test
    public void negativeValuesExceptLast() throws Exception {
        Point3DWithLabelDataset data = negativeExceptLastDataset();
        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(600, 399);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.negativeValuesExceptLast", image);
    }

    @Test
    public void negativeValues() throws Exception {
        Point3DWithLabelDataset data = negativeDataset();
        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(600, 399);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.negativeValues", image);
    }

    @Test
    public void allZeros() throws Exception {
        Point3DWithLabelDataset data = zerosDataset();
        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(600, 399);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.allZeros", image);
    }

    @Test
    public void perfectSquare() throws Exception {
        Point3DWithLabelDataset data = perfectSquareDataset();
        BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_3BYTE_BGR);
        BubbleGraph2DRenderer renderer = new BubbleGraph2DRenderer(600, 400);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("bubbleGraph2D.perfectSquare", image);
    }
}
