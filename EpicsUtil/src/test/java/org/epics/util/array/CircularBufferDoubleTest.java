/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.util.array;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import static org.epics.util.array.CollectionTest.testCollection;
import static org.epics.util.array.ListTest.testList;

/**
 *
 * @author carcassi
 */
public class CircularBufferDoubleTest {
    
    public CircularBufferDoubleTest() {
    }
    
    @Test
    public void iteration1() {
        CircularBufferDouble coll = new CircularBufferDouble(15);
        for (int i = 0; i < 10; i++) {
            coll.addDouble(1.0);
        }
        testCollection(coll);
        testList(coll);
    }
    
    @Test
    public void add1() {
        CircularBufferDouble coll = new CircularBufferDouble(10);
        for (int i = 0; i < 5; i++) {
            coll.addDouble(1.0);
        }
        assertThat(coll.size(), equalTo(5));
        for (int i = 0; i < 5; i++) {
            coll.addDouble(1.0);
        }
        assertThat(coll.size(), equalTo(10));
        for (int i = 0; i < 5; i++) {
            coll.addDouble(1.0);
        }
        assertThat(coll.size(), equalTo(10));
    }
    
    @Test
    public void add2() {
        CircularBufferDouble coll = new CircularBufferDouble(10);
        for (int i = 0; i < 11; i++) {
            coll.addDouble(i);
        }
        ListDouble reference = new ArrayDouble(new double[] {1,2,3,4,5,6,7,8,9,10});
        assertThat(coll, equalTo(reference));
    }
}
