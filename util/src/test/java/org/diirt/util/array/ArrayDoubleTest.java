/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.array;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ArrayDoubleTest {

    public ArrayDoubleTest() {
    }

    @Test
    public void wrap1() {
        ArrayDouble array = new ArrayDouble(new double[] {0, 1, 2, 3, 4, 5});
        assertThat(CollectionNumbers.doubleArrayCopyOf(array), equalTo(new double[] {0, 1, 2, 3, 4, 5}));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void wrap2() {
        ArrayDouble array = new ArrayDouble(0, 1, 2, 3, 4, 5);
        array.setDouble(0, 0);
    }

    @Test
    public void wrap3() {
        ArrayDouble array = new ArrayDouble(new double[] {0, 1, 2, 3, 4, 5}, false);
        array.setDouble(0, 5);
        array.setDouble(5, 0);
        assertThat(CollectionNumbers.doubleArrayCopyOf(array), equalTo(new double[] {5, 1, 2, 3, 4, 0}));
    }

    @Test
    public void equals1() {
        ArrayDouble array = new ArrayDouble(new double[] {Double.MIN_VALUE}, false);
        assertThat(array, equalTo(array));
    }

    @Test
    public void equals2() {
        ArrayDouble array = new ArrayDouble(new double[] {Double.MIN_VALUE}, false);
        assertThat(array, not(equalTo(null)));
    }

    @Test
    public void equals3() {
        ArrayDouble array = new ArrayDouble(new double[] {Double.MIN_VALUE}, false);
        ArrayDouble array2 = new ArrayDouble(new double[] {Double.MIN_VALUE}, false);
        assertThat(array, equalTo(array2));
    }

    @Test
    public void equals4() {
        ArrayDouble array = new ArrayDouble(new double[] {Double.MIN_VALUE}, false);
        ArrayDouble array2 = new ArrayDouble(new double[] {Double.MAX_VALUE}, false);
        assertThat(array, not(equalTo(array2)));
    }

    @Test
    public void serialization1() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(buffer);
        ArrayDouble array = new ArrayDouble(new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        stream.writeObject(array);
        ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        ArrayDouble read = (ArrayDouble) inStream.readObject();
        assertThat(read, not(sameInstance(array)));
        assertThat(read, equalTo(array));
    }
}
