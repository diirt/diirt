/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayInt;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class GraphAreaDataTest {
    
    @Test
    public void linearGraphRange() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        GraphAreaData data = new GraphAreaData();
        data.setGraphArea(5, 194, 294, 5);
        data.setGraphAreaMargins(0, 0, 0, 0);
        data.setGraphBuffer(graphBuffer);
        data.setLabelMargin(5, 5);
        data.setRanges(RangeUtil.range(-5, 5), ValueScales.linearScale(), RangeUtil.range(-10, 10), ValueScales.linearScale());
        graphBuffer.drawBackground(Color.WHITE);
        data.prepareLabels(FontUtil.getLiberationSansRegular(), Color.BLACK);
        data.prepareGraphArea(true, Color.GRAY);
        data.drawGraphArea();
        
        ImageAssert.compareImages("graphAreaData.linearGraphRange", graphBuffer.getImage());
    }
}
