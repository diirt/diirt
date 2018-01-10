/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.formula.FormulaAst;

public final class ExpressionProbe extends ScrollPane {

    @FXML
    private TextField nameField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField astField;
    @FXML
    private TableView<Property> propertiesTable;
    @FXML
    private TableColumn<Property, String> propertyNameColumn;
    @FXML
    private TableColumn<Property, String> propertyValueColumn;
    @FXML
    private TextField usageField;
    @FXML
    private CheckBox readConnectedField;
    @FXML
    private CheckBox writeConnectedField;
    @FXML
    private TitledPane expressionMetadata;
    @FXML
    private TitledPane channelMetadata;

    public ExpressionProbe() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("ExpressionProbe.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        propertyNameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        propertyValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        setExpression(null);


    }

    private Timeline timeline;

    public void startTimer() {
        stopTimer();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> channelInfo()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    public static class Property {
        private final String key;
        private final String value;

        public Property(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }

    private String expression;
    private FormulaAst ast;

    public void setExpression(String expression) {
        this.expression = expression;
        if (expression == null) {
            ast = null;
            stopTimer();
        } else {
            ast = FormulaAst.formula(expression);
            startTimer();
        }
        channelInfo();
    }

    private void channelInfo() {
        if (ast != null && ast.getType() == FormulaAst.Type.CHANNEL) {
            // Formula is a channel
            channelMetadata.setVisible(true);
            channelMetadata.setManaged(true);
            ChannelHandler handler = PVManager.getDefaultDataSource().getChannels().get(ast.toString());
            if (handler != null) {
                Set<Map.Entry<String, Object>> properties = handler.getProperties().entrySet();
                if (!properties.isEmpty()) {
                    propertiesTable.setItems(FXCollections.observableList(
                        properties.stream().map(entry -> new Property(entry.getKey(), entry.getValue().toString())).collect(Collectors.toList())));
                } else {
                propertiesTable.setItems(null);
                }
            } else {
                propertiesTable.setItems(null);
            }
        } else {
            // Formula is not a channel
            channelMetadata.setVisible(false);
            channelMetadata.setManaged(false);
        }
    }
//
//    private void numberDisplay(Display display) {
//        if (display == null) {
//            numberMetadata.setVisible(false);
//            numberMetadata.setManaged(false);
//        } else {
//            numberMetadata.setVisible(true);
//            numberMetadata.setManaged(true);
//            displayRangeField.setText(display.getLowerDisplayLimit() + " - " + display.getUpperDisplayLimit());
//            alarmRangeField.setText(display.getLowerAlarmLimit()+ " - " + display.getUpperAlarmLimit());
//            warningRangeField.setText(display.getLowerWarningLimit()+ " - " + display.getUpperWarningLimit());
//            controlRangeField.setText(display.getLowerCtrlLimit()+ " - " + display.getUpperCtrlLimit());
//            unitField.setText(display.getUnits());
//        }
//    }
//
//    private void enumMetadata(Object value) {
//        if (value instanceof org.diirt.vtype.Enum) {
//            enumMetadata.setVisible(true);
//            enumMetadata.setManaged(true);
//            labelsField.setItems(FXCollections.observableList(((org.diirt.vtype.Enum) value).getLabels()));
//        } else {
//            enumMetadata.setVisible(false);
//            enumMetadata.setManaged(false);
//        }
//    }
//
//    public static class VTableColumn {
//        private final VTable vTable;
//        private final int columnIndex;
//
//        public VTableColumn(VTable vTable, int columnIndex) {
//            this.vTable = vTable;
//            this.columnIndex = columnIndex;
//        }
//
//        public String getName() {
//            return vTable.getColumnName(columnIndex);
//        }
//
//        public String getType() {
//            return vTable.getColumnType(columnIndex).getSimpleName();
//        }
//
//        public int getSize() {
//            Object data = vTable.getColumnData(columnIndex);
//            if (data instanceof ListNumber) {
//                return ((ListNumber) data).size();
//            } else if (data instanceof List) {
//                return ((List) data).size();
//            } else {
//                return 0;
//            }
//        }
//
//
//    }
//
//    private void tableMetadata(Object value) {
//        if (value instanceof org.diirt.vtype.VTable) {
//            tableMetadata.setVisible(true);
//            tableMetadata.setManaged(true);
//            VTable vTable = (VTable) value;
//            List<VTableColumn> columns = new ArrayList<>();
//            for (int n = 0; n < vTable.getColumnCount(); n++) {
//                columns.add(new VTableColumn(vTable, n));
//            }
//            columnsTable.setItems(FXCollections.observableList(columns));
//        } else {
//            tableMetadata.setVisible(false);
//            tableMetadata.setManaged(false);
//            columnsTable.setItems(new ImmutableObservableList<>());
//        }
//    }
//
//    private void onInspectTable(ActionEvent event) {
//        VTableInspector.instpectValue((VTable) expression);
//    }

}
