/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ListNumbersTest {
    
    @Test
    public void sortedView1() {
        ArrayDouble values = new ArrayDouble(5,3,1,4,2,0);
        SortedListView sortedView = ListNumbers.sortedView(values);
        assertThat(values, equalTo(new ArrayDouble(5,3,1,4,2,0)));
        assertThat(sortedView, equalTo((ListNumber) new ArrayDouble(0,1,2,3,4,5)));
        assertThat(sortedView.getIndexes(), equalTo((ListInt) new ArrayInt(5,2,4,1,3,0)));
    }
    
    @Test
    public void sortedView2() {
        ArrayDouble values = new ArrayDouble(5,3,1,4,2,0);
        ArrayInt indexes = new ArrayInt(0,3,1,4,2,5);
        SortedListView sortedView = ListNumbers.sortedView(values, indexes);
        assertThat(values, equalTo(new ArrayDouble(5,3,1,4,2,0)));
        assertThat(sortedView, equalTo((ListNumber) new ArrayDouble(5,4,3,2,1,0)));
        assertThat(sortedView.getIndexes(), equalTo((ListInt) new ArrayInt(0,3,1,4,2,5)));
    }
}
