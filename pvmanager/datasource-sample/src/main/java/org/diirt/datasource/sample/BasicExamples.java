/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.util.List;
import org.diirt.datasource.ExceptionHandler;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.diirt.datasource.TimeoutException;
import org.diirt.datasource.WriteFunction;
import static java.time.Duration.*;

/**
 * This is the code from the examples in the docs, to make sure it
 * actually compiles
 *
 * @author carcassi
 */
public class BasicExamples {

    public void b1_readLatestValue() {
        // Let's statically import so the code looks cleaner
        // import static org.epics.pvmanager.ExpressionLanguage.*;
        // import static java.time.TimeDuration.*;

        // Get updates from channel "channelName" up to every 100 ms
        PVReader<Object> pvReader = PVManager.read(channel("channelName"))
            .readListener(new PVReaderListener<Object>() {
                @Override
                public void pvChanged(PVReaderEvent<Object> event) {
                    // Do something with each value
                    Object newValue = event.getPvReader().getValue();
                    System.out.println(newValue);
                }
            })
            .maxRate(ofMillis(100));

        // Note that "channel" returns an object. You can always substitute it for
        // a more precise operator to have guarantees you can process the value
        // (e.g. vType("channelName"), vNumber("channelName"), ...)

        // Remember to close
        pvReader.close();

        // The interval between updates can be specified in different units
        // (e.g. ms, sec, min, hour, hz). Check the documentation at org.epics.pvmanager.util.TimeDuration.

        // IMPORTANT: you _must_ keep a reference to your reader so that you can
        // close it later. If you don't, pvmanager will consider that reader "lost"
        // and it will close it automatically.
    }

    public void b2_readAllValues() {
        // Read channel "channelName" up to every 100 ms, and get all
        // the new values from the last notification.
        PVReader<List<Object>> pvReader = PVManager
            .read(newValuesOf(channel("channelName")))
            .readListener(new PVReaderListener<List<Object>>() {

                @Override
                public void pvChanged(PVReaderEvent<List<Object>> event) {
                    // Do something with each value
                    for (Object newValue : event.getPvReader().getValue()) {
                        System.out.println(newValue);
                    }
                }
            })
            .maxRate(ofMillis(100));

        // Remember to close
        pvReader.close();

        // newValuesOf limits the values in the queue, to protect memory
        // consumption in problematic circumstances. The default is 1000 elements,
        // which you can override. See all options in the ExpressionLanguage class.
    }

    public void b3_asynchronousWrite() {
        PVWriter<Object> pvWriter = PVManager.write(channel("channelName"))
            .writeListener(new PVWriterListener<Object>() {
                public void pvChanged(PVWriterEvent<Object> event) {
                    if (event.isWriteSucceeded()) {
                        System.out.println("Write finished");
                    }
                }
            })
            .async();
        // This will return right away, and the notification will be sent
        // on the listener
        pvWriter.write("New value");

        // Remember to close
        pvWriter.close();
    }

    public void b4_synchronousWrite() {
        PVWriter<Object> pvWriter = PVManager.write(channel("channelName")).sync();
        // This will block until the write is done
        pvWriter.write("New value");
        System.out.println("Write finished");

        // Remember to close
        pvWriter.close();
    }

    public void b5_readAndWrite() {
        // A PV is both a PVReader and a PVWriter
        PV<Object, Object> pv = PVManager.readAndWrite(channel("channelName"))
            .readListener(new PVReaderListener<Object>() {
                @Override
                public void pvChanged(PVReaderEvent<Object> event) {
                    // Do something with each value
                    Object newValue = event.getPvReader().getValue();
                    System.out.println(newValue);
                }
            })
            .asynchWriteAndMaxReadRate(ofMillis(10));
        pv.write("New value");

        // Remember to close
        pv.close();
    }

    public void b6_handlingErrorsOnNotification() {
        PVReader<Object> pvReader = PVManager.read(channel("channelName"))
            .readListener(new PVReaderListener<Object>() {
                @Override
                public void pvChanged(PVReaderEvent<Object> event) {
                    // By default, read exceptions are made available
                    // on the reader itself.
                    // This will give you only the last exception, so if
                    // more then one exception was generated after the last read,
                    // some will be lost.
                    Exception ex = event.getPvReader().lastException();

                    // Note that taking the exception, clears it
                    // so next call you'll get null.
                    if (event.getPvReader().lastException() == null) {
                        // Always true
                    }
                }
            })
            .maxRate(ofMillis(100));
    }

    public void b7_logAllErrors() {
        // Handling errors within the listener gives you all the advantages
        // of pvmanager, but it throttles the errors you receive. In most cases
        // this is good: if a reader connects to 100 broken channels, you
        // don't get flooded with 100 exceptions. In cases where you
        // want _all_ the exceptions, instead, you can route them to your
        // exception handling mechanism.

        // In this example, all read exceptions will be passed to the exception handler
        // on the thread that it generates them. The handler, therefore,
        // must be thread safe.
        final PVReader<Object> pvReader = PVManager.read(channel("channelName"))
                .routeExceptionsTo(new ExceptionHandler() {
                    public void handleException(Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }).maxRate(ofMillis(100));
    }

    public void b8_readTimeout() {
        // If after 5 seconds no new value comes (i.e. pvReader.getValue() == null)
        // then a timeout is sent. PVManager will _still_ try to connect,
        // until pvReader.close() is called.
        // The timeout will be notified only on the first connection.
        final PVReader<Object> pvReader = PVManager.read(channel("channelName"))
                .timeout(ofSeconds(5))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        // Timeout are passed as exceptions. This allows you to
                        // treat them as any other error conditions.
                        Exception ex = event.getPvReader().lastException();
                        if (ex instanceof TimeoutException) {
                            System.out.println("Didn't connected after 5 seconds");
                        }
                    }
                })
                .maxRate(ofMillis(100));
    }
}
