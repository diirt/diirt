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
public class IntensityGraphApp extends BaseGraphApp {

    private final IntensityGraphView intensityGraphView = new IntensityGraphView();

    @Override
    public BaseGraphView getGraphView() {
        return this.intensityGraphView;
    }

    @Override
    public void openConfigurationPanel() {
        this.intensityGraphView.getDefaultConfigurationDialog().open();
    }

    @Override
    public void start( Stage stage ) throws Exception {
        super.start( stage );

        this.addDataFormulae( "sim://gaussianWaveform",
                    "sim://sine2DWaveform(1,50,45,100,100,0.1)",
                    "=ndArray(arrayOf(1,3,2,4,3,5), dimDisplay(3,FALSE), dimDisplay(2,FALSE))",
                    "=ndArray('13SIM1:image1:ArrayData', dimDisplay('13SIM1:cam1:ArraySizeY_RBV', TRUE), dimDisplay('13SIM1:cam1:ArraySizeX_RBV', FALSE))",
                    "=histogram2DOf(tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)')), \"Y\", \"X\")",
                    "sim://square2DWaveform(1,50,45,10000,10000,0.1)");
    }

    final public static void main( String[] args ) {
        launch( args );
    }

}
