/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import com.sun.javafx.collections.ImmutableObservableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.Display;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VTypeToString;
import org.diirt.vtype.ValueUtil;

public final class ValueViewer extends ScrollPane {

    @FXML
    private TitledPane commonMetadata;
    @FXML
    private TextField typeField;
    @FXML
    private TextField alarmField;
    @FXML
    private TextField timeField;
    @FXML
    private TitledPane numberMetadata;
    @FXML
    private TextField displayRangeField;
    @FXML
    private TextField alarmRangeField;
    @FXML
    private TextField warningRangeField;
    @FXML
    private TextField controlRangeField;
    @FXML
    private TextField unitField;
    @FXML
    private TitledPane enumMetadata;
    @FXML
    private TitledPane tableMetadata;
    @FXML
    private TableView<VTableColumn> columnsTable;
    @FXML
    private TableColumn<VTableColumn, String> columnNameColumn;
    @FXML
    private TableColumn<VTableColumn, String> columnTypeColumn;
    @FXML
    private TableColumn<VTableColumn, Number> columnSizeColumn;
    @FXML
    private ListView<String> labelsField;
    @FXML
    private Button inspectTableButton;

    public ValueViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/ValueViewer.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        columnNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        setValue(null, false);
    }

    private Object value;

    public void setValue(Object value, boolean connection) {
        commonMetadata(value, connection);
        numberDisplay(ValueUtil.displayOf(value));
        enumMetadata(value);
        tableMetadata(value);
        this.value = value;
    }

    private void commonMetadata(Object value, boolean connection) {
        if (value == null) {
            typeField.setText(null);
            alarmField.setText(null);
            timeField.setText(null);
        } else {
            Class<?> clazz = ValueUtil.typeOf(value);
            if (clazz == null) {
                typeField.setText(null);
            } else {
                typeField.setText(clazz.getSimpleName());
            }
            alarmField.setText(VTypeToString.alarmToString(ValueUtil.alarmOf(value, connection)));
            timeField.setText(VTypeToString.timeToString(ValueUtil.timeOf(value)));
        }
    }

    private void numberDisplay(Display display) {
        if (display == null) {
            numberMetadata.setVisible(false);
            numberMetadata.setManaged(false);
        } else {
            numberMetadata.setVisible(true);
            numberMetadata.setManaged(true);
            displayRangeField.setText(display.getLowerDisplayLimit() + " - " + display.getUpperDisplayLimit());
            alarmRangeField.setText(display.getLowerAlarmLimit()+ " - " + display.getUpperAlarmLimit());
            warningRangeField.setText(display.getLowerWarningLimit()+ " - " + display.getUpperWarningLimit());
            controlRangeField.setText(display.getLowerCtrlLimit()+ " - " + display.getUpperCtrlLimit());
            unitField.setText(display.getUnits());
        }
    }

    private void enumMetadata(Object value) {
        if (value instanceof org.diirt.vtype.Enum) {
            enumMetadata.setVisible(true);
            enumMetadata.setManaged(true);
            labelsField.setItems(FXCollections.observableList(((org.diirt.vtype.Enum) value).getLabels()));
        } else {
            enumMetadata.setVisible(false);
            enumMetadata.setManaged(false);
        }
    }

    public static class VTableColumn {
        private final VTable vTable;
        private final int columnIndex;

        public VTableColumn(VTable vTable, int columnIndex) {
            this.vTable = vTable;
            this.columnIndex = columnIndex;
        }

        public String getName() {
            return vTable.getColumnName(columnIndex);
        }

        public String getType() {
            return vTable.getColumnType(columnIndex).getSimpleName();
        }

        public int getSize() {
            Object data = vTable.getColumnData(columnIndex);
            if (data instanceof ListNumber) {
                return ((ListNumber) data).size();
            } else if (data instanceof List) {
                return ((List) data).size();
            } else {
                return 0;
            }
        }


    }

    private void tableMetadata(Object value) {
        if (value instanceof org.diirt.vtype.VTable) {
            tableMetadata.setVisible(true);
            tableMetadata.setManaged(true);
            VTable vTable = (VTable) value;
            List<VTableColumn> columns = new ArrayList<>();
            for (int n = 0; n < vTable.getColumnCount(); n++) {
                columns.add(new VTableColumn(vTable, n));
            }
            columnsTable.setItems(FXCollections.observableList(columns));
        } else {
            tableMetadata.setVisible(false);
            tableMetadata.setManaged(false);
            columnsTable.setItems(new ImmutableObservableList<>());
        }
    }

    @FXML
    private void onInspectTable(ActionEvent event) {
        VTableInspector.instpectValue((VTable) value);
    }

}
