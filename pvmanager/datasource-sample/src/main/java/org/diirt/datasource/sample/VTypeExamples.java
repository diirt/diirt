/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.util.Arrays;
import java.util.List;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListNumber;
import static java.time.Duration.*;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueUtil;

/**
 * Examples for using vType expressions.
 *
 * @author carcassi
 */
public class VTypeExamples {

    public void v1_readNumericType() {
        // Let's statically import so the code looks cleaner
        // import static org.epics.pvmanager.data.ExpressionLanguage.*;

        // Read and Write a vNumber
        // Note that the read type is different form the write type
        final PV<VNumber, Number> pv = PVManager.readAndWrite(vNumber("currentRB"))
            .readListener(new PVReaderListener<VNumber>() {
                @Override
                public void pvChanged(PVReaderEvent<VNumber> event) {
                    VNumber value = event.getPvReader().getValue();
                    if (value != null) {
                        System.out.println(value.getValue() + " " + value.getAlarmSeverity());
                    }
                }
            })
            .asynchWriteAndMaxReadRate(ofMillis(10));
        pv.write(1.0);

        // Remember to close
        pv.close();

        // For a full list of types, refer to org.epics.pvmanager.data.ExpressionLanguage
    }

    public void v2_genericReaderExtractParialInformation() {
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

                    // setAlarm(alarm);
                    // setTime(time);
                }
            })
            .maxRate(ofMillis(10));
    }

    public void v3_genericReaderSwitchOnType() {
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

    public void v4_readArrays() {
        // Reads a numeric array of any type (double, float, int, ...)
        // This allows to work on any array type without having to create
        // bindings for each.
        final PVReader<VNumberArray> pvReader = PVManager.read(vNumberArray("channelName"))
            .readListener(new PVReaderListener<VNumberArray>() {
                public void pvChanged(PVReaderEvent<VNumberArray> event) {
                    if (event.isValueChanged()) {
                        // New value
                        VNumberArray value = event.getPvReader().getValue();
                        ListNumber data = value.getData();
                        for (int i = 0; i < data.size(); i++) {
                            // Get the double representation of the value,
                            // converting it if needed
                            double iValue = data.getDouble(i);
                            System.out.println(iValue);
                        }
                    }
                }
            })
            .maxRate(ofMillis(100));
    }

    public void v5_assemblingNumericArrayFromScalars() {
        List<String> channelNames = Arrays.asList("channel1", "channel2", "channel3", "channel4");
        // Reads a list of different numeric channels as a single array.
        // The channels can be of any numeric type (double, float, int, ...)
        final PVReader<VNumberArray> pvReader = PVManager.read(
                vNumberArrayOf(latestValueOf(vNumbers(channelNames))))
            .readListener(new PVReaderListener<VNumberArray>() {
                public void pvChanged(PVReaderEvent<VNumberArray> event) {
                    if (event.isValueChanged()) {
                        // Do something with the value
                        System.out.println(event.getPvReader().getValue());
                    }
                }
            })
            .maxRate(ofMillis(100));
    }

    public void v6_assemblingTables() {
        // You can assemble a table by giving a desired rate expression for each cell,
        // organizing them by column. You can use constant expressions for
        // labels or values that do not change.
        List<String> names = Arrays.asList("one", "two", "trhee");
        PVReader<VTable> pvReader = PVManager
                .read(vTable(column("Names", vStringConstants(names)),
                             column("Values", latestValueOf(channels(names)))))
                .readListener(new PVReaderListener<VTable>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VTable> pvReader) {
                        VTable vTable = pvReader.getPvReader().getValue();
                        // First column is the names
                        @SuppressWarnings("unchecked")
                        List<String> names = (List<String>) vTable.getColumnData(0);
                        // Second column is the values
                        ListDouble values = (ListDouble) vTable.getColumnData(1);
                        // ...
                    }
                })
                .maxRate(ofMillis(100));
    }
}
