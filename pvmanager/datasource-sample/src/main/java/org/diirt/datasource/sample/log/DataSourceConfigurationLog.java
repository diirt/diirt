/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.vtype.ExpressionLanguage.vNumber;
import java.time.Duration;
import org.diirt.vtype.VNumber;

/**
 *
 * @author carcassi
 */
public class DataSourceConfigurationLog {
    public static void main(String[] args) throws Exception {
        // Increasing logging at CONFIG level
        Logger.getLogger("").getHandlers()[0].setLevel(Level.CONFIG);
        Logger.getLogger("").setLevel(Level.CONFIG);

        System.out.println("Open channel");
        PVReader<VNumber> reader = PVManager.read(vNumber("sim://noise"))
                .readListener(new PVReaderListener<VNumber>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VNumber> event) {
                        if (event.isValueChanged()) {
                            System.out.println("... value is " + event.getPvReader().getValue().getValue());
                        }
                    }
                })
                .maxRate(Duration.ofMillis(500));

    Thread.sleep(2000);
        System.out.println("Close channel");
        reader.close();
        Thread.sleep(1000);

        System.out.println("Close data source");
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
