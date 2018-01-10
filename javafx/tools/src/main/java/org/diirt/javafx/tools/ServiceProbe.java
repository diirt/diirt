/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.diirt.javafx.tools.ServiceProbe.Editors.Editor;
import static org.diirt.javafx.tools.ServiceProbe.Editors.makeEditor;
import org.diirt.javafx.tools.ServiceProbe.Viewers.Viewer;
import static org.diirt.javafx.tools.ServiceProbe.Viewers.makeViewer;
import org.diirt.service.Service;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethod.DataDescription;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

/**
 *
 * @author asbarber
 */
public final class ServiceProbe extends VBox {

    @FXML
    private ComboBox<Service> serviceField;
    @FXML
    private ComboBox<ServiceMethod> methodField;
    @FXML
    private ListView<ArgumentPane> argumentField;
    @FXML
    private ListView<ResultPane> resultField;
    @FXML
    private Button executeField;
    @FXML
    private TextArea errorField;

    private static List<Service> listServices() {
        return ServiceRegistry.getDefault()
                .getRegisteredServiceNames().stream().sorted()
                .map(name -> (ServiceRegistry.getDefault().findService(name)))
                .collect(Collectors.toList());
    }

    private static List<ServiceMethod> listServiceMethods(Service service) {
        return service.getServiceMethods().keySet().stream()
                .map(name -> (service.getServiceMethods().get(name)))
                .collect(Collectors.toList());
    }

    // Inputting data
    static class Editors {

        // Generic editor to place in panes
        public static abstract class Editor<T> {

            public abstract Region getRegion();

            public abstract T parseInput();

            public abstract boolean isValid();
        }

        // Validation on a text field
        static abstract class ValidateEditor<T> extends Editor<T> {

            private static final String styleError = "-fx-text-box-border-color: red;-fx-focus-color: red;";
            private static final String styleDefault = "";
            protected final TextField field;

            ValidateEditor() {
                field = new TextField() {

                    @Override
                    public void replaceText(int start, int end, String text) {
                        super.replaceText(start, end, text);

                        if (!validateInput(getText())) {
                            this.setStyle(styleError);
                        } else {
                            this.setStyle(styleDefault);
                        }
                    }

                    @Override
                    public void replaceSelection(String text) {
                        super.replaceSelection(text);

                        if (!validateInput(getText())) {
                            this.setStyle(styleError);
                        } else {
                            this.setStyle(styleDefault);
                        }
                    }

                };
            }

            @Override
            public Region getRegion() {
                return field;
            }

            protected abstract boolean validateInput(String text);
        }

        // Factory to make editors
        public static Editor makeEditor(Class type) {
            if (VString.class.isAssignableFrom(type)) {
                return new StringEditor();
            } else if (VInt.class.isAssignableFrom(type)) {
                return new IntegerEditor();
            } else if (VNumber.class.isAssignableFrom(type)) {
                return new NumberEditor();
            }

            throw new IllegalArgumentException("Unsupported class type");
        }

        // String
        static class StringEditor extends Editor<VString> {

            private final TextField field;

            StringEditor() {
                field = new TextField();
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public VString parseInput() {
                return ValueFactory.newVString(field.getText(), ValueFactory.alarmNone(), ValueFactory.timeNow());
            }

            @Override
            public boolean isValid() {
                // String input is always valid
                return true;
            }

        }

        // Integer
        static class IntegerEditor extends ValidateEditor<VInt> {

            @Override
            public VInt parseInput() {
                return ValueFactory.newVInt(Integer.parseInt(field.getText()), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
            }

            @Override
            protected boolean validateInput(String text) {
                try {
                    Double.parseDouble(text);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }

            @Override
            public boolean isValid() {
                return validateInput(field.getText());
            }

        }

        // Number
        static class NumberEditor extends ValidateEditor<VNumber> {

            @Override
            public VNumber parseInput() {
                return ValueFactory.newVNumber(Double.parseDouble(field.getText()), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
            }

            @Override
            protected boolean validateInput(String text) {
                try {
                    Double.parseDouble(text);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }

            @Override
            public boolean isValid() {
                return validateInput(field.getText());
            }

        }

    }

    // Viewing data
    static class Viewers {

        // Generic viewer to place in panes
        public static abstract class Viewer<T> {

            public abstract Region getRegion();

            public abstract void putInput(T input);
        }

        // Factory to make viewers
        public static Viewer makeViewer(Class type) {
//            if (VString.class.isAssignableFrom(type)) {
//                return new TextViewer<VString>(i -> i.getValue());
//            } else if (VNumber.class.isAssignableFrom(type)) {
//                return new TextViewer<VNumber>(i -> i.getValue().toString());
//            } else if (VType.class.isAssignableFrom(type)) {
//                return new TextViewer<VType>(i -> i.toString());
//            }

            if (VType.class.isAssignableFrom(type)) {
                return new TextViewer<>(value -> {
                    if (VString.class.isAssignableFrom(value.getClass())) {
                        return ((VString) value).getValue();
                    } else if (VNumber.class.isAssignableFrom(type)) {
                        return ((VNumber) value).getValue().toString();
                    } else {
                        return value.toString();
                    }
                });
            }
            throw new IllegalArgumentException("Unsupported class type");
        }

        // Generic implementation with simple textfield
        static class TextViewer<T> extends Viewer<T> {

            private final TextField field;
            private final Function<T, String> mapping;

            public TextViewer(Function<T, String> mapping) {
                this.mapping = mapping;
                field = new TextField();
                field.setEditable(false);
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public void putInput(T input) {
                Platform.runLater(() -> {
                    field.setText(mapping.apply(input));
                });
            }
        }
    }

    // Method arguments
    static class ArgumentPane extends HBox {

        private final Label title;
        public final Editor value;
        public final DataDescription data;

        public ArgumentPane(DataDescription data) {
            this.data = data;
            this.title = new Label();
            this.value = makeEditor(data.getType());

            title.setText(data.getName());

            title.setPrefWidth(100);
            value.getRegion().setPrefWidth(200);

            this.getChildren().add(title);
            this.getChildren().add(value.getRegion());
        }
    }

    // Method results
    static class ResultPane extends HBox {

        private final Label title;
        public final Viewer value;
        public final DataDescription data;

        public ResultPane(DataDescription data) {
            this.data = data;
            this.title = new Label();
            this.value = makeViewer(data.getType());

            title.setText(data.getName());

            title.setPrefWidth(100);
            value.getRegion().setPrefWidth(200);

            this.getChildren().add(title);
            this.getChildren().add(value.getRegion());
        }
    }

    // Form creation
    public ServiceProbe() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServiceProbe.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        List<Thread> editors = new ArrayList<>();

        // SERVICE combo box
        serviceField.getItems().addAll(FXCollections.observableList(listServices()));
        serviceField.valueProperty().addListener(new ChangeListener<Service>() {

            @Override
            public void changed(ObservableValue<? extends Service> observable, Service oldValue, Service newValue) {
                argumentField.getItems().clear();
                resultField.getItems().clear();
                methodField.setItems(FXCollections.observableList(listServiceMethods(newValue)));
            }

        });
        serviceField.setConverter(new StringConverter<Service>() {

            @Override
            public String toString(Service object) {
                return object.getName();
            }

            @Override
            public Service fromString(String string) {
                throw new UnsupportedOperationException("Not supported.");
            }

        });

        // METHOD combo box
        methodField.valueProperty().addListener(new ChangeListener<ServiceMethod>() {

            @Override
            public void changed(ObservableValue<? extends ServiceMethod> observable, ServiceMethod oldValue, ServiceMethod newValue) {
                if (newValue == null) {
                    return;
                }

                argumentField.setItems(FXCollections.observableList(
                        newValue.getArguments().stream()
                        .map(arg -> (new ArgumentPane(arg)))
                        .collect(Collectors.toList())));
                resultField.setItems(FXCollections.observableList(
                        newValue.getResults().stream()
                        .map(result -> (new ResultPane(result)))
                        .collect(Collectors.toList())));

                // Stops current validation
                editors.stream().forEach((thread) -> {
                    thread.interrupt();
                });
                editors.clear();

                // Adds new validation
                Thread t = new Thread() {

                    private void action(boolean validity) {
                        if (validity) {
                            // Not enabled, then enable
                            if (executeField.disableProperty().getValue()) {
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        executeField.disableProperty().setValue(Boolean.FALSE);
                                    }

                                });
                            }
                        } else {
                            // Not disabled, then disable
                            if (!executeField.disableProperty().getValue()) {
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        executeField.disableProperty().setValue(Boolean.TRUE);
                                    }

                                });
                            }
                        }
                    }

                    @Override
                    public void run() {
                        while (!this.isInterrupted()) {
                            // Detects validity
                            boolean valid = true;
                            for (ArgumentPane pane : argumentField.getItems()) {
                                if (!pane.value.isValid()) {
                                    valid = false;
                                    break;
                                }
                            }
                            action(valid);

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }

                };
                editors.add(t);
                t.setDaemon(true);
                t.start();
            }

        });
        methodField.setConverter(new StringConverter<ServiceMethod>() {

            @Override
            public String toString(ServiceMethod object) {
                return object.getName();
            }

            @Override
            public ServiceMethod fromString(String string) {
                throw new UnsupportedOperationException("Not supported.");
            }

        });

        // EXECUTE button
        executeField.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ServiceMethod item = methodField.getSelectionModel().getSelectedItem();
                if (item != null) {
                    // Arguments
                    Map<String, Object> arguments = new HashMap<>();
                    argumentField.getItems().stream()
                            .forEach(pane -> arguments.put(pane.data.getName(), pane.value.parseInput()));

                    // Results
                    Consumer<Map<String, Object>> callback = (Map<String, Object> results) -> {
                        results.entrySet().stream().forEach(set -> {
                            for (ResultPane resultPane : resultField.getItems()) {
                                if (resultPane.data.getName().equals(set.getKey())) {
                                    resultPane.value.putInput(set.getValue());
                                    break;
                                }
                            }
                        }
                        );
                    };

                    // Execution
                    item.executeAsync(arguments, callback, ex -> errorField.appendText("Exception:\n" + ex.toString() + "\n"));
                } else {
                    // Nothing to execute
                    errorField.appendText("No method selected.\n");
                }
            }

        });
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Diirt - Service Probe", ServiceProbe.class, args);
    }
}
