/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene;

import java.awt.Color;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class ValueColorSchemesTest {
    
    public ValueColorSchemesTest() {
    }

    @Test
    @Ignore("TODO: Color schemes give wrong color outside range")
    public void jetScheme() {
        Range range = RangeUtil.range(0.0, 1.0);
        ValueColorSchemeInstance colorScheme = ValueColorSchemes.JET.createInstance(range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.0), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.6), equalTo(Color.YELLOW.getRGB()));
        assertThat(colorScheme.colorFor(1.0), equalTo(new Color(138,0,0).getRGB()));
        assertThat(colorScheme.colorFor(1.1), equalTo(new Color(138,0,0).getRGB()));
    }
    
}
