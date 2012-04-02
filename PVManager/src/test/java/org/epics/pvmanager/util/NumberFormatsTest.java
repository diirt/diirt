/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.util;

import java.text.NumberFormat;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class NumberFormatsTest {

    @Test
    public void testSomeMethod() {
        NumberFormat f = NumberFormats.format(3);
        assertThat(f.format(1234.4567), equalTo("1234.457"));
        assertThat(f.format(123), equalTo("123.000"));
        assertThat(f.format(123.4), equalTo("123.400"));

        f = NumberFormats.format(0);
        assertThat(f.format(1234.4567), equalTo("1234"));
        assertThat(f.format(123), equalTo("123"));
        assertThat(f.format(123.4), equalTo("123"));

        f = NumberFormats.format(4);
        assertThat(f.format(1234.4567), equalTo("1234.4567"));
        assertThat(f.format(123), equalTo("123.0000"));
        assertThat(f.format(123.4), equalTo("123.4000"));
    }

}