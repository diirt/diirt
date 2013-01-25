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
import org.epics.util.array.ListNumber;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class ListNumbersTest {
    
    public ListNumbersTest() {
    }
    
    @Test
    public void linearRange1() throws Exception {
        ListNumber list = ListNumbers.linearRange(0, 1000, 101);
        assertThat(list.getDouble(0), equalTo(0.0));
        assertThat(list.getDouble(35), equalTo(350.0));
        assertThat(list.getDouble(50), equalTo(500.0));
        assertThat(list.getDouble(100), equalTo(1000.0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void linearRange2() throws Exception {
        ListNumber list = ListNumbers.linearRange(0, 1000, 100);
        list.getDouble(-1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void linearRange3() throws Exception {
        ListNumber list = ListNumbers.linearRange(0, 1000, 100);
        list.getDouble(1000);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void linearRange4() throws Exception {
        ListNumber list = ListNumbers.linearRange(0, 1000, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void linearRange5() throws Exception {
        ListNumber list = ListNumbers.linearRange(0, 1000, -10);
    }
    
    @Test
    public void linearRange6() throws Exception {
        ListNumber list = ListNumbers.linearRange(1000, 0, 101);
        assertThat(list.getDouble(0), equalTo(1000.0));
        assertThat(list.getDouble(35), equalTo(650.0));
        assertThat(list.getDouble(50), equalTo(500.0));
        assertThat(list.getDouble(100), equalTo(0.0));
    }
    
    @Test
    public void linearList1() throws Exception {
        ListNumber list = ListNumbers.linearList(0, 10, 101);
        assertThat(list.getDouble(0), equalTo(0.0));
        assertThat(list.getDouble(35), equalTo(350.0));
        assertThat(list.getDouble(50), equalTo(500.0));
        assertThat(list.getDouble(100), equalTo(1000.0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void linearList2() throws Exception {
        ListNumber list = ListNumbers.linearList(0, 10, 101);
        list.getDouble(-1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void linearList3() throws Exception {
        ListNumber list = ListNumbers.linearList(0, 10, 101);
        list.getDouble(1000);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void linearList4() throws Exception {
        ListNumber list = ListNumbers.linearList(0, 10, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void linearList5() throws Exception {
        ListNumber list = ListNumbers.linearList(0, 10, -10);
    }
    
    @Test
    public void linearList6() throws Exception {
        ListNumber list = ListNumbers.linearList(1000, -10, 101);
        assertThat(list.getDouble(0), equalTo(1000.0));
        assertThat(list.getDouble(35), equalTo(650.0));
        assertThat(list.getDouble(50), equalTo(500.0));
        assertThat(list.getDouble(100), equalTo(0.0));
    }
    
}
