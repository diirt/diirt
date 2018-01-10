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
 * Tests the CacheCollector.
 *
 * @author carcassi
 */
public class CacheCollectorTest {

    @Test
    public void inputOutput() {
        CacheCollector<Integer> collector = new CacheCollector<>(5);
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(0);
        assertThat(collector.readValue(), equalTo(Arrays.asList(0)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(0)));
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        assertThat(collector.readValue(), equalTo(Arrays.asList(0,1,2,3)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(0,1,2,3)));
        collector.writeValue(4);
        collector.writeValue(5);
        collector.writeValue(6);
        assertThat(collector.readValue(), equalTo(Arrays.asList(2,3,4,5,6)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(2,3,4,5,6)));
    }

    @Test
    public void setMaxSize() {
        CacheCollector<Integer> collector = new CacheCollector<>(5);
        assertThat(collector.readValue().size(), equalTo(0));
        collector.writeValue(0);
        collector.writeValue(1);
        collector.writeValue(2);
        collector.writeValue(3);
        collector.writeValue(4);
        assertThat(collector.readValue(), equalTo(Arrays.asList(0,1,2,3,4)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(0,1,2,3,4)));
        collector.setMaxSize(2);
        assertThat(collector.readValue(), equalTo(Arrays.asList(3,4)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(3,4)));
        collector.writeValue(5);
        assertThat(collector.readValue(), equalTo(Arrays.asList(4,5)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(4,5)));
        collector.setMaxSize(5);
        collector.writeValue(6);
        assertThat(collector.readValue(), equalTo(Arrays.asList(4,5,6)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(4,5,6)));
        collector.writeValue(7);
        collector.writeValue(8);
        collector.writeValue(9);
        collector.writeValue(10);
        assertThat(collector.readValue(), equalTo(Arrays.asList(6,7,8,9,10)));
        assertThat(collector.readValue(), equalTo(Arrays.asList(6,7,8,9,10)));
    }
}
