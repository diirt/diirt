/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import java.util.Map;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.PVWriterListener;
import java.util.List;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.sim.SimulationDataSource;
import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import java.util.HashMap;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.data.Alarm;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.Time;
import org.epics.pvmanager.data.ValueUtil;
import static org.epics.pvmanager.util.Executors.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 * This is the code from the examples in the docs, to make sure it
 * actually compiles
 *
 * @author carcassi
 */
public class Examples {

    public void c2() {
        // Route notification for this pv on the Swing EDT
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swingEDT()).every(ms(100));

        // Or you can change the default
        PVManager.setDefaultNotificationExecutor(swingEDT());
    }

    public void c3() {
        // Sets CAJ (pure java implementation) as the default data source,
        // monitoring both value and alarm changes
        PVManager.setDefaultDataSource(new JCADataSource());

        // For utltimate control, you can create the JCA context yourself
        // and pass it to the data source
        Context jcaContext = null;
        PVManager.setDefaultDataSource(new JCADataSource(jcaContext, Monitor.VALUE | Monitor.ALARM));
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
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Do something with each value
                Object newValue = pvReader.getValue();
                System.out.println(newValue);
            }
        });

        // Remember to close
        pvReader.close();
    }

    public void b1a() {
        // Read channel "channelName" up to every 100 ms, and get all
        // the new values from the last notification.
        final PVReader<List<Object>> pvReader = PVManager.read(newValuesOf(channel("channelName"))).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Do something with each value
                for (Object newValue : pvReader.getValue()) {
                    System.out.println(newValue);
                }
            }
        });

        // Remember to close
        pvReader.close();
    }

    public void b2() {
        PVWriter<Object> pvWriter = PVManager.write(channel("channelName")).async();
        pvWriter.addPVWriterListener(new PVWriterListener() {

            public void pvWritten() {
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
        final PV<Object, Object> pv = PVManager.readAndWrite(channel("channelName")).asynchWriteAndReadEvery(ms(10));
        pv.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Do something with each value
                Object newValue = pv.getValue();
                System.out.println(newValue);
            }
        });
        pv.write("New value");

        // Remember to close
        pv.close();
    }
    
    public void b5() {
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // By default, read exceptions are made available
                // on the reader itself.
                // This will give you only the last exception, so if
                // more then one exception was generated after the last read,
                // some will be lost.
                Exception ex = pvReader.lastException();
                
                // Note that taking the exception, clears it
                // so next call you'll get null.
                if (pvReader.lastException() == null) {
                    // Always true
                }
            }
        });
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
                }).every(ms(100));
    }
    
    public void b7() {
        // If after 5 seconds no new value comes (i.e. pvReader.getValue() == null)
        // then a timeout is sent. PVManager will _still_ try to connect,
        // until pvReader.close() is called.
        // The timeout will be notified only on the first connection.
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).timeout(sec(5)).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Timeout are passed as exceptions. This allows you to
                // treat them as any other error conditions.
                Exception ex = pvReader.lastException();
                if (ex instanceof TimeoutException) {
                    System.out.println("Didn't connected after 5 seconds");
                }
            }
        });
    }
    
    public void m1() {
        // Read a map with the channels named "one", "two" and "three"
        final PVReader<Map<String, Object>> pvReader = PVManager.read(mapOf(latestValueOf(channels("one", "two", "three")))).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Print the values if any
                Map<String, Object> map = pvReader.getValue();
                if (map != null) {
                    System.out.println("one: " + map.get("one") +
                            " - two: " + map.get("two") + 
                            " - three: " + map.get("three"));
                }
            }
        });
        
        // Remember to close
        pvReader.close();
    }
    
    public void m2() {
        // Write a map to the channels named "one", "two" and "three"
        PVWriter<Map<String, Object>> pvWriter = PVManager.write(mapOf(channels("one", "two", "three"))).async();
        
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
        PV<Map<String, Object>, Map<String, Object>> pv = PVManager.readAndWrite(
                mapOf(latestValueOf(channels("one", "two", "three")))).asynchWriteAndReadEvery(ms(100));
        
        // Do something
        // ...
        
        // Remember to close
        pv.close();
    }
    
    public void m4() {
        // Read a map with the channels "one", "two" and "three"
        // reffered in the map as "setpoint", "readback" and "difference"
        final PVReader<Map<String, Object>> pvReader = PVManager.read(mapOf(
                latestValueOf(channel("one").as("setpoint").and(channel("two").as("readback")).and(channel("three").as("difference"))))).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                // Print the values if any
                Map<String, Object> map = pvReader.getValue();
                if (map != null) {
                    System.out.println("setpoint: " + map.get("setpoint") +
                            " - readback: " + map.get("readback") + 
                            " - difference: " + map.get("difference"));
                }
            }
        });
        
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
        final PV<VDouble, Double> pv = PVManager.readAndWrite(vDouble("currentRB")).asynchWriteAndReadEvery(ms(10));
        pv.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                VDouble value = pv.getValue();
                if (value != null) {
                    System.out.println(value.getValue() + " " + value.getAlarmSeverity());
                }
            }
        });
        pv.write(1.0);
        
        // Remember to close
        pv.close();
    }
    
    public void v2() {
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).every(ms(10));
        pvReader.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                Object value = pvReader.getValue();
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
        });
    }
    
    public void v3() {
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).every(ms(100));
        pvReader.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                // We can switch on the full type
                if (pvReader.getValue() instanceof VDouble) {
                    VDouble vDouble = (VDouble) pvReader.getValue();
                    // Do something with a VDouble
                }
                // ...
            }
        });
    }
    
    public void v4() {
        final PVReader<Object> pvReader = PVManager.read(channel("channelName")).every(ms(100));
        pvReader.addPVReaderListener(VDouble.class, new PVReaderListener() {

            @Override
            public void pvChanged() {
                // We are already guaranteed that the cast succeeds
                // and that the value is not null
                VDouble vDouble = (VDouble) pvReader.getValue();
                System.out.println(vDouble.getValue());
                // ...
            }
        });
    }
    
    public void setAlarm(Object obj) {
        
    }
}
