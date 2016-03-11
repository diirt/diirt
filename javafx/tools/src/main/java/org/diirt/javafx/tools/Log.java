/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.diirt.util.time.Timestamp;
import org.diirt.util.time.TimestampFormat;
/**
 *
 * @author carcassi
 */
public class Log {

    private final Runnable callback;
    private final List<Event> events = Collections.synchronizedList(new ArrayList<Event>());

    public Log(Runnable callback) {
        this.callback = callback;
    }

    public <T> PVReaderListener<T> createReadListener() {
        return new PVReaderListener<T>() {

            @Override
            public void pvChanged(PVReaderEvent<T> event) {
                events.add(new ReadEvent(Timestamp.now(), event.getPvReader().getName(), event, event.getPvReader().isConnected(), event.getPvReader().getValue(), event.getPvReader().lastException()));
                callback.run();
            }
        };
    }

    public <T> PVWriterListener<T> createWriteListener(final String name) {
        return new PVWriterListener<T>() {

            @Override
            public void pvChanged(PVWriterEvent<T> event) {
                events.add(new WriteEvent(Timestamp.now(), name, event, event.getPvWriter().isWriteConnected(), event.getPvWriter().lastWriteException()));
                callback.run();
            }
        };
    }

    public List<Event> getEvents() {
        return events;
    }

    private TimestampFormat format = new TimestampFormat("ss.NNNNNNNNN");

    public void print(PrintStream out) {
        for (Event event : events) {
            if (event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                out.append(format.format(readEvent.getTimestamp()))
                        .append(" R(");
                if (readEvent.getEvent().isConnectionChanged()) {
                    out.append("C");
                }
                if (readEvent.getEvent().isValueChanged()) {
                    out.append("V");
                }
                if (readEvent.getEvent().isExceptionChanged()) {
                    out.append("E");
                }
                out.append(") ").append(readEvent.getPvName());
                if (readEvent.isConnected()) {
                    out.append(" CONN ");
                } else {
                    out.append(" DISC ");
                }
                out.append(Objects.toString(readEvent.getValue()));
                if (readEvent.getLastException() != null) {
                    out.append(" ").append(readEvent.getLastException().getClass().getName())
                            .append(":").append(readEvent.getLastException().getMessage());
                } else {
                    out.append(" NoException");
                }
            }
            if (event instanceof WriteEvent) {
                WriteEvent writeEvent = (WriteEvent) event;
                out.append(format.format(writeEvent.getTimestamp()))
                        .append(" W(");
                if (writeEvent.getEvent().isConnectionChanged()) {
                    out.append("C");
                }
                if (writeEvent.getEvent().isWriteSucceeded()) {
                    out.append("S");
                }
                if (writeEvent.getEvent().isWriteFailed()) {
                    out.append("F");
                }
                if (writeEvent.getEvent().isExceptionChanged()) {
                    out.append("E");
                }
                out.append(") ").append(writeEvent.getPvName());
                if (writeEvent.isConnected()) {
                    out.append(" CONN");
                } else {
                    out.append(" DISC");
                }
                if (writeEvent.getLastException() != null) {
                    out.append(" ").append(writeEvent.getLastException().getClass().getName())
                            .append(":").append(writeEvent.getLastException().getMessage());
                } else {
                    out.append(" NoException");
                }
            }
            out.append("\n");
        }
        out.flush();
    }
}
