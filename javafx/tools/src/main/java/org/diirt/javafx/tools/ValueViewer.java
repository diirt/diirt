/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import com.sun.javafx.collections.ImmutableObservableList;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import org.diirt.vtype.Display;
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
    private ListView<String> labelsField;

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
        
        setValue(null, false);
    }
    
    public void setValue(Object value, boolean connection) {
        commonMetadata(value, connection);
        numberDisplay(ValueUtil.displayOf(value));
        enumMetadata(value);
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

}
