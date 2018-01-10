/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
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
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Ignore;
import static org.junit.Assume.assumeTrue;

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
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.1", image);
    }

    @Test
    public void test2() throws Exception {
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.2", image);
    }

    @Test
    public void test3() throws Exception {
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.3", image);
    }

    @Test
    public void test4() throws Exception {
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,10),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.4", image);
    }

    @Test
    public void testPreviousValueNaNMiddle1() throws Exception {
        Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, Double.NaN, 4, 5, 6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.1", image);
    }

    @Test
    public void testPreviousValueNaNStart1() throws Exception {
    Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(Double.NaN, 2, 0, 4, 5, 6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(4777))));
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
        Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 333 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 0 , 4, 5, Double.NaN ),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3345)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.3", image);
    }

    @Test
    public void testPreviousValueEndNotNaN1() throws Exception {
    Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 333 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 0 , 4, 5, 6 ),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3345)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(5000))));
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
        Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 111 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(Double.NaN , 2 ),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.4", image);
    }

    @Test
    public void testPreviousValueMultiNaNEnd1() throws Exception {
        Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1, 2, 3 , Double.NaN , Double.NaN , Double.NaN ),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.NaN.5", image);
    }

    @Test
    public void testPreviousValueBigFluctuation() throws Exception {
    Instant start = TimeScalesTest.create(2014, 1 , 19 , 11 , 0 , 0 , 0 );
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(10 , 0 , 100 , 0 , 10 , 0 , 100 , -100 , 0 , 10 , 100 , -100 , 100 , -100 , 0  ),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1)),
                start.plus(Duration.ofMillis(2)),
                start.plus(Duration.ofMillis(3)),
                start.plus(Duration.ofMillis(4)),
                start.plus(Duration.ofMillis(5)),
        start.plus(Duration.ofMillis(6)),
        start.plus(Duration.ofMillis(7)),
        start.plus(Duration.ofMillis(8)),
        start.plus(Duration.ofMillis(9)),
        start.plus(Duration.ofMillis(10)),
        start.plus(Duration.ofMillis(11)),
        start.plus(Duration.ofMillis(12)),
        start.plus(Duration.ofMillis(13)),
        start.plus(Duration.ofMillis(14))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.previousValue.2", image);
    }

    @Test
    public void extraGraphArea1() throws Exception {
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(0,4,3,7,6,11),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(50000)))))
               .axisRange(AxisRanges.fixed(0, 15)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.1", image);
    }

    @Test
    public void extraGraphArea2() throws Exception {
    //test using a small extra graph area gap. The gap is only 1 second in
    //this test case
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(9000)),
                start.plus(Duration.ofMillis(12000)),
                start.plus(Duration.ofMillis(15000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(16000)))))
               .axisRange(AxisRanges.fixed(0, 15)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.2", image);
    }

    @Test
    public void extraGraphArea3() throws Exception {
    //test using a huge extra graph area gap. The gap is a minute, while
    //the data points are just second apart
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(1000)),
                start.plus(Duration.ofMillis(2000)),
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(4000)),
                start.plus(Duration.ofMillis(5000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(65000)))))
               .axisRange(AxisRanges.fixed(0, 66)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.3", image);
    }

    @Test
    public void extraGraphArea4() throws Exception {
    //test extra graph area with the data points falling perfectly
    //in line with the gridlines
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 0);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(10000)),
                start.plus(Duration.ofMillis(20000)),
                start.plus(Duration.ofMillis(30000)),
                start.plus(Duration.ofMillis(40000)),
                start.plus(Duration.ofMillis(50000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(60000)))))
               .axisRange(AxisRanges.fixed(0, 10)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.4", image);
    }

    @Test
    public void lessGraphArea1() throws Exception {
    //test using an absolute time axis that doesn't fit everything.
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(10000)),
                start.plus(Duration.ofMillis(20000)),
                start.plus(Duration.ofMillis(30000)),
                start.plus(Duration.ofMillis(40000)),
                start.plus(Duration.ofMillis(50000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(30000)))))
               .axisRange(AxisRanges.fixed(0, 10)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.lessGraphArea.1", image);
    }

    @Test
    public void lessGraphArea2() throws Exception {
    //test using an absolute time axis that barely fits anything.
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,6),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(10000)),
                start.plus(Duration.ofMillis(20000)),
                start.plus(Duration.ofMillis(30000)),
                start.plus(Duration.ofMillis(40000)),
                start.plus(Duration.ofMillis(50000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(11000)))))
               .axisRange(AxisRanges.fixed(0, 10)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.lessGraphArea.2", image);
    }

        @Test
    public void lessGraphArea3() throws Exception {
    //test using a y-axis that does not fit the data
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 10, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(10,20,30,40,50,60),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(10000)),
                start.plus(Duration.ofMillis(20000)),
                start.plus(Duration.ofMillis(30000)),
                start.plus(Duration.ofMillis(40000)),
                start.plus(Duration.ofMillis(50000))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(60000)))))
               .axisRange(AxisRanges.fixed(0, 10)));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.lessGraphArea.3", image);
    }

    @Test
    public void extraGraphAreaDegenerate1() throws Exception {
    //test going backwards in time. Sure, it's a degenerate graph, but we
    //will see if it handles extending to the end of the graph correctly.
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(10,20,30,40,50,25),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(1500))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(20000))))));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.degenerate.1", image);
    }

    @Test
    public void extraGraphAreaDegenerate2() throws Exception {
    //test going backwards in time with no extra graph area. Essentially,
    //our data points extend the whole x axis range, but the last data point
    //has x value less than other data points
        Instant start = TimeScalesTest.create(2013, 4, 5, 11, 13, 3, 900);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble(1,2,3,4,5,-1),
                Arrays.asList(start,
                start.plus(Duration.ofMillis(3000)),
                start.plus(Duration.ofMillis(6000)),
                start.plus(Duration.ofMillis(8500)),
                start.plus(Duration.ofMillis(12500)),
                start.plus(Duration.ofMillis(1500))));
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(300, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.PREVIOUS_VALUE)
               .timeAxisRange(TimeAxisRanges.absolute(TimeInterval.between(start,
                       start.plus(Duration.ofMillis(12500))))));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.extraGraphArea.degenerate.2", image);
    }

    @Test
    public void testSpringForward() throws Exception {
        assumeTrue(TimeZone.getDefault().hasSameRules(TimeZone.getTimeZone("America/New_York")));
        Instant start = TimeScalesTest.create(2015, 3, 8, 1, 0, 0, 0);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble( 90,34,58,66,61,16,90,24,87,61,97,89,4,89,63,70,67,66,64,2 ),
                Arrays.asList(start,
                    start.plus(TimeDuration.ofMinutes(2.4)),
                    start.plus(TimeDuration.ofMinutes(3.3)),
                    start.plus(TimeDuration.ofMinutes(4.3)),
                    start.plus(TimeDuration.ofMinutes(21.1)),
                    start.plus(TimeDuration.ofMinutes(21.2)),
                    start.plus(TimeDuration.ofMinutes(27.9)),
                    start.plus(TimeDuration.ofMinutes(30.5)),
                    start.plus(TimeDuration.ofMinutes(34.2)),
                    start.plus(TimeDuration.ofMinutes(45.7)),
                    start.plus(TimeDuration.ofMinutes(46.9)),
                    start.plus(TimeDuration.ofMinutes(51.9)),
                    start.plus(TimeDuration.ofMinutes(60.0)),
                    start.plus(TimeDuration.ofMinutes(60.8)),
                    start.plus(TimeDuration.ofMinutes(67.0)),
                    start.plus(TimeDuration.ofMinutes(75.4)),
                    start.plus(TimeDuration.ofMinutes(81.1)),
                    start.plus(TimeDuration.ofMinutes(82.7)),
                    start.plus(TimeDuration.ofMinutes(85.3)),
                    start.plus(TimeDuration.ofMinutes(94.8))
                ));
        BufferedImage image = new BufferedImage(500, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(500, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR ));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.springForward.1", image);
    }

    @Test
    public void testMultilineLabels() throws Exception {
        Instant start = TimeScalesTest.create(2014, 12, 31, 23, 30, 0, 0);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble( 929,333,542,304,153,631,562,98,187,285,710,688,399,528,679,102,533,156,526,941 ),
                Arrays.asList(start,
                    start.plus(TimeDuration.ofMinutes(2.4)),
                    start.plus(TimeDuration.ofMinutes(3.3)),
                    start.plus(TimeDuration.ofMinutes(4.3)),
                    start.plus(TimeDuration.ofMinutes(21.1)),
                    start.plus(TimeDuration.ofMinutes(21.2)),
                    start.plus(TimeDuration.ofMinutes(27.9)),
                    start.plus(TimeDuration.ofMinutes(30.5)),
                    start.plus(TimeDuration.ofMinutes(34.2)),
                    start.plus(TimeDuration.ofMinutes(45.7)),
                    start.plus(TimeDuration.ofMinutes(46.9)),
                    start.plus(TimeDuration.ofMinutes(51.9)),
                    start.plus(TimeDuration.ofMinutes(60.0)),
                    start.plus(TimeDuration.ofMinutes(60.8)),
                    start.plus(TimeDuration.ofMinutes(67.0)),
                    start.plus(TimeDuration.ofMinutes(75.4)),
                    start.plus(TimeDuration.ofMinutes(81.1)),
                    start.plus(TimeDuration.ofMinutes(82.7)),
                    start.plus(TimeDuration.ofMinutes(85.3)),
                    start.plus(TimeDuration.ofMinutes(94.8))
                ));
        BufferedImage image = new BufferedImage(500, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(700, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR ));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.multiLineLabels.1", image);
    }

    @Test
    public void testMultilineLabelsEnd() throws Exception {
    //tests what happens if we have a date and time for the last label
    //on the graph
        Instant start = TimeScalesTest.create(2014, 12, 31, 23, 30, 0, 0);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble( 1 , 2 , 3 , 4 ),
                Arrays.asList(start,
                        start.plus( TimeDuration.ofMinutes( 10 ) ) ,
                        start.plus( TimeDuration.ofMinutes( 20 ) ) ,
                        start.plus( TimeDuration.ofMinutes( 30 ) )
                ));
        BufferedImage image = new BufferedImage(500, 200, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(500, 200);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR ));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.multiLineLabels.2", image);
    }

    @Test
    public void testMultilineLabelsStartMiddleEnd() throws Exception {
    //tests what happens if we have a date and time for the first, last labels
    //on the graph, and many in the middle
        Instant start = TimeScalesTest.create(2015, 4, 1, 0, 0, 0, 0);
        TimeSeriesDataset data = TimeSeriesDatasets.timeSeriesOf(new ArrayDouble( 1 , 2 , 3 , 4 ),
                Arrays.asList(start,
            start.plus( Duration.ofHours( 720 ) ) , /*go past all of April*/
            start.plus( Duration.ofHours( 720 + 744 ) ) , /*go past all of May*/
            start.plus( Duration.ofHours( 720 + 744 + 720 ) ) /*go past all of June*/
        ));
        BufferedImage image = new BufferedImage(30000, 100, BufferedImage.TYPE_3BYTE_BGR);
        LineTimeGraph2DRenderer renderer = new LineTimeGraph2DRenderer(30000, 100);
        renderer.update(new LineTimeGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR ));
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("lineTimeGraph.multiLineLabels.3", image);
    }
}
