/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.io;

import java.io.IOException;
import org.epics.graphene.Point1DDatasets;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.Point2DDatasets;
import org.epics.util.array.ArrayDouble;
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
}