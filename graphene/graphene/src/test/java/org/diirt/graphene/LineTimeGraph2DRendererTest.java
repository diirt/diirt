/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.graphene.TimeSeriesDatasets;
import org.diirt.graphene.TimeSeriesDataset;
import org.diirt.graphene.LineTimeGraph2DRenderer;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.LineTimeGraph2DRendererUpdate;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class LineTimeGraph2DRendererTest {
    
    public LineTimeGraph2DRendererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test1() throws Exception {
        Timestamp start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(6000)),
                start.plus(TimeDuration.ofMillis(8500)),
                start.plus(TimeDuration.ofMillis(12500)),
                start.plus(TimeDuration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.1", image);
    }
    
    @Test
    public void test2() throws Exception {
        Timestamp start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(6000)),
                start.plus(TimeDuration.ofMillis(8500)),
                start.plus(TimeDuration.ofMillis(12500)),
                start.plus(TimeDuration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.2", image);
    }
    
    @Test
    public void test3() throws Exception {
        Timestamp start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(6000)),
                start.plus(TimeDuration.ofMillis(8500)),
                start.plus(TimeDuration.ofMillis(12500)),
                start.plus(TimeDuration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.3", image);
    }
    
    @Test
    public void test4() throws Exception {
        Timestamp start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(6000)),
                start.plus(TimeDuration.ofMillis(8500)),
                start.plus(TimeDuration.ofMillis(12500)),
                start.plus(TimeDuration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.4", image);
    }
    
    @Ignore
    @Test
    public void extraGraphArea() throws Exception {
        Timestamp start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(6000)),
                start.plus(TimeDuration.ofMillis(8500)),
                start.plus(TimeDuration.ofMillis(12500)),
                start.plus(TimeDuration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(TimeDuration.ofMillis(1000)))))
               .axisRange(AxisRanges.fixed(0, 15)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea", image);
    }
    
}
