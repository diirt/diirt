/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.vtype;

import org.epics.pvmanager.vtype.VTable;
import java.util.Arrays;
import java.util.List;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.vtype.VTable;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;

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
        assertThat(result.getColumnArray(0), equalTo((Object) new String[]{"One", "Two", "Three"}));
        assertThat(result.getColumnName(1), equalTo("Values"));
        assertThat(result.getColumnType(1), equalTo((Class) Double.TYPE));
        assertThat(result.getColumnArray(1), equalTo((Object) new double[]{1.0, 2.0, 3.0}));
        assertThat(result.getColumnName(2), equalTo("Counts"));
        assertThat(result.getColumnType(2), equalTo((Class) Integer.TYPE));
        assertThat(result.getColumnArray(2), equalTo((Object) new int[]{1, 2, 3}));
    }
}
