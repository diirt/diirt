/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import java.util.List;
import org.diirt.util.array.*;
import org.diirt.util.stats.Range;

/**
 *
 * @author carcassi
 */
public class Point3DWithLabelDatasets {

    public static Point3DWithLabelDataset build(final ListNumber x, final ListNumber y, final ListNumber z, final List<String> labels) {
        if (x.size() != y.size() || x.size() != y.size() || x.size() != labels.size()) {
            throw new IllegalArgumentException("Array lengths don't match: " + x.size() + " - " + y.size() + " - " + z.size() + " - " + labels.size());
        }

        return new Point3DWithLabelDataset() {

            private final Statistics xStatistics = StatisticsUtil.statisticsOf(x);
            private final Statistics yStatistics = StatisticsUtil.statisticsOf(y);
            private final Statistics zStatistics = StatisticsUtil.statisticsOf(z);

            @Override
            public ListNumber getXValues() {
                return x;
            }

            @Override
            public ListNumber getYValues() {
                return y;
            }

            @Override
            public ListNumber getZValues() {
                return z;
            }

            @Override
            public List<String> getLabels() {
                return labels;
            }

            @Override
            public Statistics getXStatistics() {
                return xStatistics;
            }

            @Override
            public Statistics getYStatistics() {
                return yStatistics;
            }

            @Override
            public Statistics getZStatistics() {
                return zStatistics;
            }

            @Override
            public Range getXDisplayRange() {
                return null;
            }

            @Override
            public Range getYDisplayRange() {
                return null;
            }

            @Override
            public Range getZDisplayRange() {
                return null;
            }

            @Override
            public int getCount() {
                return x.size();
            }
        };
    }

}
