/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.io;

import java.io.IOException;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.Point2DDatasets;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class CommaSeparatedValueIOTest {

    public CommaSeparatedValueIOTest() {
    }

    @Test
    public void write1() throws IOException {
        Point2DDataset dataset = Point2DDatasets.lineData(new ArrayDouble(0,2,4,1,3,5), new ArrayDouble(0,0.1,0.2,0.3,0.4,0.5));
        String csv = CommaSeparatedValueIO.write(dataset);
        assertThat(csv, equalTo("x,y\n0,0\n2,0.1\n4,0.2\n1,0.3\n3,0.4\n5,0.5"));
    }

    @Test
    public void read1() throws IOException {
        Point2DDataset dataset = CommaSeparatedValueIO.read("x,y\n0,0\n2,0.1\n4,0.2\n1,0.3\n3,0.4\n5,0.5");
        assertThat(dataset.getCount(), equalTo(6));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(0,2,4,1,3,5)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(0,0.1,0.2,0.3,0.4,0.5)));
    }
}