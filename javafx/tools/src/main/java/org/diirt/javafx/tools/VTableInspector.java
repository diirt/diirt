/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static org.diirt.util.text.StringUtil.DOUBLE_REGEX_WITH_NAN;
import org.diirt.vtype.VTable;
import org.diirt.vtype.table.VTableFactory;

/**
 * FXML Controller class
 *
 * @author carcassi
 */
public class VTableInspector extends GridPane {

    @FXML
    private ListView<Object> valuesField;
    @FXML
    private ComboBox<String> columnField;
    @FXML
    private ComboBox<String> filterField;


    public VTableInspector() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("VTableInspector.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        filterField.setItems(FXCollections.observableList(Arrays.asList("All", "Not numbers")));
        filterField.getSelectionModel().select("All");
    }

    private VTable vTable;

    public void setValue(VTable vTable) {
        columnField.setItems(FXCollections.observableList(VTableFactory.columnNames(vTable)));
        this.vTable = vTable;
    }

    public static void instpectValue(VTable vTable) {
        Stage stage = new Stage();
        stage.setTitle("Inspect VTable");
        VTableInspector inspector = new VTableInspector();
        inspector.setValue(vTable);
        stage.setScene(new Scene(inspector));
        stage.show();
    }

    @FXML
    private void onColumnChange(ActionEvent event) {
        filterValues();
    }

    @FXML
    private void onFilterChange(ActionEvent event) {
        filterValues();
    }

    private static final Pattern pDouble = Pattern.compile(DOUBLE_REGEX_WITH_NAN);

    private void filterValues() {
        String columnName = columnField.getValue();
        String filter = filterField.getValue();
        int columnIndex = columnField.getItems().indexOf(columnName);
        if (columnIndex != -1 && vTable.getColumnType(columnIndex).equals(String.class)) {
            @SuppressWarnings("unchecked")
            List<Object> values = (List<Object>) vTable.getColumnData(columnIndex);
            List<Object> selectedValues;
            if (filter.equals("Not numbers")) {
                selectedValues = values.stream().filter(value -> !pDouble.matcher(value.toString()).matches()).collect(Collectors.toList());
            } else {
                selectedValues = values;
            }
            valuesField.setItems(FXCollections.observableList(selectedValues));

        } else {
            valuesField.setItems(FXCollections.singletonObservableList((Object) "Type not supported yet..."));
        }
    }

    public static void main(String[] args) {
        JavaFXLaunchUtil.launch("Diirt - VTable Inspector", VTableInspector.class, args);
    }

}
