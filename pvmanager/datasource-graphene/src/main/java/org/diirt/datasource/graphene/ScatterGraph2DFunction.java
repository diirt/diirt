/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import java.awt.image.BufferedImage;
import java.util.List;

import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.ScatterGraph2DRenderer;
import org.diirt.graphene.ScatterGraph2DRendererUpdate;
import org.diirt.datasource.QueueCollector;
import org.diirt.datasource.ReadFunction;
import org.diirt.vtype.VImage;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueUtil;

import static org.diirt.datasource.graphene.ArgumentExpressions.*;

/**
 * @author shroffk
 *
 */
public class ScatterGraph2DFunction implements ReadFunction<Graph2DResult> {

    private ReadFunction<? extends VTable> tableData;
    private ReadFunctionArgument<String> xColumnName;
    private ReadFunctionArgument<String> yColumnName;
    private ReadFunctionArgument<String> tooltipColumnName;
    private ScatterGraph2DRenderer renderer = new ScatterGraph2DRenderer(300,
            200);
    private VImage previousImage;
    private final QueueCollector<ScatterGraph2DRendererUpdate> rendererUpdateQueue = new QueueCollector<>(
            100);

    public ScatterGraph2DFunction(ReadFunction<?> tableData,
            ReadFunction<?> xColumnName,
            ReadFunction<?> yColumnName,
            ReadFunction<?> tooltipColumnName) {
        this.tableData = new CheckedReadFunction<>(tableData, "Data", VTable.class);
        this.xColumnName = stringArgument(xColumnName, "X Column");
        this.yColumnName = stringArgument(yColumnName, "Y Column");
        this.tooltipColumnName = stringArgument(tooltipColumnName, "Tooltip Column");
    }

    public QueueCollector<ScatterGraph2DRendererUpdate> getRendererUpdateQueue() {
        return rendererUpdateQueue;
    }

    @Override
    public Graph2DResult readValue() {
        VTable vTable = tableData.readValue();
        xColumnName.readNext();
        yColumnName.readNext();
        tooltipColumnName.readNext();

        // Table and columns must be available
        if (vTable == null || xColumnName.isMissing() || yColumnName.isMissing()) {
            return null;
        }

        // Prepare new dataset
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(vTable, xColumnName.getValue(), yColumnName.getValue());

        List<ScatterGraph2DRendererUpdate> updates = rendererUpdateQueue
                .readValue();
        for (ScatterGraph2DRendererUpdate scatterGraph2DRendererUpdate : updates) {
            renderer.update(scatterGraph2DRendererUpdate);
        }

        if (renderer.getImageHeight() == 0 && renderer.getImageWidth() == 0) {
            return null;
        }

        BufferedImage image = new BufferedImage(renderer.getImageWidth(),
                renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), dataset);

        previousImage = ValueUtil.toVImage(image);
        return new Graph2DResult(vTable, previousImage,
                new GraphDataRange(renderer.getXPlotRange(), renderer.getXPlotRange(), renderer.getXAggregatedRange()), new GraphDataRange(
                renderer.getYPlotRange(), renderer.getYPlotRange(), renderer.getYAggregatedRange()),
                -1);

    }
}
