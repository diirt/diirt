/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.stage.Stage;
import static javafx.application.Application.launch;

/**
 *
 * @author mjchao
 */
public class LineGraphApp extends BaseGraphApp {

    private final LineGraphView lineGraphView = new LineGraphView();

    @Override
    public BaseGraphView getGraphView() {
        return this.lineGraphView;
    }

    @Override
    public void start( Stage stage ) throws Exception {
        super.start( stage );

        this.addDataFormulae( "sim://table",
                    "sim://gaussianWaveform",
                    "sim://sineWaveform",
                    "sim://triangleWaveform",
                    "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" );
    }

    @Override
    public void openConfigurationPanel() {
        this.lineGraphView.getDefaultConfigurationDialog().open();
    }

    final public static void main( String[] args ) {
        launch( args );
    }

}
