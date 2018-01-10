/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author carcassi
 */
public class JavaFXLaunchUtil {

    private JavaFXLaunchUtil() {
        // No instances allowed
    }

    public static void launch(String title, Class<? extends Parent> rootClass, String... args) {
        appTitle = title;
        appRootClass = rootClass;
        SimpleApplication.launch(SimpleApplication.class, args);
    }

    public static void open(String title, Class<? extends Parent> rootClass) {
        try {
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(rootClass.newInstance()));
            stage.show();
        } catch (InstantiationException | IllegalAccessException instantiationException) {
            // TODO put an Alert, but requires jdk 8u40
        }
    }

    private static volatile String appTitle;
    private static volatile Class<? extends Parent> appRootClass;

    public static class SimpleApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle(appTitle);
            Scene scene = new Scene(appRootClass.newInstance());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}
