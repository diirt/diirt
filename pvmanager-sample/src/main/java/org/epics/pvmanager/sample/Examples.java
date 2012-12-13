/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import java.util.Map;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.PVWriterListener;
import java.util.List;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.vtype.VDouble;
import org.epics.pvmanager.sim.SimulationDataSource;
import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import java.util.Arrays;
import java.util.HashMap;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.vtype.Alarm;
import org.epics.pvmanager.vtype.Display;
import org.epics.pvmanager.vtype.Time;
import org.epics.pvmanager.vtype.VTable;
import org.epics.pvmanager.vtype.ValueUtil;
import static org.epics.pvmanager.util.Executors.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVWriterEvent;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.epics.pvmanager.vtype.VType;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import static org.epics.util.time.TimeDuration.*;

/**
 * This is the code from the examples in the docs, to make sure it
 * actually compiles
 *
 * @author carcassi
 */
public class Examples {

    public void c2() {
        // Route notification for this pv on the Swing EDT
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swingEDT()).maxRate(ofMillis(100));

        // Or you can change the default
        PVManager.setDefaultNotificationExecutor(swingEDT());
    }

    public void c3() {
        // Sets CAJ (pure java implementation) as the default data source,
        // monitoring both value and alarm changes
        PVManager.setDefaultDataSource(new JCADataSource());

        // For ultimate control, you can modify all the parameters, 
        // and even create the JCA context yourself
        Context jcaContext = null;
        PVManager.setDefaultDataSource(new JCADataSourceBuilder()
                .monitorMask(Monitor.VALUE | Monitor.ALARM)
                .jcaContext(jcaContext)
                .build());
    }

    public void c4() {
        // Create a multiple data source, and add different data sources
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("ca", new JCADataSource());
        composite.putDataSource("sim", new SimulationDataSource());

        // If no prefix is given to a channel, use JCA as default
        composite.setDefaultDataSource("ca");

        // Set the composite as the default
        PVManager.setDefaultDataSource(composite);
    }

    public void b1() {
        // Let's statically import so the code looks cleaner

        // Read channel "channelName" up to every 100 ms
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

        // Remember to close
        pvReader.close();
    }

    public void b1a() {
        // Read channel "channelName" up to every 100 ms, and get all
        // the new values from the last notification.
        final PVReader<List<Object>> pvReader = PVManager
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
    }

    public void b2() {
        PVWriter<Object> pvWriter = PVManager.write(channel("channelName")).async();
        pvWriter.addPVWriterListener(new PVWriterListener<Object>() {

            public void pvChanged(PVWriterEvent<Object> event) {
                System.out.println("Write finished");
            }
        });
        // This will return right away, and the notification will be sent
        // on the listener
        pvWriter.write("New value");

        // Remember to close
        pvWriter.close();
    }

    public void b3() {
        PVWriter<Object> pvWriter = PVManager.write(channel("channelName")).sync();
        // This will block until the write is done
        pvWriter.write("New value");
        System.out.println("Write finished");

        // Remember to close
        pvWriter.close();
    }

    public void b4() {
        // A PV is both a PVReader and a PVWriter
        final PV<Object, Object> pv = PVManager.readAndWrite(channel("channelName"))
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
    
    public void b5() {
        final PVReader<Object> pvReader = PVManager.read(channel("channelName"))
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
    
    public void b6() {
        // All read exceptions will be passed to the exception handler
        // on the thread that it generates them. The handler, therefore,
        // must be thread safe. Overriding the exception handling means
        // disabling the default handling, so read exception will no longer
        // be accessible with {@code pvReader.lastException()}
        final PVReader<Object> pvReader = PVManager.read(channel("channelName"))
                .routeExceptionsTo(new ExceptionHandler() {
                    public void handleException(Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }).maxRate(ofMillis(100));
    }
    
    public void b7() {
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
    
    public void m1() {
        // Read a map with the channels named "one", "two" and "three"
        final PVReader<Map<String, Object>> pvReader = PVManager
                .read(mapOf(latestValueOf(channels("one", "two", "three"))))
                .readListener(new PVReaderListener<Map<String, Object>>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Map<String, Object>> event) {
                        // Print the values if any
                        Map<String, Object> map = event.getPvReader().getValue();
                        if (map != null) {
                            System.out.println("one: " + map.get("one") +
                                    " - two: " + map.get("two") + 
                                    " - three: " + map.get("three"));
                        }
                    }
                })
                .maxRate(ofMillis(100));
        
        // Remember to close
        pvReader.close();
    }
    
    public void m2() {
        // Write a map to the channels named "one", "two" and "three"
        PVWriter<Map<String, Object>> pvWriter = PVManager
                .write(mapOf(channels("one", "two", "three")))
                .async();
        
        // Prepare the 3 values
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("one", 1.0);
        values.put("two", 2.0);
        values.put("three", "run");
        
        // Write
        pvWriter.write(values);
        
        // Remember to close
        pvWriter.close();
    }
    
    public void m3() {
        // Read and write a map to the channels named "one", "two" and "three"
        PV<Map<String, Object>, Map<String, Object>> pv = PVManager
                .readAndWrite(mapOf(latestValueOf(channels("one", "two", "three"))))
                .asynchWriteAndMaxReadRate(ofMillis(100));
        
        // Do something
        // ...
        
        // Remember to close
        pv.close();
    }
    
    public void m4() {
        // Read a map with the channels "one", "two" and "three"
        // reffered in the map as "setpoint", "readback" and "difference"
        final PVReader<Map<String, Object>> pvReader = PVManager
                .read(mapOf(latestValueOf(channel("one").as("setpoint")
                             .and(channel("two").as("readback"))
                             .and(channel("three").as("difference")))))
                .readListener(new PVReaderListener<Map<String, Object>>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Map<String, Object>> event) {
                        // Print the values if any
                        Map<String, Object> map = event.getPvReader().getValue();
                        if (map != null) {
                            System.out.println("setpoint: " + map.get("setpoint") +
                                    " - readback: " + map.get("readback") + 
                                    " - difference: " + map.get("difference"));
                        }
                    }
                })
                .maxRate(ofMillis(100));
        
        // Remember to close
        pvReader.close();
    }
    
    public void m5() {
        // Write a map to the channels named "one", "two" and "three"
        // Write "two" after "one" and write "three" after "two"
        PVWriter<Map<String, Object>> pvWriter = PVManager.write(
                mapOf(channel("one")
                      .and(channel("two").after("one"))
                      .and(channel("three").after("two")))).async();
        
        // Do something
        // ...
        
        // Remember to close
        pvWriter.close();
    }
    
    public void v1() {
        // Read and Write a vDouble
        // Note that the read type is different form the write type
        final PV<VDouble, Double> pv = PVManager.readAndWrite(vDouble("currentRB"))
                .readListener(new PVReaderListener<VDouble>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VDouble> event) {
                        VDouble value = event.getPvReader().getValue();
                        if (value != null) {
                            System.out.println(value.getValue() + " " + value.getAlarmSeverity());
                        }
                    }
                })
                .asynchWriteAndMaxReadRate(ofMillis(10));
        pv.write(1.0);
        
        // Remember to close
        pv.close();
    }
    
    public void v2() {
        final PVReader<VType> pvReader = PVManager.read(vType("channelName"))
                .readListener(new PVReaderListener<VType>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VType> event) {
                        VType value = event.getPvReader().getValue();
                        // We can extract the different aspect of the read object,
                        // so that we can work on them separately

                        // This returns the interface implemented (VDouble, VInt, ...)
                        Class<?> type = ValueUtil.typeOf(value);
                        // Extracts the alarm if present
                        Alarm alarm = ValueUtil.alarmOf(value);
                        // Extracts the time if present
                        Time time = ValueUtil.timeOf(value);
                        // Extracts a numeric value if present
                        Double number = ValueUtil.numericValueOf(value);
                        // Extract display information if present
                        Display display = ValueUtil.displayOf(value);

                        setAlarm(alarm);
                        // ...
                    }
                })
                .maxRate(ofMillis(10));
    }
    
    public void v3() {
        final PVReader<VType> pvReader = PVManager.read(vType("channelName"))
                .readListener(new PVReaderListener<VType>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VType> event) {
                        // We can switch on the full type
                        if (event.getPvReader().getValue() instanceof VDouble) {
                            VDouble vDouble = (VDouble) event.getPvReader().getValue();
                            // Do something with a VDouble
                        }
                        // ...
                    }
                })
                .maxRate(ofMillis(100));
    }
    
    public void v4() {
        final PVReader<VType> pvReader = PVManager.read(vType("channelName")).maxRate(ofMillis(100));
        pvReader.addPVReaderListener(VDouble.class, new PVReaderListener() {

            @Override
            public void pvChanged(PVReaderEvent event) {
                // We are already guaranteed that the cast succeeds
                // and that the value is not null
                VDouble vDouble = (VDouble) event.getPvReader().getValue();
                System.out.println(vDouble.getValue());
                // ...
            }
        });
    }
    
    public void t1() {
        List<String> names = Arrays.asList("one", "two", "trhee");
        final PVReader<VTable> pvReader = PVManager
                .read(vTable(column("Names", vStringConstants(names)),
                             column("Values", latestValueOf(channels(names)))))
                .readListener(new PVReaderListener<VTable>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VTable> pvReader) {
                        VTable vTable = pvReader.getPvReader().getValue();
                        // First column is the names
                        String[] names = (String[]) vTable.getColumnArray(0);
                        // Second column is the values
                        double[] values = (double[]) vTable.getColumnArray(1);
                        // ...
                    }
                })
                .maxRate(ofMillis(100));
    }
    
    public void setAlarm(Object obj) {
        
    }
}
