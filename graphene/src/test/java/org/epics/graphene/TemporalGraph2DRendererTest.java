/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
}