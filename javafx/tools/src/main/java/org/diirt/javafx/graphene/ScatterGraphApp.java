/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author mjchao
 */
public class ScatterGraphApp extends BaseGraphApp {

    private final ScatterGraphView scatterGraphView = new ScatterGraphView();

    @Override
    public BaseGraphView getGraphView() {
        return this.scatterGraphView;
    }

    @Override
    public void start( Stage stage ) throws Exception {
        super.start( stage );

        this.addDataFormulae( "sim://table",
                    "=tableOf(column(\"X\", step(0, 1)), column(\"Y\", 'sim://gaussianWaveform'))",
                    "=tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)'))",
                    "=tableOf(column(\"X\", 'sim://triangleWaveform(10,100,100,0.01)'), column(\"Y\", 'sim://triangleWaveform(20,100,100,0.01)'))" );
    }

    @Override
    public void openConfigurationPanel() {
        this.scatterGraphView.getDefaultConfigurationDialog().open();
    }

    final public static void main( String[] args ) {
        launch( args );
    }

}
