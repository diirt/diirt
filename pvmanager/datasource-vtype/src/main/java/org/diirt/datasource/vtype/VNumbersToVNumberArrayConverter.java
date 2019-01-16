/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import java.util.ArrayList;
import java.util.List;

import org.diirt.datasource.ReadFunction;
import org.epics.util.array.ListDouble;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;

/**
 * Converts numeric types to VDouble.
 *
 * @author carcassi
 */
class VNumbersToVNumberArrayConverter implements ReadFunction<VNumberArray> {

    private final List<? extends ReadFunction<? extends VNumber>> arguments;

    /**
     * Creates a new converter from the given function.
     *
     * @param argument the argument function
     */
    public VNumbersToVNumberArrayConverter(List<? extends ReadFunction<? extends VNumber>> arguments) {
        this.arguments = arguments;
    }

    @Override
    public VNumberArray readValue() {
        final List<VNumber> values = new ArrayList<VNumber>();

        Display meta = Display.none();

        for (ReadFunction<? extends VNumber> function : arguments) {
            VNumber number = function.readValue();
            values.add(number);
            if (meta == null && number != null)
                meta = Display.displayOf(number);
        }

        ListDouble data = new ListDouble() {

            @Override
            public double getDouble(int index) {
                VNumber number = values.get(index);
                if (number == null || number.getValue() == null)
                    return Double.NaN;
                else
                    return number.getValue().doubleValue();
            }

            @Override
            public int size() {
                return values.size();
            }
        };

        return VDoubleArray.of(data, Alarm.none(), Time.now(), Display.none());
    }

}
