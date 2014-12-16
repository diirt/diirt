/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.ui.tools;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.util.time.TimeDuration;

public class ProbeController implements Initializable {
    
    private PVReader<?> pv;
    
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
                    valueField.setText(Objects.toString(e.getPvReader().getValue()));
                })
                .writeListener((PVWriterEvent<Object> e) -> {
                    if (e.isConnectionChanged()) {
                        if (e.getPvWriter().isWriteConnected()) {
                            newValueField.setDisable(false);
                            newValueField.setEditable(true);
                        }
                    }
                })
                .notifyOn(exec)
                .asynchWriteAndMaxReadRate(TimeDuration.ofHertz(50));
    }
    
    private static Executor exec = new Executor() {

        @Override
        public void execute(Runnable command) {
            Platform.runLater(command);
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
