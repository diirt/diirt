/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import org.diirt.vtype.SimpleValueFormat;
import org.diirt.vtype.ValueFormat;

public final class EventViewer extends ScrollPane {

    @FXML
    private TitledPane eventReadConnection;
    @FXML
    private CheckBox readConnectedField;
    @FXML
    private TitledPane eventReadValue;
    @FXML
    private TextField valueField;
    @FXML
    private TitledPane eventReadError;
    @FXML
    private TextField readExceptionMessageField;
    @FXML
    private TextArea readExceptionField;
    @FXML
    private TitledPane eventWriteConnection;
    @FXML
    private CheckBox writeConnectedField;
    @FXML
    private TitledPane eventWriteError;
    @FXML
    private TitledPane eventWriteSucceeded;
    @FXML
    private TitledPane eventWriteFailed;
    @FXML
    private TextField writeFailedMessageField;
    @FXML
    private TextArea writeFailedField;
    @FXML
    private TextField writeExceptionMessageField;
    @FXML
    private TextArea writeExceptionField;

    public EventViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/EventViewer.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setEvent(null);
    }

    public void setEvent(Event event) {
        ReadEvent readEvent = null;
        WriteEvent writeEvent = null;
        if (event instanceof ReadEvent) {
            readEvent = (ReadEvent) event;
        }
        if (event instanceof WriteEvent) {
            writeEvent = (WriteEvent) event;
        }
        updateReadConnection(readEvent);
        updateReadValue(readEvent);
        updateReadError(readEvent);
        updateWriteConnection(writeEvent);
        updateWriteSucceeded(writeEvent);
        updateWriteFailed(writeEvent);
        updateWriteError(writeEvent);
    }

    private void updateReadConnection(ReadEvent readEvent) {
        if (readEvent != null && readEvent.getEvent().isConnectionChanged()) {
            eventReadConnection.setVisible(true);
            eventReadConnection.setManaged(true);
            readConnectedField.setSelected(readEvent.isConnected());
        } else {
            eventReadConnection.setVisible(false);
            eventReadConnection.setManaged(false);
            readConnectedField.setSelected(false);
        }
    }

    private ValueFormat format = new SimpleValueFormat(3);

    private void updateReadValue(ReadEvent readEvent) {
        if (readEvent != null && readEvent.getEvent().isValueChanged()) {
            eventReadValue.setVisible(true);
            eventReadValue.setManaged(true);
            valueField.setText(format.format(readEvent.getValue()));
        } else {
            eventReadValue.setVisible(false);
            eventReadValue.setManaged(false);
            valueField.setText(null);
        }
    }

    private void updateReadError(ReadEvent readEvent) {
        if (readEvent != null && readEvent.getEvent().isExceptionChanged()) {
            eventReadError.setVisible(true);
            eventReadError.setManaged(true);
            readExceptionMessageField.setText(readEvent.getLastException().getMessage());
            StringWriter sw = new StringWriter();
            readEvent.getLastException().printStackTrace(new PrintWriter(sw));
            readExceptionField.setText(sw.toString());
        } else {
            eventReadError.setVisible(false);
            eventReadError.setManaged(false);
            readExceptionMessageField.setText(null);
            readExceptionField.setText(null);
        }
    }

    private void updateWriteConnection(WriteEvent writeEvent) {
        if (writeEvent != null && writeEvent.getEvent().isConnectionChanged()) {
            eventWriteConnection.setVisible(true);
            eventWriteConnection.setManaged(true);
            writeConnectedField.setSelected(writeEvent.isConnected());
        } else {
            eventWriteConnection.setVisible(false);
            eventWriteConnection.setManaged(false);
            writeConnectedField.setSelected(false);
        }
    }

    private void updateWriteSucceeded(WriteEvent writeEvent) {
        if (writeEvent != null && writeEvent.getEvent().isWriteSucceeded()) {
            eventWriteSucceeded.setVisible(true);
            eventWriteSucceeded.setManaged(true);
        } else {
            eventWriteSucceeded.setVisible(false);
            eventWriteSucceeded.setManaged(false);
        }
    }

    private void updateWriteFailed(WriteEvent writeEvent) {
        if (writeEvent != null && writeEvent.getEvent().isWriteFailed()) {
            eventWriteFailed.setVisible(true);
            eventWriteFailed.setManaged(true);
            writeFailedMessageField.setText(writeEvent.getLastException().getMessage());
            StringWriter sw = new StringWriter();
            writeEvent.getLastException().printStackTrace(new PrintWriter(sw));
            writeFailedField.setText(sw.toString());
        } else {
            eventWriteFailed.setVisible(false);
            eventWriteFailed.setManaged(false);
            writeFailedMessageField.setText(null);
            writeFailedField.setText(null);
        }
    }

    private void updateWriteError(WriteEvent writeEvent) {
        if (writeEvent != null && writeEvent.getEvent().isExceptionChanged()) {
            eventWriteError.setVisible(true);
            eventWriteError.setManaged(true);
            writeExceptionMessageField.setText(writeEvent.getLastException().getMessage());
            StringWriter sw = new StringWriter();
            writeEvent.getLastException().printStackTrace(new PrintWriter(sw));
            writeExceptionField.setText(sw.toString());
        } else {
            eventWriteError.setVisible(false);
            eventWriteError.setManaged(false);
            writeExceptionMessageField.setText(null);
            writeExceptionField.setText(null);
        }
    }

}
