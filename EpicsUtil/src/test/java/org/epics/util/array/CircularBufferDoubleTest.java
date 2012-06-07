/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.util.array;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
}
