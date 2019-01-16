/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.array;

import java.util.Arrays;
import java.util.List;

import org.diirt.datasource.formula.StatefulFormulaFunction;
import org.diirt.datasource.util.NullUtils;
import org.diirt.vtype.util.ValueUtil;
import org.epics.util.stats.Range;
import org.epics.util.stats.Ranges;
import org.epics.util.stats.Statistics;
import org.epics.util.stats.StatisticsUtil;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ListNumber;
import org.epics.util.array.ListNumbers;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;

/**
 * @author shroffk
 *
 */
public class Histogram2DOfFormulaFunction extends StatefulFormulaFunction {

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "histogram2DOf";
    }

    @Override
    public String getDescription() {
        return "Returns a 2D histogram from a table.";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>> asList(VTable.class, VString.class, VString.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("Table", "Y Column", "X Column");
    }

    @Override
    public Class<?> getReturnType() {
        return VNumberArray.class;
    }

    private ListNumber previousXData;
    private ListNumber previousYData;
    private VNumberArray previousResult;
    private double previousMaxCount;
    private Range previousXRange;
    private Range previousYRange;

    @Override
    public Object calculate(List<Object> args) {
        if (NullUtils.containsNull(args)) {
            return null;
        }

        VTable table = (VTable) args.get(0);
        VString yColumnName = (VString) args.get(1);
        VString xColumnName = (VString) args.get(2);

        ListNumber yData = ValueUtil.numericColumnOf(table, yColumnName.getValue());
        ListNumber xData = ValueUtil.numericColumnOf(table, xColumnName.getValue());
        int nPoints = Math.min(yData.size(), xData.size());

        Statistics xStats = StatisticsUtil.statisticsOf(xData);
        Statistics yStats = StatisticsUtil.statisticsOf(yData);
        int nXBins = 20;
        int nYBins = 20;
        Range aggregatedXRange = xStats.getRange().combine(previousXRange);
        Range aggregatedYRange = yStats.getRange().combine(previousYRange);
        Range xRange;
        Range yRange;
        if (Ranges.overlap(aggregatedXRange, xStats.getRange()) >= 0.90) {
            xRange = aggregatedXRange;
        } else {
            xRange = xStats.getRange();
        }
        if (Ranges.overlap(aggregatedYRange, yStats.getRange()) >= 0.90) {
            yRange = aggregatedYRange;
        } else {
            yRange = yStats.getRange();
        }

        double minXValueRange = xRange.getMinimum();
        double maxXValueRange = xRange.getMaximum();
        double minYValueRange = yRange.getMinimum();
        double maxYValueRange = yRange.getMaximum();

        ListNumber xBoundaries = ListNumbers.linearListFromRange(minXValueRange, maxXValueRange, nXBins + 1);
        ListNumber yBoundaries = ListNumbers.linearListFromRange(minYValueRange, maxYValueRange, nYBins + 1);

        int[] binData = new int[nXBins*nYBins];
        double maxCount = 0;
        for (int i = 0; i < nPoints; i++) {
            double xValue = xData.getDouble(i);
            double yValue = yData.getDouble(i);
            // Check value in range
            if (xRange.contains(xValue)) {
                if (yRange.contains(yValue)) {

                    int xBin = (int) Math.floor(xRange.normalize(xValue) * nXBins);
                    int yBin = (int) Math.floor(yRange.normalize(yValue) * nYBins);
                    if (xBin == nXBins) {
                        xBin--;
                    }
                    if (yBin == nYBins) {
                        yBin--;
                    }

                    int binIndex = yBin*nXBins + xBin;
                    binData[binIndex]++;
                    if (binData[binIndex] > maxCount) {
                        maxCount = binData[binIndex];
                    }
                }
            }
        }

        // TODO: Need a better logic to auto-size dynamic display... some kind of
        // time based "forget"
        if (previousMaxCount > maxCount && (previousMaxCount < maxCount * 2.0 || maxCount < 9)) {
            maxCount = previousMaxCount;
        }

        previousMaxCount = maxCount;
        previousXRange = xRange;
        previousXData = xData;
        previousYData = yData;

        previousResult = VNumberArray.of(ArrayInteger.of(binData),
                                         ArrayInteger.of(nYBins, nXBins),
                                         Alarm.none(),
                                         Time.now(),
                                         Display.of(Range.of(0.0, maxCount), Range.of(0.0, maxCount), Range.of(0.0, maxCount), Range.of(Double.NaN, Double.NaN), "count", NumberFormats.precisionFormat(0)));

        return previousResult;
    }

}
