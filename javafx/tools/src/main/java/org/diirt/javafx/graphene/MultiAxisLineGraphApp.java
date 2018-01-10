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
public class MultiAxisLineGraphApp extends BaseGraphApp {

    private final MultiAxisLineGraphView multiAxislineGraphView = new MultiAxisLineGraphView();

    @Override
    public BaseGraphView getGraphView() {
        return this.multiAxislineGraphView;
    }

    @Override
    public void start( Stage stage ) throws Exception {
        super.start( stage );

        this.addDataFormulae( "=tableOf(column(\"Sine\", 'sim://sineWaveform(1,50,100,0.01)'), column(\"Triangle\", 'sim://triangleWaveform(2,50,100,0.01)'), column(\"Triangle\", 'sim://squareWaveform(3,50,100,0.01)'))",
                    "sim://gaussianWaveform",
                    "sim://sineWaveform",
                    "sim://triangleWaveform",
                    "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" );
    }

    @Override
    public void openConfigurationPanel() {
        this.multiAxislineGraphView.getDefaultConfigurationDialog().open();
    }

    final public static void main( String[] args ) {
        launch( args );
    }
}
