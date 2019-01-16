/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInteger;
import org.epics.vtype.VTable;
import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class VTableAggegationTest {

    @Test
    public void aggregateSimpleTable() throws Exception {
        List<String> names = Arrays.asList("One", "Two", "Three");
        List<Double> values = Arrays.asList(1.0, 2.0, 3.0);
        List<Integer> counts = Arrays.asList(1, 2, 3);
        DesiredRateExpression<VTable> exp = vTable(column("Names", vStringConstants(names)), column("Values", vDoubleConstants(values)), column("Counts", vIntConstants(counts)));
        VTable result = exp.getFunction().readValue();
        assertThat(result.getColumnCount(), equalTo(3));
        assertThat(result.getColumnName(0), equalTo("Names"));
        assertThat(result.getColumnType(0), equalTo((Class) String.class));
        assertThat(result.getColumnData(0), equalTo((Object) Arrays.asList("One", "Two", "Three")));
        assertThat(result.getColumnName(1), equalTo("Values"));
        assertThat(result.getColumnType(1), equalTo((Class) Double.TYPE));
        assertThat(result.getColumnData(1), equalTo((Object) ArrayDouble.of(1.0, 2.0, 3.0)));
        assertThat(result.getColumnName(2), equalTo("Counts"));
        assertThat(result.getColumnType(2), equalTo((Class) Integer.TYPE));
        assertThat(result.getColumnData(2), equalTo((Object) ArrayInteger.of(1, 2, 3)));
    }
}
