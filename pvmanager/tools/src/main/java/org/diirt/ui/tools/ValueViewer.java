/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.ui.tools;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.Time;
import org.diirt.vtype.VTypeToString;
import org.diirt.vtype.ValueUtil;

public class ValueViewer extends ScrollPane {
    
    @FXML
    private TextField typeField;
    @FXML
    private TextField alarmField;
    @FXML
    private TextField timeField;

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
    }
    
    public void setValue(Object value, boolean connection) {
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
