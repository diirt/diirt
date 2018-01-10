/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.Arrays;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the QueueCollector.
 *
 * @author carcassi
 */
public class QueueCollectorTest {

    @Test
    public void inputOutput() {
        QueueCollector<Integer> collector = new QueueCollector<>(5);
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(0);
        assertThat(collector.readValue(), equalTo(Arrays.asList(0)));
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        assertThat(collector.readValue(), equalTo(Arrays.asList(1,2,3)));
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        collector.writeValue(4);
        collector.writeValue(5);
        collector.writeValue(6);
        assertThat(collector.readValue(), equalTo(Arrays.asList(2,3,4,5,6)));
        assertThat(collector.readValue().size(), equalTo(0));
    }

    @Test
    public void setMaxSize() {
        QueueCollector<Integer> collector = new QueueCollector<>(5);
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        collector.writeValue(4);
        collector.writeValue(5);
        collector.writeValue(6);
        collector.setMaxSize(2);
        assertThat(collector.readValue(), equalTo(Arrays.asList(5,6)));
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        collector.setMaxSize(5);
        collector.writeValue(4);
        collector.writeValue(5);
        collector.writeValue(6);
        assertThat(collector.readValue(), equalTo(Arrays.asList(2,3,4,5,6)));
        assertThat(collector.readValue().size(), equalTo(0));
    }
}
