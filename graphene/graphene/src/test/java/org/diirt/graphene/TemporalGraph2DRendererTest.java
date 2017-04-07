/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;

import org.diirt.util.stats.Ranges;
import org.diirt.util.time.TimeInterval;
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
        Instant time = Instant.now();
        TimeInterval interval1 = TimeInterval.after(Duration.ofSeconds(1), time);
        TimeInterval interval2 = TimeInterval.after(Duration.ofSeconds(2), time);
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), sameInstance(interval2));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), sameInstance(interval2));
    }

    @Test
    public void aggregateTimeInterval2() {
        Instant time = Instant.now();
        TimeInterval interval1 = TimeInterval.after(Duration.ofSeconds(1), time);
        TimeInterval interval2 = TimeInterval.around(Duration.ofSeconds(4), time);
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), sameInstance(interval2));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), sameInstance(interval2));
    }

    @Test
    public void aggregateTimeInterval4() {
        Instant time = Instant.now();
        TimeInterval interval1 = TimeInterval.around(Duration.ofSeconds(1), time);
        TimeInterval interval2 = TimeInterval.after(Duration.ofSeconds(1), time);
        TimeInterval total = TimeInterval.between(interval1.getStart(), interval2.getEnd());
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval1, interval2), equalTo(total));
        assertThat(TemporalGraph2DRenderer.aggregateTimeInterval(interval2, interval1), equalTo(total));
    }

    @Test
    public void aggregateTimeInterval3() {
        Instant time = Instant.now();
        TimeInterval interval1 = TimeInterval.before(Duration.ofSeconds(1), time);
        TimeInterval interval2 = TimeInterval.after(Duration.ofSeconds(1), time.plus(Duration.ofSeconds(1)));
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
        Instant now = Instant.now();
        TimeInterval initialTimeInterval = TimeInterval.between(now, now.plus(Duration.ofSeconds(1)));
        renderer.calculateRanges(initialRange, initialTimeInterval);
        assertThat(renderer.getPlotRange(), sameInstance(initialRange));
        assertThat(renderer.getPlotTimeInterval(), sameInstance(initialTimeInterval));

        Range newRange = Ranges.range(5, 15);
        TimeInterval newTimeInterval = TimeInterval.between(now.minus(Duration.ofSeconds(1)), now);
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
        Instant start = TimeScalesTest.create(2013, 1, 1, 12, 0, 0, 0);
        Instant end = TimeScalesTest.create(2013, 1, 1, 12, 0, 2, 0);
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
        Instant start = TimeScalesTest.create( 2014 , 12 , 2 , 8 , 8 , 8 , 0 );
        Instant end = TimeScalesTest.create( 2014 , 12 , 3 , 8 , 8 , 8 , 0 );
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
        Instant start = TimeScalesTest.create( 2014 , 1 , 2 , 8 , 8 , 8 , 0 );
        Instant end = TimeScalesTest.create( 2014 , 1 , 23 , 8 , 8 , 8 , 0 );
        renderer.calculateRanges(Ranges.range(0, 10), TimeInterval.between(start, end));
        renderer.calculateGraphArea();
        renderer.drawGraphArea();
        ImageAssert.compareImages( "timeGraph2DAreaDays1" , image );
        //File f = new File( "src/test/resources/org/epics/graphene/timeGraph2DAreaDays1.png" );
        //ImageIO.write( image , "png" , f );
    }

}