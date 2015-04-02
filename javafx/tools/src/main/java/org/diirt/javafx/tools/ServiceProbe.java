/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
import javafx.util.StringConverter;
import org.diirt.javafx.tools.ServiceProbe.Editors.Editor;
import static org.diirt.javafx.tools.ServiceProbe.Editors.makeEditor;
import org.diirt.javafx.tools.ServiceProbe.Viewers.Viewer;
import static org.diirt.javafx.tools.ServiceProbe.Viewers.makeViewer;
import org.diirt.service.Service;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethod.DataDescription;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.Alarm;
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
    private TextArea consoleField;
    
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

    static class Editors {

        public static abstract class Editor<T> {

            public abstract Region getRegion();

            public abstract T parseInput();
        }

        public static Editor makeEditor(Class type) {
            if (type.equals(VString.class)) {
                return new StringEditor();
            }

            throw new IllegalArgumentException("Unsupported class type");
        }

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

        }
        
        static class NumberEditor extends Editor<VNumber> {

            private final TextField field;

            NumberEditor() {
                field = new TextField();
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public VNumber parseInput() {
                return ValueFactory.newVNumber(Double.parseDouble(field.getText()), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
            }

        }
 
    }

    static class Viewers {

        public static abstract class Viewer<T> {

            public abstract Region getRegion();

            public abstract void putInput(T input);
        }

        public static Viewer makeViewer(Class type) {
            if (type.equals(VString.class)) {
                return new StringViewer();
            }
            else if (type.equals(VNumber.class)){
                return new NumberViewer();
            }
            if (type.equals(VType.class)){
                return new TypeViewer();
            }
            throw new IllegalArgumentException("Unsupported class type");            
        }

        static class StringViewer extends Viewer<VString> {

            private final TextField field;

            public StringViewer() {
                field = new TextField();
                field.setEditable(false);
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public void putInput(VString input) {
                field.setText(input.getValue());
            }

        }
        
        static class NumberViewer extends Viewer<VNumber> {

            private final TextField field;

            public NumberViewer() {
                field = new TextField();
                field.setEditable(false);
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public void putInput(VNumber input) {
                field.setText(input.getValue().toString());
            }

        }
     
        static class TypeViewer extends Viewer<VType> {

            private final TextField field;

            public TypeViewer() {
                field = new TextField();
                field.setEditable(false);
            }

            @Override
            public Region getRegion() {
                return field;
            }

            @Override
            public void putInput(VType input) {
                field.setText(input.toString());
            }

        }        
    }

    static class ArgumentPane extends HBox {

        private final Label title;
        public final Editor value;
        public final DataDescription data;

        public ArgumentPane(DataDescription data) {
            this.data = data;
            this.title = new Label();
            this.value = makeEditor(data.getType());

            title.setText(data.getName());

            this.getChildren().add(title);
            this.getChildren().add(value.getRegion());
        }
    }

    static class ResultPane extends HBox {

        private final Label title;
        public final Viewer value;
        public final DataDescription data;

        public ResultPane(DataDescription data) {
            this.data = data;
            this.title = new Label();
            this.value = makeViewer(data.getType());

            title.setText(data.getName());

            this.getChildren().add(title);
            this.getChildren().add(value.getRegion());
        }
    }
    
    public ServiceProbe() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServiceProbe.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        // TODO auto-select combo boxes
        // TODO validate argument input
        serviceField.getItems().addAll(FXCollections.observableList(listServices()));
        serviceField.valueProperty().addListener(new ChangeListener<Service>() {

            @Override
            public void changed(ObservableValue<? extends Service> observable, Service oldValue, Service newValue) {
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
        methodField.valueProperty().addListener(new ChangeListener<ServiceMethod>() {

            @Override
            public void changed(ObservableValue<? extends ServiceMethod> observable, ServiceMethod oldValue, ServiceMethod newValue) {
                argumentField.setItems(FXCollections.observableList(
                        newValue.getArguments().stream()
                        .map(arg -> (new ArgumentPane(arg)))
                        .collect(Collectors.toList())));
                resultField.setItems(FXCollections.observableList(
                        newValue.getResults().stream()
                        .map(result -> (new ResultPane(result)))
                        .collect(Collectors.toList())));
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
                    consoleField.appendText("Service method executing.\n");
                    item.executeAsync(arguments, callback, ex -> consoleField.appendText("Exception:\n" + ex.toString() + "\n"));
                }
                else{
                    // Nothing to execute
                    consoleField.appendText("No method selected.\n");
                }
            }

        });
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Diirt - Service Probe", ServiceProbe.class, args);
    }
}
