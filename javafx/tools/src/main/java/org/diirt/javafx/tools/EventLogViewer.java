/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public final class EventLogViewer extends HBox {

    @FXML
    private ListView<Event> eventList;
    @FXML
    private ValueViewer valueViewer;
    @FXML
    private EventViewer eventViewer;

    private Log eventLog = new Log(new Runnable() {
        @Override
        public void run() {
            onNewEvent();
        }

    });

    public EventLogViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/EventLogViewer.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        eventList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {

            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                eventViewer.setEvent(newValue);
                if (newValue instanceof ReadEvent) {
                    ReadEvent readEvent = (ReadEvent) newValue;
                    valueViewer.setValue(readEvent.getValue(), readEvent.isConnected());
                } else {
                    WriteEvent writeEvent = (WriteEvent) newValue;

                    valueViewer.setValue(null, false);
                }


            }
        });
    }

    public Log eventLog() {
        return eventLog;
    }

    private void onNewEvent() {
        eventList.getItems().add(0, eventLog.getEvents().get(eventLog.getEvents().size() - 1));
    }

}
