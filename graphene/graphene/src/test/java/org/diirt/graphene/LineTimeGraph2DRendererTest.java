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
import org.diirt.util.time.Timestamp;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

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
    
    @Test
    public void testPreviousValueNaNMiddle1() throws Exception {
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, Double.NaN, 4, 5, 6),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000)),
                start.plus(TimeDuration.ofMillis(2000)),
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(4000)),
                start.plus(TimeDuration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.1", image);
    }
    
    @Test
    public void testPreviousValueNaNStart1() throws Exception {
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(Double.NaN, 2, 0, 4, 5, 6),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000)),
                start.plus(TimeDuration.ofMillis(2000)),
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(4000)),
                start.plus(TimeDuration.ofMillis(4777))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.2", image);
    }
    
    @Test
    public void testPreviousValueNaNEnd1() throws Exception {
	//Compare with testPreviousValueEndNotNaN() to see difference
	//there will always be a line drawn to the end, because there was a
	//previous value; however, when the end value is NaN, we do not
	//jump up at the end
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 333 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 0 , 4, 5, Double.NaN ),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000)),
                start.plus(TimeDuration.ofMillis(2000)),
                start.plus(TimeDuration.ofMillis(3345)),
                start.plus(TimeDuration.ofMillis(4000)),
                start.plus(TimeDuration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.3", image);
    }
    
    @Test
    public void testPreviousValueEndNotNaN1() throws Exception {
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 333 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 0 , 4, 5, 6 ),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000)),
                start.plus(TimeDuration.ofMillis(2000)),
                start.plus(TimeDuration.ofMillis(3345)),
                start.plus(TimeDuration.ofMillis(4000)),
                start.plus(TimeDuration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.1", image);
    }
    
    @Test
    public void testPreviousValueStartNaN2() throws Exception {
	//test how previous value deals with NaN with only 2 data points
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 111 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(Double.NaN , 2 ),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.4", image);
    }
    
    @Test
    public void testPreviousValueMultiNaNEnd1() throws Exception {
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 3 , Double.NaN , Double.NaN , Double.NaN ),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1000)),
                start.plus(TimeDuration.ofMillis(2000)),
                start.plus(TimeDuration.ofMillis(3000)),
                start.plus(TimeDuration.ofMillis(4000)),
                start.plus(TimeDuration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.5", image);
    }
    
    @Test
    public void testPreviousValueBigFluctuation() throws Exception {
	Timestamp start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(10 , 0 , 100 , 0 , 10 , 0 , 100 , -100 , 0 , 10 , 100 , -100 , 100 , -100 , 0  ),
                Arrays.asList(start,
                start.plus(TimeDuration.ofMillis(1)),
                start.plus(TimeDuration.ofMillis(2)),
                start.plus(TimeDuration.ofMillis(3)),
                start.plus(TimeDuration.ofMillis(4)),
                start.plus(TimeDuration.ofMillis(5)),
		start.plus(TimeDuration.ofMillis(6)),
		start.plus(TimeDuration.ofMillis(7)),
		start.plus(TimeDuration.ofMillis(8)),
		start.plus(TimeDuration.ofMillis(9)),
		start.plus(TimeDuration.ofMillis(10)),
		start.plus(TimeDuration.ofMillis(11)),
		start.plus(TimeDuration.ofMillis(12)),
		start.plus(TimeDuration.ofMillis(13)),
		start.plus(TimeDuration.ofMillis(14))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.2", image);
    }
}
