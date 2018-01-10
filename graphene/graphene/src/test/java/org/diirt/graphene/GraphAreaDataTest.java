/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Color;
import org.diirt.util.stats.Ranges;
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
        data.setGraphPadding(0, 0, 0, 0);
        data.setGraphBuffer(graphBuffer);
        data.setLabelMargin(5, 5);
        data.setRanges(Ranges.range(-5, 5), ValueScales.linearScale(), Ranges.range(-10, 10), ValueScales.linearScale());
        graphBuffer.drawBackground(Color.WHITE);
        data.prepareLabels(FontUtil.getLiberationSansRegular(), Color.BLACK);
        data.prepareGraphArea(true, Color.GRAY);
        data.drawGraphArea();

        ImageAssert.compareImages("graphAreaData.linearGraphRange", graphBuffer.getImage());
    }
}
