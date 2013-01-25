/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.CollectionNumbers;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class RangeUtilTest {
    
    public RangeUtilTest() {
    }
    
    @Test
    public void range1() throws Exception {
        Range range = RangeUtil.range(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void range2() throws Exception {
        Range range = RangeUtil.range(0.0, 0.0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void range3() throws Exception {
        Range range = RangeUtil.range(10.0, 0.0);
    }
    
}
