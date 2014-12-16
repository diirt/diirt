/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.ui.tools;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.util.concurrent.Executors;
import org.diirt.util.time.TimeDuration;
import org.diirt.vtype.SimpleValueFormat;
import org.diirt.vtype.ValueFormat;

public class ProbeController implements Initializable {
    
    private PVReader<?> pv;
    
    private ValueFormat format = new SimpleValueFormat(3);
    
    @FXML
    private TextField channelField;
    @FXML
    private TextField valueField;
    @FXML
    private TextField newValueField;

    @FXML
    private void onChannelChanged(ActionEvent event) {
        if (pv != null) {
            pv.close();
            newValueField.setText(null);
            valueField.setText(null);
            newValueField.setEditable(false);
            newValueField.setDisable(true);
        }

        pv = PVManager.readAndWrite(ExpressionLanguage.formula(channelField.getText()))
                .readListener((PVReaderEvent<Object> e) -> {
                    valueField.setText(format.format(e.getPvReader().getValue()));
                })
                .writeListener((PVWriterEvent<Object> e) -> {
                    if (e.isConnectionChanged()) {
                        if (e.getPvWriter().isWriteConnected()) {
                            newValueField.setDisable(false);
                            newValueField.setEditable(true);
                        }
                    }
                })
                .notifyOn(Executors.javaFXAT())
                .asynchWriteAndMaxReadRate(TimeDuration.ofHertz(50));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
