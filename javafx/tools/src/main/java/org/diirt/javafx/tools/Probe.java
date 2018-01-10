/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class Probe extends VBox {

    public Probe() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/Probe.fxml"));

        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // TODO: temporary for CS-Studio
    public static Scene createScene() throws Exception {
        Parent root = FXMLLoader.load(Probe.class.getResource("/fxml/Probe.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        return scene;
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Probe", Probe.class, args);
    }

}
