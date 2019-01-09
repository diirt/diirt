package org.diirt.support.ca;

import org.epics.util.array.CollectionNumbers;
import org.epics.util.array.ListFloat;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VFloatArray;

public class TEST {

    public static void main(String[] args) {
        VFloatArray converted = VFloatArray.of(CollectionNumbers.toListFloat(new float[]{3.25F, 3.75F, 4.25F}),
                Alarm.none(), Time.now(), Display.none());
        double[] result = converted.getData().toArray(new double[3]);
        System.out.println(converted);
        System.out.println(result);
    }
}
