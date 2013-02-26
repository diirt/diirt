/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.graphene.LinearValueScaleTest.assertAxisEquals;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;

/**
 *
 * @author carcassi
 */
public class LogValueScaleTest {

    @Test
    public void scaleValue1() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.scaleValue(10, 1, 100, 0, 100), equalTo(50.0));
    }

    @Test
    public void scaleValue2() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.scaleValue(100, 10, 1000, -100, 100), equalTo(0.0));
    }

//    @Test
//    public void references1() {
//        ValueScale logScale = ValueScales.logScale();
//        ValueAxis axis = logScale.references(RangeUtil.range(1.0, 1000.0), 2, 4);
//        assertAxisEquals(1.0, 1000.0, new double[]{1.0, 10.0, 100.0, 1000.0}, new String[]{"1", "10", "100", "1000"}, axis);
//    }
    
    @Test
    public void generateReferenceValues1() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10), 1), equalTo((ListDouble) new ArrayDouble(1, 10)));
    }
    
    @Test
    public void generateReferenceValues2() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10), 2), equalTo((ListDouble) new ArrayDouble(1, 5, 10)));
    }
    
    @Test
    public void generateReferenceValues3() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10), 5), equalTo((ListDouble) new ArrayDouble(1, 2, 4, 6, 8, 10)));
    }
    
    @Test
    public void generateReferenceValues4() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10), 10), equalTo((ListDouble) new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
    }
    
    @Test
    public void generateReferenceValues5() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 3), 10), equalTo((ListDouble) new ArrayDouble(1, 2, 3)));
    }
    
    @Test
    public void generateReferenceValues6() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 1.5), 100), equalTo((ListDouble) new ArrayDouble(1, 1.1, 1.2, 1.3, 1.4, 1.5)));
    }
    
    @Test
    public void generateReferenceValues7() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10000), 1), equalTo((ListDouble) new ArrayDouble(1, 10, 100, 1000, 10000)));
    }
    
    @Test
    public void generateReferenceValues8() {
        assertThat(LogValueScale.generateReferenceValues(RangeUtil.range(1, 10000), 2), 
                equalTo((ListDouble) new ArrayDouble(1, 5, 10, 50, 100, 500, 1000, 5000, 10000)));
    }
}