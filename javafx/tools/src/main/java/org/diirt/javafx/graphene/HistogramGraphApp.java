/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.stage.Stage;

/**
 *
 * @author mjchao
 */
public class HistogramGraphApp extends BaseGraphApp {

    private final HistogramGraphView histogramGraphView = new HistogramGraphView();

    @Override
    public BaseGraphView getGraphView() {
        return this.histogramGraphView;
    }

    @Override
    public void start( Stage stage ) throws Exception {
        super.start( stage );
        this.addDataFormulae( "sim://gaussianWaveform",
                    "=histogramOf('sim://noiseWaveform')",
                    "=arrayWithBoundaries(arrayOf(1,3,2,4,3,5), range(-10,10))",
                    "=caHistogram(\"histo\")" );
    }

    @Override
    public void openConfigurationPanel() {
        this.histogramGraphView.getDefaultConfigurationDialog().open();
    }

    final public static void main( String[] args ) {
        launch( args );
    }
}
