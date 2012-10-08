/*
 * Copyright 2011 Brookhaven National Laboratory
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
public class CollectionNumbersTest {
    
    public CollectionNumbersTest() {
    }
    
    @Test
    public void toDoubleArray1() {
        double[] data = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayDouble coll = new ArrayDouble(data);
        assertThat(CollectionNumbers.doubleArrayCopyOf(coll), equalTo(new double[] {0,1,2,3,4,5,6,7,8,9}));
    }
    
    @Test
    public void wrappedFloatArray1() {
        float[] array = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ListNumber coll = new ArrayFloat(array);
        float[] array2 = CollectionNumbers.wrappedFloatArray(coll);
        assertThat(array2, equalTo(new float[] {0,1,2,3,4,5,6,7,8,9}));
        assertThat(array2, sameInstance(array));
    }
    
    @Test
    public void wrappedFloatArray2() {
        double[] array = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ListNumber coll = new ArrayDouble(array);
        float[] array2 = CollectionNumbers.wrappedFloatArray(coll);
        assertThat(array2, nullValue());
    }
    
    @Test
    public void floatArrayCopyOf1(){
        float[] array = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayFloat coll = new ArrayFloat(array);
        float[] array2 = CollectionNumbers.floatArrayCopyOf(coll);
        assertThat(array2, equalTo(new float[] {0,1,2,3,4,5,6,7,8,9}));
        assertThat(array2, not(sameInstance(array)));
    }
    
    @Test
    public void floatArrayCopyOf2(){
        double[] array = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ListNumber coll = new ArrayDouble(array);
        float[] array2 = CollectionNumbers.floatArrayCopyOf(coll);
        assertThat(array2, equalTo(new float[] {0,1,2,3,4,5,6,7,8,9}));
    }
    
    @Test
    public void floatArrayWrappedOrCopy1(){
        float[] array = new float[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ListNumber coll = new ArrayFloat(array);
        float[] array2 = CollectionNumbers.floatArrayWrappedOrCopy(coll);
        assertThat(array2, equalTo(new float[] {0,1,2,3,4,5,6,7,8,9}));
        assertThat(array2, sameInstance(array));
    }
    
    @Test
    public void floatArrayWrappedOrCopy2(){
        double[] array = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ListNumber coll = new ArrayDouble(array);
        float[] array2 = CollectionNumbers.floatArrayWrappedOrCopy(coll);
        assertThat(array2, equalTo(new float[] {0,1,2,3,4,5,6,7,8,9}));
    }
}
