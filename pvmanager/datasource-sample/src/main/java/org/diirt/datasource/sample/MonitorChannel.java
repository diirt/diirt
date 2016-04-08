/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.formula.ExpressionLanguage;
import java.time.Duration;

/**
 * Prompts for a channel name, connects and shows the events.
 *
 * @author carcassi
 */
public class MonitorChannel {

    public static void main(String[] args) throws Exception {
        //  Prompt for channel
        System.out.print("Enter channel: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String channelName;
        try {
            channelName = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read the channel name");
            System.exit(1);
            return;
        }

        System.out.println("Starting channel " + channelName);
        PVReader<?> reader = PVManager.read(ExpressionLanguage.formula(channelName))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        System.out.println(event);
                    }
                })
                .maxRate(Duration.ofMillis(50));

        Thread.sleep(3000);

        System.out.println("Closing...");
        reader.close();
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
