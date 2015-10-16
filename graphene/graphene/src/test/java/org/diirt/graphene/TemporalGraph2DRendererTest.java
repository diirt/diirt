/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.diirt.util.stats.Ranges;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class TemporalGraph2DRendererTest {

    @Test
    public void aggregateTimeInterval1() {
        Timestamp time = Timestamp.now();
        TimeInterval interval1 = TimeDuration.ofSeconds(1).after(time);
        TimeInterval interval2 = TimeDuration.ofSeconds(2).after(time);
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), sameInstance(interval2));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), sameInstance(interval2));
    }

    @Test
    public void aggregateTimeInterval2() {
        Timestamp time = Timestamp.now();
        TimeInterval interval1 = TimeDuration.ofSeconds(1).after(time);
        TimeInterval interval2 = TimeDuration.ofSeconds(4).around(time);
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), sameInstance(interval2));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), sameInstance(interval2));
    }

    @Test
    public void aggregateTimeInterval4() {
        Timestamp time = Timestamp.now();
        TimeInterval interval1 = TimeDuration.ofSeconds(1).around(time);
        TimeInterval interval2 = TimeDuration.ofSeconds(1).after(time);
        TimeInterval total = TimeInterval.between(interval1.getStart(), interval2.getEnd());
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), equalTo(total));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), equalTo(total));
    }

    @Test
    public void aggregateTimeInterval3() {
        Timestamp time = Timestamp.now();
        TimeInterval interval1 = TimeDuration.ofSeconds(1).before(time);
        TimeInterval interval2 = TimeDuration.ofSeconds(1).after(time.plus(TimeDuration.ofSeconds(1)));
        TimeInterval total = TimeInterval.between(interval1.getStart(), interval2.getEnd());
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), equalTo(total));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), equalTo(total));
    }

    @Test
    public void calculateRanges1() throws Exception {
        TemporalGraph2DRenderer renderer = new TemporalGraph2DRenderer(300, 200) {

            @Override
            public TemporalGraph2DRendererUpdate newUpdate() {
                return new TemporalGraph2DRendererUpdate();
            }
        };

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;

        Range initialRange = Ranges.range(0, 10);
        Timestamp now = Timestamp.now();
        TimeInterval initialTimeInterval = TimeInterval.between(now, now.plus(TimeDuration.ofSeconds(1)));
        renderer.calculateRanges(initialRange, initialTimeInterval);
        assertThat(renderer.getPlotRange(), sameInstance(initialRange));
        assertThat(renderer.getPlotTimeInterval(), sameInstance(initialTimeInterval));

        Range newRange = Ranges.range(5, 15);
        TimeInterval newTimeInterval = TimeInterval.between(now.minus(TimeDuration.ofSeconds(1)), now);
        renderer.calculateRanges(newRange, newTimeInterval);
        assertThat(renderer.getPlotRange().getMinimum(), equalTo(Ranges.range(0, 15).getMinimum()));
        assertThat(renderer.getPlotRange().getMaximum(), equalTo(Ranges.range(0, 15).getMaximum()));
        assertThat(renderer.getPlotTimeInterval(), sameInstance(newTimeInterval));
    }

    @Test
    public void timeGraphArea1() throws Exception {
        TemporalGraph2DRenderer renderer = new TemporalGraph2DRenderer(300, 200) {

            @Override
            public TemporalGraph2DRendererUpdate newUpdate() {
                return new TemporalGraph2DRendererUpdate();
            }
        };
        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        Timestamp start = TimeScalesTest.create(2013, 1, 1, 12, 0, 0, 0);
        Timestamp end = TimeScalesTest.create(2013, 1, 1, 12, 0, 2, 0);
        renderer.calculateRanges(Ranges.range(0, 10), TimeInterval.between(start, end));
        renderer.calculateGraphArea();
        renderer.drawGraphArea();
        ImageAssert.compareImages("timeGraph2DArea.1", image);
    }

    @Test
    @Ignore
    public void timeGraphAreaHours2() throws Exception {
        // TODO: Fix multi-line labels
        TemporalGraph2DRenderer renderer = new TemporalGraph2DRenderer(1000, 400) {

            @Override
            public TemporalGraph2DRendererUpdate newUpdate() {
                return new TemporalGraph2DRendererUpdate();
            }
        };
        BufferedImage image = new BufferedImage(1000, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        Timestamp start = TimeScalesTest.create( 2014 , 12 , 2 , 8 , 8 , 8 , 0 );
        Timestamp end = TimeScalesTest.create( 2014 , 12 , 3 , 8 , 8 , 8 , 0 );
        renderer.calculateRanges(Ranges.range(0, 10), TimeInterval.between(start, end));
        renderer.calculateGraphArea();
        renderer.drawGraphArea();
        ImageAssert.compareImages( "timeGraph2DArea2" , image );
    }

    @Test
    @Ignore
    public void timeGraphAreaDays1() throws Exception {
        // TODO: Fix multi-line labels
        TemporalGraph2DRenderer renderer = new TemporalGraph2DRenderer(1000, 400) {

            @Override
            public TemporalGraph2DRendererUpdate newUpdate() {
                return new TemporalGraph2DRendererUpdate();
            }
        };
        BufferedImage image = new BufferedImage(1000, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.g = graphics;
        Timestamp start = TimeScalesTest.create( 2014 , 1 , 2 , 8 , 8 , 8 , 0 );
        Timestamp end = TimeScalesTest.create( 2014 , 1 , 23 , 8 , 8 , 8 , 0 );
        renderer.calculateRanges(Ranges.range(0, 10), TimeInterval.between(start, end));
        renderer.calculateGraphArea();
        renderer.drawGraphArea();
        ImageAssert.compareImages( "timeGraph2DAreaDays1" , image );
        //File f = new File( "src/test/resources/org/epics/graphene/timeGraph2DAreaDays1.png" );
        //ImageIO.write( image , "png" , f );
    }

}