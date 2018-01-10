/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import javafx.application.Application;
import javafx.stage.Stage;


public class TestGraphene extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        throw new RuntimeException( "Do not run... use IntensityGraphApp" );
        /*BaseGraphView root;
        root.reconnect( "sim://sine2DWaveform(1,50,45,100,100,0.1)" );

        Scene scene = new Scene(root);

        stage.setTitle("diirt - Graphene");
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setWidth(400);
        stage.show();*/
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
