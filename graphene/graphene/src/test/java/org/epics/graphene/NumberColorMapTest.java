/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene;

import org.epics.util.stats.Range;
import java.awt.Color;
import org.epics.util.stats.Ranges;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class NumberColorMapTest {
    
    public NumberColorMapTest() {
    }

    @Test
    public void jetScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.JET.createInstance(range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.0), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.6), equalTo(Color.YELLOW.getRGB()));
        assertThat(colorScheme.colorFor(1.0), equalTo(new Color(138,0,0).getRGB()));
        assertThat(colorScheme.colorFor(1.1), equalTo(new Color(138,0,0).getRGB()));
    }
    
    @Test
    public void optimizedScheme() {
        Range range = Ranges.range(0.0, 1.0);
        NumberColorMapInstance colorScheme = NumberColorMaps.optimize(NumberColorMaps.JET.createInstance(range), range);
        assertThat(colorScheme.colorFor(-0.1), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.0), equalTo(new Color(0,0,138).getRGB()));
        assertThat(colorScheme.colorFor(0.601), equalTo(new Color(255,254,0).getRGB()));
        assertThat(colorScheme.colorFor(1.0), equalTo(new Color(138,0,0).getRGB()));
        assertThat(colorScheme.colorFor(1.1), equalTo(new Color(138,0,0).getRGB()));
    }
    
}
