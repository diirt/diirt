/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.TimeStamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.time.Instant;

/**
 *
 * @author carcassi
 */
public class DataUtilsTest {

    @Test
    public void timestampOf1() {
        Instant timestamp = DataUtils.timestampOf(new TimeStamp(0, 1234567890));
        assertThat(timestamp, equalTo(Instant.ofEpochSecond(DataUtils.TS_EPOCH_SEC_PAST_1970 + 1, 234567890)));
    }

    @Test
    public void isTimeValid1() {
        TimeStamp timeStamp = new TimeStamp(123456789, 1234567890);
        assertThat(DataUtils.isTimeValid(timeStamp), equalTo(false));
    }

    @Test
    public void isTimeValid2() {
        TimeStamp timeStamp = new TimeStamp(123456789, 123456789);
        assertThat(DataUtils.isTimeValid(timeStamp), equalTo(true));
    }

}
