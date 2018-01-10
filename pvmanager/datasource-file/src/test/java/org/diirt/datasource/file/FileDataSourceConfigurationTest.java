/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class FileDataSourceConfigurationTest {

    @Test
    public void readDefaultConfiguration() {
        FileDataSourceConfiguration conf = new FileDataSourceConfiguration().read(this.getClass().getResourceAsStream("file.default.xml"));
        assertThat(conf.isPollEnabled(), equalTo(false));
        assertThat(conf.getPollInterval(), equalTo(Duration.ofSeconds(5)));
    }

    @Test
    public void readConfiguration1() {
        FileDataSourceConfiguration conf = new FileDataSourceConfiguration().read(this.getClass().getResourceAsStream("file.1.xml"));
        assertThat(conf.isPollEnabled(), equalTo(false));
        assertThat(conf.getPollInterval(), equalTo(Duration.ofSeconds(5)));
    }

    @Test
    public void readConfiguration2() {
        FileDataSourceConfiguration conf = new FileDataSourceConfiguration().read(this.getClass().getResourceAsStream("file.2.xml"));
        assertThat(conf.isPollEnabled(), equalTo(true));
        assertThat(conf.getPollInterval(), equalTo(Duration.ofSeconds(50)));
    }

}
