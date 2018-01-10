/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public final class Toolbox extends VBox {

    private static class Tool {
        private final String toolName;
        private final Class<? extends Parent> toolClass;

        public Tool(String toolName, Class<? extends Parent> toolClass) {
            this.toolName = toolName;
            this.toolClass = toolClass;
        }

        public String getToolName() {
            return toolName;
        }

        public Class<? extends Parent> getToolClass() {
            return toolClass;
        }

        public void open() {
            JavaFXLaunchUtil.open("Diirt - " + getToolName(), toolClass);
        }
    }

    private static final List<Tool> tools = Arrays.asList(
            new Tool("Probe", Probe.class),
            new Tool("Service Probe", ServiceProbe.class),
            new Tool("Formula Function Browser", FormulaFunctionViewer.class),
            new Tool("Service Browser", ServiceViewer.class));

    private static class ToolButton extends Button {
        private final Tool tool;

        public ToolButton(Tool tool) {
            this.tool = tool;
            setText("Open " + tool.getToolName() + "...");
            addEventHandler(ActionEvent.ACTION, (e) -> this.tool.open());
            setMaxWidth(Double.MAX_VALUE);
        }


    }

    public Toolbox() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("Toolbox.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        for (Tool tool : tools) {
            getChildren().add(new ToolButton(tool));
        }
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Diirt - Toolbox", Toolbox.class, args);
    }

}
