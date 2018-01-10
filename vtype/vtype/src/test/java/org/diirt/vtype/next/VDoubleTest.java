/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import java.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class VDoubleTest {

    @Test
    public void testCreate() {
        Alarm alarm = Alarm.create(AlarmSeverity.MINOR, "LOW");
        Time time = Time.create(Instant.ofEpochSecond(1354719441, 521786982));
        VDouble value = VDouble.create(1.0, alarm, time, Display.none());
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getAlarm(), equalTo(alarm));
        assertThat(value.getTime(), equalTo(time));
        assertThat(value.toString(), equalTo("VDouble[1.0 ,MINOR(LOW), 2012-12-05T14:57:21.521786982Z]"));
    }

}
