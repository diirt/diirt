/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.util.time.TimeDuration.ofHertz;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;

/**
 * This class tests connecting, getting a value,
 * then closing and doing the same again,
 * usually with another channel name.
 *
 * @author carcassi, Kay Kasemir
 */
public class JCAClientNotificationCount
{
    public static void main(String[] args) throws Exception
    {
        final JCADataSource jca = new JCADataSourceProvider().createInstance();
        PVManager.setDefaultDataSource(jca);

        final String[] names =
        {
            "text",
            "text {\"longString\":true}",
            "TEST_1"
        };
        final Random rand = new Random(1);
        final AtomicInteger runs = new AtomicInteger(0);
        final AtomicInteger connections = new AtomicInteger(0);
        final AtomicInteger values = new AtomicInteger(0);

        int countToPrint = 10;
        while (true)
        {
            final String name = names[rand.nextInt(names.length)];
            final CountDownLatch done = new CountDownLatch(1);

            System.out.print(name + " = ");
            final PVReader<?> pv = PVManager.read(channel(name))
                .readListener(new PVReaderListener<Object>()
                {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event)
                    {
                        if (event.isExceptionChanged()) {
                            event.getPvReader().lastException().printStackTrace();
                        }
                        if (event.isConnectionChanged())
                        {
                            connections.incrementAndGet();
                            System.out.print("[C] ");
                        }
                        if (event.isValueChanged())
                        {
                            System.out.println(event.getPvReader().getValue());
                            values.incrementAndGet();
                            done.countDown();
                        }
                    }
                }).maxRate(ofHertz(100));
            runs.incrementAndGet();

            if (! done.await(10, TimeUnit.SECONDS)) {
                System.out.println("NO VALUE");
                System.out.println(jca.getChannels().get(name).getProperties());
                pv.close();
                System.out.println("Closed");
            } else {
                pv.close();
            }

            if (runs.get() == countToPrint)
            {
                System.out.println("Runs: " + runs
                        + " Connections: " + connections
                        + " Values: " + values);
                countToPrint += 10;
            }
        }
    }
}

