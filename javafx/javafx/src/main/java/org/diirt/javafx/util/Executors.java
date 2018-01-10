/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.util;

import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Bridge from JavaFX to Executor framework.
 *
 * @author carcassi
 */
public class Executors {

    private static final Logger log = Logger.getLogger(Executors.class.getName());

    /**
     * Executes tasks on the JavaFX Application thread using
     * Platform.runLater().
     *
     * @return an executor that posts events to JavaFX
     */
    public static Executor javaFXAT() {
        return JAVA_FX_EXECUTOR;
    }

    private static final Executor JAVA_FX_EXECUTOR = new Executor() {

        @Override
        public void execute(Runnable command) {
            Platform.runLater(command);
        }
    };
}
